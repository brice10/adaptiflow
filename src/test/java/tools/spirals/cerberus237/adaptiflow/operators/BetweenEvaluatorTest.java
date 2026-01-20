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

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class BetweenEvaluatorTest {
    private BetweenEvaluator<Integer> evaluator;

    @Before
    public void setUp() {
        evaluator = new BetweenEvaluator<>(10, 20);
    }

    @Test
    public void testValueWithinBounds() {
        Assert.assertTrue(evaluator.test(15)); // Should return true
    }

    @Test
    public void testValueAtLowerBound() {
        Assert.assertTrue(evaluator.test(10)); // Should return true
    }

    @Test
    public void testValueAtUpperBound() {
        Assert.assertTrue(evaluator.test(20)); // Should return true
    }

    @Test
    public void testValueBelowLowerBound() {
        Assert.assertFalse(evaluator.test(5)); // Should return false
    }

    @Test
    public void testValueAboveUpperBound() {
        Assert.assertFalse(evaluator.test(25)); // Should return false
    }
}