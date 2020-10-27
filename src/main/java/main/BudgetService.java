package main;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.*;
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
            Budget budget = budgetMap.get(getYearMonthOfDate(start));
            if (budget == null) {
                return 0;
            } else {
                final int intervalDays = end.getDayOfMonth() - start.getDayOfMonth() + 1;
                return budget.dailyAmount() * intervalDays;
            }
        }

        double ans = 0;
        LocalDate current = start;
        while (YearMonth.from(current).isBefore(YearMonth.from(end.plusMonths(1)))) {
            Budget budget = budgetMap.get(getYearMonthOfDate(current));
            if (budget != null) {
                long overlappingDays;
                LocalDate overlappingStart;
                LocalDate overlappingEnd;
                YearMonth currentYearMonth = YearMonth.from(current);
                if (currentYearMonth.equals(YearMonth.from(start))) {
                    overlappingStart = start;
                    overlappingEnd = budget.lastDay();
                } else if (currentYearMonth.equals(YearMonth.from(end))) {
                    overlappingStart = budget.firstDay();
                    overlappingEnd = end;
                } else {
                    overlappingStart = budget.firstDay();
                    overlappingEnd = budget.lastDay();
                }
                overlappingDays = DAYS.between(overlappingStart, overlappingEnd) + 1;
                ans += budget.dailyAmount() * overlappingDays;
            }
            current = current.plusMonths(1);
        }
        return ans;
    }

    private double getEntireMonth(Budget budget) {
        if (budget == null) {
            return 0;
        } else {
            long dayCount = DAYS.between(budget.firstDay(), budget.lastDay());
            return budget.dailyAmount() * dayCount;
        }
    }

    private String getYearMonthOfDate(LocalDate date) {
        return date.format(ofPattern("yyyyMM"));
    }

    private double getFirstMonthBudget(LocalDate start) {
        Budget budget = budgetMap.get(getYearMonthOfDate(start));
        if (budget == null) {
            return 0;
        } else {
            long dayCount = DAYS.between(start, budget.lastDay()) + 1;
            return budget.dailyAmount() * dayCount;
        }
    }

    private double getLastMonthBudget(LocalDate end) {
        Budget budget = budgetMap.get(getYearMonthOfDate(end));
        if (budget == null) {
            return 0;
        } else {
            long dayCount = DAYS.between(budget.firstDay(), end) + 1;
            return budget.dailyAmount() * dayCount;
        }
    }

    private double getSingleDayBudget(Budget budget) {
        if (budget == null) {
            return 0;
        } else {
            return budget.dailyAmount();
        }
    }

    private int getNumberOfDay(LocalDate date) {
        YearMonth yearMonthObj = YearMonth.of(date.getYear(), date.getMonthValue());
        return yearMonthObj.lengthOfMonth();
    }
}
