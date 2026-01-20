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
package tools.spirals.cerberus237.adaptiflow.events;

import tools.spirals.cerberus237.adaptiflow.interfaces.Observer;
import tools.spirals.cerberus237.adaptiflow.interfaces.ThresholdProvider;
import tools.spirals.cerberus237.adaptiflow.operators.LessThanEvaluator;
import tools.spirals.cerberus237.metricscollectorbase.IMetricsCollector;

/**
 * The {@link DecreaseEvent} class extends the {@link ConditionalEvent} class
 * and is specifically designed to notify observers when a metric value decreases
 * below a certain threshold.
 * <p>
 * This class utilizes a condition evaluator to check if the current metric value
 * is less than a specified threshold, which is provided by a {@link ThresholdProvider}.
 * </p>
 *
 * @param <T> the type of data that this event will provide to its observers, which must
 *            be comparable.
 * @author Arléon Zemtsop (Cerberus)
 */
public class DecreaseEvent<T extends Comparable<? super T>> extends ConditionalEvent<T> {

    /**
     * Constructs a {@link DecreaseEvent} with the specified metrics collector
     * and threshold provider.
     * <p>
     * The constructor initializes the condition evaluator with a
     * {@link LessThanEvaluator} that checks if the metric value is less than
     * the threshold provided by the threshold provider.
     * </p>
     *
     * @param collector the metrics collector that gathers metric values for this event.
     * @param thresholdProvider the provider that supplies the threshold for the decrease condition.
     */
    public DecreaseEvent(IMetricsCollector<T> collector, ThresholdProvider<T> thresholdProvider) {
        super(collector, new LessThanEvaluator<>(thresholdProvider.getThreshold()));
        this.conditionEvaluator = new LessThanEvaluator<>(thresholdProvider.getThreshold());
        this.name = "decrease";
    }
}