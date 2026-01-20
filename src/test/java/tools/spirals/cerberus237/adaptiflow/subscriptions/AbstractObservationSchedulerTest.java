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
package tools.spirals.cerberus237.adaptiflow.subscriptions;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tools.spirals.cerberus237.adaptiflow.events.Event;
import tools.spirals.cerberus237.metricscollectorbase.IMetricsCollector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class AbstractObservationSchedulerTest {
    private List<Event> observers;
    private TestObservationScheduler subscription;

    private static class TestEventSubscription extends Event<Double> {
        private boolean isListening;

        public TestEventSubscription(IMetricsCollector<Double> collector) {
            super(collector);
        }

        @Override
        public void notifyObservers(Double metricValue) {
            // Logic to notify observers (if needed for this test)
        }

        @Override
        public void observe() {
            isListening = true; // Simulates starting to listen
        }

        public boolean isListening() {
            return isListening;
        }
    }

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

    private static class TestObservationScheduler extends AbstractObservationScheduler {
        public TestObservationScheduler(List<Event> listeners) {
            super(listeners);
        }
        @Override
        public void start() {
            events.forEach(Event::observe);
        }
        @Override
        public void stop() {}
    }

    @Before
    public void setUp() {
        observers = new ArrayList<>();

        // Initialize test metrics collectors and listeners
        TestMetricsCollector collector1 = new TestMetricsCollector(50.0);
        TestEventSubscription listener1 = new TestEventSubscription(collector1);
        observers.add(listener1);

        subscription = new TestObservationScheduler(observers);
    }

    @Test
    public void testStartCallsListenOnListeners() {
        // Start the subscription
        subscription.start();

        // Verify that the listener has started listening
        TestEventSubscription listener1 = (TestEventSubscription) observers.get(0);
        Assert.assertTrue("Listener should be listening after subscription starts", listener1.isListening());
    }

    @Test
    public void testStopDoesNotChangeListenerState() {
        // Start the subscription
        subscription.start();

        // Store the initial listening state
        boolean initialListeningState = ((TestEventSubscription) observers.get(0)).isListening();

        // Stop the subscription
        subscription.stop();

        // Verify that the listening state remains unchanged
        Assert.assertEquals("Listener state should remain unchanged after stop", initialListeningState, ((TestEventSubscription) observers.get(0)).isListening());
    }

    @After
    public void tearDown() {
        // Clean-up logic if necessary, currently not needed since stop does nothing
    }
}