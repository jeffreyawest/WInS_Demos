#!/bin/bash

export LABS_HOME="/labs/content"
export LAB_NAME="oracle-parcel-service"
export TAG_NAME="wins-vbox"
export GIT_URL="http://github.com/jeffreyawest/oracle-parcel-service.git"
export LAB_DIR=${LABS_HOME}/${LAB_NAME}

echo "Updating ${LAB_NAME} in ${LAB_DIR}..."

cd ${LAB_DIR}
git fetch --tags
git merge ${TAG_NAME}

if [ "$1" != "nowait" ]; then
  read -p "Checkout complete. Press [Enter] to close the window"
 fi
