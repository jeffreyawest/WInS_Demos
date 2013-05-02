#!/bin/sh

killWebLogic.sh
killNodeManager.sh

echo "Removing directory ${DOMAINS}..."
rm -Rf $DOMAINS

sqlplus '/ as sysdba' @sql/truncate.sql >> /dev/null

echo "Creating Domains..."
mvn -P create-domain

if [ "$?" == "0" ]; then
  echo "Starting Domains..."
  mvn -P start-domain
fi