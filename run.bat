@echo off
REM ============================================
REM Script de build et de lancement Docker
REM ============================================

echo --- Compilation du projet Java (server_ejb) ---
cd server_ejb
call mvn clean package install 

cd ..

echo --- Restauration des dépendances .NET (pret) ---
dotnet restore .\pret

echo --- Restauration des dépendances .NET (epargne) ---
dotnet restore .\epargne


echo --- Reconstruction et démarrage des conteneurs Docker ---
docker-compose down
docker-compose up -d --build
