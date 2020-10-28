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

        Period period = new Period(start, end);
        return repo.getAll()
                .stream()
                .mapToDouble(budget -> budget.getOverlappingAmount(period))
                .sum();
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
