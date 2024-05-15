import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;


class EventPublisherTest {
    EventPublisher eventPublisher;
    InventoryManager inventoryManager;
    EmailNotificationService emailNotificationService;

    String orderId = "123456";
    double amount = 2.0;
    Order order = new Order(orderId, amount);

    @BeforeEach
    void setUp() {
        eventPublisher = new EventPublisher();
        inventoryManager = mock(InventoryManager.class);
        emailNotificationService = mock(EmailNotificationService.class);
    }

    @Test
    void publishOrderToEmptyListenersThrowsIllegalStateException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> eventPublisher.publishOrderToAllListeners(order));
        assertEquals("No event listeners registered. There should be at least one event listener.", exception.getMessage());
        verify(inventoryManager, never()).onOrderPlaced(order);
        verify(emailNotificationService, never()).onOrderPlaced(order);
    }

    @Test
    void publishNullOrderThrowsIllegalArgumentException() {
        Order order = null;

        eventPublisher.subscribe(inventoryManager);
        eventPublisher.subscribe(emailNotificationService);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> eventPublisher.publishOrderToAllListeners(order));
        assertEquals("Order cannot be null.", exception.getMessage());

        verify(inventoryManager, never()).onOrderPlaced(order);
        verify(emailNotificationService, never()).onOrderPlaced(order);
    }

    @Test
    void publishOrderToOneInventoryManager() {
        eventPublisher.subscribe(inventoryManager);
        eventPublisher.publishOrderToAllListeners(order);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(inventoryManager, times(1)).onOrderPlaced(captor.capture());
        Order actualOrder = captor.getValue();
        assertEquals(order, actualOrder);
    }

    @Test
    void publishOrderToOneEmailNotificationService() {
        eventPublisher.subscribe(emailNotificationService);
        eventPublisher.publishOrderToAllListeners(order);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(emailNotificationService, times(1)).onOrderPlaced(captor.capture());
        Order actualOrder = captor.getValue();
        assertEquals(order, actualOrder);
    }

    @Test
    void publishOrderToTwoEventListeners() {
        eventPublisher.subscribe(inventoryManager);
        eventPublisher.subscribe(emailNotificationService);
        eventPublisher.publishOrderToAllListeners(order);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        verify(emailNotificationService, times(1)).onOrderPlaced(captor.capture());
        Order actualOrderForEmailNotificationService = captor.getValue();
        assertEquals(order, actualOrderForEmailNotificationService);

        verify(inventoryManager, times(1)).onOrderPlaced(captor.capture());
        Order actualOrderForInventoryManager = captor.getValue();
        assertEquals(order, actualOrderForInventoryManager);
    }

    @Test
    void publishOrderToManyDifferentEventListeners() {
        int numberOfEachTypeOfEventListener = 5;
        for (int i = 0; i < numberOfEachTypeOfEventListener; i++) {
            eventPublisher.subscribe(mock(InventoryManager.class));
            eventPublisher.subscribe(mock(EmailNotificationService.class));
        }

        eventPublisher.publishOrderToAllListeners(order);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        Order actualOrderForEventListener;
        for (int i = 0; i < numberOfEachTypeOfEventListener * 2; i++) {
            verify(eventPublisher.getAllListeners().get(i), times(1)).onOrderPlaced(captor.capture());
            actualOrderForEventListener = captor.getValue();
            assertEquals(order, actualOrderForEventListener);
        }
    }

    // Changed tests in case our method under test would return a boolean value
    /***@Test
    void publishOrderToOneInventoryManager() {
        eventPublisher.subscribe(inventoryManager);
        boolean actualResult = eventPublisher.publishOrderToAllListeners(order);
        assertTrue(actualResult);
    }

    @Test
    void publishOrderToOneEmailNotificationService() {
        eventPublisher.subscribe(emailNotificationService);
        boolean actualResult = eventPublisher.publishOrderToAllListeners(order);
        assertTrue(actualResult);
    }

    @Test
    void publishOrderToTwoEventListeners() {
        eventPublisher.subscribe(inventoryManager);
        eventPublisher.subscribe(emailNotificationService);
        boolean actualResult = eventPublisher.publishOrderToAllListeners(order);
        assertTrue(actualResult);
    }

    @Test
    void publishOrderToManyDifferentEventListeners() {
        int numberOfEachTypeOfEventListener = 5;
        for (int i = 0; i < numberOfEachTypeOfEventListener; i++) {
            eventPublisher.subscribe(mock(InventoryManager.class));
            eventPublisher.subscribe(mock(EmailNotificationService.class));
        }
        boolean actualResult = eventPublisher.publishOrderToAllListeners(order);
        assertTrue(actualResult);
    }***/

}
