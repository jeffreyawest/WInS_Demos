#!/bin/sh

killWebLogic.sh

echo "Removing directory ${DOMAINS}..."
rm -Rf ${DOMAINS}

cd ${DEMOS_HOME}/environment

sqlplus '/ as sysdba' @sql/truncate.sql >> /dev/null
sqlplus '/ as sysdba' @sql/reset_passwords.sql >> /dev/null

echo "Creating Domains..."
mvn -P create-domain

if [ "$?" == "0" ]; then
  echo "Starting Domains..."
  mvn -P start-domain
fi

