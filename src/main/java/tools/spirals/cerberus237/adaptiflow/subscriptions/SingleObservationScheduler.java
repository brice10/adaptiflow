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

import tools.spirals.cerberus237.adaptiflow.events.Event;

import java.util.List;

/**
 * The {@link SingleObservationScheduler} class extends {@link AbstractObservationScheduler}
 * to provide a mechanism for listening to events in a single execution context.
 * <p>
 * This class initiates the listening process for all registered events
 * without any continuous or scheduled execution.
 * </p>
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public class SingleObservationScheduler extends AbstractObservationScheduler {

    /**
     * Constructs a {@link SingleObservationScheduler} with the specified list of events.
     *
     * @param events a list of events to be managed by this scheduler.
     */
    public SingleObservationScheduler(List<Event> events) {
        super(events);
    }

    /**
     * Starts the single event scheduler by initiating the listening process
     * for all registered events.
     * <p>
     * This method calls the {@code listen} method on each event in the
     * listeners list once.
     * </p>
     */
    @Override
    public void start() {
        events.forEach(Event::observe);
    }

    /**
     * Stops the single event scheduler.
     * <p>
     * This method is intended to halt the event handling process.
     * Currently, it has no implementation.
     * </p>
     */
    @Override
    public void stop() {
        // Implementation for stopping the event handling process can be added here.
    }
}