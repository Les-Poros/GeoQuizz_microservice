FROM openjdk:8
VOLUME /tmp
ADD ./Mobile/target/mobile-0.0.1-SNAPSHOT.jar mobile.jar
RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/mobile.jar"]