# GymApp-Backend
Developed using Java 17 and Spring Boot.

## About The Project
The purpose of the application is to help the persons that go to the gym regularly, by
keeping track of their progress throughout time and by managing their workout
routines. The application also targets a social side, by sharing thoughts about
people’s workouts.
## Business Requirements
1. Users should be able to authenticate/register into the application.
2. Users should be able to manage their profile and view other users.
3. Users should be able to view the available gyms.
4. Users should be able to start a new empty workout.
5. Users should be able to add exercises and sets to an active workout.
6. To find a specific exercise, users should be able to filter through exercises by
specifying the name.
7. Users should be able to finish a workout and view details about their workout
(with insights about the volume and duration calculated automatically).
8. Users should be able to add comments to workouts they find interesting and
manage them.
9. Users should be able to view their favourite gym (calculated based on the
number of workouts done there).
10.Admins should be able to add new gyms in the application and manage them.
11. Admins should be able to add new examples of exercises in the application
and manage them.

## Five Main Features
### User
1. Two types of user: ADMIN and USER. The admins are allowed to
manage entities (such as exercise, gym etc.) that concern all users,
they have full control over the application, while the normal users can
make basic operations on the workouts.
2. Represent a way to identify into the application.
3. Hold information such as first name, last name, email, age.

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
