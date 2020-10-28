package main;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.DAYS;

public class BudgetService {
    private final IBudgetRepo repo;
    private Map<String, Budget> budgetMap;

    public BudgetService(IBudgetRepo repo) {
        this.repo = repo;
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }
        List<Budget> allBudgets = repo.getAll();
        budgetMap = allBudgets.stream()
                .collect(Collectors.toMap(budget -> budget.getYearMonth(), budget -> budget));
        if (YearMonth.from(start).equals(YearMonth.from(end))) {
            final int intervalDays = end.getDayOfMonth() - start.getDayOfMonth() + 1;
            return getSingleDayBudget(budgetMap.get(getYearMonthOfDate(start))) * intervalDays;
        }

        double ans = 0;
//        double ans = getLastMonthBudget(end);
        LocalDate current = start;
//        LocalDate current = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
        while (YearMonth.from(current).isBefore(YearMonth.from(end.plusMonths(1)))) {
            Budget budget = budgetMap.get(getYearMonthOfDate(current));

            if (budget != null) {
                if (YearMonth.from(current).equals(YearMonth.from(start))) {
                    long dayCount = DAYS.between(start, budget.lastDay()) + 1;
                    ans += (double) budget.dailyAmount() * dayCount;
                } else if (YearMonth.from(current).equals(YearMonth.from(end))) {
                    ans += getLastMonthBudget(end);
                } else {
                    long dayCount = DAYS.between(budget.firstDay(), budget.lastDay()) + 1;
                    ans += (double) budget.dailyAmount() * dayCount;
                }
            }
            current = current.plusMonths(1);
        }
        return ans;
    }

    private double getEntireMonth(LocalDate start, List<Budget> allBudgets) {
        Budget budget = budgetMap.get(getYearMonthOfDate(start));

        if (budget == null) {
            return 0;
        } else {
            long dayCount = DAYS.between(budget.firstDay(), budget.lastDay()) + 1;
            return (double) budget.dailyAmount() * dayCount;
        }
    }

    private double getFirstMonthBudget(LocalDate start) {
        Budget budget = budgetMap.get(getYearMonthOfDate(start));

        if (budget == null) {
            return 0;
        } else {
            long dayCount = DAYS.between(start, budget.lastDay()) + 1;
            return (double) budget.dailyAmount() * dayCount;
        }
    }

    private double getLastMonthBudget(LocalDate end) {
        Budget budget = budgetMap.get(getYearMonthOfDate(end));
        if (budget == null) {
            return 0;
        } else {
            long dayCount = DAYS.between(budget.firstDay(), end) + 1;
            return (double) budget.dailyAmount() * dayCount;
        }
    }

    private double getSingleDayBudget(Budget budget) {
        if (budget == null) {
            return 0;
        } else {
            return budget.dailyAmount();
        }
    }

    private String getYearMonthOfDate(LocalDate date) {
        return date.format(ofPattern("yyyyMM"));
    }

    private int getNumberOfDay(LocalDate date) {
        YearMonth yearMonthObj = YearMonth.of(date.getYear(), date.getMonthValue());
        return yearMonthObj.lengthOfMonth();
    }
}
