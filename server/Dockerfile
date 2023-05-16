FROM adoptopenjdk/openjdk11:latest as build
COPY . /usr/app
WORKDIR /usr/app
RUN chmod +x mvnw \
    && ./mvnw --version \
    && ./mvnw clean package
FROM adoptopenjdk/openjdk11:latest
COPY --from=build /usr/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]