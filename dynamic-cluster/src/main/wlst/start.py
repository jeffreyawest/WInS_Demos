
loadProperties('counter.properties')
#Paramaters
#user='weblogic'
#password='welcome1'
#listenAddress='localhost'
#listenPort=5556
#adminPort=7001
#Change parameter for this place
#domainName='mydomain'	
															 
USER_PROJECTS='/u01/wls1212/user_projects/domains/'
var_domain_dir = USER_PROJECTS + domainName
APP_DIR ='D:\dynamicluster'



#Connect to the Node manager
nmConnect(user,password,listenAddress,5556,domainName,var_domain_dir,'plain')

#Start the Admin Server in Domain : mydomain

adminServerStatus= nmServerStatus('AdminServer');
if( adminServerStatus != 'RUNNING'):									
	nmStart('AdminServer')




#Deploying Load Balancing application to Stand Alone Managed Server
#progress = deploy(appName=LoadBalancer, path=APP_DIR + '\LoadBalancer-1.0-SNAPSHOT.war', targets='LoadBalancer', remote='true', upload='true', block='true')
#progress.printStatus()	
	
	
#Deploying Chat Application to cluster
#progress = deploy(appName=chat-1.0.war, path=APP_DIR + '\chat-1.0.war', targets='MyCluster', remote='true', upload='true', block='true')
#progress.printStatus()

	
connect('weblogic','welcome1', 't3://localhost:7001');

#Start the MyCluser-1 in Dynamic Cluster
cluster1status= nmServerStatus('MyCluster-1');
if( cluster1status == 'SHUTDOWN'):									
	start('MyCluster-1','Server')
	
#Start the LoadBalancer Managed Server
lbstatus= nmServerStatus('LoadBalancer');
if( lbstatus == 'SHUTDOWN'):									
	start('LoadBalancer','Server')


#Deploying Load Balancing application to Stand Alone Managed Server
deploy('LoadBalancer', APP_DIR +'\LoadBalancer-1.0-SNAPSHOT.war', targets='LoadBalancer')
startApplication('LoadBalancer')

#Deploying Chat Application to cluster
deploy('Chat', APP_DIR + '\chat-1.0.war', targets='MyCluster')
startApplication('Chat')

while 1:
	servers = domainRuntimeService.getServerRuntimes();
	# Number of Active Session = Count
	count=0;
	for server in servers:
	# print 'SERVER: ' + server.getName();

	# print('APPLICATION RUNTIME INFORMATION');
		apps = server.getApplicationRuntimes();
		for app in apps:
			if (app.getName() == 'Chat'):
					# print 'Application: ' + app.getName()
					crs = app.getComponentRuntimes()
					for cr in crs:
						if (cr.getType() == 'WebAppComponentRuntime'):
							count=count+ cr.getOpenSessionsCurrentCount();
							if ( count>50 ):
								cluster2status= nmServerStatus('MyCluster-2');
								if( cluster2status == 'SHUTDOWN'):
									start('MyCluster-2','Server')
#								if ( count<100 ):
#										cluster3status= nmServerStatus('MyCluster-3');
#										if( cluster3status == 'RUNNING'):
#											nmKill('MyCluster-3')
																		
							if ( count>100 ):
								cluster3status= nmServerStatus('MyCluster-3');
								if( cluster3status == 'SHUTDOWN'):
									start('MyCluster-3','Server')
								
								
	print count;
	java.lang.Thread.sleep(4000)
	
