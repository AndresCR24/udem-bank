# Usar una imagen base confiable con JDK 17
FROM amazoncorretto:17

# Establecer directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto
COPY . .

# Dar permisos al wrapper de Gradle
RUN chmod +x ./gradlew

# Construir la aplicación sin ejecutar tests
RUN ./gradlew build -x test

RUN ls -l build/libs/

# Copiar el archivo JAR generado
RUN cp build/libs/app.jar app.jar


# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
