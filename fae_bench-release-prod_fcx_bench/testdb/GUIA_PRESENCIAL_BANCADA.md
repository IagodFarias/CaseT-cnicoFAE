# Guia da bancada — passo a passo do zero

## Objetivo deste teste

Receber os dados dos **20 medidores da bancada real** e **gravar tudo num banco
de dados local**, para não sujar o banco de produção da empresa.

No fim você terá:
- um banco de dados local com os dados do teste;
- um arquivo de log com o registro de tudo o que aconteceu;
- o banco de produção exatamente como estava antes.

**Tempo estimado:** 40 min na primeira vez (por causa do download do
PostgreSQL); 15 min nas próximas.

---

## Antes de começar — leia estas 5 coisas

**1. Existem DOIS bancos de dados. Nunca confunda.**

| Apelido | Endereço | O que é | Pode escrever? |
|---|---|---|---|
| **produção** | `127.0.0.1` porta **5432** | banco real da empresa | ❌ **NUNCA** |
| **local** | `127.0.0.1` porta **55432** | cópia sua, descartável | ✅ pode tudo |

Repare: **5432** e **55432** são parecidos, só muda um `5` a mais. **Confira a
porta antes de apertar Enter em qualquer comando.**

**2. Você vai precisar editar UMA linha de código e compilar.**

O endereço do banco está escrito fixo dentro do programa. Não existe botão nem
arquivo de configuração para trocar. Então o roteiro é:

```
editar 1 linha  →  compilar 1 arquivo  →  fazer o teste  →  DESFAZER a linha  →  compilar de novo
```

Não se assuste: é uma linha só, e o comando de compilar é copiar e colar.

**3. A última etapa é obrigatória.**

Se você esquecer de desfazer a alteração, a aplicação vai continuar gravando no
banco local **em vez do de produção** no dia seguinte. A Parte 12 existe para
isso — não pule.

**4. Onde os medidores são configurados.**

Os endereços (IPs) dos 20 medidores **não** estão no arquivo `socketConfig.xml`.
Eles estão **dentro do banco de dados**, numa tabela chamada `conversmodel`. Por
isso o banco local precisa ser uma cópia do de produção — senão o programa não
sabe onde os medidores estão.

**5. Terminal = PowerShell.**

Sempre que este guia disser "rode o comando", é no PowerShell. Como abrir e
usar está na Parte 1.

---

# PARTE 1 — Abrir o terminal (PowerShell)

## Passo 1.1 — Abrir

1. Aperte a tecla **Windows** do teclado.
2. Digite `powershell`.
3. Clique em **Windows PowerShell** (o de ícone azul).

Vai abrir uma janela preta ou azul com um cursor piscando. É isso que este guia
chama de "terminal".

## Passo 1.2 — Como usar

- **Colar um comando:** copie daqui (Ctrl+C), clique dentro da janela do
  PowerShell e aperte **botão direito do mouse**. O texto aparece. Aperte
  **Enter** para executar.
- **Comando de várias linhas:** cole tudo de uma vez, o PowerShell entende.
- **Parar um comando travado:** `Ctrl + C`.
- **Se fechar a janela sem querer:** reabra e refaça o Passo 1.3 e o 3.5.

## Passo 1.3 — Ir até a pasta do projeto

Cole e dê Enter:

```powershell
cd "C:\Users\FARIAS\Documents\2. Iago\fae_bench-release-prod_fcx_bench\CaseT-cnicoFAE\fae_bench-release-prod_fcx_bench"
```

Confira se deu certo:

```powershell
Get-Location
```

**Tem que aparecer:**

```
Path
----
C:\Users\FARIAS\Documents\2. Iago\fae_bench-release-prod_fcx_bench\CaseT-cnicoFAE\fae_bench-release-prod_fcx_bench
```

> ⚠️ **Todos os comandos deste guia dependem de você estar nessa pasta.** Se
> abrir um terminal novo, repita este passo antes de qualquer coisa.

## Passo 1.4 — Conferir o Java

```powershell
java -version
```

**Tem que aparecer** um número começando com **21**, assim:

```
openjdk version "21.0.12" 2026-07-21 LTS
```

- Apareceu 21 → ✅ siga.
- Apareceu 17 ou outro número → **pare** e vá para o erro **E8** no final.

### ✅ Checkpoint 1
- [ ] PowerShell aberto na pasta certa
- [ ] `java -version` mostra 21

---

# PARTE 2 — Instalar o banco de dados local

Você vai instalar uma cópia "portátil" do PostgreSQL: ela não instala nada no
Windows, não mexe no banco de produção, e roda numa porta separada (55432).

> **Só na primeira vez.** Se você já fez isso antes, pule para a Parte 3.

## Passo 2.1 — Verificar se já está instalado

```powershell
Test-Path "$env:LOCALAPPDATA\fae_bench_localdb\pgsql\bin\pg_ctl.exe"
```

- Respondeu **`True`** → já está instalado, **pule para a Parte 3**.
- Respondeu **`False`** → continue no passo 2.2.

## Passo 2.2 — Baixar

Cole tudo de uma vez (são 4 linhas):

