package com.vcs.finstream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Transaction {

    private final String transactionId;
    private final String userId;
    private final BigDecimal amount;
    private final TransactionType type;
    private final Category category;
    private final LocalDateTime timestamp;

}
