# Bancada REAL + banco de dados LOCAL

Como receber os dados dos 20 medidores da bancada física **sem gravar nada no
banco de produção** (`127.0.0.1:5432/ufae_bench_prod`).

Nada é recompilado além de **um único arquivo** (`ServiceInterfaceMain.java`),
já compilado em `bin/`. Não é preciso rodar `ant`, nem gerar o `.jar`.

---

## 0. O princípio

| O que | De onde vem | O que muda neste teste |
|---|---|---|
| Endereço da **BCI** | `socketConfig.xml`, socket `si` | nada — continua a bancada real |
| Endereço dos **20 medidores** | tabela **`conversmodel` do banco em uso** | precisa conter os IPs/portas reais |
| **Banco de dados** | fixo no código (`ServiceInterfaceMain`, linha 605) | editar a linha e recompilar o arquivo — seção 3 |

O ponto que costuma passar despercebido: **os IPs dos medidores saem do banco**,
não do `socketConfig.xml` (`MeterController.initiateComm()` →
`meter.getConversor().getIp()`). Se o banco local tiver os IPs do simulador
(`127.0.0.1`), a aplicação vai conectar no simulador e não na bancada — por isso
a seção 2 insiste em clonar o `conversmodel` de produção.

---

## 1. Pré-requisitos

- JDK 21 (`java -version` → 21.x).
- `bin/` compilado (já está no repositório de trabalho).
- Acesso de leitura ao banco de produção, se for clonar (opção A).
- ~700 MB livres para o PostgreSQL portátil.

---

## 2. Montar o banco local

Usa-se o PostgreSQL **portátil**, em **porta 55432** — não instala serviço, não
conflita com o PostgreSQL de produção na 5432 e é descartável.

```powershell
# 2.1 baixar e extrair (uma vez só)
$root = "$env:LOCALAPPDATA\fae_bench_localdb"
New-Item -ItemType Directory -Force $root | Out-Null
Invoke-WebRequest -Uri "https://get.enterprisedb.com/postgresql/postgresql-16.4-1-windows-x64-binaries.zip" -OutFile "$root\pg.zip"
Expand-Archive -Path "$root\pg.zip" -DestinationPath $root -Force

# 2.2 criar o cluster e subir na 55432
Set-Content "$root\pwfile" "postgres" -NoNewline -Encoding ascii
& "$root\pgsql\bin\initdb.exe" -D "$root\pgdata" -U postgres --pwfile="$root\pwfile" -E UTF8 --locale=C
& "$root\pgsql\bin\pg_ctl.exe" -D "$root\pgdata" -o "-p 55432 -c listen_addresses=127.0.0.1" -l "$root\pg.log" start
```

Parar depois: `& "$root\pgsql\bin\pg_ctl.exe" -D "$root\pgdata" stop`

### Opção A — clonar produção (recomendada para a bancada real)

Traz os conversores, lotes, tipos de medidor e configurações **reais**. `pg_dump`
é somente leitura: não altera o banco de produção.

```powershell
$root = "$env:LOCALAPPDATA\fae_bench_localdb"
$env:PGPASSWORD = "postgres"

# dump da producao (5432) -- so leitura
& "$root\pgsql\bin\pg_dump.exe" -h 127.0.0.1 -p 5432 -U postgres -Fc -f "$root\prod.dump" ufae_bench_prod

# restaura no banco local (55432)
& "$root\pgsql\bin\psql.exe"    -h 127.0.0.1 -p 55432 -U postgres -c "CREATE DATABASE ufae_bench_local;"
& "$root\pgsql\bin\pg_restore.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local "$root\prod.dump"
```

> Se quiser começar com o histórico limpo, restaure só o schema
> (`pg_restore --schema-only`) e depois copie apenas as tabelas de configuração:
> `conversmodel`, `metertypemodel`, `processconfigmodel`, `flowratemodel`,
> `valvemodel`, `pumpmodel`, `refmetermodel`, `ramalmodel`, `batchmodel`.

### Opção B — schema + seed do repositório

Sem acesso à produção. **Atenção:** o seed vem com os conversores apontando para
`127.0.0.1:2051-2070` (simulador); é obrigatório trocar pelos endereços reais.

```powershell
$root = "$env:LOCALAPPDATA\fae_bench_localdb"; $env:PGPASSWORD = "postgres"
$psql = "$root\pgsql\bin\psql.exe"
& $psql -h 127.0.0.1 -p 55432 -U postgres -c "CREATE DATABASE ufae_bench_local;"
& $psql -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -v ON_ERROR_STOP=1 -f testdb\schema_test_db.sql
& $psql -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -v ON_ERROR_STOP=1 -f testdb\seed_test_db.sql
```

Agora corrija os endereços dos 20 conversores com os valores reais (exemplo com
IP fixo e portas 2051..2070 — confira os seus em produção):