```powershell
$root = "$env:LOCALAPPDATA\fae_bench_localdb"
New-Item -ItemType Directory -Force $root | Out-Null
$ProgressPreference = 'SilentlyContinue'
Invoke-WebRequest -Uri "https://get.enterprisedb.com/postgresql/postgresql-16.4-1-windows-x64-binaries.zip" -OutFile "$root\pg.zip"
```

O download tem **340 MB** e demora alguns minutos. **A janela vai parecer
travada — é normal.** Espere o cursor voltar a piscar.

## Passo 2.3 — Descompactar

```powershell
Expand-Archive -Path "$root\pg.zip" -DestinationPath $root -Force
& "$root\pgsql\bin\pg_ctl.exe" --version
```

**Tem que aparecer:**

```
pg_ctl (PostgreSQL) 16.4
```

## Passo 2.4 — Criar o banco vazio ("cluster")

```powershell
$root = "$env:LOCALAPPDATA\fae_bench_localdb"
Set-Content "$root\pwfile" "postgres" -NoNewline -Encoding ascii
& "$root\pgsql\bin\initdb.exe" -D "$root\pgdata" -U postgres --pwfile="$root\pwfile" -E UTF8 --locale=C
```

**No final tem que aparecer:**

```
Sucesso. Você pode iniciar o servidor de banco de dados utilizando:
```

> Vai aparecer um aviso amarelo sobre `autenticação "trust"`. **Ignore** — é
> normal e não é problema de segurança aqui, porque esse banco só aceita conexão
> da própria máquina.

### ✅ Checkpoint 2
- [ ] `pg_ctl --version` respondeu `16.4`
- [ ] `initdb` terminou com "Sucesso"

---

# PARTE 3 — Ligar o banco local

**Isto precisa ser feito toda vez que você liga o computador.**

## Passo 3.1 — Ligar

```powershell
$root = "$env:LOCALAPPDATA\fae_bench_localdb"
& "$root\pgsql\bin\pg_ctl.exe" -D "$root\pgdata" -o "-p 55432 -c listen_addresses=127.0.0.1" -l "$root\pg.log" start
```

**Tem que aparecer:**

```
esperando o servidor iniciar.... feito
servidor iniciado
```

> Se disser que "outro servidor já está rodando", tudo bem — significa que já
> estava ligado.

## Passo 3.2 — Confirmar que está no ar

```powershell
netstat -ano | Select-String ":55432"
```

**Tem que aparecer** uma linha com `LISTENING`:

```
  TCP    127.0.0.1:55432        0.0.0.0:0              LISTENING       22988
```

- Não apareceu nada → vá para o erro **E2**.

## Passo 3.3 — Criar os atalhos da sessão

Esses dois atalhos encurtam os comandos das próximas partes.

```powershell
$PG = "$env:LOCALAPPDATA\fae_bench_localdb\pgsql\bin"
$env:PGPASSWORD = "postgres"
```

Não aparece nada na tela — é normal.

> ⚠️ **Se você fechar o PowerShell, esses atalhos somem.** Ao abrir um terminal
> novo, refaça o Passo 1.3 e este Passo 3.3.

## Passo 3.4 — Testar

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "SELECT version();"
```

**Tem que aparecer** uma linha começando com `PostgreSQL 16.4`.

- Deu erro de senha → erro **E17**.

### ✅ Checkpoint 3
- [ ] `netstat` mostra `55432 ... LISTENING`
- [ ] `SELECT version()` respondeu

---

# PARTE 4 — Descobrir o formato do seu backup

Você tem um arquivo de backup do banco de produção. Existem 4 formatos
possíveis, e o comando de restaurar muda conforme o formato. Esta parte descobre
qual é o seu — **sem alterar nada**.

## Passo 4.1 — Colocar o backup num lugar fácil

Copie o arquivo de backup para uma pasta com caminho curto e **sem espaços no
nome**, por exemplo `C:\backup\`.

## Passo 4.2 — Apontar para ele

Troque o caminho abaixo pelo do **seu** arquivo e rode:

```powershell
$BKP = "C:\backup\ufae_bench_prod.backup"
Test-Path $BKP
```

**Tem que responder `True`.** Se responder `False`, o caminho está errado —
confira o nome e a extensão do arquivo.

Veja o tamanho e a data, para ter certeza de que é o backup certo:

```powershell
Get-Item $BKP | Select-Object Name, @{n='MB';e={[math]::Round($_.Length/1MB,1)}}, LastWriteTime
```

## Passo 4.3 — Teste A: é um arquivo binário do PostgreSQL?

```powershell
& "$PG\pg_restore.exe" -l $BKP | Select-Object -First 12
```

Este comando **só lê** o arquivo, não altera nada.

**Se funcionar**, aparece um cabeçalho assim:

```
;
; Archive created at 2026-08-06 06:12:52
;     dbname: ufae_bench_prod
;     TOC Entries: 293
;     Format: CUSTOM
;     Dumped from database version: 16.4
```

👉 Anote duas coisas: o valor de **`Format:`** e o de
**`Dumped from database version:`**.

**Se der erro** (mensagem em vermelho), faça o Teste B.

## Passo 4.4 — Teste B: é um arquivo de texto?

```powershell
Get-Content $BKP -TotalCount 3
```

**Se aparecer:**

```
--
-- PostgreSQL database dump
--
```

então é um backup em **SQL puro**.

## Passo 4.5 — Concluir qual é o seu caso

| O que aconteceu | Seu formato | Vá para o passo |
|---|---|---|
| Teste A mostrou `Format: CUSTOM` | **custom** | **5.2** |
| Teste A mostrou `Format: TAR` | **tar** | **5.2** |
| Teste A deu erro e o B mostrou `PostgreSQL database dump` | **SQL puro** | **5.3** |
| `$BKP` é uma **pasta** (não um arquivo) com `toc.dat` dentro | **directory** | **5.5** |

> Se a linha `Dumped from database version` for **maior que 16.4** (ex.: 17.x),
> pare e leia o erro **E11** antes de continuar.

### ✅ Checkpoint 4
- [ ] Sei meu formato: `custom` / `tar` / `SQL puro` / `directory`

---

# PARTE 5 — Restaurar o backup no banco LOCAL

> ⚠️ **Todos os comandos desta parte usam `-p 55432`.** Confira antes de cada
> Enter. Um `5432` aqui escreveria em produção.

## Passo 5.1 — Criar o banco vazio

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "DROP DATABASE IF EXISTS ufae_bench_local;"
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "CREATE DATABASE ufae_bench_local;"
```

