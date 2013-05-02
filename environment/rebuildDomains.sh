#!/bin/sh

killWebLogic.sh
killNodeManager.sh

echo "Removing directory ${DOMAINS}..."
rm -Rf $DOMAINS

sqlplus '/ as sysdba' @sql/rebuild.sql >> /dev/null

echo "Creating Domains..."
mvn -P create-domain

echo "Starting Domains..."
mvn -P create-domain
