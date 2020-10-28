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
        Period another = new Period(budget.firstDay(), budget.lastDay());

        LocalDate overlappingStart = start.isAfter(another.start)
                ? start
                : another.start;
        LocalDate overlappingEnd = end.isBefore(another.end)
                ? end
                : another.end;
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
