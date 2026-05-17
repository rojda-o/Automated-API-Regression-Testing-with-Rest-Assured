# Automated API Regression Testing with Rest Assured

This repository contains an automated API regression testing project built using **Java**, **Maven**, and **JUnit 5**. The project is designed to demonstrate modern software test engineering practices by validating the stability and performance of backend services against the reliable **JSONPlaceholder** REST API.

---

## Features & Test Coverage

The automation suite covers critical regression testing paths with strict validation criteria (Assertions):
- **HTTP Status Code Verification** (e.g., `200 OK`, `201 Created`)
- **Response Body Content Validation** (matching exact JSON fields using Hamcrest matchers)
- **Performance / SLA Control** (ensuring all responses return under a specific threshold, i.e., 3000ms)

### Automated Scenarios:
1. **GET Request (`/users/1`)**: Fetches a single user's profile and validates metadata like `id`, `username`, and `email`.
2. **POST Request (`/posts`)**: Submits a structured **JSON Request Body (Payload)** to simulate new resource creation and verifies the dynamically generated response.

---

## Tech Stack & Dependencies

- **Language:** Java 21 (OpenJDK)
- **Build Tool:** Maven
- **Test Framework:** JUnit 5 (Jupiter Engine)
- **API Testing Library:** Rest Assured (v5.5.0)
- **Assertion Library:** Hamcrest

---

## Project Structure

```text
├── src
│   └── test
│       └── java
│           └── com
│               └── proje
│                   └── ApiRegressionTests.java  # Main Test Automation Script
├── target
│   └── rest-assured-logs
│       └── api-regression-test-log.txt          # Dynamically Generated Test Execution Logs
└── pom.xml                                       # Project Object Model (Dependencies)
