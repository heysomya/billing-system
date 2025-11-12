## Backend:
## Environment Setup and Requirements
1. Install Java JDK (used 21 version) and Maven
2. Intellij IDE
3. Chrome is required for automation testing

#### Tech Stack & Frameworks
1. Language: Java 21
2. Framework: Spring Boot
3. Database: Supabase (PostgreSQL)
4. Build Tool: Maven
5. API Testing: Postman
6. Version control: Git
7. Testing framework: Junit5 (Unit testing), TestNG (automation testing)
8. Selenium
9. Authentication: JWT

#### Microservices: 
1. product catalog: Server Port: 8090 
2. stock management: Server Port: 8091
3. sale recording: Server Port: 8092
4. user authentication: Server Port: 8093
5. reports: Server Port: 8094
6. user management: Server Port: 8095


#### Running the application:
1. Open the project in intellij 
2. In Integrated terminal, Go to the required service
3. Execute "mvn spring-boot:run" and click enter
4. You will see tomcat running on the server port for that service.
    1. The service will be running in http://localhost:{port_number}/
5. Run all the services so that frontend will be connected to backend in the specified ports

#### Run test file for unit testing:
Command: mvn test
