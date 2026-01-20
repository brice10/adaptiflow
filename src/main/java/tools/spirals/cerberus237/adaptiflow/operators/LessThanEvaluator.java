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

import tools.spirals.cerberus237.adaptiflow.interfaces.ConditionEvaluatorComparableDataType;

/**
 * The {@link LessThanEvaluator} class implements the {@link ConditionEvaluatorComparableDataType}
 * interface to evaluate whether a given metric is less than a specified bound.
 * <p>
 * This class checks if a metric falls below a defined threshold.
 * </p>
 *
 * @param <T> the type of data that this evaluator will work with, which must
 *            be comparable.
 * @author Arléon Zemtsop (Cerberus)
 */
public class LessThanEvaluator<T extends Comparable<? super T>> implements ConditionEvaluatorComparableDataType<T> {

    /**
     * The bound against which the metric will be compared.
     */
    private final T bound;

    /**
     * Constructs a {@code LessThanEvaluator} with the specified bound.
     *
     * @param bound the threshold value that the metric must be less than.
     */
    public LessThanEvaluator(T bound) {
        this.bound = bound;
    }

    /**
     * Evaluates the specified metric to determine if it is less than the bound.
     * <p>
     * This method returns {@code true} if the metric is less than the bound;
     * {@code false} otherwise.
     * </p>
     *
     * @param metric the metric value to be evaluated.
     * @return {@code true} if the metric is below the bound; {@code false} otherwise.
     */
    @Override
    public boolean test(T metric) {
        return metric.compareTo(bound) < 0;
    }
}