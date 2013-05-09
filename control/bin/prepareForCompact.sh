#!/bin/bash
#rm -Rf ~/.mozilla

zerofree()
{
  FILE=$1/zerofree_deleteme
  echo Creating temporary file: $FILE
  dd if=/dev/zero of=$FILE
  rm -Rf $FILE
  echo $FILE created and deleted...
}


sqlplus '/ as sysdba' @/labs/content/WInS_Demos/environment/sql/truncate.sql

killNodeManager.sh
killWebLogic.sh

rm -Rf $DOMAINS
sudo rm -Rf /tmp/*

cd /labs/content/WInS_Demos
mvn clean

cd /labs/content/oracle-parcel-service
mvn clean

find /u01/content/weblogic-innovations-seminars -name "*.sh" -exec chmod +rx {} \;

rm -Rf /labs/content/oracle-parcel-service/ops-weblogic/Oracle

zerofree /tmp
zerofree /u01

halt
