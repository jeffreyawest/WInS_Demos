#!/bin/bash
cd /labs/content/WInS_labs
echo "Updating the lab materials"
svn update
cd /labs/content/WInS_Demos
echo "Updating the demo materials"
svn update

read -p "Checkout complete. Press [Enter] to close the window"
