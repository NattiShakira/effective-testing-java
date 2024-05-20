import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private List<EventListener> listeners = new ArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void publishOrderToAllListeners(Order order) {
        if (listeners.isEmpty()) {
            throw new IllegalStateException("No event listeners registered. There should be at least one event listener.");
        }

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        for (EventListener listener : listeners) {
            listener.onOrderPlaced(order);
        }
    }

    public List<EventListener> getAllListeners() {
        return new ArrayList<>(listeners);
    }

    //Changed method: returns an order
    /***public Order publishOrderToAllListeners(Order order) {

        if (listeners.isEmpty()) {
        throw new IllegalStateException("No event listeners registered. There should be at least one event listener.");
        }

        if (order == null) {
        throw new IllegalArgumentException("Order cannot be null.");
        }

        for (EventListener listener : listeners) {
        listener.onOrderPlaced(order);
        }
        return order;
    }***/

    //Changed method: returns a boolean value
    /***public boolean publishOrderToAllListeners(Order order) {
        boolean methodWorkedCorrectly = false;

        if (listeners.isEmpty()) {
            throw new IllegalStateException("No event listeners registered. There should be at least one event listener.");
        }

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        for (EventListener listener : listeners) {
            listener.onOrderPlaced(order);
        }
        methodWorkedCorrectly = true;
        return methodWorkedCorrectly;
    }***/

}
