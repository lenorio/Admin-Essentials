@echo off
echo ===============================================
echo  Installing PowerShell 7+ via winget
echo ===============================================
echo.

winget install --id Microsoft.Powershell --source winget --accept-package-agreements --accept-source-agreements --silent

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===============================================
    echo  PowerShell 7+ installed successfully!
    echo ===============================================
    echo.
    echo Please close this window and run the build script.
) else (
    echo.
    echo ===============================================
    echo  Installation failed or winget not available
    echo ===============================================
    echo.
    echo Please install manually from:
    echo https://github.com/PowerShell/PowerShell/releases
)

pause
