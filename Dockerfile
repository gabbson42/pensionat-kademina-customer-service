FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/*.jar app.jar

#Endast Dokumentation
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]