```sql
UPDATE conversmodel SET string_ip = '192.168.254.100',
                        int_port  = 2050 + id_conversor;
SELECT string_name, string_ip, int_port FROM conversmodel ORDER BY id_conversor;
```

Para saber os valores corretos, consulte a produção (somente leitura):

```sql
SELECT string_name, string_ip, int_port, bool_enabled FROM conversmodel ORDER BY id_conversor;
```

---

## 3. Trocar o banco: edição manual + recompilação

A URL do banco é **fixa no código**. Não há arquivo de configuração, variável de
ambiente nem tela que a altere — a tela "Configuração > Configurações data base"
grava nas Preferences, mas `initSys()` força `connectionSuccess = false` e
descarta o que foi lido. Trocar de banco exige **editar e recompilar**.

### 3.1 Editar

[`src/main/ServiceInterfaceMain.java`](../src/main/ServiceInterfaceMain.java),
linha **605**. Comente a linha de produção e ative a de teste:

```java
			//   EMPRESA (ativo):
			// urlDataBase = "127.0.0.1:5432/ufae_bench_prod";
			//
			//   TESTE local (simulador): comente a linha acima e descomente:
			urlDataBase = "127.0.0.1:55432/ufae_bench_local";
```

Exatamente **uma** das duas linhas pode ficar sem `//`.

### 3.2 Recompilar só esse arquivo (~2 s)

Não é preciso `ant`, nem `create-fat-jar`, nem recompilar o projeto todo.

**PowerShell:**

```powershell
$CP = "bin;" + ((Get-ChildItem lib -Recurse -Filter *.jar | ForEach-Object { $_.FullName }) -join ";")
javac -proc:none -nowarn -encoding ISO-8859-1 -cp $CP -d bin src\main\ServiceInterfaceMain.java
```

**Git Bash:**

```bash
CP="bin;$(find lib -name '*.jar' | tr '\n' ';')"
javac -proc:none -nowarn -encoding ISO-8859-1 -cp "$CP" -d bin src/main/ServiceInterfaceMain.java
```

Saída vazia = compilou. Confirme a data do `.class`:

```powershell
(Get-Item bin\main\ServiceInterfaceMain.class).LastWriteTime
```

- `-proc:none` é obrigatório: `lib/eclipselink-jpa-modelgen.jar` declara um
  processador de anotações ausente e o `javac` aborta sem essa flag.
- `-encoding ISO-8859-1` é obrigatório: parte dos fontes está em latin-1.
- O `.jar` em `dist/` **não** é atualizado por esse comando — por isso o teste
  roda a partir de `bin/`.

### 3.3 Desfazer depois do teste (obrigatório)

Reverta a linha 605 para produção e **recompile de novo** com o mesmo comando.
No Git:

```powershell
git checkout -- src/main/ServiceInterfaceMain.java
```

Depois confirme abrindo a aplicação normal e lendo no log:
`Conectando ao banco de dados. URL: 127.0.0.1:5432/ufae_bench_prod`.

---

## 4. Apontar para a bancada real

`socketConfig.xml` (raiz), socket `si` — deixe ativo o IP da BCI real:

```xml
<socket socketId="si">
    <ip>192.168.2.6</ip>
    <port>8888</port>
</socket>
```

Confirme a rede antes: `ping 192.168.2.6`.

> A aplicação lê `./socketConfig.xml` **relativo ao diretório de trabalho** —
> rode sempre a partir da raiz do projeto.

---

## 5. Rodar

```bat
rodar_bancada_real_db_local.bat
```

O script sobe o PostgreSQL local se não estiver no ar, faz ping na BCI e abre a
interface a partir de `bin/`. **Ele não escolhe o banco** — o banco é o que foi
compilado na seção 3.

Na tela: selecione o lote → **Conectar medidores** → (Purga, se precisar) → **Run**.

---

## 6. Confirmar que está gravando no banco LOCAL

**No console/log, logo no início** (`logs/calibracao_<timestamp>.log`):

```
INFO  ... Conectando ao banco de dados. URL: 127.0.0.1:55432/ufae_bench_local | usuario: postgres
Hibernate SessionFactory created successfully!
INFO  ... Retorno de DataBasePersistence.initialize(): true - em XXXX ms
```

Se a URL disser `5432/ufae_bench_prod`, a aplicação está em produção — feche
antes de conectar medidores ou apertar Run, e refaça a seção 3 (o mais comum é
ter editado e esquecido de recompilar).

**Prova adicional pelo lado do banco** — contagens antes e depois do Run:

```sql
-- no banco LOCAL (porta 55432): tem que crescer
SELECT count(*) FROM meterdatamodel;
SELECT count(*) FROM bateladamodel;

-- no banco de PRODUCAO (porta 5432): tem que ficar igual
SELECT count(*) FROM meterdatamodel;
```

Também dá para verificar quem está conectado no banco de produção enquanto o
teste roda — a aplicação não pode aparecer:

