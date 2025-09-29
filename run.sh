# Aller dans le dossier du premier serveur et compiler
cd server_ejb
mvn clean package

# Revenir à la racine
cd ..
docker compose down 
docker compose up -d --build
