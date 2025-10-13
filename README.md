-- aretter tout les containers 
docker stop $(docker ps -q)


psql -h localhost -p 5434 -U postgres -d ejb_db
