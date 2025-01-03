# Alpano: Alpine Panorama Generator

Simple Java project created at École Polytechnique Fédérale de Lausanne (EPFL), Switzerland for generating alpine panorama.

Fork maintained by Raydelto Hernandez (raydelto@raycasters.com).

## Building

This project can be build either with Eclipse or with Maven. We support both building environments.

### Building with Maven

Make sure that you have Apache Maven installed on your system.

Execute the following command to build the Alpano project:

`mvn clean install`

Once you have build your project using Maven you can run the generated target jar by executing this command:

`java --module-path C:\javafx-sdk-23.0.1\lib --add-modules javafx.controls,javafx.fxml,javafx.swing -jar target\alpano-1.0-SNAPSHOT.jar`

We have included in this repo windows batch scripts for wrapping the `build` , `run` and `clean` processes.

These batch files are:

1. build.bat
1. run.bat
1. clean.bat

## Building using Eclipse

This repo contains the eclipse project configuration for the Alpano Project.  You should be able to import the project to your workspace and update the PATH to your local installation of JavaFX.

## JavaFX Dependency

Be aware that the current implementation depends on JavaFX, if you see any errors when running the project make sure that you have updated the project so that it is able to find this dependency on your system.

