#!/bin/bash

export LABS_HOME="/labs/content"
export LAB_NAME="oracle-parcel-service"
export TAG_NAME="wins-vbox"
export GIT_URL="http://github.com/jeffreyawest/oracle-parcel-service.git"
export LAB_DIR=${LABS_HOME}/${LAB_NAME}

. ${DEMOS_HOME}/control/bin/winsEnv.sh > /dev/null

echo "Updating ${LAB_NAME} in ${LAB_DIR}..."

GIT_PROXY=`git config --get http.proxy`

echo "GIT Proxy set to: ${GIT_PROXY}"

cd ${LAB_DIR}
git fetch --tags
git merge ${TAG_NAME}

echo "If this operation fails please check your proxy settings, especially if you are on the Oracle network"

if [ "$1" == "wait" ]; then
  read -p "Checkout complete. Press [Enter] to close the window"
 fi
