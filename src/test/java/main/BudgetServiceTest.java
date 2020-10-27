package main;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BudgetServiceTest {

    IBudgetRepo repo;
    BudgetService budgetService;

    @Before
    public void setUp() throws Exception {
        repo = Mockito.mock(IBudgetRepo.class);
        budgetService = new BudgetService(repo);
    }

    @Test
    public void test_one_month() {
        when(repo.getAll()).thenReturn(Collections.singletonList(new Budget("202009", 300)));
        assertThat(budgetService.query(LocalDate.of(2020, 9, 1),
                LocalDate.of(2020, 9, 30))).isEqualTo(300.0);
    }

    @Test
    public void test_illegal_input() {
        when(repo.getAll()).thenReturn(Collections.emptyList());
        assertThat(budgetService.query(LocalDate.of(2020, 10, 1),
                LocalDate.of(2020, 9, 30))).isEqualTo(0.0);
    }

    @Test
    public void test_two_entire_months() {
        when(repo.getAll()).thenReturn(Arrays.asList(new Budget("202009", 300),
                new Budget("202010", 3100)));
        assertThat(budgetService.query(LocalDate.of(2020, 9, 1),
                LocalDate.of(2020, 10, 31))).isEqualTo(3400.0);
    }

    @Test
    public void test_partial_months() {
        when(repo.getAll()).thenReturn(Arrays.asList(new Budget("202009", 300),
                new Budget("202010", 3100)));
        assertThat(budgetService.query(LocalDate.of(2020, 9, 30),
                LocalDate.of(2020, 10, 3))).isEqualTo(310.0);
    }

    @Test
    public void test_one_day() {
        when(repo.getAll()).thenReturn(Collections.singletonList(new Budget("202009", 300)));
        assertThat(budgetService.query(LocalDate.of(2020, 9, 1),
                LocalDate.of(2020, 9, 1))).isEqualTo(10.0);
    }

    @Test
    public void test_incomplete_month() {
        when(repo.getAll()).thenReturn(Collections.singletonList(new Budget("202009", 300)));
        assertThat(budgetService.query(LocalDate.of(2020, 9, 1),
                LocalDate.of(2020, 9, 23))).isEqualTo(230.0);
    }

    @Test
    public void test_empty_budget_between_period() {
        when(repo.getAll()).thenReturn(Arrays.asList(
                new Budget("202008", 310),
                new Budget("202009", 0),
                new Budget("202010", 3100)));

        assertThat(budgetService.query(LocalDate.of(2020, 8, 30),
                LocalDate.of(2020, 10, 3))).isEqualTo(320.0);
    }
}
