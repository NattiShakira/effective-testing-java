package zest;

public class Transaction {
    private String id;
    private double amount;
    private String status;

    public Transaction(String id, double amount, String status) {
        this.id = id;
        this.amount = amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
