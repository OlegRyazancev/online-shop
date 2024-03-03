# Online Shop Microservices Project

Welcome to the Online Shop Microservices pet-project! This project is a collection of microservices built using **Spring Boot(v 3.1.4)** for an online shopping platform.

## Table of Contents
1. [Overview](#overview)
2. [Project Schema](#project-schema)
3. [Sequence Diagram with caching](#sequence-diagram-with-caching)
4. [Sequence Diagram without caching](#sequence-diagram-without-caching)
5. [Microservices](#microservices)
6. [Technologies Used](#technologies-used)
7. [Environments](#environments)
8. [Setup](#setup)

## Overview

The Online Shop Microservices Project is designed to provide a scalable and modular architecture for an online shopping platform. Each microservice focuses on specific business functionalities to ensure maintainability, scalability, and flexibility of the system.

## Project Schema

![Online-shop schema](/docs/online_shop_schema.png)

## Sequence diagram with caching

![Online-shop schema](/docs/sequence_with_cache_diagram.png)

## Sequence diagram without caching

![Online-shop schema](/docs/sequence_without_cache_diagram.png)


## Microservices

### 🔀 Admin Microservice

The Admin microservice provides administrative functionalities.

#### Database Diagram

![Admin Microservice Database Diagram](/docs/admin_db.png)

### 🔀 Auth Microservice

The Auth microservice handles user authentication and authorization.

#### Database Diagram

![Auth Microservice Database Diagram](/docs/auth_db.png)

### 🔀 Customer Microservice

The Customer microservice handles customer-related functionalities.

#### Database Diagram

![Customer Microservice Database Diagram](/docs/customer_db.png)

### 🔀 Organization Microservice

The Organization microservice handles organization-related functionalities.

#### Database Diagram

![Organization Microservice Database Diagram](/docs/organization_db.png)

### 🔀 Product Microservice

The Product microservice manages product-related operations.

#### Database Diagram

![Product Microservice Database Diagram](/docs/product_db.png)

---

### 🔧 Purchase Microservice

The Purchase is technical microservice for purchase-related functionalities.

#### Database Diagram

![Purchase Microservice Database Diagram](/docs/purchase_db.png)

### 🔧 Review Microservice

The Review is a technical microservice for handling product reviews submitted by customers.

#### Database Diagram

![Review Microservice Database Diagram](/docs/review_db.png)

### 🔧 Logo Microservice

The Logo microservice is a technical service for uploading organization's logos.

### 🔧 Mail Microservice

The Mail microservice is a technical service for sending mails to customers.

## Technologies Used

**Spring Boot**, **Spring Security**, **JWT (JSON Web Tokens)**, **Docker**, **PostgreSQL**, **MongoDB**, **MariaDB**, **Minio**, **Redis**, **Kafka**, **Feign**, **Grafana**, **Prometheus**, **Eureka**, **Resilience4j**, **Zipkin**

## Environments

**PostgreSQL related**

- `POSTGRES_USER` - Username for accessing PostgreSQL.
- `POSTGRES_PASSWORD` - Password for accessing PostgreSQL.

- `POSTGRES_CUSTOMER_DATABASE` - Name of the database for customer-related data in PostgreSQL.
- `POSTGRES_CUSTOMER_SCHEMA` - Schema name for customer-related data in PostgreSQL.

- `POSTGRES_ORGANIZATION_SCHEMA` - Schema name for organization-related data in PostgreSQL.
- `POSTGRES_ORGANIZATION_DATABASE` - Name of the database for organization-related data in PostgreSQL.

- `POSTGRES_ADMIN_DATABASE` - Name of the database for admin-related data in PostgreSQL.
- `POSTGRES_ADMIN_SCHEMA` - Schema name for admin-related data in PostgreSQL.

- `PGADMIN_DEFAULT_EMAIL` - Default email address for PGAdmin.
- `PGADMIN_DEFAULT_PASSWORD` - Default password for PGAdmin.

---
**Mongo related**

- `MONGO_INITDB_ROOT_USERNAME` - Username for MongoDB root user.
- `MONGO_INITDB_ROOT_PASSWORD` - Password for MongoDB root user.
- `MONGO_INITDB_SERVER` - Server address for MongoDB.
- `MONGOEXPRESS_LOGIN` - Login username for MongoDB Express.
- `MONGOEXPRESS_PASSWORD` - Password for MongoDB Express.

- `MONGO_PURCHASE_DATABASE` - Name of the database for purchase-related data in MongoDB.
- `MONGO_REVIEW_DATABASE` - Name of the database for review-related data in MongoDB.

---
**MariaDB related**

- `MYSQL_ROOT_PASSWORD` - Root password for MySQL.
- `MYSQL_USER` - Username for accessing MySQL.
- `MYSQL_PASSWORD` - Password for accessing MySQL.
- `MYSQL_PMA_HOST` - Host address for MySQL PMA.

- `MYSQL_PRODUCT_DATABASE` - Name of the database for product-related data in MySQL.
- `MYSQL_AUTH_DATABASE` - Name of the database for auth-related data in MySQL.

---
**Ports**

- `ADMIN_PORT` - Port number for the admin microservice.
- `AUTH_PORT` - Port number for the auth microservice.
- `CUSTOMER_PORT` - Port number for the customer microservice.
- `LOGO_PORT` - Port number for the logo microservice.
- `MAIL_PORT` - Port number for the mail microservice.
- `ORGANIZATION_PORT` - Port number for the organization microservice.
- `PRODUCT_PORT` - Port number for the product microservice.
- `PURCHASE_PORT` - Port number for the purchase microservice.
- `REVIEW_PORT` - Port number for the review microservice.

---
**Minio related**

- `MINIO_BUCKET` - Name of the bucket in Minio.
- `MINIO_ACCESS_KEY` - Access key for Minio.
- `MINIO_SECRET_KEY` - Secret key for Minio.

- `JWT_SECRET` - Secret key for JWT.
- `JWT_ACCESS` - Expiration time for JWT access token.
- `JWT_REFRESH` - Expiration time for JWT refresh token.
- `SECURITY_CONFIRMATION_LINK_PREFIX` - Prefix for security confirmation link.
- `SECURITY_CONFIRMATION_LINK_PREFIX_DOCKER` - Prefix for security confirmation link in Docker.

- `MAIL_HOST` - Host address for email server.
- `MAIL_HOST_PORT` - Port number for email server.
- `MAIL_USERNAME` - Username for email account.
- `MAIL_PASSWORD` - Password for email account.

---
**Kafka related**

_topics_

- `KAFKA_REQUESTS_ADMIN_TOPIC` - Kafka topic for admin requests.
- `KAFKA_MAIL_TOPIC` - Kafka topic for mail events.
- `KAFKA_TOGGLE_LOCK_USER_TOPIC` - Kafka topic for toggling user lock events.
- `KAFKA_REGISTER_PRODUCT_TOPIC` - Kafka topic for product registration events.
- `KAFKA_REGISTER_ORGANIZATION_TOPIC` - Kafka topic for organization registration events.
- `KAFKA_UPDATE_CUSTOMER_TOPIC` - Kafka topic for updating customer events.
- `KAFKA_UPDATE_PRODUCT_TOPIC` - Kafka topic for updating product events.
- `KAFKA_UPDATE_USER_TOPIC` - Kafka topic for updating user events.
- `KAFKA_CHANGE_STATUS_PRODUCT_TOPIC` - Kafka topic for changing product status events.
- `KAFKA_CHANGE_STATUS_ORGANIZATION_TOPIC` - Kafka topic for changing organization status events.
- `KAFKA_DELETE_REVIEW_TOPIC` - Kafka topic for deleting review events.
- `KAFKA_DELETE_PRODUCT_TOPIC` - Kafka topic for deleting product events.
- `KAFKA_DELETE_USER_TOPIC` - Kafka topic for deleting user events.

_consumer groups_

- `KAFKA_ADMIN_GROUP` - Kafka consumer group for admin.
- `KAFKA_CUSTOMER_GROUP` - Kafka consumer group for customer.
- `KAFKA_PRODUCT_GROUP` - Kafka consumer group for product.
- `KAFKA_ORGANIZATION_GROUP` - Kafka consumer group for organization.
- `KAFKA_MAIL_GROUP` - Kafka consumer group for mail.
- `KAFKA_REVIEW_GROUP` - Kafka consumer group for review.
- `KAFKA_AUTH_GROUP` - Kafka consumer group for auth.
- `KAFKA_PURCHASE_GROUP` - Kafka consumer group for purchase.

---
**Redis related**

- `REDIS_ENCODED_PASSWORD` - Encoded password for Redis (for redisinsight).
- `REDIS_PASSWORD` - Password for Redis.

---
**Grafana related**

- `GF_SECURITY_ADMIN_USER` - Grafana admin username.
- `GF_SECURITY_ADMIN_PASSWORD` - Grafana admin password.


You can use example `.env.example` file with some predefined environments.

## Setup

You can find how to run this project locally or by Docker [here](https://youtu.be/_CqLDGYR5pw?si=GA1l6ZJYHuMHnOlT)


