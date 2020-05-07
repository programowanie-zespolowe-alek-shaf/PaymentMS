FROM maven:3.6.3-openjdk-11
WORKDIR app
COPY p-application ./p-application
COPY p-mysql ./p-mysql
COPY pom.xml ./

ENV MYSQL_DB_HOST name
ENV MYSQL_DB_PORT 3306
ENV MYSQL_DATABASE payment
ENV MYSQL_DB_USERNAME payment
ENV MYSQL_DB_PASSWORD payment
ENV EUREKA_HOST name
ENV EUREKA_PORT 8761

RUN mvn clean install -DskipTests -Pprod
RUN mv p-application/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]