# ServiceInterface — Bancada de Calibração FAE

Aplicação JavaFX que controla a bancada de calibração e verificação de medidores
ultrassônicos de vazão. Conecta-se à BCI (Bench Control Interface) por socket TCP
para acionar válvulas, bombas e sensores, comunica-se com até 20 medidores em
paralelo, e persiste lotes e resultados em PostgreSQL.

---

## 1. Pré-requisitos

| Item | Versão | Observação |
|---|---|---|
| **JDK** | **21** | Obrigatório. O JavaFX incluído em `lib/javafx-sdk-21.0.2` é compilado para a versão 21 e **não carrega em JDK 17 ou anterior**. |
| **PostgreSQL** | 12+ | Banco `ufae_bench_prod` acessível. |
| **Apache Ant** | 1.10+ | Opcional — só para usar o `build.xml`. |
| **JavaFX** | 21.0.2 | Já incluído em `lib/`, não precisa instalar. |
| **Bibliotecas** | — | Já incluídas em `lib/` (Hibernate, Jackson, Medusa, Commons Math). |

Confirme a versão do JDK antes de começar:

```bash
java -version    # precisa mostrar 21.x
javac -version   # precisa mostrar 21.x
```

Se aparecer 17, aponte o `JAVA_HOME` para um JDK 21 — o build compila, mas a
aplicação falha ao iniciar com `UnsupportedClassVersionError`.

### Hardware

A aplicação foi feita para operar contra a bancada física. Sem ela, a interface
abre mas não executa processo: a BCI e os medidores respondem por TCP nos
endereços definidos em `socketConfig.xml`.

---

## 2. Banco de dados

Crie o banco antes do primeiro start:

```sql
CREATE DATABASE ufae_bench_prod;
```

Não é preciso criar tabelas: o Hibernate está com `hbm2ddl.auto=update`
(`conf/hibernate.cfg.xml`) e cria/atualiza o schema sozinho no primeiro start.

> **Atenção — credenciais fixas no código.** `ServiceInterfaceMain.initSys()`
> ignora o que estiver salvo nas preferências e na tela de configuração, e usa
> sempre `127.0.0.1:5432/ufae_bench_prod`, usuário `postgres`, senha `postgres`.
> Para apontar para outro banco hoje é preciso editar
> [`src/main/ServiceInterfaceMain.java`](src/main/ServiceInterfaceMain.java) e
> recompilar.

---

## 3. Configuração de rede

`socketConfig.xml` (na raiz) define os endereços TCP:

```xml
<socket socketId="si">          <!-- a BCI -->
    <ip>192.168.2.6</ip>
    <port>8888</port>
</socket>
<socket socketId="CONVERSOR1">  <!-- primeiro medidor -->
    <ip>192.168.254.100</ip>
    <port>2051</port>
</socket>
```

Os 20 medidores são endereçados sequencialmente a partir de `CONVERSOR1`.
Confirme que a máquina alcança a bancada antes de rodar:

```bash
ping 192.168.2.6
```

Outros arquivos de configuração, todos na raiz: `processConfig.xml` (parâmetros
do processo de calibração), `protocolFields.properties`,
`devicesTagReference.properties`.

---

## 4. Compilar

### Com Ant (recomendado)

```bash
cd fae_bench-release-prod_fcx_bench

ant create-fat-jar      # gera dist/ServiceInterface.jar com as dependências
```

Alvos disponíveis: `clean`, `compile`, `create-jar`, `create-fat-jar`,
`create-run-script`, `run`.

### Sem Ant

```bash
mkdir -p bin
find src -name "*.java" > srcs.txt

javac -proc:none -encoding UTF-8 \
      -cp "$(find lib -name '*.jar' | tr '\n' ':')" \
      -d bin @srcs.txt
```

No Windows (PowerShell/cmd), troque o separador de classpath de `:` para `;`.

> `-proc:none` é necessário: `lib/eclipselink-jpa-modelgen.jar` declara um
> processador de anotações que não está presente, e sem essa flag o `javac`
> aborta com *"Bad service configuration file"*.

---

## 5. Rodar

A aplicação precisa de argumentos de VM para o JavaFX modular. O jeito mais
simples é gerar o script pronto:

```bash
ant create-run-script    # cria dist/run.bat
cd dist
run.bat
```

Ou rodar direto pelo Ant:

```bash
ant run
```

### Linha de comando completa

```bash
java --module-path "lib/javafx-sdk-21.0.2/lib" \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.io=ALL-UNNAMED \
     --add-opens=java.base/java.nio=ALL-UNNAMED \
     --add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
     --add-opens=java.management/sun.management=ALL-UNNAMED \
     --add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED \
     --add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED \
     --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED \
     --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED \
     -jar dist/ServiceInterface.jar
```

Classe principal: `main.ServiceInterfaceMain`.

