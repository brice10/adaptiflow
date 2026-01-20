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

import java.util.HashMap;

public class DecreaseResourceUsageEvaluator implements ConditionEvaluator<HashMap<String, Double>> {
    private final ThresholdProvider<Double> cpuThresholdProvider;
    private final ThresholdProvider<Double> memoryThresholdProvider;

    public DecreaseResourceUsageEvaluator(ThresholdProvider<Double> cpuThresholdProvider, ThresholdProvider<Double> memoryThresholdProvider) {
        this.cpuThresholdProvider = cpuThresholdProvider;
        this.memoryThresholdProvider = memoryThresholdProvider;
    }

    @Override
    public boolean test(HashMap<String, Double> metric) {
        return metric.get("cpu") != null
                && metric.get("memory") != null
                && (metric.get("cpu") <= cpuThresholdProvider.getThreshold()
                || metric.get("memory") <= memoryThresholdProvider.getThreshold());
    }
}
