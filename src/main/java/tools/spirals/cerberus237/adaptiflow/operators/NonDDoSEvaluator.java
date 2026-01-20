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
package tools.spirals.cerberus237.adaptiflow.operators;

import tools.spirals.cerberus237.adaptiflow.interfaces.ConditionEvaluator;
import tools.spirals.cerberus237.adaptiflow.interfaces.ThresholdProvider;
import tools.spirals.cerberus237.metricscollectorbase.models.ServiceMetrics;

public class NonDDoSEvaluator implements ConditionEvaluator<ServiceMetrics> {
    private final ThresholdProvider<Double> rateThresholdProvider;
    private final long timeWindowMillis;

    public NonDDoSEvaluator(ThresholdProvider<Double> rateThresholdProvider, long timeWindowMillis) {
        this.rateThresholdProvider = rateThresholdProvider;
        this.timeWindowMillis = timeWindowMillis;
    }

    @Override
    public boolean test(ServiceMetrics metrics) {
        return metrics != null && metrics.getRequestRatePerSecond(timeWindowMillis) <= rateThresholdProvider.getThreshold();
    }
}