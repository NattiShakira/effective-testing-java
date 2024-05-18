import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TicketManagerTest {
    private NotificationService notificationServiceMock;
    private LogService logServiceMock;
    private TicketRepository ticketRepositoryMock;
    private TicketManager ticketManager;

    @BeforeEach
    public void setup() {
        notificationServiceMock = mock(NotificationService.class);
        logServiceMock = mock(LogService.class);
        ticketRepositoryMock = mock(TicketRepository.class);
        ticketManager = new TicketManager(notificationServiceMock, logServiceMock, ticketRepositoryMock);
    }

    @Test
    void checkCreateTicket() {
        Ticket ticket = new Ticket("ex@ample.com", "Issue description", TicketPriority.NORMAL);

        ticketManager.createTicket(ticket);
        verify(logServiceMock).logTicketCreation(ticket);
        verify(notificationServiceMock).notifyCustomer(ticket.getCustomerEmail(),
                "Thank you for your request. Your support ticket has been created and will be processed shortly.");
        verify(ticketRepositoryMock).save(ticket);
    }

    @Test
    void checkNormalPriority_NotifyNormally() {
        Ticket normalTicket = new Ticket("ex@ample.com", "Issue description", TicketPriority.NORMAL);

        ticketManager.createTicket(normalTicket);

        verify(notificationServiceMock).notifyCustomer(normalTicket.getCustomerEmail(),
                "Thank you for your request. Your support ticket has been created and will be processed shortly.");
    }

    @Test
    void checkUrgentPriority_NotifyUrgently() {
        Ticket urgentTicket = new Ticket("urgent@example.com", "Urgent issue description", TicketPriority.URGENT);

        ticketManager.createTicket(urgentTicket);

        verify(notificationServiceMock).notifyCustomer(urgentTicket.getCustomerEmail(),
                "Thank you for your request. Your support ticket has been created and is being processed as high priority.");
    }

    @Test
    void checkLogBeforeNotifying() {
        Ticket ticket = new Ticket("ex@ample.com", "Issue description", TicketPriority.NORMAL);
        ticketManager.createTicket(ticket);
        InOrder inOrder = inOrder(logServiceMock, notificationServiceMock);
        inOrder.verify(logServiceMock).logTicketCreation(ticket);
        inOrder.verify(notificationServiceMock).notifyCustomer(ticket.getCustomerEmail(),
                "Thank you for your request. Your support ticket has been created and will be processed shortly.");
        verify(ticketRepositoryMock).save(ticket);
    }

    @Test
    void checkException_NullTicket() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(null);
        });

        assertEquals("Ticket cannot be null", exception.getMessage());
        verifyNoInteractions(logServiceMock);
        verifyNoInteractions(notificationServiceMock);
        verifyNoInteractions(ticketRepositoryMock);
    }

    @Test
    void checkException_TicketIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(null);
        });
        assertEquals("Ticket cannot be null", exception.getMessage());
        verifyNoInteractions(logServiceMock);
        verifyNoInteractions(notificationServiceMock);
        verifyNoInteractions(ticketRepositoryMock);
    }

    @Test
    void checkException_EmailIsEmpty() {
        Ticket ticket = new Ticket("", "Issue description", TicketPriority.NORMAL);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(ticket);
        });
        assertEquals("Customer email cannot be empty", exception.getMessage());
    }

    @Test
    void checkException_whenDescriptionIsEmpty() {
        Ticket ticket = new Ticket("ex@ample.com", "", TicketPriority.NORMAL);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ticketManager.createTicket(ticket);
        });
        assertEquals("Issue description cannot be empty", exception.getMessage());
    }

    @Test
    void checkSaveTicket_EvenIfLoggingFails() {
        Ticket ticket = new Ticket("ex@ample.com", "Issue description", TicketPriority.NORMAL);
        doThrow(new RuntimeException("Logging service down")).when(logServiceMock).logTicketCreation(ticket);

        ticketManager.createTicket(ticket);

        verify(ticketRepositoryMock).save(ticket);
    }

    @Test
    void checkSaveTicket_EvenIfNotificationFails() {
        Ticket ticket = new Ticket("ex@ample.com", "Issue description", TicketPriority.NORMAL);
        doThrow(new RuntimeException("Notification service down")).when(notificationServiceMock)
                .notifyCustomer(anyString(), anyString());

        ticketManager.createTicket(ticket);

        verify(ticketRepositoryMock).save(ticket);
    }


}
