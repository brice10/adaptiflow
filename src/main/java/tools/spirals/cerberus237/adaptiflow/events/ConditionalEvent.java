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

import tools.spirals.cerberus237.adaptiflow.interfaces.ConditionEvaluator;
import tools.spirals.cerberus237.adaptiflow.interfaces.Observer;
import tools.spirals.cerberus237.metricscollectorbase.IMetricsCollector;

/**
 * The {@link ConditionalEvent} class extends the {@link Event} class and adds
 * additional functionality for evaluating conditions before notifying observers.
 * <p>
 * This class allows the event to have a specific condition evaluator that must be
 * satisfied before notifying each observer, in addition to the observer's own condition.
 * </p>
 *
 * @param <T> the type of data that this event will provide to its observers.
 * @author Arléon Zemtsop (Cerberus)
 */
public class ConditionalEvent<T> extends Event<T> {

    /**
     * The condition evaluator used to determine if the event should notify observers
     * based on the collected metric value.
     */
    protected ConditionEvaluator<T> conditionEvaluator;

    /**
     * Constructs a {@code ConditionalEvent} with the specified metrics collector
     * and condition evaluator.
     *
     * @param collector the metrics collector that gathers metric values for this event.
     * @param conditionEvaluator the condition evaluator that determines whether the event
     *                           should notify observers based on the metric value.
     */
    public ConditionalEvent(IMetricsCollector<T> collector, ConditionEvaluator<T> conditionEvaluator) {
        super(collector);
        this.conditionEvaluator = conditionEvaluator;
    }

    /**
     * Constructs a {@code ConditionalEvent} with his name, the specified metrics collector
     * and condition evaluator.
     *
     * @param name the event name.
     * @param collector the metrics collector that gathers metric values for this event.
     * @param conditionEvaluator the condition evaluator that determines whether the event
     *                           should notify observers based on the metric value.
     */
    public ConditionalEvent(String name, IMetricsCollector<T> collector, ConditionEvaluator<T> conditionEvaluator) {
        super(name, collector);
        this.conditionEvaluator = conditionEvaluator;
    }

    /**
     * Observe metric changes and notifies observers if both the event's
     * condition and the observers' conditions are met.
     * <p>
     * This method retrieves the current metric value from the collector and
     * checks both the event's condition evaluator and each observer's condition evaluator.
     * If both conditions are satisfied, the observer is notified with the current
     * metric value.
     * </p>
     */
    @Override
    public void observe() {
        T metric = collector.get();
        for (Observer<T> observer : subscribers) {
            if (this.conditionEvaluator.test(metric) && observer.getConditionEvaluator().test(metric)) {
                notifyObserver(observer, metric);
            }
        }
    }
}