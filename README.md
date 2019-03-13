# GeoQuizz_microservice

Membres du projet :
- Maeva Butaye    ( Lilychaan )
- Camille Schwarz ( S-Camille )
- Léo Galassi     ( ElGitariste )
- Quentin Rimet   ( QuentinRimet )

# Prérequis :

* Maven
* Docker
* Docker-compose

## Pour récupérer les micro-services

* Cloner le dépôt soit :
    - via SSH : git clone git@github.com:Les-Poros/GeoQuizz_microservice.git
    - via HTTPS : git clone https://github.com/Les-Poros/GeoQuizz_microservice.git
    
## Lancer les micro-services

Dans chaque dossier faire : 
* mvn clean install -DskipTests 

Puis revenir dans le dossier source et faire :
* docker-compose up --build -d

Vous aurez alors accés aux differents micro-services via les ports suivants :

* Api Player : {VotreAdresseDocker}:8082
* Api BackOffice : {VotreAdresseDocker}:8083
* Api Mobile : {VotreAdresseDocker}:8084

## Ajouter la bdd prédéterminée

À la fin de l'installation complète des dockers contenant les micro-services, faire: 
* docker cp ./geoquizz_data.sql postgres:/docker-entrypoint-initdb.d/dump.sql
* docker exec -u postgres postgres psql postgres postgres -f docker-entrypoint-initdb.d/dump.sql
