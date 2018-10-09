#!/bin/bash

SPRING_PROFILES_ACTIVE=development ./gradlew clean bootRun

# For Gradle 4.9 or later, use the following command instead.
# ./gradlew bootRun --args='--spring.profiles.active=development'
