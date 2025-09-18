@ECHO OFF
SETLOCAL

REM always run from this folder
cd /d "%~dp0"

REM clean bin
if exist ..\bin rmdir /s /q ..\bin
mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM delete previous saved tasks so test starts fresh
if exist ..\data\MiMi.txt del ..\data\MiMi.txt

REM compile ONLY non-JavaFX classes needed for the text UI
javac -Xlint:none -d ..\bin ^
  ..\src\main\java\mimi\MiMi.java ^
  ..\src\main\java\mimi\Parser.java ^
  ..\src\main\java\mimi\Task.java ^
  ..\src\main\java\mimi\TaskList.java ^
  ..\src\main\java\mimi\Todo.java ^
  ..\src\main\java\mimi\Deadline.java ^
  ..\src\main\java\mimi\Event.java ^
  ..\src\main\java\mimi\DoWithinPeriodTasks.java ^
  ..\src\main\java\mimi\UiMasterList.java ^
  ..\src\main\java\mimi\Save.java ^
  ..\src\main\java\mimi\Storage.java ^
  ..\src\main\java\mimi\MiMiException.java

IF ERRORLEVEL 1 (
  echo ********** BUILD FAILURE **********
  EXIT /B 1
)

REM run the console app, redirect input and capture output
java -classpath ..\bin mimi.MiMi < input.txt > ACTUAL.TXT

IF ERRORLEVEL 1 (
  echo ********** RUNTIME FAILURE **********
  EXIT /B 1
)

REM compare actual vs expected
FC ACTUAL.TXT EXPECTED.TXT
ENDLOCAL
