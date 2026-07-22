# Sistema de Calibração de Medidores de Água

Ensaio metrológico automatizado: um **software de calibração** (Java 17) conduz o ensaio
de um medidor de água contra um **simulador de medidor de referência** (Python), trocando
comandos por TCP/IP. Ao final, calcula o erro percentual, classifica o medidor como
APROVADO ou REPROVADO e grava o laudo em PostgreSQL.

Case técnico — vaga Java Pleno.

---

## Sumário

- [Como funciona](#como-funciona)
- [Arquitetura](#arquitetura)
- [Protocolo de comunicação](#protocolo-de-comunicação)
- [Pré-requisitos](#pré-requisitos)
- [Executando](#executando)
- [Configuração](#configuração)
- [Testes](#testes)
- [Decisões de projeto](#decisões-de-projeto)

---

## Como funciona

O **simulador** representa o medidor padrão do laboratório: acumula volume continuamente
enquanto o ensaio está ativo, integrando `vazão × Δt` com relógio monotônico e aplicando
ruído gaussiano na vazão.

O **software de calibração** representa o medidor sob teste: gera pulsos numa thread
dedicada, na cadência da vazão nominal corrigida por um desvio próprio daquele medidor,
sorteado uma vez por ensaio dentro de uma faixa configurável (padrão −3% a +3%).

Ao final, compara as duas medições:

| Grandeza | Fórmula |
|---|---|
| Volume medido | `V_medido = pulsos / K` |
| Volume de referência | `V_ref = V(t_fim) − V(t_início)` |
| Vazão média | `V_ref / duração do ensaio` |
| Erro | `(V_medido − V_ref) / V_ref × 100` |
| Resultado | APROVADO se `\|erro\| ≤ 2%`, senão REPROVADO |

### Fluxo do ensaio

```
cliente                                     simulador
   |-- connect ------------------------------->|
   |-- {"command":"START"} ------------------->|  inicia acúmulo de volume
   |<------------------- {"status":"OK"} ------|
   |  [inicia thread de pulsos]  t_início      |
   |-- {"command":"READ"} -------------------->|  V(t_início)
   |<------- {"volume":..,"flowRate":..} ------|
   |                                           |
   |  ... READs periódicos de acompanhamento   |
   |                                           |
   |  [para thread de pulsos]  t_fim           |
   |-- {"command":"READ"} -------------------->|  V(t_fim)
   |<------- {"volume":..,"flowRate":..} ------|
   |-- {"command":"STOP"} -------------------->|  congela o volume
   |<------------------- {"status":"OK"} ------|
   |  [calcula erro, classifica, persiste]     |
```

---

## Arquitetura

Organização em camadas, com dependências apontando sempre para dentro — `domain/` não
conhece rede, threads nem banco de dados.

```
src/main/java/com/fae/calibracao/
├── net/           TcpClient, CommunicationException
│                  Socket, timeouts, ReentrantLock (um comando por vez)
├── protocol/      Command, Response, JsonCodec, ProtocolException
│                  Serialização JSON (Jackson); não conhece socket
├── domain/        Medicao, JanelaEnsaio, Classificador, ResultadoEnsaio, RelatorioEnsaio
│                  Cálculos metrológicos puros; sem I/O
├── measurement/   PulseGenerator, DeviationRange
│                  Thread de pulsos, AtomicLong; não toca no socket
├── service/       EnsaioService, EnsaioConfig, EnsaioObserver, ProgressoEnsaio
│                  Orquestração do fluxo e tratamento de falhas
├── persistence/   Ensaio (entidade JPA), EnsaioRepository
│                  Hibernate / PostgreSQL
└── ui/            ConsoleApp, ConsoleObserver
                   Apresentação; recebe eventos do service

simulador/
└── simulador_medidor.py     servidor TCP do medidor de referência
```

### Concorrência

O cliente roda **duas threads**:

| Thread | Responsabilidade | Estado compartilhado |
|---|---|---|
| principal | comunicação TCP, orquestração, UI | socket, protegido por `ReentrantLock` |
| `gerador-pulsos` | conta pulsos do medidor sob teste | contador `AtomicLong` |

A thread de pulsos **nunca acessa o socket**. O lock protege o socket porque o protocolo
não tem identificador de correlação — a resposta é a próxima linha do stream, então o par
*escrever + ler* precisa ser indivisível. Já o contador é um único valor com uma escritora
e várias leitoras, caso em que `AtomicLong` basta e evita acoplar a cadência de pulsos à
latência da rede.

---

## Protocolo de comunicação

JSON, **uma mensagem por linha** (delimitador `\n`), sobre TCP.

| Comando | Requisição | Resposta |
|---|---|---|
| Iniciar | `{"command":"START"}` | `{"command":"START","status":"OK"}` |
| Ler | `{"command":"READ"}` | `{"command":"READ","volume":120.45,"flowRate":15.20,"stable":true}` |
| Parar | `{"command":"STOP"}` | `{"command":"STOP","status":"OK"}` |

`READ` é repetível. Falhas devolvem `status:"ERROR"` com um campo `message`, **sem
derrubar a conexão**:

```json
{"command":"UNKNOWN","status":"ERROR","message":"JSON malformado"}
```

**Unidades:** volume em litros (L), vazão em litros por minuto (L/min).

---

## Pré-requisitos

| Software | Versão | Verificar com |
|---|---|---|
| JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -v` |
| Python | 3.8+ | `python --version` (apenas biblioteca padrão) |
| Docker + Compose | — | `docker compose version` |

---

## Executando

### 1. Subir o banco de dados

```bash
docker compose up -d
docker compose ps          # aguardar STATUS = healthy
```

Sobe um PostgreSQL 16 com banco/usuário/senha `calibracao` na porta 5432. A tabela
`ensaio` é criada automaticamente pelo Hibernate na primeira execução
(`hbm2ddl.auto=update`).

### 2. Compilar o cliente

```bash
mvn clean package
```

Gera `target/calibracao.jar`, um jar único com todas as dependências.

### 3. Iniciar o simulador — *terminal A*

```bash
python simulador/simulador_medidor.py --vazao 15.0 --ruido 0.02
```

```
[14:32:10] simulador ouvindo em 127.0.0.1:5000
[14:32:10] vazao nominal 15.0 L/min | ruido 2.0% | tick 0.05s
```

### 4. Executar o ensaio — *terminal B*

```bash
java -jar target/calibracao.jar                    # padrões: 127.0.0.1:5000, 30 s
java -jar target/calibracao.jar 127.0.0.1 5000 60  # host, porta, duração
java -jar target/calibracao.jar --help
```

<details>
<summary><b>Saída de um ensaio completo</b></summary>

```
==============================================================================
  SOFTWARE DE CALIBRACAO DE MEDIDORES DE AGUA
  Ensaio metrologico contra medidor de referencia via TCP/IP
==============================================================================
  simulador (referencia) : 127.0.0.1:5000
  vazao nominal          : 15,00 L/min
  constante K            : 100 pulsos/L
  faixa de desvio        : -3,00% a +3,00%
  duracao programada     : 8 s
  criterio de aprovacao  : |erro| <= 2,00 %
------------------------------------------------------------------------------
  [banco  ] CONECTADO
  [conexao] conectando em 127.0.0.1:5000 ...
  [conexao] CONECTADO
  [ensaio ] START aceito - ensaio EM ANDAMENTO
  [medidor] desvio sorteado -1,630 % (vazao efetiva 14,756 L/min)
------------------------------------------------------------------------------
    #   tempo   andamento               Vref      pulsos      Vmed     vazao   estavel
    1    1,0s  [###-----------------]  13%     0,259 L      24     0,240 L   15,30  nao
    2    2,0s  [#####---------------]  25%     0,513 L      49     0,490 L   14,81  sim
    4    4,0s  [##########----------]  51%     1,015 L      98     0,980 L   14,76  sim
    6    6,1s  [###############-----]  76%     1,522 L     148     1,480 L   15,20  sim
    8    8,0s  [####################] 100%     2,004 L     196     1,960 L   14,95  sim
------------------------------------------------------------------------------
  [ensaio ] STOP enviado - ensaio CONCLUIDO

==============================================================================
  RESULTADO DO ENSAIO
==============================================================================
  data/hora              : 22/07/2026 11:02:23
  duracao                : 8,00 s
  constante K            : 100 pulsos/L
  vazao configurada      : 15,00 L/min
  vazao media do ensaio  : 14,953 L/min

  pulsos gerados         : 196
  volume de referencia   :    1,994 L   (medidor padrao)
  volume medido          :    1,960 L   (pulsos / K)
  erro                   :   -1,705 %   (tolerancia +/- 2,00 %)

  RESULTADO              : APROVADO
  (desvio injetado no medidor simulado: -1,630 %)
==============================================================================
  [conexao] DESCONECTADO
  [banco  ] laudo gravado com id 4

  Ultimos ensaios registrados (total: 4)
    id  data/hora        dur(s)    erro(%)  resultado
     4  22/07 11:02:23     8,00     -1,705  APROVADO
     3  22/07 10:58:48    12,00     +2,239  REPROVADO
     2  22/07 10:39:11     8,00     +0,802  APROVADO
     1  22/07 10:37:36    20,01     +1,380  APROVADO
```

</details>

### 5. Conferir o laudo no banco

```bash
docker exec -it calibracao-postgres psql -U calibracao -d calibracao \
  -c "select id, data_hora, round(erro_percentual::numeric,3) as erro, resultado from ensaio order by id desc limit 5;"
```

---

## Configuração

### Simulador

| Opção | Padrão | Descrição |
|---|---|---|
| `--host` | `127.0.0.1` | endereço de escuta |
| `--porta` | `5000` | porta TCP |
| `--vazao` | `15.0` | vazão nominal em L/min |
| `--ruido` | `0.02` | desvio padrão relativo do ruído (`0` desliga) |
| `--tick` | `0.05` | período de integração, em segundos |
| `--banda-estavel` | `0.05` | desvio máximo para `stable=true` |
| `--aquecimento` | `2.0` | segundos antes de `stable` poder ser `true` |
| `--semente` | — | semente aleatória, para reprodutibilidade |

### Cliente

Argumentos posicionais: `[host] [porta] [duracaoSegundos]`.

Vazão nominal, K, faixa de desvio e cadências ficam em `EnsaioConfig.padrao()`. Conexão
com o banco:

| Variável de ambiente | Padrão (`persistence.xml`) |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/calibracao` |
| `DB_USER` | `calibracao` |
| `DB_PASSWORD` | `calibracao` |

Para ver o SQL emitido pelo Hibernate, mude o nível do logger `org.hibernate.SQL` para
`debug` em `src/main/resources/log4j2.xml`.

> **Acentuação:** a saída do console é ASCII de propósito. O console do Windows costuma
> estar em CP850/CP1252 e nem todo caractere sobrevive à conversão, o que corromperia o
> laudo em máquinas com codepage diferente. Para acentuação, rode `chcp 65001` antes e
> ajuste os textos em `ConsoleObserver`.

---

## Testes

```bash
mvn test
```

28 testes, sem necessidade de simulador nem Docker:

| Suíte | Cobre |
|---|---|
| `MedicaoTest` | volume medido, vazão média, erro percentual, validações |
| `ClassificadorTest` | limite de ±2% inclusivo, tolerância customizada, erro não-finito |
| `PulseGeneratorTest` | faixa de desvio, precisão da contagem, congelamento após `stop()` |
| `EnsaioRepositoryTest` | mapeamento JPA contra H2 em memória, round-trip dos campos |

### Testando o simulador isoladamente

```bash
python -c "import socket,time; s=socket.create_connection(('127.0.0.1',5000)); f=s.makefile('rwb'); e=lambda c:(f.write((c+chr(10)).encode()),f.flush(),print(f.readline().decode().strip())); e('{\"command\":\"START\"}'); time.sleep(3); e('{\"command\":\"READ\"}'); e('{\"command\":\"STOP\"}')"
```

---

## Decisões de projeto

### Alinhamento da janela temporal

O volume de referência **não** é o total acumulado pelo simulador, e sim
`V(t_fim) − V(t_início)`. O total incluiria o que a referência mediu antes de a thread de
pulsos arrancar e depois de ela parar — inflando `V_ref` e empurrando o erro para baixo
**sistematicamente, em todo ensaio**. As duas leituras de borda usam o mesmo padrão
(evento local, depois `READ`), então a latência desloca ambas no mesmo sentido e se cancela
na subtração.

Verificado com ruído desligado: o resíduo médio em ensaios de 30 s ficou em −0,063%, contra
−0,067% previstos apenas por quantização de pulso — ou seja, **viés de janela zero**.

### Quantização de pulso

Contar pulsos inteiros descarta a fração, perdendo em média meio pulso por ensaio. O efeito
relativo é `0,5 / total de pulsos`: 0,25% num ensaio de 8 s, 0,067% em 30 s. Isso é
fisicamente correto (um medidor real não emite meio pulso), mas significa que **ensaios
curtos podem empurrar um medidor limítrofe para o lado errado do ±2%** — argumento para
especificar duração mínima de ensaio.

### Tratamento de falhas

Nenhuma falha derruba a aplicação:

| Situação | Comportamento |
|---|---|
| Conexão recusada | mensagem clara, encerra limpo |
| Timeout (`setSoTimeout`) | leitura de acompanhamento vira aviso; leitura de borda aborta |
| Desconexão (`readLine()` null) | erro explícito indicando fim de stream |
| JSON malformado | `ProtocolException`; a conexão continua utilizável |
| Banco indisponível | ensaio roda e exibe o laudo, sem gravar |

`CommunicationException` (transporte quebrado) e `ProtocolException` (conteúdo inválido)
são distintas porque exigem reações distintas — a segunda não invalida a conexão. As
leituras periódicas são observacionais: perder uma não corrompe a medição, que depende
apenas das duas leituras de borda. Três falhas de comunicação consecutivas abortam.

### Desvio sorteado uma vez por ensaio

O desvio modela imprecisão de fabricação daquela unidade, fixa durante o ensaio. Se fosse
reamostrado a cada pulso viraria ruído, se cancelaria na média e o erro final tenderia a
zero. É o que separa o desvio (sistemático, do medidor sob teste) do ruído (aleatório, da
referência).

### Contagem ancorada no tempo total

`Thread.sleep(20)` sempre dorme um pouco mais que 20 ms. Somar "pulsos deste tick" a cada
iteração acumularia esse atraso ao longo do ensaio. A contagem é recalculada como
`taxa × (agora − início)` a partir do `nanoTime` inicial, de modo que o atraso de um tick é
absorvido pelo seguinte.

---

## Stack

Java 17 · Maven · Jackson · Hibernate 6 (JPA) · PostgreSQL 16 · log4j2 · JUnit 5 ·
H2 (testes) · Python 3 (stdlib) · Docker Compose
