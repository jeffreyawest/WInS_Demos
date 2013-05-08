import time

DOMAIN_NAME = 'saf_target_domain'
var_domain_dir = USER_PROJECTS + '/domains/' + DOMAIN_NAME

adminServer_ListenAddress = '127.0.0.1'
adminServer_ListenPort = 8001
adminServer_Username = 'weblogic'
adminServer_Password = 'welcome1'

adminURL = 't3://' + adminServer_ListenAddress + ':' + str(adminServer_ListenPort)

listen_address = 'localhost'

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

print 'nmConnect(' + adminServer_Username + ', ' + adminServer_Password + ', ' + listen_address + ', 5556, ' + DOMAIN_NAME + ', ' + var_domain_dir + ', \'plain\')'

nmConnect(adminServer_Username, adminServer_Password, listen_address, 5556, DOMAIN_NAME, var_domain_dir, 'plain')

print ''
print '============================================='
print 'Connected to NODE MANAGER Successfully'
print '============================================='
print ''


try:
  print 'Attempting to connect to AdminServer at URL=' + adminURL
  connect(adminServer_Username, adminServer_Password, adminURL)
except:
  print 'Unable to connect to AdminServer, attempting to start'
  startAdminServer()
  connect(adminServer_Username, adminServer_Password, adminURL)

connect(adminServer_Username, adminServer_Password, adminURL)

disconnect()