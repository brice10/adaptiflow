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
 * The {@link BetweenEvaluator} class implements the {@link ConditionEvaluatorComparableDataType}
 * interface to evaluate whether a given metric falls within a specified range.
 * <p>
 * This class checks if a metric is between a defined lower and upper bound (inclusive).
 * </p>
 *
 * @param <T> the type of data that this evaluator will work with, which must
 *            be comparable.
 * @author Arléon Zemtsop (Cerberus)
 */
public class BetweenEvaluator<T extends Comparable<? super T>> implements ConditionEvaluatorComparableDataType<T> {

    /**
     * The lower bound of the range.
     */
    private final T lowerBound;

    /**
     * The upper bound of the range.
     */
    private final T upperBound;

    /**
     * Constructs a {@code BetweenEvaluator} with specified lower and upper bounds.
     *
     * @param lowerBound the lower bound of the range.
     * @param upperBound the upper bound of the range.
     */
    public BetweenEvaluator(T lowerBound, T upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Evaluates the specified metric to determine if it falls within the defined range.
     * <p>
     * This method returns {@code true} if the metric is greater than or equal to
     * the lower bound and less than or equal to the upper bound; {@code false} otherwise.
     * </p>
     *
     * @param metric the metric value to be evaluated.
     * @return {@code true} if the metric is within the range; {@code false} otherwise.
     */
    @Override
    public boolean test(T metric) {
        return lowerBound.compareTo(metric) <= 0 && upperBound.compareTo(metric) >= 0;
    }
}