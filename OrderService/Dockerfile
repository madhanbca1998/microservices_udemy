FROM openjdk:17

#Storing the .jar file from target folder after we build it using mvn clean install
ARG JAR_FILE=target/*.jar
#copying it and storing as serviceregistry.jar
COPY ${JAR_FILE} orderservice.jar
#To access the file
ENTRYPOINT ["java","-jar","/orderservice.jar"]
EXPOSE 8082
