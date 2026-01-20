/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.spirals.cerberus237.adaptiflow.operators.database;

import tools.spirals.cerberus237.adaptiflow.interfaces.ConditionEvaluator;
import tools.spirals.cerberus237.metricscollectorbase.models.SQLDatabaseMetrics;

/**
 * Evaluator class for assessing SQL database metrics based on specified conditions.
 * <p>
 * The {@code HealthyDatabaseEvaluator} implements the {@link ConditionEvaluator}
 * interface to provide a mechanism for evaluating the health of a SQL database
 * based on its metrics, such as response time and network status.
 * </p>
 *
 * <p>
 * This class allows users to define conditions that determine if the database metrics
 * are acceptable or not. It can be used in monitoring and alerting systems to trigger
 * actions based on the health of the database.
 * </p>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * HealthyDatabaseEvaluator evaluator = new HealthyDatabaseEvaluator(200L, true);
 * SQLDatabaseMetrics metrics = new SQLDatabaseMetrics();
 * metrics.setResponseTime(250L);
 * metrics.setNetworkStatus(true);
 * boolean isHealthy = evaluator.test(metrics);
 * System.out.println("Database is healthy: " + isHealthy);
 * </pre>
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class HealthyDatabaseEvaluator implements ConditionEvaluator<SQLDatabaseMetrics> {
    private final Long maxResponseTime;
    private final boolean expectedNetworkStatus;

    /**
     * Constructs a {@code HealthyDatabaseEvaluator} with the specified maximum response time
     * and expected network status.
     *
     * @param maxResponseTime the maximum acceptable response time in milliseconds
     * @param expectedNetworkStatus the expected network status (true for healthy, false for unhealthy)
     */
    public HealthyDatabaseEvaluator(Long maxResponseTime, boolean expectedNetworkStatus) {
        this.maxResponseTime = maxResponseTime;
        this.expectedNetworkStatus = expectedNetworkStatus;
    }

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