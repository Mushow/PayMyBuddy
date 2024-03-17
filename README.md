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

## Environment Variable Configuration

For the PayMyBuddy application to connect to the database securely, you need to set environment variables for your database credentials. These variables replace hard-coded credentials in the application's configuration files. Below is a list of the required environment variables and instructions on how to set them up for your development and testing environments.

### Required Environment Variables

You'll need to set the following environment variables:

- `SPRING_DATASOURCE_USERNAME`: The username for your main database.
- `SPRING_DATASOURCE_PASSWORD`: The password for your main database.
- `SPRING_DATASOURCE_USERNAME_TEST`: The username for your test database.
- `SPRING_DATASOURCE_PASSWORD_TEST`: The password for your test database.

These variables are used in the `application.properties` file as follows:

```properties
# Main database configuration
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

# Test database configuration
spring.datasource.username=${SPRING_DATASOURCE_USERNAME_TEST:sa} (sa as default)
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD_TEST} (empty password as default)
```

# Setting Up Environment Variables

Proper configuration of environment variables is crucial to maintaining the security of your PayMyBuddy application. Below are the instructions to set up the necessary environment variables for different development environments.

## In IntelliJ IDEA

1. Navigate to `Run` -> `Edit Configurations...` from the main menu.
2. Select your Spring Boot application configuration.
3. Click the `Modify options` drop-down and select `Environment variables`.
4. Add new variables by clicking the `+` icon and entering the name and value for each of the required variables:
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `SPRING_DATASOURCE_USERNAME_TEST`
   - `SPRING_DATASOURCE_PASSWORD_TEST`

## On Windows

Environment variables can be set up either through the System Properties or via the command line.

### System Properties:

1. Access the Environment Variables window from the System Properties.
2. Add new user or system variables with the names and values provided above.

### Command Line:

Use the `setx` command to set each variable permanently in the Command Prompt as an administrator:

```cmd
setx SPRING_DATASOURCE_USERNAME "yourUsername" /M
setx SPRING_DATASOURCE_PASSWORD "yourPassword" /M
setx SPRING_DATASOURCE_USERNAME_TEST "yourTestUsername" /M
setx SPRING_DATASOURCE_PASSWORD_TEST "yourTestPassword" /M
```

## On macOS or Linux

To set up the required environment variables on macOS or Linux, add the following `export` commands to your shell initialization file such as `.bash_profile`, `.bashrc`, `.zshrc`, or any other configuration file used by your shell.

```bash
export SPRING_DATASOURCE_USERNAME=yourUsername
export SPRING_DATASOURCE_PASSWORD=yourPassword
export SPRING_DATASOURCE_USERNAME_TEST=yourTestUsername
export SPRING_DATASOURCE_PASSWORD_TEST=yourTestPassword
```

After adding these lines to the file, run the following command in your terminal to apply the changes:

```bash
source ~/.bash_profile
```

Please make sure that the users replace `yourUsername`, `yourPassword`, `yourTestUsername`, and `yourTestPassword` with their actual credentials before saving the file and running the `source` command. This will ensure that the environment variables are set correctly in their session.

## Test Types

1. **Unit Tests**: These tests are designed to test individual components of the application in isolation.
    - Run the command `mvn test` to execute all unit tests in the application.
2. **Integration Tests**: These tests are designed to test the interaction between components and the overall behavior of the application.
    - Ensure that the application is configured to connect to an appropriate test database.
    - Run the command `mvn verify` to execute the integration tests.

## How to Launch the App Locally

To get PayMyBuddy running on your local machine, follow these steps:

1. Navigate to the root directory of the project via command line or terminal.
2. Run the command `mvn spring-boot:run` to start the application.
3. Once the application is running, open your web browser and navigate to `http://localhost:8080` to access the PayMyBuddy web interface.


## Additional Documentation

- **UML Diagrams & Project Design Documents**: For detailed architectural and design models of the PayMyBuddy application, refer to the included PDF files (`James_Antoine_1_uml_032024.pdf` and `James_Antoine_2_mpd_032024.pdf`).

- [James_Antoine_1_uml_032024.pdf](James_Antoine_1_uml_032024.pdf)

- [James_Antoine_2_mpd_032024.pdf](James_Antoine_2_mpd_032024.pdf)
