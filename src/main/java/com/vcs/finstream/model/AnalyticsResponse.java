package com.vcs.finstream.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Getter
public class AnalyticsResponse {

    //Financial Overview Insights
    private final BigDecimal totalIncome;
    private final BigDecimal totalExpense;
    private final BigDecimal netBalance;
    private final long totalTransactions;
    private final BigDecimal averageTransaction;

    //Spending Insights
    private final Map<Category, BigDecimal> categoryBreakdown;
    private final Category topSpendingCategory;
    private final Transaction highestTransaction;
    private final Map<String, BigDecimal> monthlyExpenseTrend;

}
