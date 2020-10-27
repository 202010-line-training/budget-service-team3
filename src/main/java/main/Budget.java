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

    public YearMonth getMonth() {
        return YearMonth.parse(getYearMonth(), ofPattern("yyyyMM"));
    }

    double dailyAmount() {
        YearMonth yearMonthOfBudget = getMonth();
        return (double) getAmount() / yearMonthOfBudget.lengthOfMonth();
    }

    LocalDate firstDay() {
        return getMonth().atDay(1);
    }

    LocalDate lastDay() {
        return getMonth().atEndOfMonth();
    }

    Period createPeriod() {
        return new Period(firstDay(), lastDay());
    }

    double getOverlappingAmount(Period period) {
        return dailyAmount() * period.getOverlappingDays(createPeriod());
    }
}
