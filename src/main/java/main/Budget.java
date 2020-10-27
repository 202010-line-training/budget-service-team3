package main;

import java.time.LocalDate;
import java.time.YearMonth;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Budget {
    private String yearMonth;
    private int amount;

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    double dailyAmount() {
        YearMonth yearMonthOfBudget = getMonth();
        return (double) getAmount() / yearMonthOfBudget.lengthOfMonth();
    }

    public YearMonth getMonth() {
        return YearMonth.parse(getYearMonth(), ofPattern("yyyyMM"));
    }

    LocalDate firstDay() {
        return getMonth().atDay(1);
    }
}
