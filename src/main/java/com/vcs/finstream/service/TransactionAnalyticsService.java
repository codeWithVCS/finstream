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
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //TOTAL EXPENSE
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //NET BALANCE
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        //TOTAL TRANSACTIONS
        long totalTransactions = transactions.size();

        //AVERAGE TRANSACTION
        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageTransaction = totalTransactions == 0
                ? BigDecimal.ZERO
                : totalAmount.divide(
                BigDecimal.valueOf(totalTransactions),
                2,
                RoundingMode.HALF_UP
        );

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

        //TOP SPENDING CATEGORY
        Optional<Map.Entry<Category, BigDecimal>> topCategoryEntry =
                categoryBreakdown.entrySet().stream()
                        .max(Map.Entry.comparingByValue());

        Category topSpendingCategory =
                topCategoryEntry.map(Map.Entry::getKey).orElse(null);

        //HIGHEST TRANSACTION
        Transaction highestTransaction = transactions.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(null);

        //MONTHLY EXPENSE TREND (DEBIT ONLY)
        Map<String, BigDecimal> monthlyExpenseTrend = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEBIT)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getTimestamp()).toString(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        return new AnalyticsResponse(
                totalIncome,
                totalExpense,
                netBalance,
                totalTransactions,
                averageTransaction,
                categoryBreakdown,
                topSpendingCategory,
                highestTransaction,
                monthlyExpenseTrend
        );
    }
}