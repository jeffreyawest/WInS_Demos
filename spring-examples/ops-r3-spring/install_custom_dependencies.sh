export COHERENCE_HOME=/labs//wls1211/coherence_3.7
export MIDDLEWARE_HOME=/labs/wls1211
export MODULES=%MIDDLEWARE_HOME%/modules

export TOPLINKGRID_JAR_PATH=${MODULES}/com.oracle.toplinkgrid_1.2.0.0_11-1-1-6-0.jar
export COHERENCE_JAR_PATH=${COHERENCE_HOME}/lib/coherence.jar
export ECLIPSELINK_JAR_PATH=${MODULES}/org.eclipse.persistence_2.0.0.0_2-3.jar

mvn install:install-file -DgroupId=com.oracle.persistence -DartifactId=toplink-grid -Dversion=11.1.1.6 -Dpackaging=jar -Dfile=${TOPLINKGRID_JAR_PATH}
mvn install:install-file -DgroupId=com.oracle.coherence -DartifactId=coherence -Dversion=3.7.1 -Dpackaging=jar -Dfile=${COHERENCE_JAR_PATH}
mvn install:install-file -DgroupId=org.eclipse.persistence -DartifactId=eclipselink -Dversion=2.3.0 -Dpackaging=jar -Dfile=${ECLIPSELINK_JAR_PATH}

mvn install:install-file -DgroupId=com.oracle.weblogic -DartifactId=weblogic-spring -Dversion=12.1.1.0 -Dpackaging=jar -Dfile=${MIDDLEWARE_HOME}/wlserver_12.1/server/lib/weblogic-spring.jar