@echo off
REM Script de lancement de FastCar Location

echo ================================
echo FastCar Location - Application
echo ================================
echo.
echo Compilation en cours...
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d bin ^
  src\com\fastcar\model\*.java ^
  src\com\fastcar\dao\*.java ^
  src\com\fastcar\util\*.java ^
  src\com\fastcar\ui\*.java

if errorlevel 1 (
    echo.
    echo [ERREUR] La compilation a échouée !
    pause
    exit /b 1
)

echo [OK] Compilation terminee
echo.
echo Lancement de l'application...
echo.

REM Lancer l'application avec le bon classpath
java -cp "lib\mysql-connector-j-9.5.0.jar;bin" com.fastcar.ui.MainFrame

pause
