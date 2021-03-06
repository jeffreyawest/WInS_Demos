import time

DOMAIN_NAME = 'weblogic_examples_domain'

adminServer_ListenAddress = 'wins-vbox.localdomain'
adminServer_ListenPort = 7001
adminServer_Username = 'weblogic'
adminServer_Password = 'welcome1'
adminURL = 't3://' + adminServer_ListenAddress + ':' + str(adminServer_ListenPort)

listen_address = 'wins-vbox.localdomain'
var_domain_dir = USER_PROJECTS + '/domains/' + DOMAIN_NAME


def startAdminServer():
  try:
    nmStart('AdminServer')
    time.sleep(5000)
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

disconnect()