```sql
SELECT datname, application_name, client_addr FROM pg_stat_activity WHERE datname = 'ufae_bench_prod';
```

---

## 7. Confirmar que os 20 medidores estão chegando

No log, uma linha por medidor na conexão:

```
INFO [127.0.0.1  ] MeterController - Socket conectado ao medidor em <ip>:<porta> em 7 ms
```

```bash
# quantos conectaram (esperado: 20)
grep -c "Socket conectado ao medidor" logs/calibracao_*.log

# quais falharam
grep "FALHA: socket NAO conectou" logs/calibracao_*.log

# leitura em andamento, por medidor
grep "MEDIDOR-07" logs/calibracao_*.log | tail -20

# acompanhar ao vivo
Get-Content logs\calibracao_*.log -Wait -Tail 50
```

O campo `[MEDIDOR-nn]` no log identifica a posição da bancada; `[Meter Socket n
- Thread]` identifica a thread — é o que permite achar um medidor travado.

E pelo banco, dados por medidor:

```sql
SELECT id_meter, count(*) AS amostras
FROM meterdatamodel GROUP BY id_meter ORDER BY id_meter;
```

---

## 8. Voltar ao normal

- **Obrigatório:** reverter a linha 605 de `ServiceInterfaceMain.java` para
  `127.0.0.1:5432/ufae_bench_prod` e **recompilar** (seção 3.3). Enquanto isso
  não for feito, a aplicação continua gravando no banco local.
- Confirmar abrindo pelo `fcx.bat` / atalho de sempre e lendo no log:
  `Conectando ao banco de dados. URL: 127.0.0.1:5432/ufae_bench_prod`.
- Parar o banco local:
  `& "$env:LOCALAPPDATA\fae_bench_localdb\pgsql\bin\pg_ctl.exe" -D "$env:LOCALAPPDATA\fae_bench_localdb\pgdata" stop`
- Descartar o banco local: apague a pasta `%LOCALAPPDATA%\fae_bench_localdb`.
- Restaurar o `socketConfig.xml` original, se tiver mexido:
  `copy socketConfig.xml.real socketConfig.xml`

---

## 9. O que já foi validado (06/08/2026)

Validado nesta máquina, com o **simulador** no lugar da bancada (a bancada não
estava alcançável: a máquina estava na Wi-Fi `192.168.0.6`, sem rota para
`192.168.2.6`). O caminho de código exercitado é o mesmo da bancada real —
muda só o endereço no `conversmodel` e no `socketConfig.xml`.

| Item | Resultado |
|---|---|
| Banco local (PostgreSQL 16.4 portátil, 55432) | criado, **44 tabelas**, seed aplicado |
| Troca do banco por edição + recompilação de 1 arquivo | OK — log: `URL: 127.0.0.1:55432/...` |
| Reversão da linha e recompilação | OK — log: `URL: 127.0.0.1:5432/ufae_bench_prod` |
| Interface JavaFX abre com o banco local | OK — janela "Bancada Fluxus", gauges e gráficos plotando |
| Conexão com os 20 medidores | **20/20** conectados, **20/20** transmitindo em 202 ms |
| Gravação chegando ao banco local | OK — `bateladamodel` passou de 0 para 1 registro |
| Banco de produção | intocado (nem existe nesta máquina; nenhuma conexão na 5432) |

**Não validado até o fim:** o ciclo completo parou em `ZEROFLOW`, na espera
`while (!reachedNumSamp || !checkAllMetersSampleCount());`
([ProcessController.java:452](../src/controller/ProcessController.java#L452)) —
os medidores **simulados** não fecham a contagem de amostras. Consequência: não
se chegou a `SAVE_METER_DATA_IN_DB`, então `meterdatamodel` e `metermodel`
ficaram vazios. É limitação do simulador, sem relação com o desvio de banco; com
os medidores reais essa espera é satisfeita. Confirme na bancada com a query da
seção 7.

---

## 10. Erros comuns

| Sintoma | Causa | Correção |
|---|---|---|
| Log mostra `ufae_bench_prod` | linha 605 não editada, ou editada e **não recompilada** | seção 3; confira `(Get-Item bin\main\ServiceInterfaceMain.class).LastWriteTime` |
| `Error calling Driver#connect` na 55432 | cluster local parado | `pg_ctl ... start`; veja `%LOCALAPPDATA%\fae_bench_localdb\pg.log` |
| Medidores conectam em `127.0.0.1` | `conversmodel` do banco local ainda com o seed do simulador | seção 2, opção B |
| `relation "batchmodel" does not exist` | banco local criado sem schema | reaplique `schema_test_db.sql` |
| BCI vermelha | IP errado em `socketConfig.xml` ou rede | `ping 192.168.2.6` |
| `UnsupportedClassVersionError` | JDK < 21 | aponte `JAVA_HOME` para o JDK 21 |
