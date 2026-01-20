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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tools.spirals.cerberus237.adaptiflow.interfaces.ConditionEvaluator;
import tools.spirals.cerberus237.adaptiflow.interfaces.Observer;
import tools.spirals.cerberus237.adaptiflow.interfaces.ThresholdProvider;
import tools.spirals.cerberus237.adaptiflow.operators.TrueEvaluator;
import tools.spirals.cerberus237.metricscollectorbase.IMetricsCollector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class DecreaseEventTest {
    private TestMetricsCollector collector;
    private DecreaseEvent<Double> decreaseEvent;
    private TestObserver<Double> observer;

    private static class TestMetricsCollector implements IMetricsCollector<Double> {
        private double value;

        public TestMetricsCollector(double value) {
            this.value = value;
        }

        @Override
        public Double get() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    private static class TestObserver<T extends Comparable<T>> implements Observer<T> {
        private final List<T> notifiedValues = new ArrayList<>();

        @Override
        public void update(T metricValue, String message) {
            notifiedValues.add(metricValue);
        }

        @Override
        public ConditionEvaluator<T> getConditionEvaluator() {
            return new TrueEvaluator<>();
        }

        public List<T> getNotifiedValues() {
            return notifiedValues;
        }
    }

    @Before
    public void setUp() {
        collector = new TestMetricsCollector(50.0);
        ThresholdProvider<Double> thresholdProvider = () -> 50.0;
        decreaseEvent = new DecreaseEvent<>(collector, thresholdProvider);
        observer = new TestObserver<>();
        decreaseEvent.subscribe(observer);
    }

    @Test
    public void testNotifyObserverOnDecreaseEvent() {
        collector.setValue(30.0); // Value below threshold
        decreaseEvent.observe();

        Assert.assertEquals(1, observer.getNotifiedValues().size());
        Assert.assertEquals(30.0, observer.getNotifiedValues().get(0), 0.01);
    }

    @Test
    public void testNoNotificationForIncrease() {
        collector.setValue(60.0); // Above the threshold
        decreaseEvent.observe();

        Assert.assertTrue(observer.getNotifiedValues().isEmpty());
    }
}