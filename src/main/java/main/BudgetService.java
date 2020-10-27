package main;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (start.getYear() == end.getYear() && start.getMonthValue() == end.getMonthValue()) {
            if (start.getDayOfMonth() == end.getDayOfMonth()) {
                return getSingleDayBudget(start);
            }
            return getEntireMonth(start, allBudgets);
        }

        double ans = getFirstMonthBudget(start) + getLastMonthBudget(end);
        LocalDate current = LocalDate.of(start.getYear(), start.getMonthValue(), 1);
        current = current.plusMonths(1);
        while (true) {
            if (current.getYear() == end.getYear() && current.getMonthValue() == end.getMonthValue()) {
                break;
            }
            ans += getEntireMonth(current, allBudgets);
            current.plusMonths(1);
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
        return date.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private double getFirstMonthBudget(LocalDate start) {
        int dayOfMonth = start.getDayOfMonth();
        int dayCount = getNumberOfDay(start) - dayOfMonth + 1;
        return getSingleDayBudget(start) * dayCount;
    }

    private double getLastMonthBudget(LocalDate end) {
        return getSingleDayBudget(end) * end.getDayOfMonth();
    }

    private double getSingleDayBudget(LocalDate date) {
        int budgetOfMonth = budgetMap.get(getYearMonthOfDate(date)) == null ?
                            0 : budgetMap.get(getYearMonthOfDate(date)).getAmount();
        int numberOfDay = getNumberOfDay(date);
        return budgetOfMonth/numberOfDay;
    }

    private int getNumberOfDay(LocalDate date) {
        YearMonth yearMonthObj = YearMonth.of(date.getYear(), date.getMonthValue());
        return yearMonthObj.lengthOfMonth();
    }
}
