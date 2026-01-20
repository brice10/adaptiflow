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

/**
 * The {@link TrueEvaluator} class implements the {@link ConditionEvaluator}
 * interface to evaluate any given metric as true.
 * <p>
 * This class is useful as a default or fallback evaluator that always returns
 * true, regardless of the metric value.
 * </p>
 * @author Arléon Zemtsop (Cerberus)
 */
public class TrueEvaluator<T> implements ConditionEvaluator<T> {

    /**
     * Evaluates the specified metric to always return true.
     * <p>
     * This method disregards the value of the metric and will always return
     * {@code true}.
     * </p>
     *
     * @param metric the metric value to be evaluated (not used in this implementation).
     * @return {@code true} for any metric value.
     */
    @Override
    public boolean test(T metric) {
        return true;
    }
}