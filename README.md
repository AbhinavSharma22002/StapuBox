# Venue Management System

A production-ready Spring Boot application designed to manage sports venues, time slots, and bookings. This system focuses on high-concurrency handling, data integrity, and seamless deployment via Docker.

---

## 🚀 Quick Start (One-Command Setup)

The entire stack is containerized. You do not need to install MySQL or Maven locally.

1. **Prerequisites:** Install [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. **Launch:** Run the following command in the project root:
   ```bash
   docker-compose up --build

3. **Verify:** Once the logs show "Started VenueApplication," access the health check:http://localhost:8080/actuator/health

## ##

## 🛠 Technical Implementation (Rubric Highlights)

This project is built with a focus on **Production Readiness** and follows standard enterprise patterns.

1. **Concurrency & Conflict Prevention**

    To meet the requirement of preventing double bookings:

-   **Optimistic Locking**: Uses a **@Version** field in the Primary base class to prevent race conditions during slot updates.
-   **Unique Constraints**: Database-level unique indexing on **slot_id** in the **bookings** table acts as a final safety net.
-   **Transaction Management**: **@Transactional** boundaries ensure data integrity across Venue, Slot, and Booking entities.

2. **External API Integration**

    Integrates with the **StapuBox Official API** to fetch valid sport codes.

-   **Resilience**: Sport data is cached, and custom ResponseWrappers handle third-party JSON formats gracefully.
-   **Validation**: All incoming venue requests are validated against the official sport list before persistence.

3. **Security & API Design**
- **ID Masking**
    Uses an EncryptionService to encode database primary keys.
    Users interact only with encrypted identifiers (e.g., venuePk), preventing ID enumeration attacks.
- **RESTful Standards**
    Clean and intuitive URI structures:
    - **POST /api/venues/{venuePk}/slots**

    - **GET /api/venues/available**

    - **POST /api/bookings**

## ##
## ⚙️ Profile Management##

The application uses Spring Profiles to separate environments:

Profile|Target Environment|Database Host|Trigger
--- | --- | --- | ---
dev|Local IDE Development|localhost|-Dspring.profiles.active=dev
docker|Containerized Stack|db (Docker DNS)|Automatic via docker-compose

## ##

## 📋 Mandatory API Endpoints ##

**Venues**

-   **POST /api/venues**: Create/Update a venue.

-   **GET /api/venues/available**: Search available venues by sport and time range (supports optional filters).

**Slots**
-   **POST /api/venues/{venuePk}/slots**: Add time slots to a specific venue.

**Bookings**
-   **POST /api/bookings**: Book an available slot. Returns 409 Conflict if the slot is already taken.

## ##

## 📦 Docker Configuration ##

-   **Multi-Stage Build**: The Dockerfile uses a Maven build stage and a separate JRE runtime stage to minimize image size.

-   **Orchestration**: docker-compose.yml manages the network and uses a healthcheck on the MySQL container to ensure the database is ready before the application starts.

## ##

## 💡 Key Design Patterns Used ##

-   **Data Transfer Objects (DTO)**: Decouples the API layer from the Database layer.
-   **Converters**: Specialized components to transform Entities to DTOs and vice-versa.
-   **Global Exception Handling**: Centralized @ControllerAdvice for consistent error responses and HTTP status codes.

## ##

## ✅ Implementation Highlights ##
This project successfully implements all mandatory requirements:

-   **Containerization**: Ready-to-run environment using Docker & Docker Compose.

-   **Concurrency Control**: Atomic booking operations with Optimistic Locking to prevent double-booking.

-   **Data Security**: Primary Key masking via encryption for all public-facing IDs.

-   **Third-Party Integration**: Real-time validation against official Sport API data.

-   **Dynamic Filtering**: Advanced venue search with optional sport and time-range parameters.

-   **Environment Awareness**: Profile-based configuration (**dev** vs **docker**).

## ##