#!/bin/sh

echo "Creating domains..."

${DEMOS_HOME}/control/bin/rebuildDomains.sh

if [ "$?" == "0" ]; then
  echo "Installing applications into domains..."
  cd ../
  mvn -DskipTests=true install
fi

echo "Initialization complete!!!"
