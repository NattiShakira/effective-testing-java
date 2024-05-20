# E-shop

## 1. Adding pre-conditions

Before testing, we have added two pre-conditions to the method `publishOrderToAllListeners` that handle situations when a list of listeners is empty or when the passed `order` is null:

```java
public void publishOrderToAllListeners(Order order) {
    if (listeners.isEmpty()) {
        throw new IllegalStateException("No event listeners registered. There should be at least one event listener.");
    }

    if (order == null) {
        throw new IllegalArgumentException("Order cannot be null.");
    }
    ...
}
```

## 2. Testing with mocks: checking number of invocations and content of the arguments with `ArgumentCaptor`

Class `EventPublisher` has one dependency - interface `EventListener` that is implemented by two classes:
- class `InventoryManager`;
- class `EmailNotificationService`.

When an object of class `EventPublisher` is created, it gets an attribute `listeners` that is a list of event listeners which have subscribed to events broadcasted by the publisher using method `subscribe`.
When testing, this dependency can be mocked and added to the list of event listeners.

All our tests for class `EventPublisher` require the above dependency, thus, at the beginning of the test class, we created a fixture containing declaration of three instances:
- `inventoryManager`;
- `emailNotificationService`;
- `eventPublisher`;

Since our tests use those instances and may change their state, we have decided to instantiate those objects in `@BeforeEach setUp` method that guarantees that
every time a new test needs a corresponding instance, it's supplied to it with a fresh state.

Our tests also require one more object - `order` of class `Order`. Out tests don't change the state of this object, thus, we added it (declared and initialized) to the fixture at the beginning of the test class.

After analyzing the code, the following test cases were devised:
1) `publishOrderToEmptyListenersThrowsIllegalStateException`: 
this test verifies that our method under test throws `IllegalStateException` if the list of event listeners is empty. It also checks that the error message is as expected and that other internal methods are not invoked.

2) `publishNullOrderThrowsIllegalArgumentException`: this test guarantees that the method under test throws `IllegalArgumentException` if the passed `order` is null. It also verifies
that the error message is as expected and that other internal methods are not invoked.

3) `publishOrderToOneInventoryManager`, `publishOrderToOneEmailNotificationService`, `publishOrderToTwoEventListeners` and `publishOrderToManyDifferentEventListeners`:
these tests check that the method under test invokes a method `onOrderPlaced` on each of the subscribed listeners exactly one time. Additionally, using `ArgumentCaptor`, the tests verify that the
correct argument (`order`) is passed to the method `onOrderPlaced`.

To facilitate checking the test with multiple subscribers, we added to class `EventPublisher` an auxiliary method `getAllListeners` that returns a copy of the list of all subscribers:
```java
public List<EventListener> getAllListeners() {
    return new ArrayList<>(listeners);
}
```

## 3. Testing with mocks: increasing observability

We could increase observability of the method under test by making it return different values:
- one option would be to return `boolean`: at the beginning of the method, we initialize a variable with `false`; after the method does its job, this variable changes to `true` and is returned to the client:

```java
public boolean publishOrderToAllListeners(Order order) {
    boolean methodWorkedCorrectly = false;
    ...
    for (EventListener listener : listeners) {
        listener.onOrderPlaced(order);
    }
    methodWorkedCorrectly = true;
    return methodWorkedCorrectly;
}
```
- another option would be to return that same `order` that's passed to the method as an argument, although this option seems less plausible:
```java
public Order publishOrderToAllListeners(Order order) {
    ...  
    for (EventListener listener : listeners) {
      listener.onOrderPlaced(order);
    }
    return order;
}
```

Then, we would have to change our tests `publishOrderToOneInventoryManager`, `publishOrderToOneEmailNotificationService`, `publishOrderToTwoEventListeners` and `publishOrderToManyDifferentEventListeners`. They would look much simpler (see the commented out part in the test class).

Although increasing observability of the method has its advantages (our tests become much simpler, we don't have to expose/check internal mechanics of the method under test anymore, etc.),
in this particular case, tests with mocks seem to check correctness of our method under test better: we can check that the argument `order` was indeed "sent" to the event subscribers.


Screenshots of test results (using `ArgumentCaptor` and without it) are in the `assets` folder. 