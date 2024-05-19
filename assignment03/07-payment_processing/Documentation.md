# CatFactRetriever testing documentation by Markus Niemack

# 1. Refactoring Done
I had to add a Transaction class, otherwise the code was broken. The code for the interface can be found in the Transaction.java file. Then later,
to increase observability of the 'PaymentProcessor' class, I added a variable 
```java 
private Transaction lastProcessedTransaction
```
and added some code to initiate it with the last processed transaction, and a getter for the value:

```java
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
```

# 2. Tests and Their Cases
1. testNumberOfInvocations: Verifies that onTransactionComplete is called exactly once for each subscribed AuditService.
2. testContentOfInvocationsUsingArgumentCaptor: Uses ArgumentCaptor to capture and verify the contents of the transaction details passed to onTransactionComplete.
3. testContentOfInvocationsUsingIncreasedObservability: Verifies the content of the last processed transaction by increasing the observability of the PaymentProcessor class.
4. testTransactionNotProcessedWhenFraudDetected: Verifies that when fraud is detected (evaluateTransaction returns false), the transaction is not processed or published.

This achieves 100% line and branch coverage of all the classes and interfaces.
# 3. Comparison

#### Using ArgumentCaptor:
- Advantages:
- - Directly captures and verifies arguments passed to method.
- - Control and inspection of method invocations.
- Disadvantages:
- - Complex.
- - Harder to read/maintain with more captures.


#### Increasing Observability:
- Advantages:
- - Simplifies verification process by giving direct access to data
- - Code easier to debug
- Disadvantages:
- - Additional methods needed, and potentially more mutability.
- - May expose internal details that should be encapsulated.
