#!/bin/bash
sudo hdiutil attach  http://download.asperasoft.com/download/sw/connect/3.6.6/AsperaConnectInstaller-3.6.6.119698.dmg
 
sudo installer -package /Volumes/Aspera\ Connect\ Installer/AsperaConnectInstaller.pkg -target /  (sudo installer currently requires password)
 
sudo hdiutil detach /Volumes/Aspera\ Connect\ Installer/
 
MyValue=$1
#osascript <<EOD
/usr/bin/osascript<<EOF
   on run
       tell application "Aspera Connect"
            activate
            set myFile to do shell script "echo '$MyValue'"
            tell application "System Events"      (terminal needs to have access for inspector on system events)
                tell process "Aspera Connect"
                    repeat until exists window 1
                    delay 1
                    end repeat
                    keystroke "g" using {command down, shift down}
                    delay 2
                    keystroke myFile
                    delay 1
                    keystroke return
                    delay 2
                    keystroke return
                 end tell
           end tell
       end tell
       delay 10
       tell application "Aspera Connect"
            activate
            tell application "System Events"
                tell process "Aspera Connect"
                    repeat until exists of button 1 of window 1
                    delay 1
                    end repeat
                    delay 2
                    keystroke return
               end tell
           end tell
       end tell
   end run
---------------------------------------------------------------------------------------------------------------------------------------------------------------------