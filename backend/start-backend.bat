@echo off
:: ====================================================================
::  Campus Circular Backend Starter (Windows)
::  Automatically downloads Maven if not installed, then builds + runs
:: ====================================================================
setlocal

set MAVEN_VERSION=3.9.6
set MAVEN_ZIP_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip
set MAVEN_DIR=%USERPROFILE%\.m2\wrapper\apache-maven-%MAVEN_VERSION%
set MVN_CMD=%MAVEN_DIR%\bin\mvn.cmd

echo.
echo ============================================================
echo  Campus Circular - Backend Startup
echo ============================================================
echo.

:: Check if Java is available
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java not found. Install Java 17+ and add to PATH.
    pause
    exit /b 1
)

:: Download Maven if needed
if not exist "%MVN_CMD%" (
    echo [INFO] Maven not found. Downloading Maven %MAVEN_VERSION%...
    if not exist "%USERPROFILE%\.m2\wrapper" mkdir "%USERPROFILE%\.m2\wrapper"
    powershell -Command "Invoke-WebRequest -Uri '%MAVEN_ZIP_URL%' -OutFile '%TEMP%\maven.zip'" 2>nul
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to download Maven. Please install Maven manually.
        echo         https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
    powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper' -Force"
    echo [INFO] Maven downloaded successfully.
)

echo [INFO] Java OK, Maven OK.
echo.
echo [1/2] Building Spring Boot application (first time takes a few minutes)...
cd /d "%~dp0"
call "%MVN_CMD%" clean package -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build failed. Common fixes:
    echo   - Make sure PostgreSQL is running on localhost:5432
    echo   - Database 'campus_circular' must exist (run create-database.sql)
    echo   - Check credentials in src\main\resources\application.properties
    echo.
    call "%MVN_CMD%" clean package -DskipTests 2>&1 | findstr /i "error"
    pause
    exit /b 1
)

echo.
echo [2/2] Starting backend on http://localhost:8080 ...
echo.
echo   Admin login : admin@campus.edu  /  admin123
echo   Student     : arjun.mehta@campus.edu  /  password123
echo.
echo ============================================================
echo.

java -jar target\campus-circular-backend-1.0.0.jar
