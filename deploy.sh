#!/bin/bash

WILDFLY_HOME="/home/tsialone/wildfly"
DEPLOYMENTS="$WILDFLY_HOME/standalone/deployments"

mvn clean install  package



rm -f $DEPLOYMENTS/server-ejb.war
rm -f $DEPLOYMENTS/server-api.jar

rm -f $DEPLOYMENTS/client-ejb.war
# rm -f $DEPLOYMENTS/change-ejb.war

cp server-ejb/target/server-ejb.war $DEPLOYMENTS/
cp server-api/target/server-api.jar $DEPLOYMENTS/
cp client-ejb/target/client-ejb.war $DEPLOYMENTS/

docker cp change-ejb/devises.json change-server:/opt/devises.json

# docker exec change-server rm -f /opt/jboss/wildfly/standalone/deployments/change-ejb.war
# le .war
docker cp change-ejb/target/change-ejb.war change-server:/opt/jboss/wildfly/standalone/deployments/change-ejb.war

# le .jar
docker cp change-api/target/change-api.jar change-server:/opt/jboss/wildfly/standalone/deployments/change-api.jar



