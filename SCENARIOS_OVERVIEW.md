# Adaptable TeaStore Scenarios Documentation

---

## Table of Contents
1. **[Database Unavailable](DATABASE_UNAVAILABLE.md)**  
   *The system detects database unavailability, triggers maintenance mode, and restores normal operation after recovery.*

2. **[Benign Traffic Increase](BENIGN_TRAFFIC_INCREASE.md)**  
   *Scales services or switches to low-power modes, enable cache, use external image provider to handle genuine traffic surges.*

3. **[Malicious Traffic Increase (DDoS)](MALICIOUS_TRAFFIC_INCREASE.md)**  
     *Deploys circuit breakers, restrictive authentication, and cached dependencies to mitigate attacks.*  

---

## Scenario Files
| **File**                              | **Description**                                                                 |
|---------------------------------------|---------------------------------------------------------------------------------|
| [DATABASE_UNAVAILABLE.md](DATABASE_UNAVAILABLE.md) | Details adaptation to database downtime and recovery.                          |
| [BENIGN_TRAFFIC_INCREASE.md](BENIGN_TRAFFIC_INCREASE.md) | Describes scaling strategies for organic traffic growth.                       |
| [MALICIOUS_TRAFFIC_INCREASE.md](MALICIOUS_TRAFFIC_INCREASE.md) | Outlines DDoS mitigation tactics.                                              |

---

Each scenario includes:  
- **Overview** of the problem and adaptation goals. 
- **Flow diagrams** (where applicable) to visualize the adaptation process.
- **Microservices involved** and their roles.  
- **Metrics collectors**, **evaluators**, and **adaptation actions** used.  
- **Code snippets** for critical components.
