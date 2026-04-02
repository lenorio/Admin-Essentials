@echo off
echo ===============================================
echo  Deleting old command folder
echo ===============================================

if exist "src\main\java\net\russianphonks\adminmod\command" (
    rd /s /q "src\main\java\net\russianphonks\adminmod\command"
    echo Old command folder deleted successfully!
) else (
    echo Old command folder already deleted.
)

echo.
echo ===============================================
echo  Building Minecraft Admin Mod
echo ===============================================
echo.

call gradlew.bat clean build --stacktrace

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===============================================
    echo  BUILD SUCCESSFUL!
    echo ===============================================
    echo.
    echo JAR file location:
    echo %CD%\build\libs\adminmod-1.0.0.jar
    echo.
) else (
    echo.
    echo ===============================================
    echo  BUILD FAILED!
    echo ===============================================
    echo.
    echo Please check the error messages above.
)

pause
