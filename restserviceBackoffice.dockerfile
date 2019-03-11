FROM openjdk:8
VOLUME /tmp
ADD ./Backoffice/target/backoffice-0.0.1-SNAPSHOT.jar backoffice.jar
RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/backoffice.jar"]