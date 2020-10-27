package main;

import java.time.LocalDate;

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
        LocalDate overlappingStart = getStart().isAfter(firstDay)
                ? getStart()
                : firstDay;
        LocalDate overlappingEnd = getEnd().isBefore(lastDay)
                ? getEnd()
                : lastDay;
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
