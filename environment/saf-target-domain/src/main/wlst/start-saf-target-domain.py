import time

DOMAIN_NAME = 'saf_target_domain'
var_domain_dir = USER_PROJECTS + '/domains/' + DOMAIN_NAME

adminServer_ListenAddress = '127.0.0.1'
adminServer_ListenPort = 8001
adminServer_AdministrationPort = 8200
adminServer_Username = 'weblogic'
adminServer_Password = 'welcome1'

managed_server_name_base = 'ms'
managed_server_port_base = '810'
managed_server_count = 2

listen_address = 'localhost'
jms_sever_name_base = 'jms-server'
cluster_name = 'cluster-1'
machine_name = 'localhost'


########################################
def startCluster():

    try:
        start(cluster_name, 'Cluster')
    except:
        print '============================================='
        print 'Unable to start Cluster!!!'
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

print 'nmConnect('+adminServer_Username+', '+adminServer_Password+', '+listen_address+', 5556, '+DOMAIN_NAME+', '+ var_domain_dir+', \'plain\')'

nmConnect(adminServer_Username, adminServer_Password, listen_address, 5556, DOMAIN_NAME, var_domain_dir, 'plain')

print ''
print '============================================='
print 'Connected to NODE MANAGER Successfully'
print '============================================='
print ''

adminURL='t3://' + adminServer_ListenAddress + ':' + str(adminServer_ListenPort)

try:
    print 'Attempting to connect to AdminServer at URL='+adminURL
    connect(adminServer_Username, adminServer_Password, adminURL)
except:
    print 'Unable to connect to AdminServer, attempting to start'
    startAdminServer()
    connect(adminServer_Username, adminServer_Password, adminURL)


connect(adminServer_Username, adminServer_Password, 't3://' + adminServer_ListenAddress + ':' + str(adminServer_ListenPort))

domainRuntime()
startCluster()
disconnect()