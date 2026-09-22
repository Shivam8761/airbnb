# 🏨 airbnb — Hotel & Room Booking Backend

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-blue.svg)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-blue.svg)](https://www.postgresql.org/)
[![Stripe](https://img.shields.io/badge/Stripe-Payments-6772e5.svg)](https://stripe.com/)
[![OpenAPI](https://img.shields.io/badge/Swagger-OpenAPI%203-green.svg)](https://swagger.io/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

airbnb is an enterprise-grade RESTful backend platform for hotel and room bookings built with **Java 25**, **Spring Boot 4.0.3**, **Spring Security with JWT**, **PostgreSQL**, and **Stripe Payments**.

Engineered with clean architecture, layered separation of concerns, and robust business workflows, StaySphere supports end-to-end guest reservation pipelines, dynamic pricing computation, real-time inventory allocation, automated Stripe Checkout with webhooks, role-based hotel administration, and analytics reporting.

---

## 👨‍💻 Author & Identity

- **Project:** StaySphere — Hotel & Room Booking Backend
- **Developer:** Shivam Vishwakarma
- **GitHub:** [@shivam8761](https://github.com/shivam8761)
- **Repository:** [https://github.com/shivam8761/staysphere](https://github.com/shivam8761/staysphere)

---

## 🌟 Core Features

### 1. Authentication & Role-Based Authorization
- **Stateless JWT Security:** Access tokens and secure HTTP-only refresh tokens.
- **BCrypt Encryption:** Secure password hashing for user credentials.
- **Role Enforcement:** Granular permissions (`ROLE_GUEST`, `ROLE_HOTEL_MANAGER`).
- **Profile Management:** Authenticated endpoints to inspect and update user profile information and history.

### 2. Hotel & Room Management
- **Hotel Operations:** Create, update, activate/deactivate, and delete hotels (restricted to hotel managers).
- **Room Types & Amenities:** Configure room capacities, base pricing, photos, and configurations.
- **Ownership Scoping:** Hotel administrators can only manage and view bookings belonging to their properties.

### 3. Inventory Management & Locking
- **Daily Room Inventory:** Date-partitioned inventory tracking room availability and surges.
- **Atomic Reservation Locking:** Temporarily reserve capacity during booking checkout.
- **Inventory Confirmation & Release:** Finalize room allocation upon payment confirmation or release inventory back on booking cancellation.

### 4. Dynamic Pricing Engine (Decorator Pattern)
StaySphere computes room rates dynamically based on multiple market conditions using a Strategy/Decorator pipeline:
- **Base Pricing:** Standard room nightly rate.
- **Surge Pricing:** Adjusted by daily surge multiplier.
- **Occupancy Pricing:** Automatically increases price by 1.25x when capacity reaches or exceeds 80%.
- **Urgency Pricing:** Applies a 1.5x multiplier for check-ins within 7 days.
- **Holiday & Weekend Pricing:** Increases price by 1.25x for peak dates and weekends.

### 5. Booking Workflow
- **Multi-Step Checkout:**
  1. `Initialize Booking` — Verify dates and reserve room inventory.
  2. `Add Guests` — Attach detailed guest information to reservation.
  3. `Initiate Payment` — Generate Stripe Checkout Session.
  4. `Webhook Confirmation` — Asynchronously confirm reservation upon Stripe event.
- **Self-Service Cancellations:** Guests can cancel bookings with automatic inventory restitution and Stripe refund processing.
- **Guest History:** Access personal booking records and real-time status.

### 6. Stripe Payment & Webhook Integration
- **Stripe Checkout Sessions:** Hosted payment page generation with embedded metadata.
- **Cryptographic Webhook Verification:** Verifies `Stripe-Signature` header against webhook signing secret.
- **Event Handling:** Processes `checkout.session.completed` events to atomically confirm bookings and capture transaction IDs.
- **Automated Refunds:** Dispatches refund requests to Stripe API upon cancellation.

### 7. Hotel Analytics & Reporting
- **Revenue Metrics:** Generate date-range reports including confirmed booking counts, total revenue, and average revenue per booking.

---

## 🛠️ Tech Stack

| Component | Technology | Version / Details |
|---|---|---|
| **Language** | Java | OpenJDK 25 |
| **Framework** | Spring Boot | 4.0.3 |
| **Web & REST** | Spring MVC | RESTful APIs with Global Exception Handling |
| **Security** | Spring Security & JJWT | Stateless JWT (0.12.6) + BCrypt |
| **Persistence** | Spring Data JPA / Hibernate | ORM with PostgreSQL driver |
| **Database** | PostgreSQL / H2 | PostgreSQL 16+ (Production), H2 (In-memory testing) |
| **Payments** | Stripe Java SDK | 33.5.0-alpha.2 (Checkout Sessions & Webhooks) |
| **Mapping & Utils** | ModelMapper & Lombok | DTO conversions and boilerplate reduction |
| **Documentation** | SpringDoc OpenAPI 3 | Swagger UI 3.1.0 |
| **Build Tool** | Apache Maven | 3.9+ with Maven Wrapper |

---

## 🏛️ System Architecture

```text
                                  ┌─────────────────────────────┐
                                  │      Client Applications    │
                                  │ (Web / Mobile / Third-Party)│
                                  └──────────────┬──────────────┘
                                                 │ HTTPS / JSON
                                                 ▼
                                  ┌─────────────────────────────┐
                                  │    Spring Security Filter   │
                                  │  (JwtAuthFilter & CORS)     │
                                  └──────────────┬──────────────┘
                                                 │
                   ┌─────────────────────────────┴─────────────────────────────┐
                   ▼                                                           ▼
     ┌───────────────────────────┐                               ┌───────────────────────────┐
     │      Public & Guest       │                               │     Hotel Administration  │
     │  (Auth, Browse, Bookings) │                               │  (Hotels, Rooms, Reports) │
     └─────────────┬─────────────┘                               └─────────────┬─────────────┘
                   │                                                           │
                   └─────────────────────────────┬─────────────────────────────┘
                                                 │
                                                 ▼
                                  ┌─────────────────────────────┐
                                  │       Service Layer         │
                                  │   (Business Logic & Rules)  │
                                  └──────┬───────────────┬──────┘
                                         │               │
                     ┌───────────────────┘               └───────────────────┐
                     ▼                                                       ▼
      ┌─────────────────────────────┐                         ┌─────────────────────────────┐
      │   Dynamic Pricing Pipeline  │                         │    Stripe Payment Service   │
      │ (Base, Surge, Occupancy...) │                         │ (Checkout, Webhook, Refund) │
      └─────────────────────────────┘                         └─────────────────────────────┘
                     │                                                       │
                     └───────────────────┬───────────────────────────────────┘
                                         │
                                         ▼
                                  ┌─────────────────────────────┐
                                  │   Spring Data Repositories  │
                                  └──────────────┬──────────────┘
                                                 │
                                                 ▼
                                  ┌─────────────────────────────┐
                                  │      PostgreSQL Database    │
                                  └─────────────────────────────┘
```

---

## 📁 Project Structure

```text
staysphere/
├── pom.xml                                   # Maven project dependencies & plugins
├── README.md                                 # Comprehensive documentation
├── .env.example                              # Template for environment variables
├── src/
│   ├── main/
│   │   ├── java/com/shivam/airBnb/
│   │   │   ├── StaySphereApplication.java    # Spring Boot bootstrap application
│   │   │   ├── advice/                       # Global API response envelope & error handlers
│   │   │   │   ├── ApiError.java
│   │   │   │   ├── ApiResponse.java
│   │   │   │   ├── GlobalAPIResponse.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── config/                       # Application, CORS, Stripe, and OpenAPI configs
│   │   │   │   ├── AppConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── StripeConfig.java
│   │   │   ├── controller/                   # REST API Controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── HotelBookingController.java
│   │   │   │   ├── HotelBrowseController.java
│   │   │   │   ├── HotelController.java
│   │   │   │   ├── InventoryController.java
│   │   │   │   ├── RoomAdminController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── WebhookController.java
│   │   │   ├── dto/                          # Data Transfer Objects
│   │   │   ├── entity/                       # JPA Entities
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Guest.java
│   │   │   │   ├── Hotel.java
│   │   │   │   ├── HotelContactInfo.java
│   │   │   │   ├── HotelMinPrice.java
│   │   │   │   ├── Inventory.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Room.java
│   │   │   │   ├── User.java
│   │   │   │   └── enums/
│   │   │   ├── exceptions/                   # Domain exceptions
│   │   │   ├── repository/                   # Spring Data JPA repositories
│   │   │   ├── security/                     # JWT tokens, filter, and security rules
│   │   │   ├── service/                      # Business logic implementations
│   │   │   ├── strategy/                     # Dynamic pricing strategies
│   │   │   └── utility/                      # Helper utilities
│   │   └── resources/
│   │       └── application.properties        # Main runtime properties
│   └── test/
│       ├── java/com/shivam/airBnb/
│       │   └── StaySphereApplicationTests.java
│       └── resources/
│           └── application.properties        # In-memory test configuration
```

---

## 🔌 API Endpoints Summary

Base Context Path: `/api/v1`

### Authentication (`/auth`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/auth/signup` | Register a new user | Public |
| `POST` | `/auth/login` | Login and obtain JWT access & refresh token | Public |
| `POST` | `/auth/refresh` | Refresh access token using cookie | Public |

### User Profile (`/users`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/users/myProfile` | View authenticated user details | Authenticated |
| `PATCH` | `/users/profile` | Update profile information | Authenticated |
| `GET` | `/users/myBookings` | View user's past and current bookings | Authenticated |

### Hotel Browsing & Search (`/hotels`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/hotels/search` | Search hotels by city, date, and rooms | Public |
| `GET` | `/hotels/{hotelId}/info` | Get hotel details and room rate list | Public |

### Booking Lifecycle (`/bookings`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/bookings/init` | Initialize booking and reserve inventory | Authenticated |
| `POST` | `/bookings/{bookingId}/addGuests` | Add guest list to booking | Authenticated |
| `POST` | `/bookings/{bookingId}/payments` | Initiate Stripe checkout session | Authenticated |
| `POST` | `/bookings/{bookingId}/cancel` | Cancel booking, release rooms, trigger refund | Authenticated |
| `GET` | `/bookings/{bookingId}/status` | Check real-time booking status | Authenticated |

### Hotel Administration (`/admin/hotels`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/admin/hotels` | Create new hotel | `HOTEL_MANAGER` |
| `GET` | `/admin/hotels/{hotelId}` | Get hotel details | `HOTEL_MANAGER` |
| `PUT` | `/admin/hotels/{hotelId}` | Update hotel details | `HOTEL_MANAGER` |
| `DELETE` | `/admin/hotels/{hotelId}` | Delete hotel | `HOTEL_MANAGER` |
| `PATCH` | `/admin/hotels/{hotelId}` | Activate/Deactivate hotel | `HOTEL_MANAGER` |
| `GET` | `/admin/hotels/{hotelId}/bookings` | View hotel bookings | `HOTEL_MANAGER` |
| `GET` | `/admin/hotels/{hotelId}/reports` | Revenue and bookings report | `HOTEL_MANAGER` |

### Room Administration (`/admin/hotels/{hotelId}/rooms`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/admin/hotels/{hotelId}/rooms` | Create new room type | `HOTEL_MANAGER` |
| `GET` | `/admin/hotels/{hotelId}/rooms` | List hotel rooms | `HOTEL_MANAGER` |
| `GET` | `/admin/hotels/{hotelId}/rooms/{roomId}` | Get room details | `HOTEL_MANAGER` |
| `DELETE` | `/admin/hotels/{hotelId}/rooms/{roomId}` | Delete room type | `HOTEL_MANAGER` |

### Inventory Administration (`/admin/inventory`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/admin/inventory/rooms/{roomId}` | Fetch room inventory dates | `HOTEL_MANAGER` |
| `PATCH` | `/admin/inventory/rooms/{roomId}` | Update capacity/surge factor | `HOTEL_MANAGER` |

### Stripe Webhook (`/webhook`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/webhook` | Stripe webhook listener for payment events | Public (Stripe Signature) |

---

## 📖 Swagger & OpenAPI

Interactive API documentation and schema explorer are provided by SpringDoc OpenAPI.

- **Swagger UI:** `http://localhost:8080/api/v1/swagger-ui/index.html`
- **OpenAPI JSON Spec:** `http://localhost:8080/api/v1/v3/api-docs`

---

## ⚙️ Environment Variables

Create a `.env` file in the project root based on `.env.example`:

```properties
# PostgreSQL Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/staysphere
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT Security
# Generate a secure 256-bit or 512-bit secret
JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Frontend Integration
FRONTEND_URL=http://localhost:3000

# Stripe Payment Keys (Test or Live)
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
STRIPE_WEBHOOK_SECRET=whsec_your_stripe_webhook_signing_secret
```

---

## 🚀 Local Setup & Installation

### Prerequisites
- **JDK 25** (or compatible modern OpenJDK)
- **Apache Maven 3.9+** (or bundled `./mvnw`)
- **PostgreSQL 16+**

### Step 1: Clone Repository
```bash
git clone https://github.com/shivam8761/staysphere.git
cd staysphere
```

### Step 2: Configure Database
Open PostgreSQL terminal or pgAdmin:
```sql
CREATE DATABASE staysphere;
```

### Step 3: Run the Application
You can pass environment variables inline or via `.env`:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/staysphere"
export DB_USERNAME="postgres"
export DB_PASSWORD="your_password"
export JWT_SECRET_KEY="your_jwt_secret"
export STRIPE_SECRET_KEY="sk_test_xxx"
export STRIPE_WEBHOOK_SECRET="whsec_xxx"

./mvnw spring-boot:run
```

The application will start on port `8080` under context path `/api/v1`.

### Step 4: Run Tests
The test suite utilizes an isolated in-memory H2 database and runs with zero external dependencies:

```bash
./mvnw clean test
```

---

## 🔮 Future Improvements

- [ ] Multi-image file upload via AWS S3 or Cloudinary.
- [ ] Guest reviews, ratings, and moderation system.
- [ ] User wishlist and favorite hotels feature.
- [ ] Redis caching for hotel search queries and pricing calculation.
- [ ] Asynchronous email notifications with Spring Mail and Thymeleaf templates.
- [ ] Docker Compose setup for instant one-command local orchestration.
- [ ] Database migration versioning with Flyway or Liquibase.

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
