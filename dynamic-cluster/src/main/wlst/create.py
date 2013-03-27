
#loadProperties('counter.properties')

#parameter Declaration

user='weblogic'
password='welcome1'
listenAddress='localhost'
listenPort=5556
adminPort=7001
domainName='dynamic-cluster-domain'
														 
USER_PROJECTS='/u01/wls1212/user_projects/domains/'
var_domain_dir = USER_PROJECTS + domainName

############################################################################################################################################
#BEGIN MAIN
#print 'Creating domain in path=' + var_domain_dir
#createDomain( WL_HOME +'common/templates/wls/wls.jar',USER_PROJECTS+'weblogic_examples_domain','weblogic','welcome1')
#print 'domain created'
#readDomain(var_domain_dir)
#print 'read domain'

#startNodaManager()

nmConnect(user,password,listenAddress,listenPort,domainName,var_domain_dir,'plain')
#nmConnect('weblogic','welcome1','localhost','5556','dynamic-cluster-domain','/u01/wls1212/user_projects/domains/dynamic-cluster-domain', 'plain')
print ''
print '============================================='
print 'Connected to NODE MANAGER Successfully...!!!'
print '============================================='
print ''

adminServerStatus= nmServerStatus('AdminServer');
if( adminServerStatus != 'RUNNING'):									
	nmStart('AdminServer')

connect(user,password, 't3://' + listenAddress + ':' + str(adminPort))
edit()
startEdit()

cmo.createMachine('MyMachine')

activate()
startEdit()
cd('/')
cmo.createServer('LoadBalancer')

cd('/Servers/LoadBalancer')
cmo.setListenAddress('localhost')
cmo.setListenPort(7002)
cmo.setMachine(getMBean('/Machines/MyMachine'))
cmo.setCluster(None)
cd('/Servers/LoadBalancer/SSL/LoadBalancer')
cmo.setEnabled(false)
activate()

startEdit()

cd('/')
cmo.createServerTemplate('MyCluster-Template')

cd('/ServerTemplates/MyCluster-Template')
cmo.setListenPort(7002)

cd('/ServerTemplates/MyCluster-Template/SSL/MyCluster-Template')
cmo.setListenPort(8100)

cd('/ServerTemplates/MyCluster-Template')
cmo.setMachine(getMBean('/Machines/MyMachine'))

cd('/')
cmo.createCluster('MyCluster')

cd('/Clusters/MyCluster')
cmo.setClusterMessagingMode('unicast')

cd('/ServerTemplates/MyCluster-Template')
cmo.setCluster(getMBean('/Clusters/MyCluster'))

cd('/Clusters/MyCluster/DynamicServers/MyCluster')
cmo.setServerTemplate(getMBean('/ServerTemplates/MyCluster-Template'))
cmo.setMaximumDynamicServerCount(3)
cmo.setCalculatedListenPorts(true)
cmo.setCalculatedMachineNames(false)
cmo.setCalculatedListenPorts(true)
cmo.setServerNamePrefix('MyCluster-')

save()
activate()

shutdown()
