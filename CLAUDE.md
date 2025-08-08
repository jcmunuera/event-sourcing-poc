jor # Gemini-CLI Context: Event Sourcing for Audit Logging

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

These are the main projects:

1.  **financialcontract:** Contains shared data transfer objects (DTOs) used by the APIs.
2.  **financialapi-2:** Implements the domain APIs.
3.  **channelapi-2:** Implements the experience APIs (BFFs).
4.  **prospective:** (has AOP aspects) implements the AOP aspects for generating raw events.
5.  **auditevent:** listen raw events from kakfa topic, transform to domain events and store them in Axon Event Store using Axon Framework
6.  **audiprojection:** projection for electronic journal. Get domain events from Axon Server and feeds electronic journal materialiez view in PostgreSQL
   
I have succesfully complete this PoC usin Axon Server as Event Store. Unfortunately, I can't use commercial version of Axon for my project and OSS version does not fit accurate HA capabilities, so I jave another Plan. I will use PostgreSQL + Citus as Event Store and Debezium as Event Streaming. So Raw events sent to Kafka by AOP will be now transformed to domain events and store in PostgreSQL as Event Store. Debezium will be monitoring the Event Store and send events to a electronic journal kafka topic. Electronic Journal projection will be listening this topic and feeding electronic journal materialized view. So new projects appeared in my mind:

7. **auditeventLC:** new raw events listener implementation. Transform raw events to domain events ans store them in the PostgreSQL Event Store
8. **auditprojectionLC:** new project comming soon. Should get domain events from electronic journal projection topic (queued by debezium) and feed electronic journal materialize view in PostgreSQL.

So auditeventLC and auditprojectionLC are alternative implementations of auditevent amd auditprojection not using Axon.

