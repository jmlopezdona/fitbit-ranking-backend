FROM maven:3.5.2-jdk-8 AS build
COPY docker/settings.xml /usr/share/maven/conf/settings.xml
WORKDIR /app
COPY pom.xml /app
#RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline
RUN mvn dependency:go-offline
COPY src /app/src
#RUN mvn -B -e -o -T 1C verify
RUN mvn package

FROM openjdk:8-jre-alpine
COPY --from=build /app/target/fitbit-ranking*.jar /app/fitbit-ranking.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/fitbit-ranking.jar"]