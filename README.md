# fridget-server

[![Build Status](https://travis-ci.org/YuKitAs/fridget-server.svg?branch=master)](https://travis-ci.org/YuKitAs/fridget-server)

The server-side of [Fridget](https://github.com/YuKitAs/fridget-android) built with [Spring Boot](https://spring.io/projects/spring-boot).

## Local Development

### Prerequisite

* MySQL (>= 5.5, < 8.0) is installed and running
* JDK 1.8 is installed
* Port `8080` is not in use

### Run Server

* Configure database according to spring configurations specified in `src/main/resources/application-development.yml`.
* Run the following Bash script to execute Gradle `bootRun` task with Spring profile `development`:
  ```console
  $ ./boot-run.sh
  ```
* The server will be running by default under `http://localhost:8080`

### Test

* Configure database according to spring configurations specified in `src/test/resources/application.yml`.
* Execute the following Gradle task to run all the unit tests:
  ```console
  $ ./gradlew test
  ```
* Execute the following Gradle task to run all the integration tests:
  ```console
  $ ./gradlew integrationTest
  ```
* Run the following Bash script for all tests:
  ```console
  $ ./test.sh
  ```

## Production

### Prerequisite

* [Docker](https://docs.docker.com/install/) (>=18.04) and [Docker Compose](https://docs.docker.com/compose/install/#prerequisites) are installed
* A MySQL image named `mysql:5.7.23` is running (see [note](https://github.com/YuKitAs/tech-note/blob/master/container/dockerize-spring-boot-app-with-mysql.md))

### Run Server in Docker Container

* Build artefact `fridget-server-0.1.0.jar` with Gradle Wrapper:
  ```console
  $ ./gradlew clean build
  ```
  
  [Optional] Skip the tests:
  ```console
  $ ./gradlew build -x test integrationTest
  ```
  
* Build Docker image from the existing `Dockerfile`:
  ```console
  # docker build --no-cache -t fridget-server-docker .
  ```
  
* Use Docker Compose to link containers:
  ```console
  # docker-compose up
  ```

## TODO

See cards on [project board](https://github.com/YuKitAs/fridget-android/projects/3)
