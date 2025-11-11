# billing-system (BillFast)

## Overview
Inventory Management System designed to streamline retail operations by integrating product
management, stock tracking, sales processing, and reporting into one platform.


## Backend:
## Environment Setup
1. Install Java JDK (used 21 version) and Maven
2. Intellij IDE and VSCode

#### Tech Stack
1. Language: Java
2. Framework: Spring Boot
3. Database: Supabase (PostgreSQL)
4. Build Tool: Maven
5. API Testing: Postman
6. Version control: Git
7. Testing framework: Junit5 (Unit testing), TestNG (automation testing)
8. 

#### Microservices: 
1. product catalog: Server Port: 8090 
2. stock management: Server Port: 8091
3. sale recording: Server Port: 8092
4. user authentication: Server Port: 8093
5. reports: Server Port: 8094
6. user management: Server Port: 8095


#### Running the application:
1. In terminal, Go to the required service
2. Execute "mvn spring-boot:run" and click enter
3. You will see tomcat running on the server port for that service.
    1. The service will be running in http://localhost:{port_number}/
4. Run all the services so that frontend will be connected to backend in the specified ports

#### Run test file for unit testing:
Command: mvn test


#### Automation Testing:
### Dependencies
- Selenium
- Spring Boot
- WebDriverManager (bonigarcia)
- testng (TestNG framework)
- extentreports (For reports)
- commons-io (For screenshots capturing)

### Running Tests
- To run the test select /testng.xml file
- Right click on the file and
- click run the file

### Reports
- test report file can be found under target/site/extent-report.html
- It has interactive html file for test reports generated