**Tem que aparecer:**

```
NOTICE:  database "ufae_bench_local" does not exist, skipping
DROP DATABASE
CREATE DATABASE
```

> O `NOTICE` só aparece na primeira vez — significa "não existia, tudo bem".

## Passo 5.2 — Restaurar (formato **custom** ou **tar**)

```powershell
& "$PG\pg_restore.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local --no-owner --no-privileges $BKP
```

**Tem que aparecer... nada.** Silêncio = deu certo. Pode demorar alguns minutos.

Depois **pule para o Passo 5.6**.

## Passo 5.3 — Restaurar (formato **SQL puro**)

Antes, verifique se o arquivo cria o banco sozinho:

```powershell
Select-String -Path $BKP -Pattern "^CREATE DATABASE" | Select-Object -First 3
```

- **Não apareceu nada** → use o comando abaixo e siga para 5.6:

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -v ON_ERROR_STOP=1 -q -f $BKP
```

Vão passar muitas linhas na tela. **Não pode aparecer nenhuma linha com
`ERROR`.** Se aparecer, o comando para sozinho — vá para o erro **E3**.

- **Apareceu `CREATE DATABASE`** → use o Passo 5.4.

## Passo 5.4 — SQL puro que cria o banco sozinho

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d postgres -v ON_ERROR_STOP=1 -q -f $BKP
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "ALTER DATABASE ufae_bench_prod RENAME TO ufae_bench_local;"
```

**Tem que terminar com:** `ALTER DATABASE`

> O nome `ufae_bench_prod` aqui não é perigoso: é outro servidor, na porta
> 55432. Renomeamos justamente para não confundir depois.

## Passo 5.5 — Restaurar (formato **pasta**)

```powershell
& "$PG\pg_restore.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local --no-owner --no-privileges -Fd $BKP
```

## Passo 5.6 — Conferir se veio tudo

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "SELECT count(*) AS tabelas FROM information_schema.tables WHERE table_schema='public';"
```

**Tem que aparecer um número perto de 44:**

```
 tabelas
---------
      44
```

- Veio `0` → a restauração falhou. Erro **E3**.

Agora confira os dados:

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local `
  -c "SELECT count(*) AS conversores FROM conversmodel;" `
  -c "SELECT count(*) AS lotes FROM batchmodel;" `
  -c "SELECT count(*) AS tipos_medidor FROM metertypemodel;"
```

**Tem que aparecer:** `conversores = 20`, e os outros com pelo menos 1.

### ✅ Checkpoint 5
- [ ] Restauração terminou sem `ERROR`
- [ ] ~44 tabelas
- [ ] 20 conversores e pelo menos 1 lote

---

# PARTE 6 — Conferir os endereços dos 20 medidores

**Esta é a parte que decide se o teste vai funcionar.** Se os endereços
estiverem errados, o programa não acha os medidores.

## Passo 6.1 — Ver o que está gravado

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "SELECT id_conversor, string_name, string_ip, int_port, bool_enabled FROM conversmodel ORDER BY id_conversor;"
```

**Tem que aparecer** uma tabela com 20 linhas:

```
 id_conversor | string_name |    string_ip    | int_port | bool_enabled
--------------+-------------+-----------------+----------+--------------
            1 | CONVERSOR1  | 192.168.254.100 |     2051 | t
            2 | CONVERSOR2  | 192.168.254.100 |     2052 | t
           ...
           20 | CONVERSOR20 | 192.168.254.100 |     2070 | t
```

## Passo 6.2 — Comparar com o que você espera

| O que você vê na coluna `string_ip` | Significa | O que fazer |
|---|---|---|
| Endereços da bancada (ex.: `192.168.254.x`) | ✅ certo | siga para a Parte 7 |
| `127.0.0.1` | ❌ é configuração de simulador | Passo 6.3 |
| Menos de 20 linhas | ❌ falta conversor | Passo 6.5 |
| Algum `bool_enabled` = `f` | ❌ conversor desligado | Passo 6.4 |

