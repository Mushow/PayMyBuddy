# PayMyBuddy Application

## Description

PayMyBuddy is a convenient platform designed to make it easy to manage financial transactions between friends. Whether you need to split bills, share expenses, or simply send money to a friend, PayMyBuddy streamlines these transactions with ease and security.

## Tech Stack

- **Java 17**: The core programming language used for backend development.
- **Spring Boot (version 3.2.2)**: Provides a range of tools for web application development, including Spring MVC for web services, Spring Data JPA for database interaction, and Spring Security for authentication and authorization.
- **Maven**: Used for project management and build automation.
- **MySQL (Connector version 8.0.17)**: The database used for storing user and transaction data.
- **Thymeleaf**: Server-side Java template engine for web applications.
- **Spring Security**: Provides authentication and authorization support.
- **Bouncy Castle (version 1.77)**: A lightweight cryptography API for Java.
- **Lombok**: A Java library that automatically plugs into your editor and build tools, spicing up your java.
- **JUnit & Spring Boot Test**: For unit and integration testing.

## How to Start Unit Test & Integration Test

To run the unit and integration tests for PayMyBuddy, follow these steps:

1. **Unit Tests**: These tests are designed to test individual components of the application in isolation.
    - Run the command `mvn test` to execute all unit tests in the application.

2. **Integration Tests**: These tests are designed to test the interaction between components and the overall behavior of the application.
    - Ensure that the application is configured to connect to an appropriate test database.
    - Run the command `mvn verify` to execute the integration tests.

## How to Launch the App Locally

To get PayMyBuddy running on your local machine, follow these steps:

1. Ensure you have **Java 17**, **Maven** and **MySQL** installed on your local machine.
2. Clone the repository or download the source code to your local machine.
3. Please make sure to change the database credentials within the `application.properties` under `src\main\resources`.;w:w
4. Navigate to the root directory of the project via command line or terminal.
5. Run the command `mvn spring-boot:run` to start the application.
6. Once the application is running, open your web browser and navigate to `http://localhost:8080` to access the PayMyBuddy web interface.


## Additional Documentation

- **UML Diagrams & Project Design Documents**: For detailed architectural and design models of the PayMyBuddy application, refer to the included PDF files (`James_Antoine_1_uml_032024.pdf` and `James_Antoine_2_mpd_032024.pdf`).
