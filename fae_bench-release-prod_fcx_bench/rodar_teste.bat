@echo off
REM ===========================================================================
REM  rodar_teste.bat -- sobe o ambiente de teste completo e abre a interface.
REM
REM  NAO recompila nada: usa o build que ja esta em bin\.
REM  Abre uma janela para o PostgreSQL, uma para os simuladores e roda a app
REM  nesta janela.
REM
REM  Pre-requisito: o cluster PostgreSQL portatil e o banco ufae_bench_test ja
REM  criados (ver testdb\README_TESTE.md). Se PG_DIR abaixo nao existir mais,
REM  o script avisa e segue -- a app cai no banco de producao, entao confira.
REM ===========================================================================
setlocal

cd /d "%~dp0"

set "PG_DIR=%LOCALAPPDATA%\Temp\claude\c--Users-FARIAS-Documents-2--Iago-fae-bench-release-prod-fcx-bench-fae-bench-release-prod-fcx-bench\3fcdd92a-78e4-444d-bb36-ffa10e9661e1\scratchpad"

echo.
echo === 1/4  PostgreSQL (porta 55432) ===
netstat -ano | findstr ":55432" | findstr "LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo     ja esta no ar.
) else (
    if exist "%PG_DIR%\pgsql\bin\pg_ctl.exe" (
        start "PostgreSQL 55432" /min "%PG_DIR%\pgsql\bin\pg_ctl.exe" -D "%PG_DIR%\pgdata" -o "-p 55432 -c listen_addresses=127.0.0.1" -l "%PG_DIR%\pg.log" start
        timeout /t 5 /nobreak >nul
        echo     iniciado.
    ) else (
        echo     [AVISO] cluster nao encontrado em:
        echo             %PG_DIR%
        echo             Sem banco de teste a aplicacao cai no FALLBACK de PRODUCAO.
        echo             Veja testdb\README_TESTE.md secao 2.
        pause
    )
)

echo.
echo === 2/4  Simuladores: BCI (8888) + 20 medidores (2051-2070) ===
netstat -ano | findstr ":8888" | findstr "LISTENING" >nul 2>&1
if %errorlevel%==0 (
    echo     ja estao no ar.
) else (
    start "Simuladores BCI + Medidores" cmd /k "cd /d "%~dp0Simulador" && python run_all.py"
    timeout /t 6 /nobreak >nul
    echo     iniciados em janela separada.
)

echo.
echo === 3/4  Conferindo as portas ===
for %%P in (55432 8888 2051 2070) do (
    netstat -ano | findstr ":%%P" | findstr "LISTENING" >nul 2>&1
    if errorlevel 1 (echo     porta %%P .... NAO ESCUTANDO) else (echo     porta %%P .... ok)
)

echo.
echo === 4/4  Abrindo a interface ===
echo     Na tela: selecione LOTE-TESTE-001, clique Conectar medidores, depois Run.
echo     Log da execucao: logs\calibracao_^<timestamp^>.log
echo.

setlocal enabledelayedexpansion
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
echo Aplicacao encerrada. Os simuladores e o PostgreSQL continuam rodando.
pause
