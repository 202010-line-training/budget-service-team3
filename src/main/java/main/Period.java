package main;

import java.time.LocalDate;
import java.time.YearMonth;

import static java.time.temporal.ChronoUnit.DAYS;

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    long getOverlappingDays(Budget budget) {
        LocalDate firstDay = budget.firstDay();
        LocalDate lastDay = budget.lastDay();
        LocalDate overlappingStart = start.isAfter(firstDay)
                ? start
                : firstDay;
        LocalDate overlappingEnd = end.isBefore(lastDay)
                ? end
                : lastDay;
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
