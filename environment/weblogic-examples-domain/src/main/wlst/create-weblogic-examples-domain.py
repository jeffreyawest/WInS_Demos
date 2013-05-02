import time

loadProperties('environment.properties')

DOMAIN_NAME = 'weblogic_examples_domain'

print 'Creating DOMAIN:' + DOMAIN_NAME

datasource_jndi_name = 'jdbc.ds.weblogic_examples'
datasource_global_transactions = 'None'
datasource_jdbc_driver = 'oracle.jdbc.OracleDriver'
datasource_user = 'weblogic_examples_domain'
datasource_password = 'weblogic_examples_domain'

adminServer_ListenAddress = 'wins-vbox.localdomain'
adminServer_ListenPort = 7001
adminServer_AdministrationPort = 7200
adminServer_Username = 'weblogic'
adminServer_Password = 'welcome1'
adminServer_AdministrationURL = 't3://' + adminServer_ListenAddress + ':' + str(adminServer_ListenPort)
managed_server_count = 2
managedServerName_base = 'ms'
managed_server_port_base = '710'
managed_server_admin_port_base = '720'

listen_address = 'wins-vbox.localdomain'

jms_sever_name_base = 'jms-server'
cluster_Name = 'cluster-1'
machine_Name = 'wins-vbox'
machine_ListenAddress = 'wins-vbox.localdomain'

wins_demos_home = '/labs/content/WInS_Demos'

coh_cluster_name = 'coherence-cluster-1'
coh_listen_address = 'wins-vbox'
coh_listen_port = 8088
coh_ttl = 0
coh_server_count = 2

jmsServerMBeans = []
managedServerMBeans = []
migratableTargetMBeans = []

coh_server_cp = MW_HOME + '/modules/com.oracle.toplinkgrid_1.0.0.0_11-1-1-5-0.jar:'\
                + MW_HOME + '/modules/org.eclipse.persistence_1.1.0.0_2-1.jar:'\
                + MW_HOME + '/coherence_3.7/lib/coherence.jar:'\
                + MW_HOME + '/modules/javax.management_1.2.1.jar:'\
                + MW_HOME + '/modules/javax.management.remote_1.0.1.3.jar:'\
                + MW_HOME + '/modules/javax.persistence_1.0.0.0_2-0-0.jar:'\
                + MW_HOME + '/wlserver_12.1/server/lib/ojdbc6.jar:'\
                + MW_HOME + '/coherence_3.7/lib/coherence-web-spi.war:'\
                + MW_HOME + '/modules/features/weblogic.server.modules.coherence.server_12.1.1.0.jar '

coh_server_args = '-Dtangosol.coherence.management.remote=true '\
                  '-Dtangosol.coherence.management=all '\
                  '-Dtangosol.coherence.distributed.localstorage=true '\
                  '-Dtangosol.coherence.session.localstorage=true '\
                  '-Dtangosol.coherence.cacheconfig=/labs/content/WInS_Demos/coherence-examples/session-cache-config.xml'




################################################### JDBC

def createPhysicalDataSource(jndiNames, driver, globalTX, url, user, passwd, target):
  dsName = jndiNames[0]

  print 'Creating Physical DataSource ' + dsName

  cd('/')
  jdbcSystemResource = create(dsName, "JDBCSystemResource")

  cd('/JDBCSystemResource/' + dsName + '/JdbcResource/' + dsName)
  dataSourceParams = create('dataSourceParams', 'JDBCDataSourceParams')
  dataSourceParams.setGlobalTransactionsProtocol(globalTX)
  cd('JDBCDataSourceParams/NO_NAME_0')

  try:
    set('JNDINames', jarray.array(jndiNames, String))
  except:
    dumpStack()

  cd('/JDBCSystemResource/' + dsName + '/JdbcResource/' + dsName)
  connPoolParams = create('connPoolParams', 'JDBCConnectionPoolParams')
  connPoolParams.setMaxCapacity(20)
  connPoolParams.setInitialCapacity(5)
  connPoolParams.setCapacityIncrement(1)
  connPoolParams.setTestConnectionsOnReserve(true)
  connPoolParams.setTestTableName('SQL SELECT 1 FROM DUAL')

  cd('/JDBCSystemResource/' + dsName + '/JdbcResource/' + dsName)
  driverParams = create('driverParams', 'JDBCDriverParams')
  driverParams.setUrl(url)
  driverParams.setDriverName(driver)
  driverParams.setPasswordEncrypted(passwd)
  cd('JDBCDriverParams/NO_NAME_0')

  create(dsName, 'Properties')
  cd('Properties/NO_NAME_0')

  create('user', 'Property')
  cd('Property/user')
  cmo.setValue(user)

  cd('/JDBCSystemResource/' + dsName)
  jdbcSystemResource.setTargets(jarray.array([target], weblogic.management.configuration.TargetMBean))

  print dsName + ' successfully created.'
  return jdbcSystemResource

################################################### JMS Module