Execute a partir da pasta que contém `socketConfig.xml`, `processConfig.xml` e
`conf/` — esses arquivos são lidos por caminho relativo ao diretório de trabalho.

### Sequência de operação

1. A janela principal abre e o LED da BCI indica a conexão (verde = conectada).
2. Selecione o lote (*batch*).
3. **Conectar medidores** — os 20 são conectados em paralelo; cada LED fica
   amarelo conforme o respectivo medidor responde.
4. **Purga**, se necessário.
5. **Run** para iniciar a calibração.

---

## 6. Logs

Todo o output da aplicação é gravado em arquivo rotativo, com timestamp, nível e
**nome da thread** — esse último campo é o que permite diagnosticar travamentos,
já que a aplicação roda 20 threads de socket simultâneas.

### Onde ficam

Por padrão em `./logs/` (relativo ao diretório de trabalho). O caminho absoluto é
impresso no console na primeira linha ao iniciar:

```
Log file: C:/.../dist/logs/ProcessLog_0.log  (level=INFO, max 10 x 5MB)
```

Durante um processo, os logs passam a ser gravados por lote e execução:

```
logs/
├── ProcessLog_0.log              <- log geral
└── <ID_DO_LOTE>/
    └── <nº da execução>/
        └── ProcessLog_0.log      <- log daquela execução
```

### Formato

```
2026-08-01 20:23:53.567 INFO    [Meter Socket 3 - Thread] amostra 0
2026-08-01 20:23:53.560 SEVERE  [Main - Thread] java.lang.IllegalStateException: falha simulada
```

Níveis: `SEVERE`, `WARNING`, `INFO`, `FINE`.

### Rotação

10 arquivos de 5 MB por destino (teto de 50 MB), reciclados automaticamente.
`ProcessLog_0.log` é sempre o mais recente. Não é necessária limpeza manual.

### Opções

| Propriedade | Padrão | Efeito |
|---|---|---|
| `-Dfae.log.dir=<caminho>` | `<dir de trabalho>/logs` | Muda a pasta de destino. |
| `-Dfae.log.level=<nível>` | `INFO` | `SEVERE`, `WARNING`, `INFO` ou `FINE`. |

Para investigar um problema, suba a verbosidade e mande os logs para um disco
com espaço:

```bash
java -Dfae.log.level=FINE -Dfae.log.dir=D:/logs_bancada ... -jar dist/ServiceInterface.jar
```

### O que é capturado

Além das chamadas explícitas a `ProcessLoggerUtil`, o `System.out` e o
`System.err` são espelhados para o arquivo — o que inclui todos os
`printStackTrace()` do código, com as pilhas completas. O console continua
funcionando normalmente.

### Ver logs em tempo real

```bash
# PowerShell
Get-Content logs\ProcessLog_0.log -Wait -Tail 50

# Git Bash
tail -f logs/ProcessLog_0.log
```

Filtrar apenas erros, ou uma thread específica:

```bash
grep SEVERE logs/ProcessLog_0.log
grep "Meter Socket 3" logs/ProcessLog_0.log
```

---

## 7. Problemas conhecidos

### `UnsupportedClassVersionError` ao iniciar

JDK menor que 21. Veja a seção 1.

### A janela abre mas os LEDs ficam vermelhos

A BCI não respondeu. Verifique o IP em `socketConfig.xml` e a conectividade de
rede. A janela permanece responsiva nesse cenário — a verificação de conexão roda
em background.

### `Bad service configuration file` ao compilar

Falta a flag `-proc:none`. Veja a seção 4.

### Erro de conexão com o banco

Confirme que o PostgreSQL está no ar em `127.0.0.1:5432` com o banco
`ufae_bench_prod` e credenciais `postgres`/`postgres` — que estão fixas no
código (seção 2).

---

## 8. Estrutura do projeto

```
src/
├── main/          ponto de entrada e máquina de estados principal
├── bciapi/        API de controle da bancada (comandos JSON via socket)
├── metercomm/     comunicação binária com os medidores
├── controller/    lógica de processo, medidores, válvulas, bombas
├── services/      cálculos de calibração
├── si/dbcomm/     modelos JPA e DAOs (Hibernate)
├── view/          telas JavaFX e beans de apresentação
└── util/          utilitários, incluindo ProcessLoggerUtil
conf/              hibernate.cfg.xml
lib/               dependências e JavaFX SDK
rsrc/              imagens e recursos
```

Documentação adicional por módulo: `codebase_description.md`,
`services_description.md`, `si_description.md`, `socket_description.md`,
`view_description.md`, `util_description.md`, `model_description.md`,
`bciapi_documentation.md`, `diagrams.md`.

---

> **Nota:** este README documentava anteriormente um utilitário Python de
> comparação de hashes (`compare_hashes.py`). O script não está presente no
> repositório, e o conteúdo foi substituído pela documentação da aplicação.
