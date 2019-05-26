# Examination Management Application 
#### TechStack and additional libraries:

- Spring Boot Framework v2.1.3 + JavaFX (Java 8)
- Hibernate (ORM)
- H2 Embedded Database (in memory) 
- Liquibase (creating database structure from XML file)
- Tomcat Embedded Application Server
- Lombok (automatic generation of getters/setters etc.)
- MapStruct (mapping between data layers Entity/Business Object)
- Maven (build project tool)

### HOW TO RUN?

- Install JDK version **jdk1.8.0_211** from:  https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

- Install APACHE MAVEN from: https://maven.apache.org/download.cgi

- Configure Java and Maven

- Run following command in project directory to build project:
```sh
mvn clean package
```

- After downloading necessary packages from maven repository tool will build a project

- If build will be SUCCESSFUL you can run project by command:

```sh
java -jar <project directory>/target/ManagementApp_SpringBootJavaFX.jar
```

- Now you can enjoy the application 

### IF YOU HAVE A COMPILED *.JAR FILE YOU CAN SIMPLY RUN BY:

```sh
Start "" /B <path-to-jdk-1.8.0_211>\bin\javaw.exe -jar <path-to-jar-file>\ManagementApp_SpringBootJavaFX.jar
```

### DEFAULT CREDENTIALS FOR DIFFERENT USER ROLES:

Role | Username | Password
--- | --- | ---
ADMINISTRATOR | test@test.com | qwe123
USER | test1@test.com | qwe123
DOCTOR | test2@test.com | qwe123
TECHNICIAN | test3@test.com | qwe123

### ACCESS TO DATABASE

- Go to the internet browser to address

```sh
localhost:8086/h2-console/
```

- With settings:

```sh
Driver class:       org.h2.Driver
    JDBC URL:       jdbc:h2:mem:testdb
   User name:       root
    Password:       <empty>
```

- Now you can view raw data in every table.