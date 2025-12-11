@echo off
REM Script de lancement de FastCar Location
set "JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.3\jbr"
set "JAVAC=%JAVA_HOME%\bin\javac.exe"
set "JAVA=%JAVA_HOME%\bin\java.exe"

echo ================================
echo FastCar Location - Application
echo ================================
echo.
echo Compilation en cours...
"%JAVAC%" -cp "lib\mysql-connector-j-9.5.0.jar" -d bin ^
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
"%JAVA%" -cp "lib\mysql-connector-j-9.5.0.jar;bin" com.fastcar.ui.MainFrame

pause
