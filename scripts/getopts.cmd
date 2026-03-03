::
:: Original author Ghandi Manning http://superuser.com/users/222559/ghandi-manning
:: Credit http://superuser.com/questions/637133/is-there-a-way-to-parse-arguments-into-a-batch-script
:: Licensed under version 3.0 of the CC-BY-SA license: https://creativecommons.org/licenses/by-sa/3.0/legalcode
:: The original work has been modified.
:: Warranty Disclaimer: UNLESS OTHERWISE MUTUALLY AGREED TO BY THE PARTIES IN WRITING, LICENSOR OFFERS THE WORK AS-IS
:: AND MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND CONCERNING THE WORK, EXPRESS, IMPLIED, STATUTORY OR
:: OTHERWISE, INCLUDING, WITHOUT LIMITATION, WARRANTIES OF TITLE, MERCHANTIBILITY, FITNESS FOR A PARTICULAR PURPOSE,
:: NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, ACCURACY, OR THE PRESENCE OF ABSENCE OF ERRORS, WHETHER
:: OR NOT DISCOVERABLE. SOME JURISDICTIONS DO NOT ALLOW THE EXCLUSION OF IMPLIED WARRANTIES, SO SUCH EXCLUSION MAY NOT
:: APPLY TO YOU.
::
@echo off
cls

set getopt_ParmCounter=1
set paramc=1
set DEBUG=1

set argc=0
for %%x in (%*) do Set /A argc+=1

set _myvar=%*

rem Loop through all command line arguments one at a time
:varloop
set isparam=1
for /f "tokens=1*" %%a in ('echo %_myvar%') DO (
   set getopt_Parm=%%~a
   set _myvar=%%b
   call :paramtype

   rem shift along arguments and rerun loop
   if NOT "%%b"=="" goto varloop
)
goto :eof

:paramtype
rem If first character starts with a / it must be an option
if /i "%getopt_Parm:~0,1%"=="/" call :option
if /i "%isparam%"=="1" call :param
goto :eof

:indexof [%1 - string ; %2 - find index of ; %3 - if defined will store the result in variable with same name]
set "str=%~1"
set "sub=%~2"
for /L %%i in (0,1,4096) do (
    if "!str:~%%i,1!" == "%sub%" (
        set pos=%%i
    )
)
if "%pos%" == "" set pos=-1
if "%~3" neq "" (set %~3=%pos%) else echo %pos%
exit /B 0

:option
   set isparam=0
   call :indexof "%getopt_Parm%" ":" getopt_EqIdx
   set /a getopt_EqIdx+=1

   rem If the index is GE 0 then we must have a colon in the option.
   if /i "%getopt_EqIdx%"=="0" (call :nocolon) else (call :colon)
   goto :eof

      :colon
         rem set the OPTION value to the stuff to the right of the colon
         set /a getopt_ParmNameEnd=%getopt_EqIdx%-2
         call set getopt_ParmName=%%getopt_Parm:~1,%getopt_ParmNameEnd%%%
         call set getopt_ParmValue=%%getopt_Parm:~%getopt_EqIdx%%%
         set OPTION_%getopt_ParmName%=%getopt_ParmValue%
         goto :eof

      :nocolon
         rem This is a flag, so simply set the value to 1
         set getopt_ParmName=%getopt_Parm:~1%
         set getopt_ParmValue=1
         set OPTION_%getopt_ParmName%=%getopt_ParmValue%
         goto :eof

:param
   rem There was no / or - found, therefore this must be a paramater, not an option
   set PARAM_%getopt_ParmCounter%=%getopt_Parm%
   set PARAM_0=%getopt_ParmCounter%
   set /a getopt_ParmCounter+=1
   goto :eof