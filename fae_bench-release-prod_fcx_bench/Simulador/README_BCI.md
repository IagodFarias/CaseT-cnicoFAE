# Simulador da BCI — canal A.1 (JSON sobre TCP)

Simula a **Bench Control Interface**: válvulas, bombas, linha, sensores, medidores
de referência e contadores de pulso. Não simula os 20 medidores ultrassônicos —
esse é o canal A.2, ainda não implementado.

## Arquivos

| Arquivo | Conteúdo |
|---|---|
| `bci_simulator.py` | O simulador. Sem dependências além da biblioteca padrão do Python 3. |
| `bci_config.json` | Host/porta, valores de sensor, medidores de referência, injeção de falha. |
| `logs_simulador/` | Um arquivo por execução: `bci_<timestamp>.log`. |

## Rodar

```bash
cd Simulador

python bci_simulator.py                        # usa bci_config.json (127.0.0.1:8888)
python bci_simulator.py --port 8888            # sobrepõe a porta
python bci_simulator.py --host 0.0.0.0         # aceita conexões externas
python bci_simulator.py --faults               # liga a injeção de falha
python bci_simulator.py --selftest             # bateria interna, não abre socket
```

Para escutar em `192.168.2.6:8888` (o endereço real da bancada), esse IP precisa
estar atribuído a uma interface desta máquina. Em bancada de teste, o normal é
usar `127.0.0.1` e apontar o `socketConfig.xml` para lá.

## Apontar a aplicação para o simulador

Edite [`socketConfig.xml`](../socketConfig.xml) na raiz do projeto. Só o bloco
`socketId="si"` interessa — é o da BCI:

```xml
<socket socketId="si">
    <ip>127.0.0.1</ip>
    <port>8888</port>
</socket>
```

O IP de produção (`192.168.2.6`) está logo acima no arquivo; comente-o em vez de
apagar, para não perder a referência.

⚠️ O arquivo é lido por **caminho relativo ao diretório de trabalho**
(`./socketConfig.xml`, em `BCISocketComm.readConfigXML()`). Se você roda pelo
`dist/run.bat`, quem vale é `dist/socketConfig.xml` — o alvo do Ant
`create-fat-jar` copia a raiz para `dist/`. Edite **os dois** ou rode a app a
partir da raiz do projeto.

## Validar

### 1. Selftest do simulador (sem a aplicação)

```bash
python bci_simulator.py --selftest
```

Deve terminar com `SELFTEST OK`. Cobre `checkConnectionWithBci`, a sequência
`closeLine`+`getLineState`, reflexo de estado de válvula/bomba, convergência da
vazão, incremento do contador, a banda de ±3 % da pressão diferencial usada por
`assertStaticZeroFlow`, e a **coerência pulso→vazão** (refaz a conta de
`BenchController.calcPulseToFlowRate` e confere contra o setpoint).

### 2. `checkConnectionWithBci()` passa

Suba o simulador e a aplicação. No log da app
(`logs/calibracao_<timestamp>.log`, da instrumentação SLF4J) procure:

```
INFO  [BANCADA] [BOOT] ... - ETAPA INICIO: INICIALIZACAO DA BANCADA - verificando conexao com a BCI
INFO  [BANCADA] [BOOT] ... - BCI conectada em /127.0.0.1:8888 em 3 ms
INFO  [BANCADA] [BOOT] ... - SAIDA checkConnectionWithBci(): BCI RESPONDENDO - verificado em 5 ms
INFO  [BANCADA] [BOOT] ... - ETAPA FIM: INICIALIZACAO DA BANCADA concluida em 42 ms
```

Se aparecer `BCI NAO respondeu`, o `socketConfig.xml` não está apontando para o
simulador (ou a app está lendo outra cópia do arquivo — ver o aviso acima).

No log do simulador, o mesmo instante aparece como:

```
[#1 0001] <-- {"command":"getAlarms", "alarmSel": 0}
[#1 0001] --> {"command": "getAlarms", "alarmSel": 0}
```

Na interface, o **LED da BCI fica verde**.

### 3. A máquina de estados sai de `CLOSELINE`

Selecione o lote `LOTE-TESTE-001`, conecte os medidores e dê **Run**. No log da
aplicação:

```
INFO  [BANCADA] [INITIAL_RUN_CONFIGURATION] ... - ETAPA FIM: INITIAL_RUN_CONFIGURATION - duracao N ms
INFO  [BANCADA] [CLOSELINE] ... - ETAPA INICIO: CLOSELINE (transicao INITIAL_RUN_CONFIGURATION -> CLOSELINE)
INFO  [BANCADA] [CLOSELINE] ... - Linha fechada com sucesso. Somente verificacao: false
INFO  [BANCADA] [CLOSELINE] ... - ETAPA FIM: CLOSELINE - duracao N ms
INFO  [BANCADA] [DOWNLOAD_INIT_CALIB_VALUES] ... - ETAPA INICIO: DOWNLOAD_INIT_CALIB_VALUES
```

