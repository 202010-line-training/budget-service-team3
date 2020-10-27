package main;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.*;

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

        double ans = getFirstMonthBudget(start) + getLastMonthBudget(end);
        LocalDate current = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
        current = current.plusMonths(1);
        while (current.getYear() != end.getYear() || current.getMonthValue() != end.getMonthValue()) {
            ans += getEntireMonth(current, allBudgets);
            current = current.plusMonths(1);
        }
        return ans;
    }

    private int getEntireMonth(LocalDate start, List<Budget> allBudgets) {
        return allBudgets.stream()
                .filter(budget -> budget.getYearMonth()
                        .equals(getYearMonthOfDate(start)))
                .findFirst().get().getAmount();
    }

    private String getYearMonthOfDate(LocalDate date) {
        return date.format(ofPattern("yyyyMM"));
    }

    private double getFirstMonthBudget(LocalDate start) {
        Budget budget = budgetMap.get(getYearMonthOfDate(start));
        if (budget == null) {
            return 0;
        } else {
            int dayOfMonth = start.getDayOfMonth();
            int dayCount = getNumberOfDay(start) - dayOfMonth + 1;
            return budget.dailyAmount() * dayCount;
        }
    }

    private double getLastMonthBudget(LocalDate end) {
        return getSingleDayBudget(budgetMap.get(getYearMonthOfDate(end))) * end.getDayOfMonth();
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
