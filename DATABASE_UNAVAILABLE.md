# Real-World Adaptation Scenario: Database Unavailable

## Table of Contents
- [Scenario Overview](#scenario-overview)
- [Microservices Involved](#microservices-involved)
- [Metrics, Evaluators, and Adaptation Actions](#metrics-evaluators-and-adaptation-actions)
- [Code Implementation](#code-implementation)
- [Important Note on Database Restoration](#important-note-on-database-restoration)
---

## Scenario Overview
**Scenario**: The system is deployed in a **barebone configuration** with local services (Auth, Recommender, Image, Persistence, WebUI). The **Persistence service** detects timeouts from the local database due to an unexpected interruption. It triggers adaptation actions across dependent services (Auth, Recommender, Image, WebUI) to gracefully degrade functionality. The WebUI displays a **maintenance message**, and the system administrator is alerted to restart the database. Once restored, services resume normal operation.

![Database Unavailable Flow Diagram](documentation/Database_Unavailable_Flow.png)

---

## Microservices Involved
| **Service**       | **Role**                                                                | **Observed Data**                                                 |
|--------------------|-------------------------------------------------------------------------|-------------------------------------------------------------------|
| **Persistence**    | Manages database connections and queries.                               | Database connexion status, response time, active connections.     |
| **WebUI**          | Renders the user interface and handles client requests.                | No Observations                                                   |
| **Auth**           | Handles user authentication and session management.                    | No Observations |
| **Recommender**    | Generates product recommendations.                                     | No Observations |

---

## Metrics, Evaluators, and Adaptation Actions

### **1. Persistence Service**
#### **Metrics Collectors**
| **Collector**                          | **Description**                                                                                                  |
|----------------------------------------|------------------------------------------------------------------------------------------------------------------|
| `LocalDatabaseMetricsCollector`        | Return an aggregated data with database **response time**, **network status**, **active connexions** and **pending queries** |

#### **Condition Evaluator**

| **Evaluator**                    | **Description**                                                                                                                                                                                                                                                           |
|----------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `UnHeathyDatabaseEvaluator` | evaluate some conditions to check if the database service is not available or unhealthy `metric.getResponseTime() > this.maxResponseTime  \|\| !this.expectedNetworkStatus` if this condition is evaluated as True then we need to adapt                                  |
| `HeathyDatabaseEvaluator` | evaluate some conditions to check if the database service is available and healthy `metric.getResponseTime() <= this.maxResponseTime  && this.expectedNetworkStatus` if this condition is evaluated as True then we need to restore the whole application normal behavior |

UnHealthyDatabaseEvaluator
```java
public class UnHealthyDatabaseEvaluator implements ConditionEvaluator<SQLDatabaseMetrics> {
   private final Long maxResponseTime;
   private final boolean expectedNetworkStatus;
   ...
    /**
     * Tests whether the database metrics indicate an unhealthy state.
     *
     * @param metric the SQL database metrics to evaluate
     * @return {@code true} if the response time exceeds the threshold or network status differs from expected,
     *         indicating an unhealthy state; {@code false} otherwise
     */
    @Override
    public boolean test(SQLDatabaseMetrics metric) {
        return metric.getResponseTime() > this.maxResponseTime
                || metric.getNetworkStatus() != this.expectedNetworkStatus;
    }
}
```
HealthyDatabaseEvaluator
```java
public class HealthyDatabaseEvaluator implements ConditionEvaluator<SQLDatabaseMetrics> {
   private final Long maxResponseTime;
   private final boolean expectedNetworkStatus;
   ...
    /**
     * Evaluates the given SQL database metrics based on the defined conditions.
     *
     * @param metric the SQL database metrics to evaluate
     * @return {@code true} if the metrics indicate a healthy state (response time does not exceed
     *         the maximum and network status matches the expected value); {@code false} otherwise
     */
    @Override
    public boolean test(SQLDatabaseMetrics metric) {
        return metric.getResponseTime() <= this.maxResponseTime
                && metric.getNetworkStatus() == this.expectedNetworkStatus;
    }
}
```

#### **Adaptation Actions**
| **Action**                          | **Description**                                                                                                                                                              |
|-------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `DatabaseUnavailableEventBroadcast` | Broadcasts a "database unavailable" event to all dependent services via **REST**. This will be made with a message service like **RabbitMQ** or **Kafka** in future versions |
| `DisableCache`                      | Disables caching to reduce dependency on the failing database.                                                                                                               |
| `DatabaseAvailableEventBroadcast`   | Broadcasts a "database available" event to all dependent services via **REST**. This will be made with a message service like **RabbitMQ** or **Kafka** in future versions   |
| `EnableCache`                       | Enable caching to restore normal behavior.                                                                                                                                   |

---

### **2. WebUI Service**
#### **Metrics Collectors**
> No observation in this service.

#### **Condition Evaluator**
> No evaluation needed

#### **Adaptation Actions**
| **Action**               | **Description**                                               |
|--------------------------|---------------------------------------------------------------|
| `EnableMaintenanceMode`  | Displays a maintenance page until the database service is up. |
| `DisableMaintenanceMode` | Restores normal UI functionality after database recovery.     |

---

### **4. Recommender Service**
#### **Metrics Collectors**
> No observation in this service and no evaluation needed
#### **Adaptation Actions**
| **Action**     | **Description**                                             |
|----------------|-------------------------------------------------------------|
| `LowPowerMode` | Disable the recommendations to avoid querying the database. |
| `NormalMode`   | Enable the recommendations with the default algorithm.      |

---

## Code Implementation

### Adaptation Scenario Setup In The Persistence Service
```java
public void listenToEventAndTriggerAdaptationScenario() {
    // 1. Create action lists for different database states
    List<IAdaptationAction> databaseAvailableActionList = List.of(
        new DatabaseAvailableEventBroadcast(), 
        new EnableCache()
    );
    List<IAdaptationAction> databaseUnavailableActionList = List.of(
        new DatabaseUnavailableEventBroadcast(), 
        new DisableCache()
    );

    // 2. Create subscribers for each event type
    List<Observer<SQLDatabaseMetrics>> databaseAvailableEventSubscriberList = 
        List.of(new EventSubscriber<>(databaseAvailableActionList));
    List<Observer<SQLDatabaseMetrics>> databaseUnavailableEventSubscriberList = 
        List.of(new EventSubscriber<>(databaseUnavailableActionList));

    // 3. Initialize metrics collector with database connection parameters
    IMetricsCollector<SQLDatabaseMetrics> collector = LocalDatabaseMetricsCollector.getInstance(
        "jdbc:mysql://db:3306/teadb?useSSL=false", 
        "teauser", 
        "teapassword"
    );

    // 4. Create database available/unavailable conditional events with evaluators
    ConditionalEvent<SQLDatabaseMetrics> databaseAvailableEvent = 
        new ConditionalEvent<>(collector, new HealthyDatabaseEvaluator(5000L, true));
    ConditionalEvent<SQLDatabaseMetrics> databaseUnavailableEvent = 
        new ConditionalEvent<>(collector, new UnHealthyDatabaseEvaluator(5000L, true));

    // 5. Subscribe observers to events
    databaseAvailableEvent.subscribeAll(databaseAvailableEventSubscriberList);
    databaseUnavailableEvent.subscribeAll(databaseUnavailableEventSubscriberList);

    // 6. Create and start continuous event observation
    var eventSubscription = new ContinuousObservationScheduler(
        List.of(databaseAvailableEvent, databaseUnavailableEvent), 
        EVENT_LISTENING_INTERVAL_MS
    );
    eventSubscription.start();
}
```

### Adaptation Action Implementations
```java
// Broadcasts database availability to dependent services
public class DatabaseAvailableEventBroadcast implements IAdaptationAction {
    private final String targetURI = "adapt";
    private final List<String> webUIAdaptationActionList = List.of("DisableMaintenanceMode");
    private final List<String> recommenderAdaptationActionList = List.of("NormalMode");

    @Override
    public void perform() {
        // Notify WebUI to disable maintenance mode
        ServiceLoadBalancer.multicastRESTOperation(Service.WEBUI,
            targetURI, String.class, client -> client.getEndpointTarget().path("")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(webUIAdaptationActionList, MediaType.APPLICATION_JSON))
                .readEntity(String.class));

        // Notify Recommender to resume normal operations
        ServiceLoadBalancer.multicastRESTOperation(Service.RECOMMENDER,
            targetURI, String.class, client -> client.getEndpointTarget().path("")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(recommenderAdaptationActionList, MediaType.APPLICATION_JSON))
                .readEntity(String.class));
    }
}

// Broadcasts database unavailability to dependent services
public class DatabaseUnavailableEventBroadcast implements IAdaptationAction {
    private final String targetURI = "adapt";
    private final List<String> webUIAdaptationActionList = List.of("EnableMaintenanceMode");
    private final List<String> recommenderAdaptationActionList = List.of("LowPowerMode");

    @Override
    public void perform() {
        // Notify WebUI to enable maintenance mode
        ServiceLoadBalancer.multicastRESTOperation(Service.WEBUI,
            targetURI, String.class, client -> client.getEndpointTarget().path("")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(webUIAdaptationActionList, MediaType.APPLICATION_JSON))
                .readEntity(String.class));

        // Notify Recommender to enter low power mode
        ServiceLoadBalancer.multicastRESTOperation(Service.RECOMMENDER,
            targetURI, String.class, client -> client.getEndpointTarget().path("")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(recommenderAdaptationActionList, MediaType.APPLICATION_JSON))
                .readEntity(String.class));
    }
}
```

## Important Note on Database Restoration

**Database Restart Procedure**:  
While the system supports automatic adaptation to database availability changes, the actual database restart process currently requires manual intervention for demonstration purposes:

```bash
# For Docker environments:
docker-compose pause [database_service_name]  # Simulate database failure
docker-compose unpause [database_service_name]  # Restore database

# For Kubernetes environments:
kubectl scale deployment [database_service_name] --replicas=0  # Simulate failure
kubectl scale deployment [database_service_name] --replicas=1  # Restore service
```

**Note on Automation**:  
> A production-grade implementation would include:
> 1. Automated database recovery mechanisms
> 2. Message queue integration (RabbitMQ/Kafka) for reliable notifications

For demonstration purposes, manual database control allows clearer observation of the adaptation mechanisms in action. The current implementation focuses on the adaptation logic rather than infrastructure management.
