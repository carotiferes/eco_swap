#FROM openjdk:11.0.5-jre-slim as builder

#COPY . /app
#WORKDIR /app
#RUN ./gradlew clean build --info --stacktrace
#RUN ./gradlew clean build -x test --info --stacktrace --no-daemon

#FROM 356317836475.dkr.ecr.us-east-1.amazonaws.com/openjdk:11.0.5-jre-slim


#FROM openjdk:11.0.5-jre-slim
FROM openjdk:11.0.5-jre-slim 
#as builder
COPY . /app
EXPOSE 8080
ADD ./build/libs/ms-auth-manager-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


