
# Simple Banking System

This project is a simple banking system API developed in Java using Spring Boot. It provides essential functionalities like account creation, crediting and debiting accounts, bill payment processing, and account balance inquiry.

## Features
- Account creation
- Account crediting and debiting
- Bill payment processing
- Account balance and transaction history inquiry

## Technologies
- Java 17
- Spring Boot (v3.3.5)
- Spring Data JPA (ORM layer)
- PostgreSQL (Database integration)
- JUnit (Testing framework for unit and integration tests)

## Prerequisites
1. Java 17+ installed
2. Maven for project build and dependency management

## Setup and Installation

1. Clone the Repository
   `git clone https://github.com/mustafaozdemiir/simple-banking-project.git`
   `cd simple-banking-project`

2. Configure Database (Optional)  
   This project is preconfigured for PostgreSQL. To use another database, update the `application.properties` file with the appropriate configuration.

3. Build the Project
   `mvn clean install`

4. Run the Application
   `mvn spring-boot:run`
   The application will start at `http://localhost:7070`.

## API Endpoints

### 1. Create an Account
   **URL**: `POST /account/v1`
   **Request Body**:
     `{ "owner": "John Doe", "accountNumber": "123456" }`
   **Response**: Created account object

### 2. Credit an Account
   **URL**: `POST /account/v1/credit/{accountNumber}`
   **Request Body**:
     `{ "amount": 100.0 }`
   **Response**:
     `{ "status": "OK", "approvalCode": "UUID-string" }`

### 3. Debit an Account
   **URL**: `POST /account/v1/debit/{accountNumber}`
   **Request Body**:
     `{ "amount": 50.0 }`
   **Response**:
     `{ "status": "OK", "approvalCode": "UUID-string" }`

### 4. Bill Payment
   **URL**: `POST /account/v1/bill-payment/{accountNumber}`
   **Request Body**:
     `{ "payee": "Electric Company", "amount": 30.0 }`
   **Response**:
     `{ "status": "OK", "approvalCode": "UUID-string" }`

### 5. Get Account Details
   **URL**: `GET /account/v1/{accountNumber}`
   **Response**: Account details, including balance and transaction history.


## Running Tests
Execute unit tests using Maven:
`mvn test`

## Swagger
`http://localhost:7070/swagger-ui/index.html`
