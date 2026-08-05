# Ambiente de teste — build, banco, simulador e execução

Receita para reproduzir o ambiente em que a aplicação foi colocada para rodar
contra o simulador da BCI, sem hardware.

---

## 1. Build

⚠️ **`ant create-fat-jar` NÃO funciona como está.** O [build.xml](../build.xml)
declara `encoding="UTF-8"` na task `<javac>`, mas parte dos fontes está em
**latin-1** — no JDK 21 isso gera **95 erros** de *unmappable character*.

Duas coisas precisam ser corrigidas no `build.xml` para o build por Ant funcionar:

```xml
<!-- 1) encoding correto -->
<javac srcdir="${src.dir}" destdir="${bin.dir}" includeantruntime="false"
       encoding="ISO-8859-1">

<!-- 2) copiar os recursos de src/ (33 .fxml, 30 imagens, 1 .css).
        Hoje só o Eclipse faz isso; o Ant não, e a aplicação não abre sem eles. -->
<copy todir="${bin.dir}">
    <fileset dir="${src.dir}" excludes="**/*.java"/>
</copy>
```

Build manual equivalente (foi o usado):

```bash
cd fae_bench-release-prod_fcx_bench
rm -rf bin dist && mkdir -p bin dist

CP=$(find lib -name '*.jar' | tr '\n' ';')
find src -name "*.java" > srcs.txt
javac -proc:none -nowarn -encoding ISO-8859-1 -cp "$CP" -d bin @srcs.txt

# recursos que o javac não copia
(cd src && find . -type f ! -name "*.java" -exec cp --parents {} ../bin/ \;)
cp -r rsrc conf bin/
cp socketConfig.xml processConfig.xml protocolFields.properties devicesTagReference.properties bin/

printf 'Main-Class: main.ServiceInterfaceMain\n' > bin/MANIFEST.MF
(cd bin && jar cfm ../dist/ServiceInterface.jar MANIFEST.MF .)
```

Resultado: `dist/ServiceInterface.jar` — 530 classes + 33 FXML.

---

## 2. PostgreSQL

Foi usado o **binário portátil** (não instala nada no sistema, roda em porta
própria e é descartável):

```bash
curl -sSL -o pg.zip \
  "https://get.enterprisedb.com/postgresql/postgresql-16.4-1-windows-x64-binaries.zip"
unzip pg.zip          # gera pgsql/

echo postgres > pwfile
pgsql/bin/initdb.exe -D pgdata -U postgres --pwfile=pwfile -E UTF8 --locale=C
pgsql/bin/pg_ctl.exe -D pgdata -o "-p 55432 -c listen_addresses=127.0.0.1" -l pg.log start
```

Parar: `pgsql/bin/pg_ctl.exe -D pgdata stop`

### Banco, schema e seed

```bash
export PGPASSWORD=postgres
PSQL="pgsql/bin/psql.exe -h 127.0.0.1 -p 55432 -U postgres"

$PSQL -c "CREATE DATABASE ufae_bench_test;"
$PSQL -d ufae_bench_test -v ON_ERROR_STOP=1 -f testdb/schema_test_db.sql
$PSQL -d ufae_bench_test -v ON_ERROR_STOP=1 -f testdb/seed_test_db.sql
```

Aplicou limpo: **44 tabelas**, 41 sequences, 20 conversores, lote `LOTE-TESTE-001`.

---

## 3. Apontar a aplicação para o banco de teste

```java
import java.util.prefs.Preferences;
public class SetPrefs {
  public static void main(String[] a) throws Exception {
    Preferences p = Preferences.userRoot().node("util");
    p.put("settingsDataBaseUrl",      "127.0.0.1:55432/ufae_bench_test");
    p.put("settingsDataBaseUserName", "postgres");
    p.put("settingsDataBasePassword", "postgres");
    p.flush();
  }
}
```

⚠️ Se essa conexão falhar, a app cai **silenciosamente** no fallback
`10.55.0.14:5432/ufae_bench_prod` — o banco de **produção**. Confirme sempre na
primeira linha do log:

```
Conectando ao banco de dados. URL: 127.0.0.1:55432/ufae_bench_test | usuario: postgres
```

---

## 4. Simulador da BCI

```bash
cd Simulador && python bci_simulator.py --host 127.0.0.1 --port 8888
```

O [socketConfig.xml](../socketConfig.xml) já está apontando para `127.0.0.1:8888`.
O original está em `socketConfig.xml.bak`. Lembre-se de copiar para `bin/` e
`dist/` se rodar a partir de lá.

---

## 5. Rodar a aplicação

Os **VM args são obrigatórios** — sem `--add-opens java.base/java.lang`, o
Hibernate 5.2 falha no JDK 21 com
`InaccessibleObjectException: module java.base does not "opens java.lang"`.

```bash
CP=$(find lib -name '*.jar' | tr '\n' ';')
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
     -cp "bin;$CP" main.ServiceInterfaceMain
```

### O que se vê quando funciona

