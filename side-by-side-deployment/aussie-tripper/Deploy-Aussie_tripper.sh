#!/bin/sh
. "${WL_HOME}/server/bin/setWLSEnv.sh

echo CLASSPATH=${CLASSPATH}

export JVM_ARGS="-Dweblogic.security.TrustKeyStore=DemoTrust"

${JAVA_HOME}/bin/java ${JVM_ARGS} weblogic.Deployer -adminurl https://localhost:7200 -user weblogic -password welcome1 -name aussie-tripper-v1 -deploy -remote -upload aussie-tripper-v1.ear
