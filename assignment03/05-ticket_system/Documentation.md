The `TicketManager` class manages customer support tickets for a software company. It integrates with third-party services for notifications (`NotificationService`) and logging (`LogService`). 
## External dependencies and test doubles
The `TicketManager` class manages customer support tickets for a software company. The class interacts with several external components:
- `LogService`: Used to log ticket creation events.
- `NotificationService` : Used for notifying the customer when a ticket is created.
-  `TicketRepository`: Manages database interactions for saving ticket details.

`LogService` and `NotificationService` should be mocked because they involve external interactions that are outside the core functionality of ticket creation, such as logging and sending notifications, which can involve external systems or services.
`TicketRepository` should also be mocked to isolate the database operations from unit tests, ensuring that the tests do not depend on database access and are not affected by the database state.

## Unit test
Various implementation to the `TicketManager` class were made to properly handle a variety of input conditions and maintain the integrity of the system's operations:
- Input Validation: Introduced preconditions to the `createTicket` method to handle various types of invalid inputs effectively:  
  Null Ticket Check: Ensured that the method throws an IllegalArgumentException if the input ticket is null.   
  Empty Customer Email: Added a check to throw an IllegalArgumentException if the customer email is empty or null.   
  Empty Issue Description: Incorporated a validation to throw an IllegalArgumentException if the issue description is empty.
- Logging Before Notification: Used logical sequencing in the code to make sure logging the ticket creation happens before notifying the customer. 
- Ticket Priority Feedback: Enhanced the method to handle priority message differently for urgent or normal tickets.

Various tests were implemented:
- `checkCreateTicket`: Ensures complete ticket processing workflow including logging, notification, and saving.
- `checkNormalPriority_NotifyNormally` and `checkUrgentPriority_NotifyUrgently`: Validate that the notification messages are tailored according to the ticket's priority.
- `checkLogBeforeNotifying`: Confirms the correct sequence of operations, with logging always preceding notification.
- `checkException_NullTicket` and `checkException_TicketIsNull`: Tests the response to null ticket inputs to ensure the system does not process such entries.
- `checkException_EmailIsEmpty`: Assesses handling of tickets with invalid or empty email addresses.
- `checkException_whenDescriptionIsEmpty`: Checks the system's response to tickets lacking a proper issue description.


## Disadvantages of test doubles

- The use of mocks for `LogService`, `NotificationService`, and `TicketRepository` isolate the unit tests for `TicketManager`. This could lead to unforeseen situations, where the tests pass under test conditions but fail in production due to issues like network failures or service downtime.
- Failure to consistently update mocks can lead to tests that pass despite having bugs or issues that would manifest in the live environment, potentially undermining the reliability of the software.
  Whenever the behavior or interface of a service like `LogService` or `NotificationService` changes, the corresponding mocks in the `TicketManager` tests also need to be updated.
- By relying only on mocks, tests for `TicketManager` may not effectively cover the integration points between the `TicketManager` and its services. This approach can overlook how these components interact under actual operating conditions.


## Handling of failures in notification and logging
`TicketManager` was implemented to handle failures in notification and logging:
- The class is equipped with try-catch blocks around the calls to the logging and notification services. This design pattern prevents exceptions in these services from terminating the ticket creation process.
- When a service fails, the error is logged using `System.err.println()`, providing visibility into the failure without disrupting the flow of execution.
- After catching exceptions from the `LogService` or `NotificationService`, the process continues saving the ticket, ensuring that every ticket is accounted for, regardless of the status of external services.

Two tests were designed to simulate and verify the behaviour of the `TicketManager` when service failures occur:
- `checkSaveTicket_EvenIfLoggingFails`: This test simulates a failure in the `LogService`. It verifies that the ticket is still saved to the repository even if an exception is thrown during the logging process.
- `checkSaveTicket_EvenIfNotificationFails`: This test triggers a failure in the `NotificationService` to ensure that ticket saving proceeds even if there are failures in the notification process.