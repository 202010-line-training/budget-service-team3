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

    YearMonth getMonth() {
        return YearMonth.parse(getYearMonth(), ofPattern("yyyyMM"));
    }

    int dailyAmount() {
        return getAmount() / getMonth().lengthOfMonth();
    }

    LocalDate lastDay() {
        return getMonth().atEndOfMonth();
    }

    LocalDate firstDay() {
        return getMonth().atDay(1);
    }

    Period createPeriod() {
        return new Period(firstDay(), lastDay());
    }
}
