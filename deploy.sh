#!/bin/bash

WILDFLY_HOME="/home/tsialone/wildfly"
DEPLOYMENTS="$WILDFLY_HOME/standalone/deployments"

mvn clean install compile package



rm -f $DEPLOYMENTS/server-ejb.war
rm -f $DEPLOYMENTS/client-ejb.war
rm -f $DEPLOYMENTS/change-ejb.war

cp server-ejb/target/server-ejb.war $DEPLOYMENTS/
cp client-ejb/target/client-ejb.war $DEPLOYMENTS/

docker cp change-ejb/target/change-ejb.jar change-server:/opt/jboss/wildfly/standalone/deployments/change-ejb.jar


