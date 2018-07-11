# fridget-server

The server-side of [Fridget](https://github.com/YuKitAs/fridget-android) built with [Spring Boot](https://spring.io/projects/spring-boot).

**Usage**

* Make sure MySQL (>= 5.5, < 8.0) is installed and running.
* Configure database according to spring configurations for development environment specified in `src/main/resources/application.yml`.
* Run the following Bash script to execute Gradle `bootRun` task with Spring profile `development`:
  ```console
  $ ./build.sh
  ```
* The server will be running by default under `http://localhost:8080`

**TODO**

See cards on [project board](https://github.com/YuKitAs/fridget-android/projects/3)
