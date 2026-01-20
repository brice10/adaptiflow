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
package tools.spirals.cerberus237.adaptiflow.interfaces;

/**
 * The {@link ConditionEvaluator} interface defines a contract for evaluating
 * conditions based on a metric value of type {@code T}.
 * <p>
 * Implementations of this interface will provide specific logic to determine
 * whether a given metric satisfies certain conditions.
 * </p>
 *
 * @param <T> the type of data that this evaluator will work with.
 * @author Arléon Zemtsop (Cerberus)
 */
public interface ConditionEvaluator<T> {

    /**
     * Evaluates the specified metric to determine if it meets the condition.
     * <p>
     * This method returns {@code true} if the metric satisfies the condition,
     * and {@code false} otherwise.
     * </p>
     *
     * @param metric the metric value to be evaluated.
     * @return {@code true} if the metric meets the condition; {@code false} otherwise.
     */
    boolean test(T metric);
}