## Passo 6.3 — Corrigir os endereços (só se estiverem errados)

Primeiro descubra os endereços corretos **lendo** a produção (só leitura, não
altera nada):

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 5432 -U postgres -d ufae_bench_prod -c "SELECT string_name, string_ip, int_port FROM conversmodel ORDER BY id_conversor;"
```

Agora aplique **no banco local** (repare: `55432`):

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "UPDATE conversmodel SET string_ip = '192.168.254.100', int_port = 2050 + id_conversor;"
```

> Esse comando assume "mesmo IP, portas 2051 a 2070". Se na sua bancada cada
> medidor tem um IP diferente, corrija um por um:
> ```powershell
> & "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "UPDATE conversmodel SET string_ip='192.168.254.107', int_port=2051 WHERE string_name='CONVERSOR7';"
> ```

Confira de novo com o comando do Passo 6.1.

## Passo 6.4 — Ligar conversores desabilitados

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "UPDATE conversmodel SET bool_enabled = true WHERE bool_enabled IS DISTINCT FROM true;"
```

## Passo 6.5 — Conferir se existem os 20

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "SELECT string_name FROM conversmodel ORDER BY id_conversor;"
```

**Tem que ir de `CONVERSOR1` até `CONVERSOR20`**, sem faltar nenhum.

## Passo 6.6 — Escolher o lote

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "SELECT id_batch, string_batchid, string_description FROM batchmodel ORDER BY id_batch DESC LIMIT 10;"
```

**Anote o `string_batchid`** do lote que você vai usar. Você vai precisar dele na
Parte 10.

Lote escolhido: `________________________`

### ✅ Checkpoint 6
- [ ] 20 conversores, endereços reais, todos com `t`
- [ ] Lote anotado

---

# PARTE 7 — Alterar o programa para usar o banco local

Agora vem a única edição de código. É **uma linha**.

## Passo 7.1 — Abrir o arquivo

No VS Code (ou Bloco de Notas), abra:

```
src\main\ServiceInterfaceMain.java
```

## Passo 7.2 — Achar a linha 605

No VS Code: aperte **Ctrl+G**, digite `605`, Enter.

Você vai ver este trecho:

```java
			// ================================================================
			// BANCO DE DADOS -- so um destes pode ficar ativo.
			//
			//   EMPRESA (ativo):
			urlDataBase = "127.0.0.1:5432/ufae_bench_prod";        <-- LINHA 605
			//
			//   TESTE local (simulador): comente a linha acima e descomente:
			// urlDataBase = "127.0.0.1:55432/ufae_bench_test";
			// ================================================================
```

## Passo 7.3 — Fazer a troca

Deixe o trecho **exatamente assim** (mudou só a linha 605, e o `//` no começo):

```java
			// ================================================================
			// BANCO DE DADOS -- so um destes pode ficar ativo.
			//
			//   EMPRESA (ativo):
			// urlDataBase = "127.0.0.1:5432/ufae_bench_prod";
			//
			//   TESTE local (simulador): comente a linha acima e descomente:
			urlDataBase = "127.0.0.1:55432/ufae_bench_local";
			// ================================================================
```

O que você fez:
1. Colocou `// ` na frente da linha de **produção** (isso "desliga" a linha);
2. Tirou o `// ` da frente da linha de teste e trocou o nome final para
   `ufae_bench_local`.

**Salve o arquivo:** Ctrl+S.

> 📸 **Tire uma foto ou print desse trecho agora.** Vai ajudar na Parte 12, na
> hora de desfazer.

## Passo 7.4 — Compilar

Volte ao PowerShell (na pasta do projeto) e cole:

```powershell
$CP = "bin;" + ((Get-ChildItem lib -Recurse -Filter *.jar | ForEach-Object { $_.FullName }) -join ";")
javac -proc:none -nowarn -encoding ISO-8859-1 -cp $CP -d bin src\main\ServiceInterfaceMain.java
```

**Tem que aparecer... nada.** Silêncio = compilou. Leva 2 a 5 segundos.

> Isso compila **só esse arquivo**, não o programa inteiro. Não precisa de Ant,
> nem gerar `.jar`.

Confirme que compilou agora:

```powershell
(Get-Item bin\main\ServiceInterfaceMain.class).LastWriteTime
```

**Tem que mostrar a data e hora de agora.**

**Se aparecerem mensagens de erro em vermelho:** você provavelmente apagou uma
aspa ou um ponto e vírgula. Volte ao Passo 7.3 e compare caractere por caractere.
Erro **E19**.

### ✅ Checkpoint 7
- [ ] Linha de produção comentada, linha de teste ativa com `ufae_bench_local`
- [ ] `javac` rodou sem erro
- [ ] Data do `.class` é de agora

---

# PARTE 8 — Conferir o endereço da BCI

A BCI é o equipamento que comanda a bancada (válvulas, bombas, sensores). O
endereço dela fica num arquivo, não no banco.

## Passo 8.1 — Ver o que está configurado

