#!/bin/sh
JAVA_OPTS="-server -Xss500m"
JAVA_OPTS="${JAVA_OPTS} -XX:SurvivorRatio=10"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC  -XX:CMSMaxAbortablePrecleanTime=5000 -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=80"
JAVA_OPTS="${JAVA_OPTS} -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="${JAVA_OPTS} -XX:+DisableExplicitGC"
JAVA_OPTS="${JAVA_OPTS} -verbose:gc -Xloggc:/root/logs/app-gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
JAVA_OPTS="${JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/root/logs/app-java.hprof"
JAVA_OPTS="${JAVA_OPTS} -Djava.awt.headless=true"
JAVA_OPTS="${JAVA_OPTS} -Dsun.net.client.defaultConnectTimeout=10000"
JAVA_OPTS="${JAVA_OPTS} -Dsun.net.client.defaultReadTimeout=30000"
JAVA_OPTS="${JAVA_OPTS} -Dserver.port=9000"
JAVA_OPTS="${JAVA_OPTS} -DTESLA_MYSQL_JDBC_URL=*****"
JAVA_OPTS="${JAVA_OPTS} -DTESLA_MYSQL_USERNAME=*****"
JAVA_OPTS="${JAVA_OPTS} -DTESLA_MYSQL_PASSWORD=*****"
JAVA_OPTS="${JAVA_OPTS} -DDUBBO_REGISTRY_ADDRESS=*****"
JAVA_OPTS="${JAVA_OPTS} -DSPRINGCLOUD_REGISTRY_ADDRESS=***"

java $JAVA_OPTS -jar gateway-1.0.0.jar