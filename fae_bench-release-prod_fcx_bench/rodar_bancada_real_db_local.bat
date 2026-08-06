@echo off
REM ===========================================================================
REM  rodar_bancada_real_db_local.bat
REM
REM  Roda a aplicacao contra a BANCADA REAL (BCI + 20 medidores fisicos)
REM  usando o build que ja esta em bin\. NAO recompila nada.
REM
REM  ATENCAO -- este script NAO escolhe o banco de dados.
REM  O endereco do banco esta FIXO NO CODIGO, em:
REM      src\main\ServiceInterfaceMain.java   (linha ~605)
REM  Para gravar no banco LOCAL em vez do de PRODUCAO e preciso editar essa
REM  linha e recompilar o arquivo ANTES de rodar este script.
REM  Passo a passo: testdb\README_BANCADA_REAL.md  secao 3.
REM
REM  Confira SEMPRE, no log, a linha:
REM      "Conectando ao banco de dados. URL: ..."
REM  Se disser ufae_bench_prod, feche antes de conectar medidores ou dar Run.
REM ===========================================================================
setlocal enabledelayedexpansion

cd /d "%~dp0"

set "PG_ROOT=%LOCALAPPDATA%\fae_bench_localdb"
set "PG_PORT=55432"

echo.
echo === 1/3  PostgreSQL local (porta %PG_PORT%) ===
netstat -ano | findstr ":%PG_PORT%" | findstr "LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo     ja esta no ar.
) else (
    if exist "%PG_ROOT%\pgsql\bin\pg_ctl.exe" (
        start "PostgreSQL %PG_PORT%" /min "%PG_ROOT%\pgsql\bin\pg_ctl.exe" -D "%PG_ROOT%\pgdata" -o "-p %PG_PORT% -c listen_addresses=127.0.0.1" -l "%PG_ROOT%\pg.log" start
        timeout /t 5 /nobreak >nul
        echo     iniciado.
    ) else (
        echo     [AVISO] cluster local nao encontrado em:
        echo             %PG_ROOT%\pgsql
        echo             Monte o banco local antes: testdb\README_BANCADA_REAL.md
        pause
    )
)

echo.
echo === 2/3  Bancada real ===
for /f "tokens=2 delims=<>" %%I in ('findstr /r "<ip>" socketConfig.xml ^| findstr /v "!--"') do (
    if not defined BCI_IP set "BCI_IP=%%I"
)
echo     BCI configurada em socketConfig.xml: !BCI_IP!
ping -n 1 -w 1500 !BCI_IP! >nul 2>&1
if errorlevel 1 (
    echo     [AVISO] !BCI_IP! nao respondeu ao ping. A interface abre, mas a
    echo             BCI ficara vermelha e os medidores nao conectam.
) else (
    echo     BCI respondeu ao ping.
)
echo     Os IPs dos 20 medidores vem da tabela conversmodel do banco em uso.

echo.
echo === 3/3  Abrindo a interface ===
echo.
echo     *** CONFIRA NO LOG QUAL BANCO ESTA EM USO ***
echo     Procure a linha: "Conectando ao banco de dados. URL: ..."
echo       - ufae_bench_local  -^> ok, teste isolado
echo       - ufae_bench_prod   -^> PRODUCAO: feche antes de Conectar/Run
echo.
echo     Na tela: selecione o lote, Conectar medidores, depois Run.
echo.

set "CP=bin"
for %%J in (lib\*.jar) do set "CP=!CP!;%%J"
for %%J in (lib\javafx-sdk-21.0.2\lib\*.jar) do set "CP=!CP!;%%J"

java --module-path "lib\javafx-sdk-21.0.2\lib" ^
     --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base ^
     --add-opens java.base/java.lang=ALL-UNNAMED ^
     --add-opens java.base/java.io=ALL-UNNAMED ^
     --add-opens=java.base/java.nio=ALL-UNNAMED ^
     --add-opens=java.base/sun.nio.ch=ALL-UNNAMED ^
     --add-opens=java.management/sun.management=ALL-UNNAMED ^
     --add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED ^
     --add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED ^
     --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED ^
     --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED ^
     -Dfae.log.level=INFO ^
     -cp "!CP!" main.ServiceInterfaceMain

endlocal
echo.
echo Aplicacao encerrada. O PostgreSQL local continua rodando.
echo Lembre-se de DESFAZER a alteracao em ServiceInterfaceMain.java e
echo recompilar antes de voltar a operar em producao.
pause
