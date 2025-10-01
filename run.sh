# Aller dans le dossier du premier serveur et compiler
mvn clean package -f server_ejb

# restore dotnet
dotnet restore ./pret
dotnet restore ./epargne

#revenir a la racine
docker compose down 
docker compose up -d --build
