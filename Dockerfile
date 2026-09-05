FROM eclipse-temurin:26
RUN mkdir /opt/app
ARG JAR_FILE
COPY ${JAR_FILE} /opt/app/japp.jar
CMD ["java", "-jar", "/opt/app/japp.jar"]