#!/bin/bash

echo "Création de l'architecture pour le serveur TomEE..."

mkdir -p server/src/main/java/com/example/ejb
mkdir -p server/src/main/java/com/example/rest
mkdir -p server/src/main/java/com/example/api
touch server/pom.xml
touch server/Dockerfile

echo "Architecture créée pour le serveur."
