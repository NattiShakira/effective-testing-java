// TicketManager class to handle ticket creation and interaction with services
public class TicketManager {
    private NotificationService notificationService;
    private LogService logService;
    private TicketRepository ticketRepository;

    public TicketManager(NotificationService notificationService, LogService logService, TicketRepository ticketRepository) {
        this.notificationService = notificationService;
        this.logService = logService;
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        if (ticket.getCustomerEmail() == null || ticket.getCustomerEmail().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be empty");
        }
        if (ticket.getIssueDescription() == null || ticket.getIssueDescription().isEmpty()) {
            throw new IllegalArgumentException("Issue description cannot be empty");
        }
        try {
            logService.logTicketCreation(ticket);
        } catch (Exception e) {
            System.err.println("Logging failed: " + e.getMessage()); // Handle logging failure
        }

        try {
            String message = (ticket.getPriority() == TicketPriority.URGENT) ?
                    "Thank you for your request. Your support ticket has been created and is being processed as high priority." :
                    "Thank you for your request. Your support ticket has been created and will be processed shortly.";
            notificationService.notifyCustomer(ticket.getCustomerEmail(), message);
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage()); // Handle notification failure
        }

        // Save the ticket to the database
        saveTicket(ticket);
    }

    // Method to save ticket to a database
    private void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}
