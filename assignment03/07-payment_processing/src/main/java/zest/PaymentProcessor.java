package zest;

public class PaymentProcessor {
    private EventPublisher eventPublisher;
    private TransactionService transactionService;
    private FraudDetectionService fraudDetectionService;
    private Transaction lastProcessedTransaction;
    public PaymentProcessor(EventPublisher publisher, TransactionService service, FraudDetectionService fraudService) {
        this.eventPublisher = publisher;
        this.transactionService = service;
        this.fraudDetectionService = fraudService;
    }

    public void processPayment(Transaction transaction) {
        if (fraudDetectionService.evaluateTransaction(transaction)) {
            transactionService.processTransaction(transaction);
            eventPublisher.publishTransactionComplete(transaction);
            lastProcessedTransaction = transaction;
        }
    }

    public Transaction getLastProcessedTransaction() {
        return lastProcessedTransaction;
    }
}