def createBaseJMSResources(moduleName, clusterTarget, jmsServerTargets):
  print 'Creating BASE JMS Resources...'

  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  cf_name = 'com.oracle.example.jms.base.cf'

  myCF = create(cf_name, 'ConnectionFactory')
  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)
  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue = create('com.oracle.example.jms.base.queue', 'UniformDistributedQueue')
  queue.setJNDIName('com.oracle.example.jms.base.queue')
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic_name = 'com.oracle.example.jms.base.topic'
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Replicated')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')

  topic_name = 'com.oracle.example.jms.base.clearscreen'
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Replicated')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')

  topic_name = 'com.oracle.example.jms.base.replicated-topic'
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Replicated')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')

  topic_name = 'com.oracle.example.jms.base.partitioned-topic'
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Partitioned')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')


################################################### JMS Module

def createMigrationJMSResources(moduleName, clusterTarget, jmsServerTargets):
  print 'Creating BASE JMS Resources...'

  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  cf_name = 'com.oracle.example.jms.migration.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)
  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  queue_name = 'com.oracle.example.jms.migration.queue'
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')


################################################### JMS Module

def createOPSJMSResources(moduleName, clusterTarget, jmsServerTargets):
  print 'Creating OPS JMS Resources...'

  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  cf_name = 'com.oracle.demo.ops.jms.cf'

  myCF = create(cf_name, 'ConnectionFactory')
  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)
  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue_name = 'com.oracle.demo.ops.jms.eventQueue'
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue_name = 'com.oracle.demo.ops.jms.shipmentQueue'
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic_name = 'com.oracle.demo.ops.jms.eventTopic'
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Partitioned')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')


def createUtilityJMSResources(moduleName, clusterTarget, jmsServerTargets):
  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  ######## Connection Factory
  cf_name = 'com.oracle.example.jms.util.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)

  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  #### Queue
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue_name = 'com.oracle.example.jms.util.notification'
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')

  #### Queue
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue_name = 'com.oracle.example.jms.util.deadlock'
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')

  #### TOPIC
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic_name = 'com.oracle.example.jms.util.jdbchogger'
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Replicated')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')

  #### TOPIC
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic_name = 'com.oracle.example.jms.util.stuck_thread_generator'
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Replicated')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')


def createUOOResources(moduleName, clusterTarget, jmsServerTargets):
  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  ######## Connection Factory
  cf_name = 'com.oracle.example.jms.uoo.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)

  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  #### Queue
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')

  queue_name = 'com.oracle.example.jms.uoo.queue'
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')


def createWLDFJMSResources(moduleName, clusterTarget, jmsServerTargets):
  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  ######## Connection Factory
  cf_name = 'com.oracle.example.jms.wldf.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)

  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  #### Queue
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')

  queue_name = 'com.oracle.example.jms.wldf.notification'
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')


def createUOWResources(moduleName, clusterTarget, jmsServerTargets):
  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  ######## Connection Factory
  cf_name = 'com.oracle.example.jms.uow.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)

  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  #### Queue
  errorQueueName = 'com.oracle.example.jms.uow.error-queue'
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  errorQueue = create(errorQueueName, 'UniformDistributedQueue')
  errorQueue.setJNDIName(errorQueueName)
  errorQueue.setDefaultTargetingEnabled(false)
  errorQueue.setSubDeploymentName('cluster-subdeployment')

  #### Queue
  queue_name = 'com.oracle.example.jms.uow.queue'

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue = create(queue_name, 'UniformDistributedQueue')

  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')
  queue.setLoadBalancingPolicy('Round-Robin')
  queue.setResetDeliveryCountOnForward(true)
  queue.setIncompleteWorkExpirationTime(30000)
  queue.setForwardDelay(-1)
  queue.setAttachSender('supports')
  queue.setSAFExportPolicy('All')
  queue.setProductionPausedAtStartup(false)
  queue.setDefaultUnitOfOrder(false)
  queue.setUnitOfOrderRouting('Hash')
  queue.setUnitOfWorkHandlingPolicy('SingleMessageDelivery')
  queue.setInsertionPausedAtStartup(false)
  queue.setMessagingPerformancePreference(25)
  queue.setConsumptionPausedAtStartup(false)

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/UniformDistributedQueues/' + queue_name)
  failureParams = create(queue_name, 'DeliveryFailureParams')
  failureParams.setExpirationPolicy('Redirect')
  failureParams.setErrorDestination(errorQueue)


########################################

