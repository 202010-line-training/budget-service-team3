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

    public double getOverlappingAmount(Period period) {
        return (double) dailyAmount() * period.getOverlappingDays(createPeriod());
    }

    private YearMonth getMonth() {
        return YearMonth.parse(yearMonth, ofPattern("yyyyMM"));
    }

    private int dailyAmount() {
        return amount / getMonth().lengthOfMonth();
    }

    private LocalDate lastDay() {
        return getMonth().atEndOfMonth();
    }

    private LocalDate firstDay() {
        return getMonth().atDay(1);
    }

    private Period createPeriod() {
        return new Period(firstDay(), lastDay());
    }
}
