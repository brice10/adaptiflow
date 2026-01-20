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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.spirals.cerberus237.adaptiflow.events.Event;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@link ContinuousObservationScheduler} class extends {@link AbstractObservationScheduler}
 * to provide a mechanism for continuously listening to events at a fixed interval.
 * <p>
 * This class utilizes a scheduled executor service to invoke the listening
 * process for all registered events at regular intervals.
 * </p>
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class ContinuousObservationScheduler extends AbstractObservationScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(ContinuousObservationScheduler.class);

    /**
     * The interval in milliseconds at which to listen for events.
     */
    private final int interval;

    /**
     * The scheduled executor service used to run tasks at fixed intervals.
     */
    private ScheduledExecutorService scheduler;

    private boolean running = false;

    /**
     * Constructs a {@code ContinuousEventScheduler} with the specified list of events
     * and an interval for event listening.
     *
     * @param events a list of events to be managed by this scheduler.
     * @param interval the interval in milliseconds for listening to events.
     */
    public ContinuousObservationScheduler(List<Event> events, int interval) {
        super(events);
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the continuous event scheduler by initiating the listening process
     * for all registered events at the specified interval.
     * <p>
     * This method uses a scheduled executor service to call the {@code listen}
     * method on each event in the listeners list at regular intervals.
     * </p>
     */
    @Override
    public void start() {
        LOG.info("Start Continuous Event Observations ...");
        scheduler.scheduleAtFixedRate(() -> events.forEach(Event::observe), interval, interval, TimeUnit.MILLISECONDS);
        this.running = true;
    }

    /**
     * Stops the continuous event scheduler.
     * <p>
     * This method halts the listening process and shuts down the scheduled executor service.
     * </p>
     */
    @Override
    public void stop() {
        LOG.info("Stop Continuous Event Observations ...");
        scheduler.shutdownNow();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }
}