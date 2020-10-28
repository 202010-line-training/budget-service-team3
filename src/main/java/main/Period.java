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
        LocalDate overlappingStart = start.isAfter(budget.firstDay())
                ? start
                : budget.firstDay();
        LocalDate overlappingEnd = end.isBefore(budget.lastDay())
                ? end
                : budget.lastDay();
        if (budget.getMonth().equals(YearMonth.from(getStart()))) {
//            overlappingStart = getStart();
//            overlappingEnd = budget.lastDay();
        } else if (budget.getMonth().equals(YearMonth.from(getEnd()))) {
//            overlappingStart = budget.firstDay();
//            overlappingEnd = getEnd();
        } else {
//            overlappingStart = budget.firstDay();
//            overlappingEnd = budget.lastDay();
        }
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
