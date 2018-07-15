# fridget-server

[![Build Status](https://travis-ci.org/YuKitAs/fridget-server.svg?branch=master)](https://travis-ci.org/YuKitAs/fridget-server)

The server-side of [Fridget](https://github.com/YuKitAs/fridget-android) built with [Spring Boot](https://spring.io/projects/spring-boot).

## Usage

## Prerequisite

* MySQL (>= 5.5, < 8.0) is installed and running
* JDK 1.8 is installed
* Port `8080` is not in use

### Development

* Configure database according to spring configurations specified in `src/main/resources/application.yml`.
* Run the following Bash script to execute Gradle `bootRun` task with Spring profile `development`:
  ```console
  $ ./build.sh
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

## TODO

See cards on [project board](https://github.com/YuKitAs/fridget-android/projects/3)