O sinal de que **travou** em CLOSELINE é a repetição de
`closeLine() retornou FALSE - a linha nao foi fechada` sem transição de etapa.

No simulador, cada tentativa aparece como o par:

```
<-- {"command":"closeLine"}
--> {"command": "ackResponse", "responseTo": "closeLine"}
<-- {"command":"getLineState"}
--> {"command": "getLineState", "lineState": "CLOSED"}
```

`closeLine` são **dois round-trips**: a app pede o ACK e em seguida confirma o
estado com `getLineState` (`BenchControlImpl.closeLine()`).

## Injeção de falha

Desligada por padrão. Ligue com `--faults` ou `"enabled": true` no
`fault_injection` do config. Cada regra tem seu próprio `enabled`.

| `action` | Efeito |
|---|---|
| `nack` | responde `{"command":"nackResponse","responseTo":"<cmd>"}` |
| `delay` | dorme `delay_seconds` antes de responder (>10 s estoura o `soTimeout` da app) |
| `silence` | não responde nada — a app fica bloqueada até o timeout |
| `garbage` | devolve texto que não é JSON |

Seleção da ocorrência: `occurrences: [1,5]` (a 1ª e a 5ª vez que o comando
chega) **ou** `every_n: 50` (a cada 50 chamadas).

```json
{ "command": "closeLine", "action": "nack", "occurrences": [1], "enabled": true }
```

## Como o simulador se comporta

- **Estado real, não respostas fixas.** `openValve XV2` → `getValveState XV2`
  devolve `OPENED`. `setPumpLoad BP1 45` → `getPumpState BP1` devolve 45 % e
  `ON`. `closeLine` → `getLineState` devolve `CLOSED`.
- **Vazão com inércia.** `setFlowRate` define o setpoint; a vazão converge com
  constante de tempo de ~1 s. O `readRefMeter` só reporta `STABLE` quando está a
  menos de 2 % do alvo — é isso que faz `runFlowRate` retornar `true`.
- **Pulsos derivados, não inventados.** O peso do pulso usa a mesma fórmula da
  aplicação (`RefMeterController.calcFreqPulseWeight`):
  `mL_por_pulso = (max_flow_lph / 3.6) / freq_max_hz`. Por isso a vazão que a app
  **recalcula** a partir dos pulsos bate com a vazão entregue (erro < 0,05 % no
  selftest). Se você mudar `max_flow_lph`/`freq_max_hz` no config, mude também em
  `refmetermodel` no seed — divergência aqui gera constantes K erradas **sem**
  quebrar o ciclo, que é o pior tipo de falha.
- **Contador é totalizador.** Nunca reinicia (exceto no `reset`). A app calcula
  `counterDiff = c2 - c1` e **aborta a vazão** se der negativo
  (`"Sample Counter diff is negative"`).
- **Pressão diferencial estável em vazão zero.** `assertStaticZeroFlow` lê PTDIF
  uma vez como setpoint e depois exige que fique dentro de ±3 %. O ruído padrão é
  0,4 %, com folga. Aumentar `press_dif_noise` acima de ~0,01 começa a reprovar a
  vazão zero de forma intermitente.
- **Uma resposta por requisição, sempre.** A app serializa `send`/`read` com um
  semáforo (`BCISocketComm`); uma linha a mais dessincroniza o canal inteiro.

## Limitação conhecida

`setAutoFlowRate` **não está implementado**. Ele é um protocolo de *streaming*:
`BenchControlImpl.setAutoFlowRate()` calcula `numberOfResponses` e lê N linhas
para uma única requisição. Responder uma linha só dessincronizaria o canal.
Nenhum ponto do fluxo de calibração o chama — o simulador registra o pedido e
devolve NACK. Se algum dia entrar no fluxo, precisa ser implementado como stream.

## O que foi verificado

- `--selftest`: todas as verificações passam.
- **Cliente Java real** usando `json.JSonParser` e as classes de
  `bciapi.command.model` do projeto: 18 verificações, todas OK. Isso prova que as
  respostas desserializam — o `ObjectMapper` da app usa a configuração padrão do
  Jackson, com `FAIL_ON_UNKNOWN_PROPERTIES` ligado, então **qualquer campo extra
  numa resposta faria o parse devolver `null`** e o comando falhar em silêncio.
  Por isso cada resposta contém exatamente os campos da classe correspondente.
- Reconexão: 3 conexões sequenciais, todas OK.
- Concorrência: 3 clientes simultâneos, todos OK.
- Injeção de falha: `nack` na 1ª ocorrência e `silence` gerando
  `SocketTimeoutException` real no cliente.

**Não verificado:** a aplicação JavaFX completa contra o simulador — depende do
PostgreSQL com o banco de teste, que não existe nesta máquina.
