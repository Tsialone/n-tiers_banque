# Aller dans le dossier du premier serveur et compiler
mvn clean install package

cp server-ejb/target/server-ejb.war server-ejb/deploy/
cp client-ejb/target/client-ejb.war server-ejb/deploy/


# restore dotnet
dotnet restore ./pret
dotnet restore ./epargne

#revenir a la racine
docker compose down 
docker compose up -d --build
