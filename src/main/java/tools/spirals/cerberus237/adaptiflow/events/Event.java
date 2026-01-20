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

import tools.spirals.cerberus237.adaptiflow.interfaces.Observable;
import tools.spirals.cerberus237.adaptiflow.interfaces.Observer;
import tools.spirals.cerberus237.metricscollectorbase.IMetricsCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Event} class represents an observable event that can have multiple observers
 * subscribed to it. It collects metrics using a specified metrics collector and notifies
 * its observers when those metrics change and meet certain conditions.
 * <p>
 * This class implements the {@code Observable} interface, allowing observers to subscribe
 * or unsubscribe and receive notifications based on metric values collected.
 * </p>
 *
 * @param <T> the type of data that this event will provide to its observers.
 * @author Arléon Zemtsop (Cerberus)
 */
public class Event<T> implements Observable<T> {

    /**
     * Event name.
     */
    protected String name;

    /**
     * A list of observers subscribed to this event.
     */
    protected final List<Observer<T>> subscribers = new ArrayList<>();

    /**
     * The metrics collector used to gather metric values for this event.
     */
    protected final IMetricsCollector<T> collector;

    /**
     * Constructs an {@code Event} with the specified metrics collector.
     *
     * @param collector the metrics collector that gathers metric values for this event.
     */
    public Event(IMetricsCollector<T> collector) {
        this.collector = collector;
    }

    /**
     * Constructs an {@code Event} with his name and the specified metrics collector.
     *
     * @param collector the metrics collector that gathers metric values for this event.
     */
    public Event(String name, IMetricsCollector<T> collector) {
        this.name = name;
        this.collector = collector;
    }

    /**
     * Subscribes a single observer to this event.
     * <p>
     * This method adds the specified observer to the list of subscribers
     * that will receive notifications when the event's metrics change.
     * </p>
     *
     * @param subscriber the observer to be added to the subscription list.
     */
    @Override
    public void subscribe(Observer<T> subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unsubscribes a single observer from this event.
     * <p>
     * This method removes the specified observer from the list of subscribers,
     * preventing it from receiving further notifications about metric changes.
     * </p>
     *
     * @param subscriber the observer to be removed from the subscription list.
     */
    @Override
    public void unsubscribe(Observer<T> subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Subscribes multiple observers to this event.
     * <p>
     * This method adds all specified observers to the list of subscribers
     * that will receive notifications when the event's metrics change.
     * </p>
     *
     * @param subscribers a list of observers to be added to the subscription list.
     */
    @Override
    public void subscribeAll(List<Observer<T>> subscribers) {
        subscribers.forEach(this::subscribe);
    }

    /**
     * Unsubscribes multiple observers from this event.
     * <p>
     * This method removes all specified observers from the list of subscribers,
     * preventing them from receiving further notifications about metric changes.
     * </p>
     *
     * @param subscribers a list of observers to be removed from the subscription list.
     */
    @Override
    public void unsubscribeAll(List<Observer<T>> subscribers) {
        subscribers.forEach(this::unsubscribe);
    }

    /**
     * Observe metric changes and notifies observers if the conditions are met.
     * <p>
     * This method retrieves the current metric value from the collector and
     * checks each observer's condition evaluator. If the condition is satisfied,
     * the observer is notified with the current metric value.
     * </p>
     */
    public void observe() {
        T metric = collector.get();
        for (Observer<T> observer : subscribers) {
            if (observer.getConditionEvaluator().test(metric)) {
                notifyObserver(observer, metric);
            }
        }
    }

    /**
     * Notifies all subscribed observers with the given metric value.
     * <p>
     * This method triggers an update to all observers, passing them the current
     * metric value, which they can use to react to the change.
     * </p>
     *
     * @param metricValue the new metric value to be sent to the observers.
     */
    @Override
    public void notifyObservers(T metricValue) {
        for (Observer<T> subscriber : subscribers) {
            notifyObserver(subscriber, metricValue);
        }
    }

    /**
     * Notifies a specific observer with the given metric value.
     * <p>
     * This method triggers an update to the specified observer only,
     * passing the current metric value for its consideration.
     * </p>
     *
     * @param observer  the observer to be notified.
     * @param metricValue the new metric value to be sent to the observer.
     */
    @Override
    public void notifyObserver(Observer<T> observer, T metricValue) {
        observer.update(metricValue, "Handling " + this.name + " event");
    }

    /**
     * Returns the list of currently subscribed observers.
     * <p>
     * This method allows access to the list of subscribers for external use.
     * </p>
     *
     * @return a list of observers currently subscribed to this event.
     */
    public List<Observer<T>> getSubscribers() {
        return subscribers;
    }

}