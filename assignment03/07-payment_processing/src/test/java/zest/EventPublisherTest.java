package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventPublisherTest {
    private EventPublisher eventPublisher;
    private AuditService mockAuditService1;
    private AuditService mockAuditService2;
    private Transaction testTransaction;

    @BeforeEach
    public void setUp() {
        eventPublisher = new EventPublisher();
        mockAuditService1 = Mockito.mock(AuditService.class);
        mockAuditService2 = Mockito.mock(AuditService.class);
        testTransaction = new Transaction("txn123", 100.0, "COMPLETED");

        eventPublisher.subscribe(mockAuditService1);
        eventPublisher.subscribe(mockAuditService2);
    }

    @Test
    public void testNumberOfInvocations() {
        eventPublisher.publishTransactionComplete(testTransaction);

        verify(mockAuditService1, times(1)).onTransactionComplete(testTransaction);
        verify(mockAuditService2, times(1)).onTransactionComplete(testTransaction);
    }

    @Test
    public void testContentOfInvocationsUsingArgumentCaptor() {
        eventPublisher.publishTransactionComplete(testTransaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(mockAuditService1).onTransactionComplete(transactionCaptor.capture());
        Transaction capturedTransaction1 = transactionCaptor.getValue();
        assertEquals("txn123", capturedTransaction1.getId());
        assertEquals(100.0, capturedTransaction1.getAmount());
        assertEquals("COMPLETED", capturedTransaction1.getStatus());

        verify(mockAuditService2).onTransactionComplete(transactionCaptor.capture());
        Transaction capturedTransaction2 = transactionCaptor.getAllValues().get(1);
        assertEquals("txn123", capturedTransaction2.getId());
        assertEquals(100.0, capturedTransaction2.getAmount());
        assertEquals("COMPLETED", capturedTransaction2.getStatus());
    }

    @Test
    public void testContentOfInvocationsUsingIncreasedObservability() {
        EventPublisher mockEventPublisher = Mockito.mock(EventPublisher.class);
        TransactionService mockTransactionService = Mockito.mock(TransactionService.class);
        FraudDetectionService mockFraudDetectionService = Mockito.mock(FraudDetectionService.class);
        PaymentProcessor paymentProcessor = new PaymentProcessor(mockEventPublisher, mockTransactionService, mockFraudDetectionService);

        when(mockFraudDetectionService.evaluateTransaction(testTransaction)).thenReturn(true);
        paymentProcessor.processPayment(testTransaction);

        verify(mockTransactionService, times(1)).processTransaction(testTransaction);
        verify(mockEventPublisher, times(1)).publishTransactionComplete(testTransaction);
        assertEquals(testTransaction, paymentProcessor.getLastProcessedTransaction());
    }

    @Test
    public void testTransactionNotProcessedWhenFraudDetected() {
        EventPublisher mockEventPublisher = Mockito.mock(EventPublisher.class);
        TransactionService mockTransactionService = Mockito.mock(TransactionService.class);
        FraudDetectionService mockFraudDetectionService = Mockito.mock(FraudDetectionService.class);
        PaymentProcessor paymentProcessor = new PaymentProcessor(mockEventPublisher, mockTransactionService, mockFraudDetectionService);

        when(mockFraudDetectionService.evaluateTransaction(testTransaction)).thenReturn(false);
        paymentProcessor.processPayment(testTransaction);

        verify(mockTransactionService, never()).processTransaction(testTransaction);
        verify(mockEventPublisher, never()).publishTransactionComplete(testTransaction);
        assertEquals(null, paymentProcessor.getLastProcessedTransaction());
    }
}
