# RideShare API

A ride-sharing backend application built with Spring Boot, MongoDB, and JWT authentication.

## 📮 Postman Collection

Test the API using our Postman collection:  
[Open in Postman](https://ujjwal-sahu-s-team.postman.co/workspace/My-Workspace~0959c800-31c2-43a2-8e4b-8962626f7151/collection/47865743-afa2c41a-d33e-43ab-92c1-de0bda6cc1ca?action=share&creator=47865743)

## Tech Stack
- Spring Boot 4.0.0
- MongoDB Atlas
- JWT Authentication
- Spring Security
- Java 17

## Authentication

All endpoints (except auth) require JWT token in header:
```
Authorization: Bearer <your-jwt-token>
```

## API Endpoints

### 🔐 Authentication (PUBLIC)

**Register**
```http
POST /api/auth/register
```
```json
{
  "username": "john",
  "password": "password123",
  "role": "ROLE_USER"
}
```
Roles: `ROLE_USER` or `ROLE_DRIVER`

**Login**
```http
POST /api/auth/login
```
```json
{
  "username": "john",
  "password": "password123"
}
```
Returns JWT token

---

### 👤 User Endpoints (ROLE_USER)

**Create Ride Request**
```http
POST /api/v1/rides
```
```json
{
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar"
}
```

**View My Rides**
```http
GET /api/v1/user/rides
```

---

### 🚗 Driver Endpoints (ROLE_DRIVER)

**View Pending Requests**
```http
GET /api/v1/driver/rides/requests
```

**Accept Ride**
```http
POST /api/v1/driver/rides/{rideId}/accept
```

---

### ✅ Complete Ride (USER or DRIVER)

```http
POST /api/v1/rides/{rideId}/complete
```

---

## Ride Status Flow

```
REQUESTED → ACCEPTED → COMPLETED
```

- **REQUESTED**: User creates ride
- **ACCEPTED**: Driver accepts ride
- **COMPLETED**: User or Driver completes ride

## Run Application

```bash
./mvnw spring-boot:run
```

Server runs on: `http://localhost:8080`
