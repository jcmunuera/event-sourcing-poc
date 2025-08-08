# Event Sourcing PoC for Banking Application Audit Logging

This repository demonstrates a comprehensive **Event Sourcing** implementation for audit logging in a banking microservices architecture. The project showcases two different approaches to Event Sourcing: one using **Axon Server** and another using **PostgreSQL + Debezium** for change data capture.

## 🏗️ Architecture Overview

The system captures user interactions with banking APIs, transforms them into domain events, and persists them for long-term audit logging and analysis. The architecture implements Event Sourcing principles to ensure data integrity and enable future analysis.

### Key Components Flow

```
Banking APIs (BFF + Domain) 
    ↓ (AOP Interceptors)
Raw Events → Kafka Topic
    ↓ (Event Processors)
Domain Events → Event Store
    ↓ (Change Data Capture / Event Streaming)
Electronic Journal Topic
    ↓ (Projections)
Materialized Views (PostgreSQL)
```

## 📁 Project Structure

### Core Banking Applications

- **🌐 channelapi-2**: Mock BFF (Backend for Frontend) implementation for web/mobile banking channels
- **💰 financialapi-2**: Mock domain API implementation for financial services (login, account inquiry, movements, transfers)
- **📋 financialcontract**: Shared library containing DTOs for financial APIs
- **🔍 common-context**: Library with HTTP interceptor implementation for context creation/propagation
- **🎯 prospective**: Library containing AOP aspects for generating raw events from API endpoint executions

### Event Sourcing - Axon Server Approach

- **📨 auditevent**: Raw event aggregator/listener that transforms raw events to domain events and stores them in Axon Server Event Store
- **📊 auditprojection**: Electronic journal projection that consumes domain event streaming from Axon Server

### Event Sourcing - PostgreSQL + Debezium Approach

- **📨 auditeventLC**: Alternative to `auditevent` using PostgreSQL as Event Store instead of Axon Server
- **📊 auditprojectionLC**: Alternative to `auditprojection` using Debezium/Kafka for event streaming
- **🔌 debezium-connector-postgres**: Debezium PostgreSQL connector project with required libraries for custom Docker image

### Infrastructure & Configuration

- **⚙️ helm/kafka**: Kafka configuration descriptors (values.yaml)
- **🧪 dummy-app**: Dummy application for validating common-context functionality
- **🐳 Kubernetes Manifests**: Complete deployment configurations for all components
- **📋 postgres-connector-config.json**: Debezium connector configuration
- **🎨 event_sourcing_presentation.html**: Interactive HTML presentation of the architecture

## 🚀 Getting Started

### Prerequisites

- Kubernetes cluster
- Docker
- Strimzi Kafka Operator
- PostgreSQL
- Maven 3.8+
- Java 17+

### Deployment

1. **Install Strimzi Kafka Operator**:
   ```bash
   kubectl apply -f strimzi-install.yaml
   ```

2. **Deploy Kafka Cluster**:
   ```bash
   kubectl apply -f my-kafka-controller.yaml
   kubectl apply -f my-kafka-broker.yaml
   ```

3. **Deploy PostgreSQL**:
   ```bash
   # Deploy your PostgreSQL instance for audit database
   # Ensure audituser/auditpassword credentials and auditdb database exist
   ```

4. **Deploy Debezium Connect**:
   ```bash
   kubectl apply -f kafka-connect-jaas-config.yaml
   kubectl apply -f kafka-sasl-credentials.yaml
   kubectl apply -f debezium-connect-deployment.yaml
   kubectl apply -f debezium-connect-service.yaml
   ```

5. **Configure PostgreSQL Connector**:
   ```bash
   kubectl exec deployment/debezium-connect -- curl -X POST \
     -H "Content-Type: application/json" \
     --data @postgres-connector-config.json \
     http://localhost:8083/connectors
   ```

6. **Deploy Event Processing Components**:
   ```bash
   kubectl apply -f auditeventLC-deployment.yaml
   kubectl apply -f auditprojectionLC-deployment.yaml
   ```

