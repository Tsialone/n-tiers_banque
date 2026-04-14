-- aretter tout les containers 
docker stop $(docker ps -q)

-- supprimer tout les containers
docker container prune -f

psql -h localhost -p 5432 -U postgres -d epargne_db
psql -h localhost -p 5433 -U postgres -d pret_db
psql -h localhost -p 5434 -U postgres -d ejb_db


server version
WildFly Core 19.0.1.Final


