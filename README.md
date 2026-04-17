# QuantityMeasurementApp
# Quantity Measurement App - UC21 (Microservices Architecture)

## 📌 Overview
This project is an implementation of the **Quantity Measurement Application** using **Microservices Architecture**.

In **UC21**, the monolithic application is decomposed into multiple independent services for better scalability, maintainability, and deployment flexibility.

---

## 🏗️ Architecture

The system consists of the following microservices:

### 🔹 Services
- **API Gateway** – Entry point for all client requests
- **Eureka Server** – Service discovery and registration
- **Quantity Measurement Service** – Core business logic
- **Security Service** – Authentication & authorization (JWT/OAuth2)

---

## 📊 Architecture Flow

Client → API Gateway → Microservices → Database  
                                   ↓  
                              Eureka Server  

---

## ⚙️ Technologies Used
- Java 17
- Spring Boot
- Spring Cloud
- Spring Cloud Gateway
- Netflix Eureka
- Spring Security
- JWT Authentication
- Maven
- REST APIs

---

## 📁 Project Structure


microservices/
├── api-gateway/
│ ├── config/
│ ├── filter/
│ └── ApiGatewayApplication.java
│
├── eureka-server/
│ └── EurekaServerApplication.java
│
├── quantity-measurement-app/
│ ├── controller/
│ ├── service/
│ ├── repository/
│ └── entity/
│
├── security-service/
│ ├── controller/
│ ├── service/
│ ├── repository/
│ └── config/


---

## 🔐 Features
- Centralized API Gateway routing
- Service discovery using Eureka
- Independent deployable services
- JWT-based authentication
- Secure communication between services
- Scalable and loosely coupled architecture

---
