FROM java:8
VOLUME /tmp
EXPOSE 8080
ADD build/libs/fridget-server-0.1.0.jar fridget-server-0.1.0.jar
RUN bash -c 'touch /fridget-server-0.1.0.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=production","-jar","/fridget-server-0.1.0.jar"]