```powershell
Select-String -Path socketConfig.xml -Pattern "socketId|<ip>|<port>" | Select-Object -First 12
```

## Passo 8.2 — O que tem que estar

Logo abaixo de `socketId="si"`, a linha **sem** `<!--` e `-->` em volta tem que
ser o IP da BCI real:

```xml
<socket socketId="si">
    <ip>192.168.2.6</ip>
    <port>8888</port>
</socket>
```

`<!-- ... -->` é a marca de "linha desligada" no XML.

**Se a linha ativa for `127.0.0.1`**, abra o `socketConfig.xml` num editor,
coloque `<!-- -->` em volta dela e tire o dos `192.168.2.6`. Salve.

> O arquivo que vale é o da **pasta raiz**. Existe um `socketConfig.xml` dentro
> de `dist\` também — esse **não** é usado por este roteiro.

### ✅ Checkpoint 8
- [ ] `socketId="si"` com o IP real da BCI, sem comentário

---

# PARTE 9 — Conferir a rede

## Passo 9.1 — Ver seu endereço de rede

```powershell
ipconfig | Select-String "Adaptador|adapter|IPv4"
```

Procure o **Adaptador Ethernet** (cabo). Ele tem que ter um IP na faixa da
bancada, como `192.168.2.xxx`.

> ❌ Estar só no **Wi-Fi** (`192.168.0.xxx`) **não funciona** — não existe
> caminho até a bancada. Se o Ethernet disser "Mídia desconectada", **o cabo
> está fora**.

## Passo 9.2 — Testar a BCI

```powershell
ping 192.168.2.6
```

**Tem que aparecer:** `Resposta de 192.168.2.6: bytes=32 tempo<1ms`

**Se aparecer "Esgotado o tempo limite":** erro **E4**.

## Passo 9.3 — Testar as portas

```powershell
Test-NetConnection 192.168.2.6     -Port 8888
Test-NetConnection 192.168.254.100 -Port 2051
```

**Tem que aparecer** `TcpTestSucceeded : True` nos dois.

### ✅ Checkpoint 9
- [ ] IP Ethernet na faixa da bancada
- [ ] Ping na BCI responde
- [ ] `TcpTestSucceeded: True` nas duas portas

---

# PARTE 10 — Abrir a aplicação e fazer o teste

## Passo 10.1 — Abrir

```powershell
.\rodar_bancada_real_db_local.bat
```

A janela da aplicação abre em alguns segundos.

## Passo 10.2 — ⚠️ A CONFERÊNCIA MAIS IMPORTANTE

Olhe o texto que passou no PowerShell e procure esta linha:

```
Conectando ao banco de dados. URL: 127.0.0.1:55432/ufae_bench_local | usuario: postgres
```

| O que está escrito | Significa | O que fazer |
|---|---|---|
| `55432/ufae_bench_local` | ✅ está no banco local | pode continuar |
| `5432/ufae_bench_prod` | ❌ **está em produção** | **feche a aplicação agora**, erro **E1** |

Logo depois tem que aparecer:

```
Hibernate SessionFactory created successfully!
Retorno de DataBasePersistence.initialize(): true - em XXXX ms
```

> 🔴 Se estiver em produção: só abrir a aplicação **não grava nada**. Feche antes
> de clicar em "Conectar medidores" ou "Run" e nada será afetado.

## Passo 10.3 — Anotar o arquivo de log

Na primeira linha do PowerShell aparece:

```
Arquivo de log desta execucao: C:\...\logs\calibracao_2026-08-06_05-47-39.log
```

**Anote esse caminho.**

## Passo 10.4 — Abrir uma janela para acompanhar o log

Abra um **segundo** PowerShell (Parte 1), vá até a pasta do projeto (Passo 1.3) e
cole:

```powershell
Get-Content (Get-ChildItem logs\calibracao_*.log | Sort-Object LastWriteTime -Descending | Select-Object -First 1).FullName -Wait -Tail 40
```

As linhas vão aparecendo em tempo real. Deixe essa janela aberta o teste inteiro.
Para parar: `Ctrl+C`.

## Passo 10.5 — Conferir a BCI na tela

Na janela da aplicação:
- o LED da BCI fica **verde**;
- os medidores de pressão e temperatura começam a mostrar valores;
- os gráficos começam a desenhar.

No log tem que aparecer:

```
BCI conectada em /192.168.2.6:8888 em X ms
SAIDA checkConnectionWithBci(): BCI RESPONDENDO - verificado em X ms
```

**LED vermelho?** Erro **E4**.

## Passo 10.6 — Selecionar o lote

Na tela, escolha o lote que você anotou no Passo 6.6.

**Lista vazia?** Erro **E12**.

## Passo 10.7 — Conectar os medidores

Clique em **Conectar medidores**.

Os 20 são conectados ao mesmo tempo, e cada posição acende conforme o medidor
responde. É rápido (cada um tem 1 segundo de tolerância).

Na janela do log você vai ver uma linha por medidor:

```
INFO [MEDIDOR-01 ] MeterController - Socket conectado ao medidor em 192.168.254.100:2051 em 7 ms
INFO [MEDIDOR-02 ] MeterController - Socket conectado ao medidor em 192.168.254.100:2052 em 0 ms
```

## Passo 10.8 — Contar quantos conectaram

Abra um **terceiro** PowerShell (ou use o primeiro), vá até a pasta do projeto e
cole:

```powershell
$log = (Get-ChildItem logs\calibracao_*.log | Sort-Object LastWriteTime -Descending | Select-Object -First 1).FullName
(Select-String -Path $log -Pattern "Socket conectado ao medidor").Count
```

**Tem que aparecer: `20`**

Veja se algum falhou:

```powershell
Select-String -Path $log -Pattern "FALHA: socket NAO conectou"
```

**Tem que aparecer... nada.**

- Deu menos de 20 → erro **E5**.

## Passo 10.9 — Conferir que os dados estão chegando

```powershell
(Select-String -Path $log -Pattern "Iniciando a thread de leitura").Count
```

**Tem que aparecer: `20`**

Para olhar um medidor específico (por exemplo o 7):

```powershell
Select-String -Path $log -Pattern "MEDIDOR-07" | Select-Object -Last 20
```

Para procurar erros:

```powershell
Select-String -Path $log -Pattern "ERROR|SEVERE" | Select-Object -Last 30
```

Um ou outro erro isolado pode acontecer. O que preocupa é o **mesmo medidor**
repetindo erro.

### ✅ Checkpoint 10
- [ ] Log mostrou `55432/ufae_bench_local`
- [ ] LED da BCI verde
- [ ] Contagem de conexões = **20**
- [ ] Nenhum `FALHA: socket NAO conectou`
- [ ] 20 threads de leitura

---

# PARTE 11 — (Opcional) Rodar o processo e provar que gravou certo

Faça esta parte só se quiser executar a calibração/verificação de verdade.

## Passo 11.1 — Anotar os números ANTES

No banco **local**:

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local `
  -c "SELECT count(*) AS bateladas FROM bateladamodel;" `
  -c "SELECT count(*) AS medidores FROM metermodel;" `
  -c "SELECT count(*) AS amostras FROM meterdatamodel;"
