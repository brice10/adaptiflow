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

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import tools.spirals.cerberus237.adaptiflow.events.Event;
import tools.spirals.cerberus237.metricscollectorbase.IMetricsCollector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class SingleEventSchedulerTest {
    private SingleObservationScheduler scheduler;
    private TestEvent event1;
    private TestEvent event2;

    private static class TestEvent extends Event<Double> {
        private boolean listened = false;

        public TestEvent(IMetricsCollector<Double> collector) {
            super(collector);
        }

        @Override
        public void observe() {
            listened = true; // Mark as listened
        }

        public boolean isListened() {
            return listened;
        }
    }

    @Before
    public void setUp() {
        IMetricsCollector<Double> collector1 = () -> 10.0; // Mock collector for event1
        IMetricsCollector<Double> collector2 = () -> 20.0; // Mock collector for event2
        event1 = new TestEvent(collector1);
        event2 = new TestEvent(collector2);
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        scheduler = new SingleObservationScheduler(events);
    }

    @Test
    public void testStartCallsObserveOnAllEvents() {
        scheduler.start();

        Assert.assertTrue(event1.isListened()); // Check that event1 listened
        Assert.assertTrue(event2.isListened()); // Check that event2 listened
    }

    @Test
    public void testStopDoesNotThrowException() {
        try {
            scheduler.stop(); // Should not throw any exception
        } catch (Exception e) {
            Assert.fail("Stop method should not throw an exception");
        }
    }

    @Test
    public void testConstructorInitializesListeners() {
        Assert.assertEquals(2, scheduler.events.size()); // Check the number of listeners
        Assert.assertTrue(scheduler.events.contains(event1));
        Assert.assertTrue(scheduler.events.contains(event2));
    }
}