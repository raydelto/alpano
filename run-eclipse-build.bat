@echo off
REM Batch script for starting Alpano on Windows

setlocal
set PATH=C:\javafx-sdk-23.0.1\bin;%PATH%

echo Starting Alpano...
java --module-path C:\javafx-sdk-23.0.1\lib -cp .\bin;C:\javafx-sdk-23.0.1\lib\javafx.base.jar;C:\javafx-sdk-23.0.1\lib\javafx.controls.jar;C:\javafx-sdk-23.0.1\lib\javafx.fxml.jar;C:\javafx-sdk-23.0.1\lib\javafx.graphics.jar;C:\javafx-sdk-23.0.1\lib\javafx.media.jar;C:\javafx-sdk-23.0.1\lib\javafx.swing.jar;C:\javafx-sdk-23.0.1\lib\javafx.web.jar;C:\javafx-sdk-23.0.1\lib\javafx.swt.jar --add-modules javafx.controls,javafx.fxml ch.epfl.alpano.gui.Alpano

endlocal
exit /b 0