```

Anote: bateladas `____`  medidores `____`  amostras `____`

No banco de **produção** (só leitura):

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 5432 -U postgres -d ufae_bench_prod -c "SELECT count(*) AS amostras_producao FROM meterdatamodel;"
```

Anote: produção `____`

## Passo 11.2 — Executar

Na aplicação: **Purga** (se necessário) → **Run**.

Acompanhe os estados na janela do log. Eles mudam assim:

```
INITIAL_RUN_CONFIGURATION → CLOSELINE → ZEROFLOW → CALCULATE_ZEROFLOW → ... → SAVE_METER_DATA_IN_DB
```

**Travou em `ZEROFLOW`?** Erro **E10**.

## Passo 11.3 — Conferir os números DEPOIS

Repita os dois comandos do Passo 11.1.

**Tem que acontecer:**
- banco **local**: os três números **aumentaram** ✅
- banco de **produção**: número **igual** ao que você anotou ✅

Amostras por medidor:

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -d ufae_bench_local -c "SELECT id_meter, count(*) AS amostras FROM meterdatamodel GROUP BY id_meter ORDER BY id_meter;"
```

## Passo 11.4 — Prova final: produção nunca foi tocada

Rode **com a aplicação ainda aberta**:

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 5432 -U postgres -d ufae_bench_prod -c "SELECT pid, application_name, client_addr, state FROM pg_stat_activity WHERE datname='ufae_bench_prod';"
```

**Tem que aparecer** só a sua própria consulta. **Não pode** aparecer nenhuma
linha com `PostgreSQL JDBC Driver` — isso seria a aplicação conectada em
produção.

### ✅ Checkpoint 11
- [ ] Números do banco local aumentaram
- [ ] Número de produção igual ao anotado
- [ ] Nenhum `JDBC Driver` em produção

---

# PARTE 12 — 🔴 DESFAZER (obrigatório)

**Não pule esta parte.** Se você esquecer, amanhã a aplicação vai gravar no banco
local em vez do de produção.

## Passo 12.1 — Fechar a aplicação

Feche a janela da aplicação.

## Passo 12.2 — Guardar as evidências do teste

```powershell
$destino = "C:\evidencias_teste_$(Get-Date -Format yyyy-MM-dd)"
New-Item -ItemType Directory -Force $destino | Out-Null
Copy-Item logs\* $destino -Recurse -Force
Get-ChildItem $destino
```

Se quiser levar também o banco do teste:

```powershell
& "$PG\pg_dump.exe" -h 127.0.0.1 -p 55432 -U postgres -Fc -f "$destino\resultado_teste.dump" ufae_bench_local
```

## Passo 12.3 — Desfazer a alteração no código

Abra de novo `src\main\ServiceInterfaceMain.java`, vá na linha 605 (Ctrl+G) e
deixe **exatamente como estava**:

```java
			// ================================================================
			// BANCO DE DADOS -- so um destes pode ficar ativo.
			//
			//   EMPRESA (ativo):
			urlDataBase = "127.0.0.1:5432/ufae_bench_prod";
			//
			//   TESTE local (simulador): comente a linha acima e descomente:
			// urlDataBase = "127.0.0.1:55432/ufae_bench_test";
			// ================================================================
```

