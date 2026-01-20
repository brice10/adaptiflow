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
 * The {@link AbstractObservationScheduler} class serves as a base class for
 * event schedulers that manage a list of events to be listened to.
 * <p>
 * This abstract class provides functionality to start listening to events
 * and can be extended by concrete implementations that define specific
 * behaviors for starting and stopping event handling.
 * </p>
 *
 * @author Arléon Zemtsop (Cerberus)
 */
public abstract class AbstractObservationScheduler {

    /**
     * A list of events that this scheduler listens to.
     */
    protected final List<Event> events;

    /**
     * Constructs an {@code AbstractEventScheduler} with the specified list of events.
     *
     * @param events a list of events to be managed by this scheduler.
     */
    public AbstractObservationScheduler(List<Event> events) {
        this.events = events;
    }

    /**
     * Starts the event scheduler by initiating the listening process
     * for all registered events.
     * <p>
     * This method is intended to start the event handling process.
     * Currently, it has no implementation.
     * </p>
     */
    public abstract void start();

    /**
     * Stops the event scheduler.
     * <p>
     * This method is intended to halt the event handling process.
     * Currently, it has no implementation.
     * </p>
     */
    public abstract void stop();
}