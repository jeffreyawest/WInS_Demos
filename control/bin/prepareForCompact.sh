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

sudo rm -Rf /tmp/*

sqlplus '/ as sysdba' @/u01/content/weblogic-innovation-seminars/WInS_Demos/environment/domainSQL/truncate.sql >> /dev/null

if [ -d $DOMAINS ]; then

  cd /u01/content/weblogic-innovations-seminars/WInS_Demos/environment
  mvn -P stop-domain

  rm -Rf $DOMAINS
fi

cd /u01/content/oracle-parcel-service
mvn clean

cd /u01/content/weblogic-innovations-seminars/WInS_Demos
mvn clean

cd /u01/content/oracle-parcel-service
mvn clean

rm -Rf /u01/content/oracle-parcel-service/ops-weblogic/Oracle

zerofree /tmp
zerofree /u01

halt
