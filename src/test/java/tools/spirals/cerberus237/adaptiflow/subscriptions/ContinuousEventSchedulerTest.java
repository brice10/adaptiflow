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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class ContinuousEventSchedulerTest {
    private ContinuousObservationScheduler scheduler;
    private TestEvent event1;
    private TestEvent event2;
    private List<Event> events;

    private static class TestEvent extends Event<Double> {
        private final AtomicBoolean listened = new AtomicBoolean(false);

        public TestEvent(IMetricsCollector<Double> collector) {
            super(collector);
        }

        @Override
        public void observe() {
            listened.set(true); // Mark as listened
        }

        public boolean isListened() {
            return listened.get();
        }

        public void reset() {
            listened.set(false); // Reset for testing
        }
    }

    @Before
    public void setUp() {
        IMetricsCollector<Double> collector1 = () -> 10.0; // Mock collector for event1
        IMetricsCollector<Double> collector2 = () -> 20.0; // Mock collector for event2
        event1 = new TestEvent(collector1);
        event2 = new TestEvent(collector2);
        events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        scheduler = new ContinuousObservationScheduler(events, 100);
    }

    @Test
    public void testStartCallsObserveOnAllEventsAtInterval() throws InterruptedException {
        scheduler.start();

        // Allow some time for the scheduled task to run
        TimeUnit.MILLISECONDS.sleep(250); // Wait for a few intervals

        Assert.assertTrue(event1.isListened()); // Check that event1 listened
        Assert.assertTrue(event2.isListened()); // Check that event2 listened

        // Stop the scheduler
        scheduler.stop();
        // Reset the listened state for further tests
        event1.reset();
        event2.reset();
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