@echo off
setlocal
set "ROOT=%~dp0"
set "BUILD=%ROOT%build"

if exist "%BUILD%" rmdir /s /q "%BUILD%"
mkdir "%BUILD%\classes"

javac -encoding UTF-8 -d "%BUILD%\classes" "%ROOT%src\main\java\mx\utm\spaceinvaders\SpaceInvaders.java"
if errorlevel 1 exit /b 1

echo Main-Class: mx.utm.spaceinvaders.SpaceInvaders> "%BUILD%\manifest.mf"
jar cfm "%BUILD%\SpaceInvaders.jar" "%BUILD%\manifest.mf" -C "%BUILD%\classes" .
if errorlevel 1 exit /b 1

echo.
echo Compilacion terminada: %BUILD%\SpaceInvaders.jar
