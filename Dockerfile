FROM maven:3.8-openjdk-17

WORKDIR /app

COPY pom.xml .

COPY src ./src

EXPOSE 8080

RUN mvn clean package -DskipTests

RUN mv target/*.jar target/core.jar

ENTRYPOINT ["java", "-jar", "target/core.jar"]