def createOPSEEResources(moduleName, clusterTarget, jmsServerTargets):
  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  ######## Connection Factory
  cf_name = 'com.oracle.demo.ops.jms.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)

  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  #### Queue
  queue_name = 'com.oracle.demo.ops.jms.eventQueue'

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue = create(queue_name, 'UniformDistributedQueue')

  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')
  queue.setLoadBalancingPolicy('Round-Robin')
  queue.setResetDeliveryCountOnForward(true)
  queue.setIncompleteWorkExpirationTime(30000)
  queue.setForwardDelay(-1)
  queue.setAttachSender('supports')
  queue.setSAFExportPolicy('All')
  queue.setProductionPausedAtStartup(false)
  queue.setDefaultUnitOfOrder(false)
  queue.setUnitOfOrderRouting('Hash')
  queue.setUnitOfWorkHandlingPolicy('SingleMessageDelivery')
  queue.setInsertionPausedAtStartup(false)
  queue.setMessagingPerformancePreference(25)
  queue.setConsumptionPausedAtStartup(false)

  #### Queue
  queue_name = 'com.oracle.demo.ops.jms.shipmentQueue'

  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue = create(queue_name, 'UniformDistributedQueue')

  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')
  queue.setLoadBalancingPolicy('Round-Robin')
  queue.setResetDeliveryCountOnForward(true)
  queue.setIncompleteWorkExpirationTime(30000)
  queue.setForwardDelay(-1)
  queue.setAttachSender('supports')
  queue.setSAFExportPolicy('All')
  queue.setProductionPausedAtStartup(false)
  queue.setDefaultUnitOfOrder(false)
  queue.setUnitOfOrderRouting('Hash')
  queue.setUnitOfWorkHandlingPolicy('SingleMessageDelivery')
  queue.setInsertionPausedAtStartup(false)
  queue.setMessagingPerformancePreference(25)
  queue.setConsumptionPausedAtStartup(false)

  #TOPIC
  topic_name = 'com.oracle.demo.ops.jms.eventTopic'
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  topic = create(topic_name, 'UniformDistributedTopic')
  topic.setJNDIName(topic_name)
  topic.setForwardingPolicy('Partitioned')
  topic.setDefaultTargetingEnabled(false)
  topic.setSubDeploymentName('cluster-subdeployment')

########################################

def createSpringJMSTempResources(moduleName, clusterTarget, jmsServerTargets):
  cd('/')
  jmsMySystemResource = create(moduleName, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterTarget], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + moduleName)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerTargets, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + moduleName + '/JmsResource/NO_NAME_0')

  ######## Connection Factory
  cf_name = 'com.oracle.example.jms.spring.cf'
  myCF = create(cf_name, 'ConnectionFactory')
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)

  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  #### Queue
  cd('/JMSSystemResources/' + moduleName + '/JmsResource/NO_NAME_0')
  queue_name = 'com.oracle.example.jms.spring.queue'

  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')


########################################
def createSAFStoresAndAgents():
  for n in range(1, int(managed_server_count) + 1):
    targets = jarray.array([migratableTargetMBeans[n - 1]], weblogic.management.configuration.TargetMBean)

    cd('/')
    jdbcStoreName = 'saf-store-' + str(n)
    jdbcStore = create(jdbcStoreName, 'JDBCStore')
    jdbcStore.setDataSource(jdbcClusterDatasource)
    jdbcStore.setPrefixName('SAFSTORE' + str(n))
    jdbcStore.setTargets(targets)

    cd('/')
    safAgent = create('saf-agent-' + str(n), 'SAFAgent')
    safAgent.setStore(jdbcStore)
    safAgent.setTargets(targets)

    safAgent.setAcknowledgeInterval(-1)
    safAgent.setForwardingPausedAtStartup(false)
    safAgent.setLoggingEnabled(true)
    safAgent.setReceivingPausedAtStartup(false)
    safAgent.setDefaultRetryDelayMultiplier(1.0)
    safAgent.setMessageBufferSize(-1)
    safAgent.setWindowSize(10)
    safAgent.setServiceType('Sending-only')
    safAgent.setConversationIdleTimeMaximum(0)
    safAgent.setDefaultTimeToLive(0)
    safAgent.setIncomingPausedAtStartup(false)
    safAgent.setDefaultRetryDelayMaximum(180000)
    safAgent.setDefaultRetryDelayBase(20000)
    safAgent.setWindowInterval(0)

########################################

