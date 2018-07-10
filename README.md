# fridget-server

The server-side of [Fridget](https://github.com/YuKitAs/fridget-android) built with [Spring Boot](https://spring.io/projects/spring-boot).

**Usage**

* Make sure MySQL (>= 5.5) is installed and running.
* Configure datenbank according to spring configurations for development environment specified in `src/main/resources/application.yml`.
* Run the following Bash script to execute Gradle `bootRun` task with Spring profile `development`:
  ```console
  $ ./build.sh
  ```

**TODO**

See cards in [Project](https://github.com/YuKitAs/fridget-android/projects/3)
