#!/bin/sh

echo "Creating domains..."

cd environment
./rebuildDomains.sh

if [ "$?" == "0" ]; then
  echo "Installing applications into domains..."
  cd ../
  mvn -DskipTests=true install
fi
