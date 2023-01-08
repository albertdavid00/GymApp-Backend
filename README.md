# GymApp-Backend
## About The Project
GymApp is a REST API made using Spring Boot for the people that want to easily keep track of their gym progress.

<!-- GETTING STARTED -->
## Getting Started

### Installation

1. Clone the repo
   ```sh
   https://github.com/albertdavid00/GymApp-Backend.git
   ```
2. Install Docker Desktop from [here](https://www.docker.com/products/docker-desktop).
  
3. Run MariaDB instance inside a docker container
   ```sh
   docker pull mariadb:10.4
   docker run --name mariadb-gymapp -e MYSQL_ROOT_PASSWORD=mypass -p 3310:3306 -d mariadb:10.4
   
   Create Schema gymapp using MySQL Workbench
   ```
4. Run Keycloak instance inside a docker container
   ```sh
   docker pull jboss/keycloak
   docker run -d -p 8081:8080 --name keycloak-gymapp jboss/keycloak
   docker exec keycloak-gymapp /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin
   docker restart keycloak-gymapp
   
   Accessing localhost:8081/auth (username: admin, password: admin)
   Add Realm -> GymApp
   Create client -> gym-client
   Add Realm Roles -> ADMIN, USER
   ```
