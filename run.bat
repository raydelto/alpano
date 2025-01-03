@echo off
REM Batch script for starting Alpano on Windows

setlocal
set PATH=C:\javafx-sdk-23.0.1\bin;%PATH%

echo Starting Alpano...
java --module-path C:\javafx-sdk-23.0.1\lib --add-modules javafx.controls,javafx.fxml,javafx.swing -jar target\alpano-1.0-SNAPSHOT.jar

endlocal
exit /b 0