@echo off
SET JAVA=%JAVA_HOME%\bin\java.exe
echo %JAVA%
for %%F in (*.jar) do call :runCmd %%F
goto :justExit
:runCmd
echo run: %1
"%JAVA%" -jar "%CD%\%1"
:justExit
