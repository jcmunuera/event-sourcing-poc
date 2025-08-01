# Gemini-CLI Context: Event Sourcing for Audit Logging

This document provides the context for a project focused on implementing an Event Sourcing solution for audit logging in a microservices architecture. The goal is to capture user activity within a banking application and store it for auditing and analysis purposes.

## Project Overview

We are building a system that captures user interactions with a banking application through APIs, transforms these interactions into domain events, and persists them for long-term audit logging. We are using Event Sourcing principles to ensure data integrity and enable future analysis.

## Architecture

The architecture involves the following components:

1.  **Microservices:** The core banking application is composed of microservices, including:

    *   **Experience APIs (BFFs):** These APIs are tailored to specific user interfaces (web/mobile) and orchestrate calls to domain APIs.
    *   **Domain APIs:** These APIs implement core business logic.

2.  **Event Generation:** We use Aspect-Oriented Programming (AOP) to intercept method calls in the API layer and generate raw events. These events capture information about the method executed, its parameters, and the result.

3.  **Kafka:** The raw events are sent asynchronously to a Kafka topic for reliable message queuing and buffering.

4.  **Event Processing (Axon Server):** A Kafka listener consumes the raw events and transforms them into domain events. These events are then stored in an Event Store (Axon Server). Axon Framework is used to simplify Event Sourcing and CQRS implementation.

5.  **Projections and Materialized Views:** Projections consume the domain events and create materialized views, which are optimized for specific query patterns (e.g., a "user activity" view for audit purposes).

6.  **Data Store:** Materialized views are persisted in a database (SQL or NoSQL) for long-term storage and efficient querying.

## Code Structure

We have three main projects:

1.  **financialcontract:** Contains shared data transfer objects (DTOs) used by the APIs.
2.  **financialapi:** Implements the domain APIs.
3.  **channelapi:** Implements the experience APIs (BFFs).
4.  **prospective:** (has AOP aspects) implements the AOP aspects for generating raw events.