- Janela **"Bancada Fluxus"** abre.
- LED **BCI Connect** fica **VERDE**.
- Gauges mostram os valores do `bci_config.json`: pressão jusante ~1.10 Bar,
  montante ~1.20, diferencial ~0.10, temperaturas ~20 °C.
- Gráficos *Temperatura Linha* e *Pressão Linha* plotam em tempo real
  (polling a ~3,3 Hz).
- As 20 posições de medidor ficam **apagadas** — correto: falta o simulador dos
  medidores (canal A.2).

No log (`logs/calibracao_<timestamp>.log`):

```
ETAPA INICIO: initSys() - conexao com banco de dados e socket da BCI
Conectando ao banco de dados. URL: 127.0.0.1:55432/ufae_bench_test
BCI conectada em /127.0.0.1:8888 em 6 ms
TEMPO: etapa 'initSys()' concluida em 2646 ms
SAIDA checkConnectionWithBci(): BCI RESPONDENDO - verificado em 155 ms
BCI conectada. Iniciando thread de leitura de sensores (BenchDataController)
```

---

## 6. Verificação headless (sem abrir a interface)

Existe um harness que exercita as classes reais da aplicação — banco, BCI,
válvulas, bombas, sensores, vazão e contador de pulsos — sem JavaFX visível.
Resultado da última execução: **34 passos OK, 0 falhas**, incluindo:

```
[OK] checkConnectionWithBci() = TRUE
[OK] BenchController.closeLine() = TRUE em 20 ms  <-- SAI DE CLOSELINE
[OK] isFlowPathBlocked() = FALSE
[OK] setFlowRate(2500 L/h) -> estado STABLE
     t+2,8s  vazao= 2492,12 L/h  estabilidade=STABLE
[OK] vazao recalculada dos pulsos = 2495,8 L/h (alvo 2500, erro 0,17%)
```

⚠️ Um harness headless precisa inicializar o toolkit JavaFX mesmo sem janela:
os controllers atualizam beans de view via `Platform.runLater` **mesmo quando só
estão lendo sensores** (`RefMeterController.readRefMeter` → `updateBenchView` →
`RefMeterBean.setFlowRate`). Sem `Platform.startup(...)` isso lança
`IllegalStateException: Toolkit not initialized`.

---

## Ordem de subida

```
1. PostgreSQL         pgsql/bin/pg_ctl.exe -D pgdata -o "-p 55432" -l pg.log start
2. Simuladores        cd Simulador && python run_all.py        <-- BCI + 20 medidores
3. Aplicação          java ... main.ServiceInterfaceMain
```

`run_all.py` sobe a BCI (`127.0.0.1:8888`) e os 20 medidores
(`127.0.0.1:2051..2070`) no mesmo processo, com a vazão dos medidores acoplada ao
estado da BCI. Detalhes em [Simulador/README_MEDIDORES.md](../Simulador/README_MEDIDORES.md).

---

## 7. Roteiro completo, do zero

```bash
cd "fae_bench-release-prod_fcx_bench"

# 1) build
CP=$(find lib -name '*.jar' | tr '\n' ';')
rm -rf bin dist && mkdir -p bin dist
find src -name "*.java" > srcs.txt
javac -proc:none -nowarn -encoding ISO-8859-1 -cp "$CP" -d bin @srcs.txt
(cd src && find . -type f ! -name "*.java" -exec cp --parents {} ../bin/ \;)
cp -r rsrc conf bin/
cp socketConfig.xml processConfig.xml protocolFields.properties devicesTagReference.properties bin/

# 2) banco (PostgreSQL já no ar na 55432)
export PGPASSWORD=postgres
PSQL="pgsql/bin/psql.exe -h 127.0.0.1 -p 55432 -U postgres"
$PSQL -c "DROP DATABASE IF EXISTS ufae_bench_test;" -c "CREATE DATABASE ufae_bench_test;"
$PSQL -d ufae_bench_test -v ON_ERROR_STOP=1 -f testdb/schema_test_db.sql
$PSQL -d ufae_bench_test -v ON_ERROR_STOP=1 -f testdb/seed_test_db.sql

# 3) simuladores (deixe rodando noutro terminal)
cd Simulador && python run_all.py

# 4) aplicação
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
     -cp "bin;$CP" main.ServiceInterfaceMain
```

Na interface: selecione **LOTE-TESTE-001** → **Conectar medidores** (os 20 LEDs
acendem) → **Run**.

### Até onde o ciclo chega hoje

`CONNECT_METERS` → `INITIAL_RUN_CONFIGURATION` → `CLOSELINE` →
`DOWNLOAD_INIT_CALIB_VALUES` → **`ZEROFLOW`**, e para aí: o trim AGC (`0x23`)
estoura o timeout. A causa é um defeito de leitura de socket na aplicação, não do
simulador — veja "Limitação conhecida" em
[Simulador/README_MEDIDORES.md](../Simulador/README_MEDIDORES.md).

### Parar tudo

```bash
# aplicação e simuladores: Ctrl+C nos respectivos terminais
pgsql/bin/pg_ctl.exe -D pgdata stop
```
