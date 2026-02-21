package com.vcs.finstream.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final String transactionId;
    private final String userId;
    private final BigDecimal amount;
    private final TransactionType type;
    private final Category category;
    private final LocalDateTime timestamp;

    public Transaction(
            String transactionId,
            String userId,
            BigDecimal amount,
            TransactionType type,
            Category category,
            LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {return transactionId;}
    public String getUserId() {return userId;}
    public BigDecimal getAmount() {return amount;}
    public TransactionType getType() {return type;}
    public Category getCategory() {return category;}
    public LocalDateTime getTimestamp() {return timestamp;}

}
