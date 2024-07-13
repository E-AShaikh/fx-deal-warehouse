# FX Deal Project Documentation

## Table of Contents
1. [Overview of the System](#overview-of-the-system)
2. [Getting Started](#getting-started)
3. [Detailed Components Description](#detailed-components-description)
4. [Technical Details](#technical-details)

## Overview of the System
### Project Description

This project aims to develop a Java application for managing FX deals within a data warehouse environment. It focuses on accepting, validating, and persisting FX deal details into a database.

### Project Architecture

The project follows an n-layer design, structured as follows:

- **Controller Layer**: Handles incoming requests, manages input validation, and delegates business logic to the service layer.
- **Service Layer**: Implements business logic and orchestrates interactions between controllers and repositories.
- **Repository Layer**: Manages data persistence and retrieval operations, interacts directly with the database using Spring Data and Hibernate.

The `FxDeal` entity forms the core of the application's domain model.

## Getting Started
### Installation and Setup
To set up the database cluster, use the provided docker-compose file. This will start all the necessary components, including the Bootstrapping Node and a pre-defined number of Regular Nodes.

To run the cluster I'm using [docker compose](Final/docker-compose.yml), execute:
```bash
docker-compose up
```

## Detailed Components Description

### `FxDeal` Entity

The `FxDeal` entity represents a foreign exchange deal and includes fields such as `fromCurrencyISO`, `toCurrencyISO`, `timestamp`, and `amount`. It is annotated with validation constraints using Hibernate Validator annotations (`@NotNull`, `@Pattern`, `@PastOrPresent`, `@DecimalMin`) to ensure data integrity and validity at the entity level.

### `FxDealController`

The `FxDealController` class handles incoming HTTP requests related to FX deals:

- **Endpoints**:
    - `/deal/add`: POST endpoint to add a single FX deal. Uses `@Valid` annotation to validate the incoming `FxDeal` object based on entity validations.
    - `/deal/addDeals`: POST endpoint to add multiple FX deals. Validates each `FxDeal` object in the list individually and handles exceptions like `DataIntegrityViolationException` for duplicate entries.
    - `/deal`: GET endpoint to retrieve all FX deals stored in the database.

### `FxDealService`

The `FxDealService` class contains business logic related to FX deals:

- **Methods**:
    - `getAllDeals()`: Retrieves all FX deals from the repository.
    - `saveFxDeal(FxDeal deal)`: Saves a single FX deal using the repository's `insert()` method, bypassing potential Hibernate `save()` conflicts.
    - `saveFxDeals(List<FxDeal> deals)`: Saves multiple FX deals individually, capturing exceptions to handle duplicate entries using a `Map` to store results.

### `FxDealRepository`

The `FxDealRepository` interface manages data persistence operations for FX deals using MySQL:

- **Methods**:
    - `insert(FxDeal d)`: Custom method to insert an `FxDeal` object into the `fx_deal` table, specified as a native SQL query. Ensures direct insertion without update behavior to prevent duplicate entries.
### Validation Approach

- **Entity Level**: Validation annotations (`@NotNull`, `@Pattern`, etc.) applied directly to `FxDeal` entity fields ensure that data meets specified criteria before being processed or persisted.
- **Controller Level**: `@Valid` annotation in controllers (`FxDealController`) triggers validation of incoming request bodies against entity constraints.

## Technical Details

### Logging

#### Logging Approach

Logging in the FX Deal project utilizes Aspect-Oriented Programming (AOP) with Spring AOP and SLF4J. This approach allows for consistent logging of method entry, exit, return values, and exceptions across the application.

The `LoggingAspect` intercepts methods annotated with `@ToLog` and performs the following logging operations:
- **Before**: Logs method entry along with the method arguments.
- **After**: Logs method exit.
- **After Returning**: Logs the return value of the method.
- **After Throwing**: Logs exceptions thrown by the method.

### Error Handling

#### Error Handling Approach

Error handling in the FX Deal project is centralized using `@RestControllerAdvice`. This ensures consistent and structured responses for various exceptions encountered during request processing.

The global exception handlers manage different types of exceptions and provide appropriate responses, enhancing the reliability and user experience of the application. The use of `@RestControllerAdvice` allows for a clean separation of error handling logic from the main application code, promoting maintainability and readability.