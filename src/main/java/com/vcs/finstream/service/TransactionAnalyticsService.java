package com.vcs.finstream.service;

import com.vcs.finstream.model.AnalyticsResponse;
import com.vcs.finstream.model.Category;
import com.vcs.finstream.model.Transaction;
import com.vcs.finstream.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionAnalyticsService {

    public AnalyticsResponse analyze(List<Transaction> transactions) {

        if (transactions == null || transactions.isEmpty()) {
            throw new RuntimeException("Transaction list is empty");
        }

        //TOTAL INCOME
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.CREDIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        //TOTAL EXPENSE
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        //NET BALANCE
        BigDecimal netBalance = totalIncome.subtract(totalExpense)
                .setScale(2, RoundingMode.HALF_UP);

        //TOTAL TRANSACTIONS
        long totalTransactions = transactions.size();

        //AVERAGE TRANSACTION
        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageTransaction = totalTransactions == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : totalAmount
                .divide(BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP);

        //CATEGORY BREAKDOWN (DEBIT ONLY)
        Map<Category, BigDecimal> categoryBreakdown = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        // Sort category breakdown descending & normalize scale
        Map<Category, BigDecimal> sortedCategoryBreakdown =
                categoryBreakdown.entrySet()
                        .stream()
                        .sorted(Map.Entry.<Category, BigDecimal>comparingByValue().reversed())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().setScale(2, RoundingMode.HALF_UP),
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));

        //TOP SPENDING CATEGORY
        Category topSpendingCategory = sortedCategoryBreakdown.keySet()
                .stream()
                .findFirst()
                .orElse(null);

        //HIGHEST EXPENSE TRANSACTION (DEBIT ONLY)
        Transaction highestTransaction = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(null);

        //MONTHLY EXPENSE TREND (DEBIT ONLY)
        Map<String, BigDecimal> monthlyExpenseTrend = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getTimestamp()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // chronological order
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().setScale(2, RoundingMode.HALF_UP),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        return new AnalyticsResponse(
                totalIncome,
                totalExpense,
                netBalance,
                totalTransactions,
                averageTransaction,
                sortedCategoryBreakdown,
                topSpendingCategory,
                highestTransaction,
                monthlyExpenseTrend
        );
    }
}