7. **Deploy Banking APIs**:
   ```bash
   # Build and deploy financialapi-2 and channelapi-2
   # Ensure Kafka configurations match your cluster
   ```

## 🔄 Event Flow

### 1. Raw Event Generation
- AOP aspects in `prospective` library intercept API method calls
- Raw events contain method information, parameters, and results
- Events are sent asynchronously to Kafka topic `raw-audit-events`

### 2. Event Processing (Two Approaches)

#### Axon Server Approach
- `auditevent` consumes raw events from Kafka
- Transforms them into domain events using Axon Framework
- Stores in Axon Server Event Store
- `auditprojection` consumes from Axon Server and creates materialized views

#### PostgreSQL + Debezium Approach  
- `auditeventLC` consumes raw events from Kafka
- Transforms them into domain events
- Stores directly in PostgreSQL Event Store (`domain_events` table)
- Debezium monitors the table and streams changes to `electronic-journal-projection` topic
- `auditprojectionLC` consumes from this topic and creates materialized views

### 3. Domain Events Examples
- **UserLoggedInEvent**: User authentication events
- **ToBeReviewedEvent**: Operations requiring review
- Additional domain events based on business rules

## 🔧 Configuration

### Kafka Authentication
The system uses SASL/SCRAM-SHA-256 authentication:
- Username: `user1`  
- Password: Configured in secrets

### Database Configuration
- **Event Store DB**: `auditdb` 
- **Schema**: `auditevent`
- **Main Table**: `domain_events`
- **User**: `audituser`

### Debezium Connector
- **Source**: `auditevent.domain_events` table
- **Target Topic**: `electronic-journal-projection`
- **Transform**: Regex router transforms topic names

## 📊 Monitoring

Check connector status:
```bash
# List connectors
kubectl exec deployment/debezium-connect -- curl -s http://localhost:8083/connectors

# Check connector status
kubectl exec deployment/debezium-connect -- curl -s http://localhost:8083/connectors/auditevent-postgres-connector/status | jq .

# View connector configuration  
kubectl exec deployment/debezium-connect -- curl -s http://localhost:8083/connectors/auditevent-postgres-connector/config | jq .
```

## 🏛️ Why Two Approaches?

This PoC demonstrates both approaches to provide flexibility:

1. **Axon Server Approach**: 
   - ✅ Purpose-built for Event Sourcing
   - ✅ Rich feature set for CQRS/ES
   - ❌ Commercial license required for HA
   - ❌ Additional infrastructure component

2. **PostgreSQL + Debezium Approach**:
   - ✅ Uses existing PostgreSQL infrastructure  
   - ✅ Open source and highly available
   - ✅ Mature CDC solution
   - ❌ More complex setup
   - ❌ Not purpose-built for Event Sourcing

## 📈 Benefits

- **Audit Trail**: Complete history of all banking operations
- **Compliance**: Meets regulatory requirements for financial audit logging
- **Analytics**: Rich data source for business intelligence
- **Debugging**: Full event history for troubleshooting
- **Scalability**: Distributed architecture handles high throughput
- **Flexibility**: Two implementation approaches for different requirements

## 🧪 Testing

The repository includes comprehensive deployment configurations for testing both approaches side by side. Use the interactive HTML presentation (`event_sourcing_presentation.html`) to understand the complete architecture flow.

## 📚 Additional Resources

- **CLAUDE.md**: Detailed project context and instructions
- **Interactive Presentation**: Open `event_sourcing_presentation.html` in a browser
- **Kafka Values**: `helm/kafka/values.yaml` for Helm-based Kafka deployment

## 🤝 Contributing

This is a Proof of Concept demonstrating Event Sourcing patterns for banking audit logging. The implementation showcases production-ready patterns that can be adapted for real-world financial applications.

---

**Note**: This is a demonstration project. For production use, ensure proper security measures, secret management, and compliance with financial regulations are implemented.