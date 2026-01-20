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
 * Evaluator class for detecting unhealthy SQL database states based on specified conditions.
 * <p>
 * The {@code UnHealthyDatabaseEvaluator} implements the {@link ConditionEvaluator}
 * interface to evaluate when a SQL database is in an unhealthy state by checking
 * metrics such as response time and network status.
 * </p>
 *
 * <p>
 * This class is typically used to trigger alerts or adaptation actions when
 * database performance degrades beyond acceptable thresholds.
 * </p>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * UnHealthyDatabaseEvaluator evaluator = new UnHealthyDatabaseEvaluator(200L, true);
 * SQLDatabaseMetrics metrics = new SQLDatabaseMetrics();
 * metrics.setResponseTime(250L);  // Exceeds threshold
 * metrics.setNetworkStatus(true);
 * boolean isUnhealthy = evaluator.test(metrics);
 * System.out.println("Database is unhealthy: " + isUnhealthy);  // Will print "true"
 * </pre>
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class UnHealthyDatabaseEvaluator implements ConditionEvaluator<SQLDatabaseMetrics> {
    private final Long maxResponseTime;
    private final boolean expectedNetworkStatus;

    /**
     * Constructs an {@code UnHealthyDatabaseEvaluator} with the specified thresholds.
     *
     * @param maxResponseTime the maximum acceptable response time in milliseconds before considering unhealthy
     * @param expectedNetworkStatus the required network status (true = healthy, false = unhealthy)
     */
    public UnHealthyDatabaseEvaluator(Long maxResponseTime, boolean expectedNetworkStatus) {
        this.maxResponseTime = maxResponseTime;
        this.expectedNetworkStatus = expectedNetworkStatus;
    }

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