Ou seja: tire o `// ` da linha de produção e coloque de volta na linha de teste.
**Salve** (Ctrl+S).

> Se o projeto estiver no Git, dá para desfazer com um comando só:
> ```powershell
> git checkout -- src/main/ServiceInterfaceMain.java
> ```

## Passo 12.4 — Compilar de novo

```powershell
$CP = "bin;" + ((Get-ChildItem lib -Recurse -Filter *.jar | ForEach-Object { $_.FullName }) -join ";")
javac -proc:none -nowarn -encoding ISO-8859-1 -cp $CP -d bin src\main\ServiceInterfaceMain.java
```

Silêncio = compilou.

## Passo 12.5 — CONFIRMAR que voltou ao normal

Abra a aplicação do jeito de sempre (o atalho / `fcx.bat`) e confira no log:

```
Conectando ao banco de dados. URL: 127.0.0.1:5432/ufae_bench_prod
```

**Tem que dizer `5432/ufae_bench_prod`.** Se ainda disser `ufae_bench_local`,
você esqueceu de compilar (Passo 12.4).

## Passo 12.6 — Desligar o banco local (opcional)

```powershell
& "$env:LOCALAPPDATA\fae_bench_localdb\pgsql\bin\pg_ctl.exe" -D "$env:LOCALAPPDATA\fae_bench_localdb\pgdata" stop
```

Para apagar tudo do teste: exclua a pasta `%LOCALAPPDATA%\fae_bench_localdb`.

### ✅ Checkpoint 12
- [ ] Código voltou ao original
- [ ] Recompilado
- [ ] **Log confirmou `5432/ufae_bench_prod`**
- [ ] Evidências salvas

---

# Ficha para imprimir

Data: `____/____/______`   Responsável: `______________________`

| # | Verificação | OK? | Observação |
|---|---|---|---|
| 1 | PowerShell na pasta certa / Java 21 | ☐ | |
| 2 | PostgreSQL local instalado | ☐ | |
| 3 | Banco local ligado na 55432 | ☐ | |
| 4 | Formato do backup identificado | ☐ | |
| 5 | Restaurado: 44 tabelas, 20 conversores | ☐ | |
| 6 | Endereços dos medidores conferidos | ☐ | |
| 7 | Código alterado e compilado | ☐ | |
| 8 | `socketConfig.xml` com a BCI real | ☐ | |
| 9 | Rede: ping + portas OK | ☐ | |
| 10 | Log mostrou `ufae_bench_local` | ☐ | |
| 10 | 20/20 medidores conectados | ☐ | |
| 11 | Local cresceu / produção inalterada | ☐ | |
| 12 | **Código desfeito e recompilado** | ☐ | |

Lote usado: `____________________`
Arquivo de log: `_________________________________________`

---

# ERROS E TRATATIVAS

## E1 — O log mostra `ufae_bench_prod` (risco de gravar em produção)

**O que fazer agora:** feche a aplicação **antes** de clicar em "Conectar
medidores" ou "Run". Só abrir não grava nada.

