set COHERENCE_HOME=C:\u01\wls1211\coherence_3.7
set MIDDLEWARE_HOME=C:\u01\wls1211
set MODULES=%MIDDLEWARE_HOME%/modules

set TOPLINKGRID_JAR_PATH=%MODULES%/com.oracle.toplinkgrid_1.2.0.0_11-1-1-6-0.jar
set COHERENCE_JAR_PATH=%COHERENCE_HOME%/lib/coherence.jar
set ECLIPSELINK_JAR_PATH=%MODULES%/org.eclipse.persistence_2.0.0.0_2-3.jar

call mvn install:install-file -DgroupId=com.oracle.persistence -DartifactId=toplink-grid -Dversion=11.1.1.6 -Dpackaging=jar -Dfile=%TOPLINKGRID_JAR_PATH%
call mvn install:install-file -DgroupId=com.oracle.coherence -DartifactId=coherence -Dversion=3.7.1 -Dpackaging=jar -Dfile=%COHERENCE_JAR_PATH%
call mvn install:install-file -DgroupId=org.eclipse.persistence -DartifactId=eclipselink -Dversion=2.3.0 -Dpackaging=jar -Dfile=%ECLIPSELINK_JAR_PATH%

call mvn install:install-file -DgroupId=com.oracle.weblogic -DartifactId=weblogic-spring -Dversion=12.1.1.0 -Dpackaging=jar -Dfile=%MIDDLEWARE_HOME%/wlserver_12.1/server/lib/weblogic-spring.jar
