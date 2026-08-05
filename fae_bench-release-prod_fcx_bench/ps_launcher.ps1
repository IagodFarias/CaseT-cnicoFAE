param(
  [string]$ProjectDir = "C:\Users\eduardo.andrade\Desktop\serv\service-interface-java-21"
)

$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$logFile = Join-Path $ProjectDir "service_log_$timestamp.txt"

cd $ProjectDir
& "C:\Program Files\Java\jdk-21\bin\java.exe" '@C:\Users\EDUARD~1.AND\AppData\Local\Temp\cp_3yezr7sk63x4g7gi653ixqsru.argfile' 'main.ServiceInterfaceMain' | Tee-Object -FilePath $logFile
