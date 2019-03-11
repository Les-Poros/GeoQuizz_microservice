FROM openjdk:8
VOLUME /tmp
ADD ./Player/target/player-0.0.1-SNAPSHOT.jar player.jar
RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/player.jar"]