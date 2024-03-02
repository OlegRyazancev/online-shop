@echo off
setlocal

rem Get the full path of the directory containing this script
set "source=%~dp0"

rem List of destination directories
set "destinations=customer product organization api-gateway admin auth logo mail purchase review"

rem Loop through each destination directory and copy the .env file
for %%d in (%destinations%) do (
    echo Copying .env file to %%d directory...
    copy /Y "%source%.env" "%source%%%d\"
    if errorlevel 1 (
        echo Failed to copy .env file to %%d directory.
    ) else (
        echo .env file copied successfully to %%d directory.
    )
)

pause
