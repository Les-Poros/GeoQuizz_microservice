FROM openjdk:8

RUN mkdir /app
WORKDIR /app

ADD ./target/player-0.0.1-SNAPSHOT.jar /app/player.jar
RUN bash -c 'touch /restservice.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/player.jar"]