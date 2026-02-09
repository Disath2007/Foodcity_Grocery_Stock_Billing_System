@echo off
title Foodcity Grocery Stock & Billing System
echo Starting the System...

:: Using 'start javaw' allows the GUI to run independently and the CMD window to close.
start javaw -cp "build/classes;Lib/*" GUI.SplashScreen

:: Exit the CMD panel
exit

