# Simulador dos 20 medidores — canal A.2 (binário sobre TCP)

Um servidor TCP por medidor em `127.0.0.1:2051..2070`, casando com as linhas de
`conversmodel` do seed (`CONVERSOR1..CONVERSOR20`).

## Arquivos

| Arquivo | Conteúdo |
|---|---|
| `meter_simulator.py` | Os 20 medidores. Só biblioteca padrão do Python 3. |
| `meter_config.json` | Portas, firmware, física por medidor, injeção de falha. |
| `run_all.py` | Ponto de entrada comum: BCI + medidores no mesmo processo, com a vazão acoplada. |

## Rodar

```bash
cd Simulador

python run_all.py                    # BCI + 20 medidores, vazão acoplada  <-- use este
python meter_simulator.py --flow 2500  # só os medidores, vazão fixa
python meter_simulator.py --selftest   # bateria interna
python run_all.py --faults             # liga a injeção de falha nos dois
```

**Use `run_all.py` para o ciclo real.** O `BenchState` da BCI é a única fonte da
verdade sobre a vazão corrente, e os medidores precisam produzir tempos de
trânsito coerentes com ela *no mesmo instante*. Rodar os dois separados faria o
K sair errado.

---

## Protocolo implementado

### Formato 1 — pacote de medição (stream, após `0x21`)

```
8 × 0xFF (preâmbulo) + 78 bytes little-endian = 86 bytes, sem checksum
```

| Offset | Tipo | Campo |
|---|---|---|
| 0 | short | agStatus |
| 2 | short | r1 |
| 4 | short | gm1 |
| 6 | double | hfc |
| 14 | double | ttstd |
| 22 | double | ttrev |
| 30 | double | vel |
| 38 | double | velRe |
| 46 | double | volFlowRe (m³/h) |
| 54 | double | volRe |
| 62 | double | accReVolume |
| 70 | double | accReVolumeRev |

