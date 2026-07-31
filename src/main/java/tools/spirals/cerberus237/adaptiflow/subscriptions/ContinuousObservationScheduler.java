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
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
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
public class ContinuousObservationScheduler extends AbstractObservationScheduler implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(ContinuousObservationScheduler.class);

    /**
     * The interval in milliseconds at which to listen for events.
     */
    private final int interval;

    /**
     * The scheduled executor service used to run tasks at fixed intervals.
     */
    private ScheduledExecutorService scheduler;

    /**
     * The scheduled task currently tracking the periodic observation loop.
     */
    private ScheduledFuture<?> scheduledTask;

    private boolean running = false;

    /**
     * Constructs a {@code ContinuousObservationScheduler} with the specified list of events
     * and an interval for event listening.
     *
     * @param events a list of events to be managed by this scheduler.
     * @param interval the interval in milliseconds for listening to events.
     */
    public ContinuousObservationScheduler(List<Event> events, int interval) {
        this(events, interval, null);
    }

    /**
     * Constructs a {@code ContinuousObservationScheduler} with the specified list of events,
     * an interval for event listening, and a descriptive name.
     *
     * @param events a list of events to be managed by this scheduler.
     * @param interval the interval in milliseconds for listening to events.
     * @param name a descriptive name for this scheduler, or {@code null} when not provided.
     */
    public ContinuousObservationScheduler(List<Event> events, int interval, String name) {
        super(events, name);
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(createThreadFactory(name, events));
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
        if (running) {
            return;
        }

        if (scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor(createThreadFactory(name, events));
        }

        LOG.info("[AdaptiFlow] Start Continuous Event Observations ...");
        scheduledTask = scheduler.scheduleWithFixedDelay(() -> {
            if (Thread.currentThread().isInterrupted()) {
                LOG.warn("[AdaptiFlow] Observation thread interrupted; stopping scheduler.");
                stop();
                return;
            }

            try {
                events.forEach(Event::observe);
            } catch (RuntimeException e) {
                LOG.warn("[AdaptiFlow] Error while observing events", e);
            }
        }, interval, interval, TimeUnit.MILLISECONDS);
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
        if (!running && scheduledTask == null) {
            return;
        }

        LOG.info("[AdaptiFlow] Stop Continuous Event Observations ...");
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTask = null;
        }

        scheduler.shutdownNow();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                LOG.warn("[AdaptiFlow] Scheduler did not terminate in time.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.warn("[AdaptiFlow] Interrupted while stopping scheduler", e);
        }

        scheduler = Executors.newSingleThreadScheduledExecutor(createThreadFactory(name, events));
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private static ThreadFactory createThreadFactory(String schedulerName, List<Event> events) {
        return runnable -> {
            Thread thread = new Thread(runnable, buildThreadName(schedulerName, events));
            thread.setDaemon(true);
            return thread;
        };
    }

    private static String buildThreadName(String schedulerName, List<Event> events) {
        StringBuilder builder = new StringBuilder();
        if (schedulerName != null && !schedulerName.isEmpty()) {
            builder.append(schedulerName);
        } else {
            builder.append("ContinuousObservationScheduler");
        }

        if (events != null && !events.isEmpty()) {
            builder.append(" : ");
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                if (event != null) {
                    String eventName = event.getName();
                    if (eventName == null || eventName.isEmpty()) {
                        eventName = event.getClass().getSimpleName();
                    }
                    builder.append(eventName);
                    if (i < events.size() - 1) {
                        builder.append("+");
                    }
                }
            }
        }

        builder.append("-").append(UUID.randomUUID().toString().substring(0, 8));
        return builder.toString();
    }

    @Override
    public void close() {
        stop();
    }
}