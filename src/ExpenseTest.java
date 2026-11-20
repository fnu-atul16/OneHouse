
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseTest {

    public static void main(String[] args) {

        String memberId = "ATUL123";
        ExpenseService service = new ExpenseService();

        Expense e1 = new Expense(
                ExpenseType.GROCERY,
                memberId,
                "October groceries",
                new BigDecimal("100"),
                LocalDate.of(2026, 10, 15)
        );

        Expense e2 = new Expense(
                ExpenseType.WIFI,
                memberId,
                "November WiFi",
                new BigDecimal("60"),
                LocalDate.of(2026, 11, 5)
        );

        Expense e3 = new Expense(
                ExpenseType.ELECTRICITY,
                memberId,
                "November electricity",
                new BigDecimal("90"),
                LocalDate.of(2026, 11, 12)
        );

        service.addExpense(e1);
        service.addExpense(e2);
        service.addExpense(e3);

        BigDecimal november =
                service.getMonthlyPaidByMember(memberId, 2026, 11);

        BigDecimal october =
                service.getMonthlyPaidByMember(memberId, 2026, 10);

        BigDecimal december =
                service.getMonthlyPaidByMember(memberId, 2026, 12);

        System.out.println("November: $" + november);
        System.out.println("October: $" + october);
        System.out.println("December: $" + december);

        if (november.compareTo(new BigDecimal("150")) != 0) {
            throw new AssertionError("November total incorrect");
        }

        if (october.compareTo(new BigDecimal("100")) != 0) {
            throw new AssertionError("October total incorrect");
        }

        if (december.compareTo(BigDecimal.ZERO) != 0) {
            throw new AssertionError("December total incorrect");
        }

        // Test constructor overloading
        Expense e4 = new Expense(
                ExpenseType.GAS,
                memberId,
                "Gas bill",
                new BigDecimal("40")
        );

        if (!e4.getExpenseDate().equals(LocalDate.now())) {
            throw new AssertionError("Automatic date incorrect");
        }

        System.out.println("All Expense tests passed!");
    }
}