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
 * The {@link ThresholdProvider} interface defines a contract for classes
 * that provide a threshold value of a comparable type.
 * <p>
 * Implementing classes must provide an implementation of the {@code getThreshold}
 * method to return a threshold value that can be used for comparisons.
 * </p>
 *
 * @param <T> the type of the threshold value, which must be comparable.
 * @author Arléon Zemtsop (Cerberus)
 */
public interface ThresholdProvider<T extends Comparable<? super T>> {

    /**
     * Retrieves the threshold value.
     *
     * @return the threshold value of type {@code T}.
     */
    T getThreshold();
}