#!/bin/bash

export LABS_HOME="/labs/content"
export LAB_NAME="WInS_Demos"
export TAG_NAME="wins-vbox"
export GIT_URL="http://github.com/jeffreyawest/WInS_Demos.git"
export LAB_DIR=${LABS_HOME}/${LAB_NAME}

. ${DEMOS_HOME}/control/bin/winsEnv.sh > /dev/null

echo "Updating ${LAB_NAME} in ${LAB_DIR}..."

GIT_PROXY=`git config --get http.proxy`

echo "GIT Proxy set to: ${GIT_PROXY}"

cd ${LAB_DIR}
git fetch --tags
git merge ${TAG_NAME}

if [ "$1" == "wait" ]; then
  read -p "Checkout complete. Press [Enter] to close the window"
 fi