def createSAFSourceModules():
  print 'Creating SAF SOURCE JMS Resources...'

  module_name = 'jms-module-saf-source'

  cd('/')
  jmsMySystemResource = create(module_name, 'JMSSystemResource')
  jmsMySystemResource.setTargets(jarray.array([clusterMBean], weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResources/' + module_name)
  subdeployment = create('cluster-subdeployment', 'SubDeployment')
  subdeployment.setTargets(jarray.array(jmsServerMBeans, weblogic.management.configuration.TargetMBean))

  cd('/JMSSystemResource/' + module_name + '/JmsResource/NO_NAME_0')

  cf_name = 'com.oracle.example.jms.saf.cf'

  myCF = create(cf_name, 'ConnectionFactory')
  myCF.setJNDIName(cf_name)
  myCF.setDefaultTargetingEnabled(true)

  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0/ConnectionFactories/' + cf_name)
  lbParams = create(cf_name, 'LoadBalancingParams')
  lbParams.setLoadBalancingEnabled(true)
  lbParams.setServerAffinityEnabled(false)

  txParams = create(cf_name, 'TransactionParams')
  txParams.setXAConnectionFactoryEnabled(true)

  ##################

  queue_name = 'com.oracle.example.jms.saf.local-queue'
  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0')
  queue = create(queue_name, 'UniformDistributedQueue')
  queue.setJNDIName(queue_name)
  queue.setDefaultTargetingEnabled(false)
  queue.setSubDeploymentName('cluster-subdeployment')

  ##################

  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0')
  safRemoteContext = create('remote-saf-context-1', 'SAFRemoteContext')

  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0/SAFRemoteContexts/remote-saf-context-1/')
  loginContext = create('remote-saf-context-1', 'SAFLoginContext')
  loginContext.setLoginURL('t3://wins-vbox.localdomain:8101,wins-vbox.localdomain:8102')
  #  loginContext.setUsername('weblogic')
  #  loginContext.setPasswordEncrypted('welcome1')

  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0')
  safErrorHandling = create('saf-error-handling', 'SAFErrorHandling')
  safErrorHandling.setSAFErrorDestination(None)
  safErrorHandling.setPolicy('Log')
  safErrorHandling.setLogFormat(None)

  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0')

  dest = create('saf-imported-destinations', 'SAFImportedDestinations')
  dest.setJNDIPrefix('saf/')
  dest.setSAFRemoteContext(safRemoteContext)
  dest.setSAFErrorHandling(safErrorHandling)
  dest.setTimeToLiveDefault(0)
  dest.setUseSAFTimeToLiveDefault(false)
  dest.setDefaultTargetingEnabled(true)
  #
  #    cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0/SAFImportedDestinations/saf-imported-destinations')
  #    cmo.setJNDIPrefix('saf/')
  #    cmo.setSAFRemoteContext(safRemoteContext)
  #    cmo.setSAFErrorHandling(safErrorHandling)
  #    cmo.setTimeToLiveDefault(0)
  #    cmo.setUseSAFTimeToLiveDefault(false)
  #    cmo.setDefaultTargetingEnabled(true)

  cd('/JMSSystemResources/' + module_name + '/JmsResource/NO_NAME_0/SAFImportedDestinations/saf-imported-destinations')
  safQueue = create('imported-saf-queue', 'SAFQueue')
  safQueue.setRemoteJNDIName('com.oracle.example.jms.saf.local-queue')

  ########################################


def createMachine(machine_name, nodemanager_type, listen_address, listen_port):
  cd('/')
  machine = create(machine_name, 'Machine')

  cd('/Machines/' + machine_name + '/')
  nodeManager = create(machine_name, 'NodeManager')

  #cd('/Machines/' + machine_name + '/NodeManager/' + machine_name)
  nodeManager.setNMType(nodemanager_type)
  nodeManager.setListenAddress(listen_address)
  nodeManager.setListenPort(listen_port)

  return machine

########################################

def createCoherenceCluster(coh_cluster_name,
                           coh_listen_address,
                           coh_listen_port,
                           targets_array,
                           coh_ttl):
  cd('/')
  cmo.createCoherenceClusterSystemResource(coh_cluster_name)

  cd(
    '/CoherenceClusterSystemResources/' + coh_cluster_name + '/CoherenceClusterResource/' + coh_cluster_name + '/CoherenceClusterParams/' + coh_cluster_name)
  cmo.setUnicastListenAddress(coh_listen_address)
  cmo.setUnicastListenPort(coh_listen_port)
  cmo.setUnicastPortAutoAdjust(true)

  cmo.setMulticastListenAddress('231.1.1.1')
  cmo.setMulticastListenPort(7777)

  cd('/CoherenceClusterSystemResources/' + coh_cluster_name)
  cmo.addTarget(getMBean('/Clusters/' + cluster_Name))

  cd(
    '/CoherenceClusterSystemResources/' + coh_cluster_name + '/CoherenceClusterResource/' + coh_cluster_name + '/CoherenceClusterParams/' + coh_cluster_name + '/CoherenceClusterWellKnownAddresses/' + coh_cluster_name)
  cmo.createCoherenceClusterWellKnownAddress('WKA-0')

  cd(
    '/CoherenceClusterSystemResources/' + coh_cluster_name + '/CoherenceClusterResource/' + coh_cluster_name + '/CoherenceClusterParams/' + coh_cluster_name + '/CoherenceClusterWellKnownAddresses/' + coh_cluster_name + '/CoherenceClusterWellKnownAddresses/WKA-0')
  cmo.setListenPort(coh_listen_port)
  cmo.setListenAddress(coh_listen_address)

  cd(
    '/CoherenceClusterSystemResources/' + coh_cluster_name + '/CoherenceClusterResource/' + coh_cluster_name + '/CoherenceClusterParams/' + coh_cluster_name)
  cmo.setTimeToLive(coh_ttl)

  #  cd('/Clusters/' + cluster_name)
  #  cmo.setCoherenceClusterSystemResource(getMBean('/CoherenceClusterSystemResources/' + coh_cluster_name))

  print 'Created Cluster name=[' + coh_cluster_name + ']'

########################################


def createCoherenceServer(coh_server_name,
                          cluster_name, machine_name,
                          coh_listen_address,
                          coh_listen_port,
                          coh_server_args,
                          coh_server_cp):
  print 'Creating Coherence Server JAVA_HOME=[' + JAVA_HOME + '] name=[' + coh_server_name + '] listenAddress=[' + coh_listen_address + '] listenPort=[' + str(
    coh_listen_port) + '] classpath=[' + coh_server_cp + '] args=[' + coh_server_args + ']'

  cd('/')
  cmo.createCoherenceServer(coh_server_name)

  cd('/CoherenceServers/' + coh_server_name)
  cmo.setMachine(getMBean('/Machines/' + machine_name))
  cmo.setCoherenceClusterSystemResource(getMBean('/CoherenceClusterSystemResources/' + cluster_name))
  cmo.setUnicastListenAddress(coh_listen_address)
  cmo.setUnicastListenPort(coh_listen_port)
  cmo.setUnicastPortAutoAdjust(true)

  cd('/CoherenceServers/' + coh_server_name + '/CoherenceServerStart/' + coh_server_name)
  cmo.setJavaHome(JAVA_HOME)
  cmo.setArguments(coh_server_args)
  cmo.setClassPath(coh_server_cp)

  print 'Successfully created Coherence server name=[' + coh_server_name + ']'

########################################
def createForeignJMSSpringModules_online():
  jms_module_name = 'jms-module-ops-spring'

  cd('/')
  cmo.createJMSSystemResource(jms_module_name)

  cd('/JMSSystemResources/' + jms_module_name)
  set('Targets', jarray.array([ObjectName('com.bea:Name=cluster-1,Type=Cluster')], ObjectName))

  for n in range(1, int(managed_server_count) + 1):
    jms_server_name = jms_sever_name_base + '-' + str(n)
    foreign_server_name = 'ops-spring-foreign-server-' + str(n)

    cd('/JMSSystemResources/' + jms_module_name)
    cmo.createSubDeployment(jms_server_name + '-subdeployment')

    cd('/JMSSystemResources/' + jms_module_name + '/SubDeployments/' + jms_server_name + '-subdeployment')
    set('Targets', jarray.array([ObjectName('com.bea:Name=' + jms_server_name + ',Type=JMSServer')], ObjectName))

    cd('/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name)
    cmo.createForeignServer(foreign_server_name)

    cd(
      '/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name + '/ForeignServers/' + foreign_server_name)
    cmo.setDefaultTargetingEnabled(false)
    cmo.setSubDeploymentName(jms_server_name + '-subdeployment')

    cd(
      '/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name + '/ForeignServers/' + foreign_server_name)
    cmo.createForeignDestination('com.oracle.demo.ops.jms.eventTopic-' + str(n))

    cd(
      '/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name + '/ForeignServers/' + foreign_server_name + '/ForeignDestinations/com.oracle.demo.ops.jms.eventTopic-' + str(
        n))
    cmo.setRemoteJNDIName(jms_server_name + '@com.oracle.demo.ops.jms.eventTopic')
    cmo.setLocalJNDIName('foreign.com.oracle.demo.ops.jms.eventTopic')

########################################

def createSpringWLDFModule_online():
  cd('/')
  cmo.createWLDFSystemResource('SpringMBeanWLDFModule')

  cd('/WLDFSystemResources/SpringMBeanWLDFModule')
  cmo.setDescription('Spring_WLDF_Module')
  #set('Targets',jarray.array([ObjectName('com.bea:Name=cluster-1,Type=Cluster')], ObjectName))

  cd(
    '/WLDFSystemResources/SpringMBeanWLDFModule/WLDFResource/SpringMBeanWLDFModule/WatchNotification/SpringMBeanWLDFModule')
  cmo.createWatch('SpringCounterMBeanWatch')

  cd(
    '/WLDFSystemResources/SpringMBeanWLDFModule/WLDFResource/SpringMBeanWLDFModule/WatchNotification/SpringMBeanWLDFModule/Watches/SpringCounterMBeanWatch')
  cmo.setRuleType('Harvester')
  cmo.setEnabled(true)
  cmo.setRuleExpression(
    '(${ServerRuntime//[com.oracle.weblogic.examples.spring.counter.CounterBeanMBean]counter.bean:Name=CounterBean//Value} = \'\')')
  cmo.setAlarmType(None)

  cd(
    '/WLDFSystemResources/SpringMBeanWLDFModule/WLDFResource/SpringMBeanWLDFModule/WatchNotification/SpringMBeanWLDFModule')
  cmo.createJMSNotification('SpringCounterJMSNotification')

  cd(
    '/WLDFSystemResources/SpringMBeanWLDFModule/WLDFResource/SpringMBeanWLDFModule/WatchNotification/SpringMBeanWLDFModule/JMSNotifications/SpringCounterJMSNotification')
  cmo.setEnabled(true)
  cmo.setDestinationJNDIName('com.oracle.example.jms.wldf.notification')
  cmo.setConnectionFactoryJNDIName('com.oracle.example.jms.wldf.cf')

########################################

def createJavaEEWLDFModule_online():
  cd('/')
  cmo.createWLDFSystemResource('JavaEEMBeanWLDFModule')

  cd('/WLDFSystemResources/JavaEEMBeanWLDFModule')
  cmo.setDescription('JavaEE_WLDF_Module')
  #set('Targets',jarray.array([ObjectName('com.bea:Name=cluster-1,Type=Cluster')], ObjectName))

  cd(
    '/WLDFSystemResources/JavaEEMBeanWLDFModule/WLDFResource/JavaEEMBeanWLDFModule/WatchNotification/JavaEEMBeanWLDFModule')
  cmo.createWatch('JavaEECounterMBeanWatch')

  cd(
    '/WLDFSystemResources/JavaEEMBeanWLDFModule/WLDFResource/JavaEEMBeanWLDFModule/WatchNotification/JavaEEMBeanWLDFModule/Watches/JavaEECounterMBeanWatch')
  cmo.setRuleType('Harvester')
  cmo.setEnabled(true)
  cmo.setRuleExpression(
    '(${ServerRuntime//[com.oracle.weblogic.examples.mbeans.counter.CounterBeanMBean]counter.bean:Name=CounterBean//Value} = \'\')')
  cmo.setAlarmType(None)

  cd(
    '/WLDFSystemResources/JavaEEMBeanWLDFModule/WLDFResource/JavaEEMBeanWLDFModule/WatchNotification/JavaEEMBeanWLDFModule')
  cmo.createJMSNotification('JavaEECounterJMSNotification')

  cd(
    '/WLDFSystemResources/JavaEEMBeanWLDFModule/WLDFResource/JavaEEMBeanWLDFModule/WatchNotification/JavaEEMBeanWLDFModule/JMSNotifications/JavaEECounterJMSNotification')
  cmo.setEnabled(true)
  cmo.setDestinationJNDIName('com.oracle.example.jms.wldf.notification')
  cmo.setConnectionFactoryJNDIName('com.oracle.example.jms.wldf.cf')

############################################################################################################################################

def getJMSServerName(n):
  jms_server_name = jms_sever_name_base + '-' + str(n)
  return jms_server_name


############################################################################################################################################

def getManagedServerListenPort(n):
  managedServer_ListenPort = int(str(managed_server_port_base) + str(n))
  return managedServer_ListenPort;

############################################################################################################################################

def getManagedServerName(n):
  managedServerName = managedServerName_base + '-' + str(n)
  return managedServerName;

############################################################################################################################################

def getManagedServerAdminPort(n):
  managedServer_AdminPort = int(str(managed_server_admin_port_base) + str(n))
  return managedServer_AdminPort

############################################################################################################################################

def createEventDrivenSpringModule_online(moduleName, clusterTarget, jmsServerTargets):
  jms_module_name = moduleName

  cd('/')
  cmo.createJMSSystemResource(jms_module_name)

  cd('/JMSSystemResources/' + jms_module_name)
  set('Targets', jarray.array([ObjectName('com.bea:Name=cluster-1,Type=Cluster')], ObjectName))

  for n in range(1, int(managed_server_count) + 1):
    jms_server_name = getJMSServerName(n)
    foreign_server_name = 'ops-spring-foreign-server-' + str(n)

    # Subdeployment
    cd('/JMSSystemResources/' + jms_module_name)
    cmo.createSubDeployment(jms_server_name + '-subdeployment')
    cd('/JMSSystemResources/' + jms_module_name + '/SubDeployments/' + jms_server_name + '-subdeployment')
    set('Targets', jarray.array([ObjectName('com.bea:Name=' + jms_server_name + ',Type=JMSServer')], ObjectName))

    cd('/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name)
    cmo.createForeignServer(foreign_server_name)

    # Targeting
    cd(
      '/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name + '/ForeignServers/' + foreign_server_name)
    cmo.setDefaultTargetingEnabled(false)
    cmo.setSubDeploymentName(jms_server_name + '-subdeployment')

    # Foreign Destinations
    cd(
      '/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name + '/ForeignServers/' + foreign_server_name)
    cmo.createForeignDestination('com.oracle.demo.ops.jms.eventTopic-' + str(n))

    cd(
      '/JMSSystemResources/' + jms_module_name + '/JMSResource/' + jms_module_name + '/ForeignServers/' + foreign_server_name + '/ForeignDestinations/com.oracle.demo.ops.jms.eventTopic-' + str(
        n))
    cmo.setRemoteJNDIName(jms_server_name + '@com.oracle.demo.ops.jms.eventTopic')
    cmo.setLocalJNDIName('foreign.com.oracle.demo.ops.jms.eventTopic')

############################################################################################################################################

def configuraManagedServersOnline():
  for n in range(1, int(managed_server_count) + 1):
    managedServer_Name = getManagedServerName(n)
    cd('/Servers/' + managedServer_Name + '/')
    cmo.setHealthCheckIntervalSeconds(60)
    cmo.setStuckThreadMaxTime(180)

    cd('/Servers/' + managedServer_Name + '/OverloadProtection/' + managedServer_Name)
    cmo.createServerFailureTrigger()

    cd(
      '/Servers/' + managedServer_Name + '/OverloadProtection/' + managedServer_Name + '/ServerFailureTrigger/' + managedServer_Name)
    cmo.setMaxStuckThreadTime(180)
    cmo.setStuckThreadCount(0)

############################################################################################################################################

def createCoherenceServers(coh_cluster_name, coh_listen_address, coh_listen_port, coh_server_args, coh_server_count,
                           coh_server_cp, machine_Name):
  for n in range(1, int(coh_server_count) + 1):
    coh_server_name = 'coh-' + str(n)
    createCoherenceServer(coh_server_name,
                          coh_cluster_name, machine_Name,
                          coh_listen_address,
                          coh_listen_port,
                          coh_server_args,
                          coh_server_cp)

############################################################################################################################################

def deploySharedLibraries():

  deploySharedLibrary('coherence', COHERENCE_HOME + '/lib/coherence.jar')
  deploySharedLibrary('coherence-web-spi', COHERENCE_HOME + '/lib/coherence-web-spi.war')
  deploySharedLibrary('active-cache', WL_HOME + '/common/deployable-libraries/active-cache-1.0.jar')
  deploySharedLibrary('weblogic-spring', WL_HOME + '/server/lib/weblogic-spring.jar')
  deploySharedLibrary('toplink-grid', WL_HOME + '/common/deployable-libraries/toplink-grid-1.0.jar')

############################################################################################################################################

def deploySharedLibrary(appName, appPath):
  progress = deploy(appName=appName, path=appPath, targets=cluster_Name, libraryModule='true')
  progress.printStatus()

############################################################################################################################################
#BEGIN MAIN

var_domain_dir = USER_PROJECTS + '/domains/' + DOMAIN_NAME
print 'Creating domain...'
print '--var_domain_dir=' + var_domain_dir
print '--DOMAIN_TEMPLATE=' + DOMAIN_TEMPLATE
print '--adminServer_Username=' + adminServer_Username
print '--adminServer_Password=' + adminServer_Password

try:
  createDomain(DOMAIN_TEMPLATE, var_domain_dir, adminServer_Username, adminServer_Password)
  print 'Domain created Sucessfully'

  readDomain(var_domain_dir)
  print 'Domain Read Successfully'

  cd('/')
  cmo.setExalogicOptimizationsEnabled(false)
  cmo.setClusterConstraintsEnabled(false)
  cmo.setGuardianEnabled(false)
#  cmo.setAdministrationPort(int(adminServer_AdministrationPort))
#  cmo.setAdministrationPortEnabled(true)
  cmo.setConsoleEnabled(true)
  cmo.setConsoleExtensionDirectory('console-ext')
  cmo.setProductionModeEnabled(false)
  cmo.setAdministrationProtocol('t3s')
  cmo.setConfigBackupEnabled(false)
  cmo.setConfigurationAuditType('none')
  cmo.setInternalAppsDeployOnDemandEnabled(false)
  cmo.setConsoleContextPath('console')

  print 'Configuring Admin Server...'
  cd('/Servers/AdminServer')
  cmo.setListenPortEnabled(true)
  cmo.setListenPort(int(adminServer_ListenPort))
  cmo.setListenAddress(listen_address)
  cmo.setWeblogicPluginEnabled(false)
  cmo.setJavaCompiler('javac')
  cmo.setStartupMode('RUNNING')
  cmo.setVirtualMachineName(DOMAIN_NAME + '_AdminServer')
  cmo.setClientCertProxyEnabled(false)

  print 'Configuring Admin Server ServerStart...'
  create('AdminServer', 'ServerStart')
  cd('/Servers/AdminServer/ServerStart/AdminServer')
  cmo.setJavaHome(JAVA_HOME)
  cmo.setArguments('-Xms=256m -Xmx=256m -Dweblogic.nodemanager.sslHostNameVerificationEnabled=false -Dweblogic.security.SSL.ignoreHostnameVerify=true -Dweblogic.security.SSL.ignoreHostnameVerification=true -Dweblogic.security.TrustKeyStore=DemoTrust')

except:
  print 'Unable to create domain!'
  dumpStack()
  exit()

try:
  print 'updating domain'
  updateDomain()
except:
  print 'Unable to update domain'
  dumpStack()
  exit()

def createJMSModules(clusterMBean, jmsServerMBeans):
  createBaseJMSResources('jms-module-base', clusterMBean, jmsServerMBeans)
  createUOWResources('jms-module-uow', clusterMBean, jmsServerMBeans)
  createUOOResources('jms-module-uoo', clusterMBean, jmsServerMBeans)
  createUtilityJMSResources('jms-module-util', clusterMBean, jmsServerMBeans)
  createOPSJMSResources('jms-module-ops', clusterMBean, jmsServerMBeans)
  createSAFSourceModules()
  createSAFStoresAndAgents()
  createWLDFJMSResources('jms-module-wldf', clusterMBean, jmsServerMBeans)
  createMigrationJMSResources('jms-module-migration', clusterMBean, jmsServerMBeans)
  createSpringJMSTempResources('jms-module-temp', clusterMBean, jmsServerMBeans)


cd('/')
machine = createMachine(machine_Name, 'Plain', machine_ListenAddress, 5556)

cd('/')
clusterMBean = create(cluster_Name, 'Cluster')

try:
  jdbcClusterDatasource = createPhysicalDataSource(['com.oracle.demo.ops.jdbc.cluster-ds'],
                                                                                          datasource_jdbc_driver,
                                                                                          datasource_global_transactions
                                                                                          ,
                                                                                          datasource_jdbc_url,
                                                                                          datasource_user,
                                                                                          datasource_password,
                                                                                          clusterMBean)
  clusterMBean.setMigrationBasis('database')

  clusterMBean.setDataSourceForAutomaticMigration(jdbcClusterDatasource)


  ####### Create Managed Servers

  print 'Creating ' + str(managed_server_count) + ' Managed Servers...'

  for n in range(1, int(managed_server_count) + 1):
    managedServer_Name = getManagedServerName(n)
    managedServer_ListenPort = getManagedServerListenPort(n)
    managedServer_ListenAddress = listen_address
    managedServer_AdminPort = getManagedServerAdminPort(n)

    migratableTargetName = managedServer_Name + ' (migratable)'

    print 'Creating Server Name=[' + managedServer_Name + '] with Listen Port: ' + str(managedServer_ListenPort)
    cd('/')
    managedServer = create(managedServer_Name, 'Server')
    managedServer.setListenPort(managedServer_ListenPort)
    managedServer.setListenAddress(managedServer_ListenAddress)
    managedServer.setAdministrationPort(managedServer_AdminPort)
    managedServer.setCluster(clusterMBean)
    managedServer.setMachine(machine)
    managedServer.setAutoRestart(false)
    managedServerMBeans.append(managedServer)

    cd('/Servers/' + managedServer_Name)
    managedServerStart = create(managedServer_Name, 'ServerStart')
    managedServerStart.setArguments(
      '-Xmx=512m -Xms=256m -Dweblogic.nodemanager.sslHostNameVerificationEnabled=false -Dweblogic.security.SSL.ignoreHostnameVerify=true -Dweblogic.security.SSL.ignoreHostnameVerification=true -Dweblogic.security.TrustKeyStore=DemoTrust -Dtangosol.coherence.ttl=0 -Dtangosol.coherence.distributed.localstorage=true -Dtangosol.coherence.session.localstorage=true -Dtangosol.coherence.cacheconfig=' + wins_demos_home + '/coherence-examples/session-cache-config.xml -Dcom.sun.jersey.server.impl.cdi.lookupExtensionInBeanManager=true')
    managedServerStart.setJavaHome(JAVA_HOME)

    cd('/')

    print 'Creating MigratableTarget name=[' + migratableTargetName + ']'
    migratableTarget = create(migratableTargetName, 'MigratableTarget')
    migratableTarget.setUserPreferredServer(managedServer)
    migratableTarget.setMigrationPolicy('failure-recovery')
    migratableTargetMBeans.append(migratableTarget)

    migratableTarget.setConstrainedCandidateServers(managedServerMBeans)

    jmsServer_Name = getJMSServerName(n)
    jdbcStoreName = jmsServer_Name + '-jdbcStore'
    managedServer_Name = managedServerMBeans[n - 1].getName()

    print 'Managed Server Name: ' + managedServer_Name

    jmsServerTargets = jarray.array([migratableTargetMBeans[n - 1]], weblogic.management.configuration.TargetMBean)

    print 'Creating JMS JDBC Store [' + jdbcStoreName + ']'
    cd('/')
    jdbcStore = create(jdbcStoreName, 'JDBCStore')
    jdbcStore.setDataSource(jdbcClusterDatasource)
    jdbcStore.setPrefixName('JMSSTORE' + str(n))
    jdbcStore.setTargets(jmsServerTargets)

    print 'Creating JMS Server Name=[' + jmsServer_Name + ']'
    cd('/')
    jmsServer = create(jmsServer_Name, 'JMSServer')
    jmsServerMBeans.append(jmsServer)
    jmsServer.setPersistentStore(jdbcStore)
    jmsServer.setTargets(jmsServerTargets)

    cd('/')

  createJMSModules(clusterMBean, jmsServerMBeans)

  cd('/')

  createPhysicalDataSource([datasource_jndi_name],
                                                 datasource_jdbc_driver,
                                                 datasource_global_transactions,
                                                 datasource_jdbc_url,
                                                 datasource_user,
                                                 datasource_password,
                                                 clusterMBean)
  updateDomain()

except:
  dumpStack()
  print 'Unable to complete domain offline steps'
  exit()

print ''
print '-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-'
print 'Completed WLST OFFLINE successfully...!!!'
print '-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-'
print ''

########################################################################################################################
########################################################################################################################

print ''
print '-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-'
print 'Beginning ONLINE configuration tasks'
print '-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-==-'
print ''

print ''
print '============================================='
print 'Connecting to NODE MANAGER..'
print '============================================='
print ''

nmConnect(adminServer_Username, adminServer_Password, machine_ListenAddress, 5556, DOMAIN_NAME, var_domain_dir, 'plain')

print ''
print '============================================='
print 'Starting AdminServer...'
print '============================================='
print ''

nmStart('AdminServer')

connect(adminServer_Username, adminServer_Password, adminServer_AdministrationURL)

edit()
startEdit()

configuraManagedServersOnline()

createCoherenceCluster(coh_cluster_name,
                       coh_listen_address,
                       coh_listen_port,
                       jarray.array([ObjectName('com.bea:Name=' + cluster_Name + ',Type=Cluster')], ObjectName),
                       coh_ttl)

createCoherenceServers(coh_cluster_name, coh_listen_address, coh_listen_port, coh_server_args, coh_server_count,
                       coh_server_cp, machine_Name)

print '============================================='

createSpringWLDFModule_online()
createJavaEEWLDFModule_online()

print 'Creating OPS Modules'
createEventDrivenSpringModule_online('jms-module-ops-foreign', clusterMBean, jmsServerMBeans)

print '============================================='

save()
activate(block="true")

deploySharedLibraries()

shutdown()