# Usa una imagen oficial de OpenJDK como base
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el jar construido por Maven
COPY target/microServicioFactura-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la app
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]