#!/bin/bash
echo "Updating the lab materials"
rm -Rf /labs/content/WInS_labs
svn co https://svn.java.net/svn/weblogic-examples~weblogic-workshops/WInS_labs /labs/content/WInS_labs


~/updateDemos.sh

~/updateOPS.sh

read -p "Checkout complete. Press [Enter] to close the window"
