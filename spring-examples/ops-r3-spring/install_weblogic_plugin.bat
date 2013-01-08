set WL_HOME=C:\oracle\middleware\wlserver_10.3
set JAVA_HOME=C:\oracle\middleware\jrockit_160_24_D1.1.2-4
set M2_HOME=C:\apache-maven-2.2.1

cd %WL_HOME%\server\lib 

%JAVA_HOME%\bin\java -jar wljarbuilder.jar -profile weblogic-maven-plugin

%M2_HOME%\bin\mvn install:install-file -Dfile=weblogic-maven-plugin.jar -DpomFile=pom.xml