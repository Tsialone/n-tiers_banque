#!/bin/bash

WILDFLY_HOME="/home/tsialone/wildfly"
DEPLOYMENTS="$WILDFLY_HOME/standalone/deployments"



PID=$(lsof -ti:8080)
if [ -n "$PID" ]; then
    echo "Killing process on port 8080 (PID $PID)..."
    kill -9 $PID
fi
