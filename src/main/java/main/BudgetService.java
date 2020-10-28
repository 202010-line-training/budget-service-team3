package main;

import java.time.LocalDate;
import java.time.YearMonth;

import static java.time.format.DateTimeFormatter.ofPattern;

public class BudgetService {
    private final IBudgetRepo repo;

    public BudgetService(IBudgetRepo repo) {
        this.repo = repo;
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }

        Period period = new Period(start, end);
        return repo.getAll()
                .stream()
                .mapToDouble(budget -> budget.getOverlappingAmount(period))
                .sum();
    }
}
