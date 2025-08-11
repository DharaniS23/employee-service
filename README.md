# Employee Service - Spring Boot CRUD API

A Spring Boot REST API for managing employees with full CRUD (Create, Read, Update, Delete) operations.  
This project is built as a demo for clean coding, testing, logging, and API documentation.

---

## 🚀 Features
- **CRUD Operations** for Employee entity
- **JUnit & Mockito** unit tests for service and controller layers
- **SLF4J Logging** for tracking application flow and debugging
- **Swagger/OpenAPI** integration for interactive API documentation
- **Spring Data JPA** for database operations
---

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Web**
- **H2 / MySQL**
- **JUnit 5 & Mockito**
- **SLF4J Logging**
- **Swagger UI**
- **Maven**

---

## 📦 Getting Started

### Prerequisites
Make sure you have installed:
- Java 17+
- Maven 3.8+
- Git

### Installation
1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/employee-service.git
   ```
2. **Navigate to the project directory**
   ```bash
   cd employee-service
   ```
3. **Build the project**
   ```bash
   mvn clean install
   ```
4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

---

## 📚 API Endpoints

| Method | Endpoint         | Description             |
|--------|-----------------|-------------------------|
| GET    | `/employees`    | Get all employees       |
| GET    | `/employees/{id}` | Get employee by ID    |
| POST   | `/employees`    | Create a new employee   |
| PUT    | `/employees/{id}` | Update employee       |
| DELETE | `/employees/{id}` | Delete employee       |

---

## 🧪 Testing
- Run tests with:
  ```bash
  mvn test
  ```
- Unit tests cover service and controller layers using **JUnit 5** and **Mockito**.

---

## 📝 Logging
- Uses **SLF4J** for structured logging.
- Logs important events like API requests, errors, and database operations.

---

## 📖 API Documentation
Swagger UI is available after starting the application:
```
http://localhost:8080/swagger-ui/index.html
```
