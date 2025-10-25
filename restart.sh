#!/bin/bash

WILDFLY_HOME="/home/tsialone/wildfly"
DEPLOYMENTS="$WILDFLY_HOME/standalone/deployments"



PID=$(lsof -ti:8080)
if [ -n "$PID" ]; then
    echo "Killing process on port 8080 (PID $PID)..."
    kill -9 $PID
fi


$WILDFLY_HOME/bin/standalone.sh -b 0.0.0.0 &

tail -f $WILDFLY_HOME/standalone/log/server.log
