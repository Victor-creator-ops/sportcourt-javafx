@echo off
setlocal

if exist "%ProgramData%\chocolatey\bin" set "PATH=%PATH%;%ProgramData%\chocolatey\bin"

call :ensure java temurin11
call :ensure mvn maven
call :ensure mysql mysql

net start MySQL >nul 2>&1
net start MySQL80 >nul 2>&1

mvn -q -DskipTests clean package

call :loadschema

java -jar target\sportcourt-system-1.0.0-jar-with-dependencies.jar
exit /b

:ensure
where %1 >nul 2>&1
if %ERRORLEVEL% neq 0 (
  if exist "%ProgramData%\chocolatey\bin\choco.exe" (
    choco install -y %2
  ) else (
    echo Instale %2 manualmente.
    exit /b 1
  )
)
goto :eof

:loadschema
set "PROP=src\main\resources\application.properties"
for /f "tokens=1,* delims==" %%a in ('findstr /B "database.username=" "%PROP%"') do set "DB_USER=%%b"
for /f "tokens=1,* delims==" %%a in ('findstr /B "database.password=" "%PROP%"') do set "DB_PASS=%%b"
if defined DB_USER (
  set "MYSQL_PWD=%DB_PASS%"
  mysql -u %DB_USER% < schema.sql >nul 2>&1
  if %ERRORLEVEL% neq 0 echo Nao foi possivel executar schema.sql, verifique MySQL.
  set "MYSQL_PWD="
)
goto :eof
