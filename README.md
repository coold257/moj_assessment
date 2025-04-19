# HMCTS Project

## Setup Instructions

### Backend Setup

Navigate to the backend directory and start the Spring Boot application:

```bash
cd hmcts
mvn clean install
./mvnw spring-boot:run
```

### Frontend Setup

Navigate to the frontend directory and start the development server:

```bash
cd hmcts-frontend
npm install
npm run dev
```

## System Requirements

- Java 11 or higher
- Maven 3.6+ (or use the included Maven wrapper)
- Node.js 14+ and npm

## Access the Application

- Backend API: http://localhost:8080
- Frontend: http://localhost:3000