| Causa | Como confirmar | Solução |
|---|---|---|
| Esqueceu de compilar depois de editar | `(Get-Item bin\main\ServiceInterfaceMain.class).LastWriteTime` é antiga | refaça o Passo 7.4 |
| Editou a linha errada | abra o arquivo na linha 605 | refaça o Passo 7.3 |
| Comentou as duas linhas por engano | as duas começam com `//` | uma tem que ficar sem `//` |
| Abriu pelo `.jar` de `dist\` | você rodou `java -jar` | use `.\rodar_bancada_real_db_local.bat` |

## E2 — O banco local não liga na porta 55432

```powershell
Get-Content "$env:LOCALAPPDATA\fae_bench_localdb\pg.log" -Tail 20
```

| Mensagem no `pg.log` | Solução |
|---|---|
| `Address already in use` | outra coisa usa a 55432: `netstat -ano \| Select-String ":55432"` e feche o processo |
| `database files are incompatible` | apague a pasta `pgdata` e refaça o Passo 2.4 |
| arquivo vazio | o comando nem rodou: confira o caminho do Passo 3.1 |

## E3 — A restauração falhou ou veio incompleta

| Causa | Solução |
|---|---|
| Restaurou no banco errado | confira `-d ufae_bench_local` e `-p 55432` |
| Erro de dono (`role ... does not exist`) | veja **E15** |
| Banco não estava vazio (`already exists`) | veja **E14** |
| Backup só tem dados, sem estrutura | precisa de um backup completo |
| Arquivo corrompido | `& "$PG\pg_restore.exe" -l $BKP` — se der erro de leitura, o arquivo está danificado |

Recomece sempre do zero: Passo 5.1 (DROP + CREATE) e depois o restore.

## E4 — LED da BCI vermelho / não responde ao ping

| Causa | Como confirmar | Solução |
|---|---|---|
| Cabo de rede fora | `ipconfig` sem IP `192.168.2.x` | conectar o cabo — Wi-Fi não serve |
| IP errado no XML | Parte 8 | ativar a linha `<ip>192.168.2.6</ip>` |
| Rodou de outra pasta | — | o arquivo é lido da pasta onde você está; rode da raiz |
| Porta bloqueada | `Test-NetConnection 192.168.2.6 -Port 8888` = False | firewall/switch |
| Outra cópia do programa aberta | — | feche as outras e reabra |
| BCI travada | ping responde mas o LED continua vermelho | reiniciar a BCI |

## E5 — Menos de 20 medidores conectam

**Mensagem no log:** `FALHA: socket NAO conectou ao medidor em <ip>:<porta>`

| Causa | Solução |
|---|---|
| Endereço errado no banco | Passo 6.3 e reconecte |
| Conversor desabilitado | Passo 6.4 |
| Medidor sem energia / cabo solto | o log diz a posição em `[MEDIDOR-nn]` |
| Conexão presa de um teste anterior | desconecte na tela, espere 30 s, reconecte; se insistir, reinicie o conversor |
| Rede instável | `Test-NetConnection <ip> -Port <porta>` na posição que falhou |

Para ver quais conectaram:

```powershell
Select-String -Path $log -Pattern "Socket conectado ao medidor" | ForEach-Object { ($_.Line -split "medidor em ")[1] }
```

## E6 — Erro `NullPointerException` ao conectar medidores

**Causa:** falta a linha de algum conversor no banco.

**Solução:** Passo 6.5 — tem que existir de `CONVERSOR1` a `CONVERSOR20`.

## E8 — `UnsupportedClassVersionError` ou Java errado

**Causa:** o Windows está usando um Java mais antigo que o 21.

```powershell
$env:JAVA_HOME = "C:\caminho\do\jdk-21"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
```

Tem que mostrar `21.x`. Rode o `.bat` **nessa mesma janela**.

## E10 — O processo trava em `ZEROFLOW`

O programa espera **todos** os medidores completarem as amostras. Se um parar de
responder, ele espera para sempre.

| Causa | Solução |
|---|---|
| Um medidor parou de transmitir | `Select-String $log -Pattern "MEDIDOR-" \| Select-Object -Last 40` — a posição que **não** aparece é a suspeita; desconecte-a e refaça o Run |
| Vazamento na linha | o log traz `vazao zero FORA dos limites estaticos ... Possivel vazamento` e o LED de vazamento fica vermelho |
| Tempo configurado muito alto | `SELECT long_zero_flow_time FROM processconfigmodel;` (o valor está em **milissegundos**) |

## E11 — `server version mismatch`

**Causa:** o backup veio de uma versão do PostgreSQL mais nova que a 16.4.

**Solução:** baixe os binários portáteis da versão maior (mesmo link do Passo
2.2, trocando o número da versão) e refaça a restauração com eles.

Use **sempre** os programas de `%LOCALAPPDATA%\fae_bench_localdb\pgsql\bin\` —
nunca um `psql` que já exista no computador.

## E12 — A lista de lotes está vazia na tela

| Causa | Solução |
|---|---|
| Banco local sem lotes | Passo 6.6 — se der 0, o backup não tinha lotes |
| Conectou no banco errado | Passo 10.2 |

## E14 — `ERROR: relation "xxx" already exists` no restore

**Causa:** você restaurou por cima de um banco que já tinha dados.

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "DROP DATABASE IF EXISTS ufae_bench_local;"
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "CREATE DATABASE ufae_bench_local;"
```

E repita o comando de restauração.

## E15 — `ERROR: role "xxx" does not exist` no restore

**Causa:** o dono das tabelas em produção não existe no seu banco local.

**Solução:** use `--no-owner --no-privileges` no comando de restauração (já está
assim no Passo 5.2). Se o backup for SQL puro, peça um novo no formato custom.

## E16 — `database is being accessed by other users`

**Causa:** alguma janela ainda está conectada no banco.

```powershell
& "$PG\psql.exe" -h 127.0.0.1 -p 55432 -U postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='ufae_bench_local' AND pid <> pg_backend_pid();"
```

Depois repita o `DROP DATABASE`.

## E17 — `password authentication failed`

**Causa:** você fechou o PowerShell e perdeu os atalhos.

**Solução:** refaça o Passo 1.3 e o Passo 3.3.

## E18 — O `.bat` abre e fecha na hora

| Causa | Solução |
|---|---|
| Duplo clique numa pasta errada | rode pelo PowerShell, a partir da raiz do projeto |
| Java fora do PATH | **E8** |

Rodando pelo PowerShell a janela não fecha, e você consegue ler a mensagem.

## E19 — Erro em vermelho ao compilar (`javac`)

**Causa:** algum caractere foi apagado sem querer na edição.

A mensagem indica a linha, por exemplo:

```
src\main\ServiceInterfaceMain.java:605: error: ';' expected
```

**Solução:** abra o arquivo nessa linha e compare com o modelo do Passo 7.3. Os
erros mais comuns são: aspas faltando, ponto e vírgula no fim, ou ter apagado a
palavra `urlDataBase`.

Se estiver no Git e quiser começar do zero:

```powershell
git checkout -- src/main/ServiceInterfaceMain.java
```

e refaça a Parte 7.
