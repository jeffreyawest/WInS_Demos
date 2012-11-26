import time

loadProperties('environment.properties')

DOMAIN_NAME = 'weblogic_examples_domain'

adminServer_ListenAddress = '127.0.0.1'
adminServer_ListenPort = 7001
adminServer_Username = 'weblogic'
adminServer_Password = 'welcome1'

cluster_name = 'cluster-1'

managed_server_count = 2
managed_server_name_base = 'ms'

listen_address = 'localhost'
var_domain_dir = USER_PROJECTS + '/domains/' + DOMAIN_NAME

coh_server_count = 2

########################################

def stopCoherenceServers():
    for n in range(1, int(coh_server_count) + 1):
        coherenceServerName = 'coh-' + str(n)

        lifecycle = getMBean('/CoherenceServerLifeCycleRuntimes/' + coherenceServerName)

        try:
            print("Stopping Coherence Server: " + coherenceServerName)
            lifecycle.stop()
            print("Cohernece Server Status:" + lifecycle.getState())

        except:
            print '============================================='
            print 'Unable to stop Coherence server: [' + coherenceServerName + ']'
            print '============================================='
            print ''
            print("Cohernece Server Status:" + lifecycle.getState())
            dumpStack()

########################################

def stopCluster():
    try:
        shutdown(cluster_name, 'Cluster', ignoreSessions='true', force='true')
        state(cluster_name, 'Cluster')
    except:
        print '============================================='
        print 'Unable to shutdown Cluster!!!'
        print '============================================='
        print ''
        dumpStack()

########################################

def startAdminServer():
    try:
        nmStart('AdminServer')
    except:
        print '============================================='
        print 'Unable to start Admin Server!!!'
        print '============================================='
        print ''
        dumpStack()

########################################

print ''
print '============================================='
print 'Connecting to Node Manager...'
print '============================================='
print ''

nmConnect(adminServer_Username, adminServer_Password, listen_address, 5556, DOMAIN_NAME, var_domain_dir, 'plain')

print ''
print '============================================='
print 'Connected to NODE MANAGER Successfully'
print '============================================='
print ''

adminURL = 't3://' + adminServer_ListenAddress + ':' + str(adminServer_ListenPort)

try:
    print 'Attempting to connect to AdminServer at URL=' + adminURL
    connect(adminServer_Username, adminServer_Password, adminURL)
except:
    print 'Unable to connect to AdminServer, attempting to start'
    startAdminServer()
    connect(adminServer_Username, adminServer_Password, adminURL)

domainRuntime()
stopCluster()
stopCoherenceServers()

shutdown()
