call asadmin create-domain --user admin --passwordfile pw.txt --adminport 4848 cheetahplatform
call asadmin change-master-password --savemasterpassword=true cheetahplatform
call asadmin start-domain cheetahplatform
call asadmin login

REM MySQL
call asadmin create-jdbc-connection-pool --user admin --passwordfile pw.txt --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlXADataSource --restype javax.sql.DataSource --property portNumber=3306:password=topsecret:user=cheetahplatform:serverName=localhost:databaseName=cheetahplatform MySQLPool
call asadmin create-jdbc-resource --connectionpoolid MySQLPool --user admin --passwordfile pw.txt jdbc/MySQLPool

REM create JMS resources
call asadmin create-jms-resource --restype javax.jms.QueueConnectionFactory --user admin --passwordfile pw.txt jms/QueueConnectionFactory
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/main
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/login_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/sessionVerifier_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/dispatcher_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/getActiveTasks_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/getRecommendation_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/dummy
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/launchActivity_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/completeActivity_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/cancelActivity_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/instantiateSchema_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/generateTestData_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/generateSchema_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/retrieveLateBindingBoxSubProcesses_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/selectLateBindingSequence_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/retrieveLateModelingBox_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/initializeCacheService_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/selectLateModelingBoxContentService_1.0
call asadmin create-jms-resource --restype javax.jms.Queue --user admin --passwordfile pw.txt jms/queue/terminateDeclarativeProcessInstanceService_1.0



REM create Lifecycle module for initializing jboss cache
call asadmin create-lifecycle-module --classname org.cheetahplatform.cacheInitializer.CacheInitializer --user admin --passwordfile pw.txt --failurefatal=true CacheInitializer

REM shut down to allow the eclipse plug-in taking over
call asadmin stop-domain cheetahplatform