Cada pacote sai num **único `sendall()`** sob lock — nunca parcial, nunca com o
preâmbulo quebrado. Isso é obrigatório: durante a coleta (`saveData`), **um
único pacote perdido ou malformado reprova o medidor na hora**, sem esperar
timeout ([MeterSocketThread.java:361](../src/metercomm/socket/MeterSocketThread.java#L361)).

### Formato 2 — resposta a comando

```
ufoId (4 bytes) + byte de comando + ACK(0xF1)/NACK(0xF2) + payload opcional
```

O **ufoId é escolhido pelo simulador** (`A0 00 00 NN`, distinto por medidor) e
*aprendido* pela aplicação na primeira leitura (`MeterSocketComm.readID()`).

### Comandos

| Comando | Byte | Payload enviado | Resposta |
|---|---|---|---|
| READ_FIRMWARE_VERSION | 0x01 | — | ACK + 8 bytes |
| READ_SERIAL_NUMBER | 0x02 | — | ACK + 12 bytes |
| WRITE_SERIAL_NUMBER | 0x03 | 12 | ACK, payload, ACK |
| READ/WRITE_REPLACEMENT_DATE | 0x04/0x05 | — / 8 | ACK + 8 / ACK, payload, ACK |
| LOAD_*_OPMODE | 0x10–0x15 | — | ACK |
| ENABLE/DISABLE_DATA_TRANSMIT | 0x21/0x22 | — | ACK (+ inicia/cessa stream) |
| TRIM_AG_STAGE_2 | 0x23 | — | ACK |
| LOAD_CALIB_PARAMETERS | 0xAA | 42 + 16×N | ACK, payload, ACK (com parse e log) |
| READ/WRITE_CONFIG_PARAMETERS | 0xBB/0xCC | — / 654 | ACK + 654 / ACK, payload, ACK |
| UPDATE/READ_DATE | 0xD0/0xD1 | 6 / — | ACK, payload, ACK / ACK + 6 |
| WRITE/READ_WMBUS_CONFIG | 0xD3/0xD4 | 34 / — | ACK, payload, ACK / ACK + 34 |

**Comandos com payload usam DOIS ACKs** — um antes do payload e outro depois.
Isso vem de `MeterSocketThread.run()`: com firmware ≥ mínimo, após enviar os
dados a app chama `configureAckOrNackCheck()` e espera um segundo ACK.

**O byte de comando chega duas vezes** (~20 ms entre as cópias). O simulador
descarta a duplicata — inclusive *antes de ler o payload*, senão a segunda cópia
vira o primeiro byte dos dados. Foi exatamente esse bug que fez o `N` do 0xAA ser
lido como 1214 em vez de 4 no primeiro selftest.

---

## Eco condicional por firmware — decisão documentada

`ViewStatesUtil.arrayMinVersion = {1,0,0,0,8,0,1,0}` (lê-se `1.0.8.1`).

A app compara `fwVersionToLong(recebido) < fwVersionToLong(mínima)`:

- **menor** → ela espera o **eco byte a byte** dos comandos de escrita;
- **maior ou igual** → **não há eco**, ela espera um segundo ACK.

**Decisão: o simulador declara `1.0.8.2` (≥ mínima) e NÃO ecoa.** Configurável em
`firmware.version_bytes` / `firmware.echo_writes`. Se você baixar a versão (ex.:
`[1,0,0,0,7,0,0,0]`), **tem de ligar `echo_writes`** — senão a app fica esperando
um eco que nunca vem e o download de constantes falha em silêncio.

Detalhe de leitura: `readFwVersion()` lê 6 bytes e, se `fw[0]==1 && fw[4]<8`,
assume build 0; senão lê mais 2. Com `fw[4]=8` ela lê os 8 — por isso enviamos 8.

---

## Modelo físico

Invertido a partir de `CalibrationService`, com `c = sosWater(T)`:

```
ttstd = L/(c+v) + tau + delta
ttrev = L/(c-v) + tau - delta
```

Na vazão zero a app calcula `dzc = -delta` e `dsos = tau`; em `ttToVel()` os
termos se cancelam e ela recupera **v exatamente**. É isso que faz o K sair ~1,0.

**Verificado contra a aplicação real:**

| Grandeza | Injetado | Recuperado pela app | Erro |
|---|---|---|---|
| `delta` (→ DZC) | 1,0e-9 s | −9,984e-10 s | 0,16 % |
| `tau` (→ DSOS) | 1,0e-6 s | 1,0000e-6 s | 0,0002 % |
| vazão (volFlowRe) | 2500 L/h | 2500,0 L/h | 0,000 % |
| velocidade | 8,8419 m/s | 8,8419 m/s | 0,000 % |

### Parâmetros que reprovam se mal ajustados

**`tt_noise_s` (padrão 1e-11 = 10 ps).** `calculateStdDeviationZeroFlow()`
reprova o medidor se `2×desvio(vel) ≥ 0,0015 m/s`. Com L=0,084 e t≈56,7 µs,
`σ_vel ≈ 1,85e7 × σ_t`. Em 10 ps dá `2σ ≈ 3,4e-4` — folga de ~4×. **Acima de
~4e-11 começa a reprovar de forma intermitente.**

**`us_path_length_m` e `reduction_diameter_m`** têm de ser **iguais** aos de
`metertypemodel` no seed. Divergência aqui gera K errado **sem quebrar o ciclo** —
o pior tipo de falha.

### Perfil por medidor

```json
"meters": { "7": { "bias_calib": 0.004, "bias_verif": 0.006 } }
```

- `bias_calib` — erro sistemático na **calibração**; o K calculado fica ~`1/(1+bias)`.
- `bias_verif` — erro residual na **verificação**. Limites do seed: ±5 % em Q1/Q2,
  ±2 % em Q3/Q4. Use `0.10` para reprovar de propósito.

O simulador aplica a **tabela K/Reynolds recebida via 0xAA**, interpolando
linearmente — não é um fator fixo.

---

## Injeção de falha

Desligada por padrão (`--faults` liga). Cada regra tem seu próprio `enabled`.

| `action` | Efeito |
|---|---|
| `nack` | responde NACK ao comando |
| `silence` | não responde (o medidor não conecta / trava a etapa) |
| `delay` | atrasa a resposta |
| `corrupt_stream` | emite pacote truncado — **reprova na coleta** |
| `nan_stream` | emite ttstd/ttrev/vel como NaN — a app descarta o pacote |

---

## O que foi verificado

`python meter_simulator.py --selftest` — tudo passa:

```
(a) 20 portas 2051..2070 aceitando conexao
(b) MEDIDOR-01/07/20 ufoId + 0x10 ACK
(c) payload de 78 bytes, offsets conferem
    volFlowRe -> 2500.0 L/h (alvo 2500, erro 0.001%)
(d) 51 pacotes integros em 5 s (10.2 Hz), 0 perdidos
(e) 0xAA: 2 ACKs (106 bytes, N=4); tabela Reynolds/K igual a enviada
```

**Com as classes reais da aplicação** (`MeterSocketComm` + `PackageHandler`):

```
20/20 conectados, 20/20 verificados (0x10), 20/20 com firmware lido (0x01)
MEDIDOR-01 ufoId aprendido = A0000001 | firmware = 1.0.8.2
60 pacotes integros, 0 invalidos
volFlowRe medio = 2500,0 L/h (erro 0,000%)
DZC = -9,98e-10 (injetado -1e-9) | DSOS = 1,0000e-6 (injetado 1e-6)
2*desvio = 3,4e-04 < 0,0015 -> APROVADO na vazao zero
0xAA: 1o e 2o ACK recebidos
CMD_LOAD_CALIB_OPMODE / VERIF / NORMAL / TRIM_AG_STAGE_2 -> ACK
```

**No ciclo completo**, com banco + BCI + medidores:

```
20/20 medidores conectados
20/20 medidores transmitindo em 201 ms
ESTADO -> INITIAL_RUN_CONFIGURATION
ESTADO -> ZEROFLOW
```

---

## ⚠️ Limitação conhecida — o ciclo trava no trim AGC

**O ciclo chega a `ZEROFLOW` mas não avança**: o trim AGC (`0x23`) estoura o
timeout de 50 s em cada medidor, e todos são reprovados.

### O que eu apurei

- `0x11` (CALIB_OPMODE) e `0x21` (ENABLE_TRANSMIT) são confirmados em **~72 ms**
  cada, pelo **mesmo** mecanismo (`sendCommandToMeter` + `checkForAck`).
- `0x23` usa código idêntico e **nunca** é confirmado. A única diferença: ele é
  emitido **depois** que o stream começou.
- O simulador **envia o ACK corretamente**. Capturei os bytes crus: o ACK
  `A0 00 00 01 23 F1` chega no **offset 0**, imediatamente.
- Replicando o algoritmo de `checkForAck()` byte a byte com leitura crua, o ACK
  é **encontrado em 91 bytes**. O algoritmo funciona.
- `jstack` confirma a thread dentro de `checkForAck` → `readByte`.
- O `ufoId` visto pela app é `A0 00 00 01` — o correto.

### Causa

`MeterSocketComm.readDataLine()` cria um **`new BufferedInputStream(input)` a
cada chamada** ([linha 819](../src/metercomm/socket/MeterSocketComm.java#L819)).
Um `BufferedInputStream` **lê adiante** (até 8 KB). Os bytes que ele buferiza
além dos 86 consumidos são **silenciosamente descartados** quando o objeto é
substituído na chamada seguinte. Já `checkForAck()` lê do `input` **cru**.

Com o stream ligado, `readDataLine()` roda continuamente — então **qualquer ACK
que caia na janela de read-ahead é destruído**. Comandos emitidos *antes* do
stream (0x11, 0x21) não sofrem disso; os emitidos *depois* (0x23) sofrem sempre.

### Por que não dá para consertar no simulador

Testei as quatro combinações — stream pausado × rodando, ACK único × reasseverado
a cada 250 ms indefinidamente. **Todas falham.** O ACK é destruído do lado da
aplicação, depois de já ter chegado no socket. Nenhuma estratégia no fio resolve.

Além disso, pausar o stream tem efeito colateral próprio: sem os `0xFF`,
`readDataLine()` fica em laço até o timeout de 2 s sem nunca chegar a
`checkForAck()`. Por isso `quiet_window_s` **deve ficar em 0**.

### O que resolveria (exige mexer em `src/`, que está fora do escopo combinado)

Trocar o `BufferedInputStream` descartável por **um único stream buferizado,
criado uma vez e compartilhado** por `readDataLine()`, `readByte()` e `readID()`.
Hoje há dois caminhos de leitura concorrentes sobre o mesmo socket, um deles
perdendo bytes — é um defeito real da aplicação, que também deve se manifestar
contra o hardware verdadeiro sempre que um comando for emitido com o medidor
transmitindo.

**Não apliquei essa mudança** porque a instrução foi não alterar `src/`. Se você
autorizar, é uma correção pequena e localizada em `MeterSocketComm`.

---

## Estado do ciclo

| Etapa | Status |
|---|---|
| CONNECT_METERS (20 medidores) | ✅ 20/20 em 201 ms |
| INITIAL_RUN_CONFIGURATION | ✅ |
| CLOSELINE | ✅ |
| DOWNLOAD_INIT_CALIB_VALUES | ✅ |
| ZEROFLOW — chegada | ✅ |
| ZEROFLOW — trim AGC (0x23) | ❌ bloqueado (acima) |
| CALCULATE_ZEROFLOW em diante | ⛔ não alcançado |
| SAVE_METER_DATA_IN_DB | ⛔ não alcançado |
