import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BalanceServiceTest {
    private static final BalanceService service = new BalanceService();
    private static int passed = 0;

    public static void main(String[] args) {

        List<String> members = List.of("M1", "M2", "M3");

        Expense grocery = new Expense(
                ExpenseType.GROCERY,
                "M1",
                "Grocery",
                new BigDecimal("90")
        );

        Expense wifi = new Expense(
                ExpenseType.WIFI,
                "M2",
                "WiFi",
                new BigDecimal("60")
        );

        List<Expense> expenses = List.of(grocery, wifi);

        ExpenseSplit g1 = new ExpenseSplit(
                grocery.getExpenseId(), "M1", new BigDecimal("30"));

        ExpenseSplit g2 = new ExpenseSplit(
                grocery.getExpenseId(), "M2", new BigDecimal("30"));

        ExpenseSplit g3 = new ExpenseSplit(
                grocery.getExpenseId(), "M3", new BigDecimal("30"));

        ExpenseSplit w1 = new ExpenseSplit(
                wifi.getExpenseId(), "M1", new BigDecimal("20"));

        ExpenseSplit w2 = new ExpenseSplit(
                wifi.getExpenseId(), "M2", new BigDecimal("20"));

        ExpenseSplit w3 = new ExpenseSplit(
                wifi.getExpenseId(), "M3", new BigDecimal("20"));

        List<ExpenseSplit> splits = List.of(g1, g2, g3, w1, w2, w3);

        // TEST 1: Normal balance calculation

        List<MemberBalance> balances =
                service.calculateBalance(expenses, splits, members);

        checkBalance(balances, "M1", "90", "50", "-40");
        checkBalance(balances, "M2", "60", "50", "-10");
        checkBalance(balances, "M3", "0", "50", "50");

        pass("Normal balance calculation");


        // TEST 2: No expenses

        balances = service.calculateBalance(
                List.of(), List.of(), members);

        checkBalance(balances, "M1", "0", "0", "0");
        checkBalance(balances, "M2", "0", "0", "0");
        checkBalance(balances, "M3", "0", "0", "0");

        pass("No expenses");


        // TEST 3: Expense shared by selected members

        balances = service.calculateBalance(
                List.of(grocery),
                List.of(
                        new ExpenseSplit(grocery.getExpenseId(),
                                "M1", new BigDecimal("45")),
                        new ExpenseSplit(grocery.getExpenseId(),
                                "M2", new BigDecimal("45"))
                ),
                members
        );

        checkBalance(balances, "M1", "90", "45", "-45");
        checkBalance(balances, "M2", "0", "45", "45");
        checkBalance(balances, "M3", "0", "0", "0");

        pass("Selected members splitting");


        // TEST 4: Net balances must sum to zero

        balances = service.calculateBalance(expenses, splits, members);

        BigDecimal totalNetBalance = BigDecimal.ZERO;

        for (MemberBalance balance : balances) {
            totalNetBalance =
                    totalNetBalance.add(balance.getNetBalance());
        }

        checkAmount(totalNetBalance, "0", "Total net balance");

        pass("Net balances sum to zero");


        // TEST 5: Null expenses list

        expectException("Null expenses list", () ->
                service.calculateBalance(null, splits, members));


        // TEST 6: Null splits list

        expectException("Null splits list", () ->
                service.calculateBalance(expenses, null, members));


        // TEST 7: Null members list

        expectException("Null members list", () ->
                service.calculateBalance(expenses, splits, null));


        // TEST 8: Empty members list

        expectException("Empty members list", () ->
                service.calculateBalance(expenses, splits, List.of()));


        // TEST 9: Duplicate member IDs

        expectException("Duplicate member IDs", () ->
                service.calculateBalance(
                        expenses, splits, List.of("M1", "M1", "M2")));


        // TEST 10: Blank member ID

        expectException("Blank member ID", () ->
                service.calculateBalance(
                        expenses, splits, List.of("M1", " ", "M3")));


        // TEST 11: Null member ID

        expectException("Null member ID", () ->
                service.calculateBalance(
                        expenses, splits,
                        Arrays.asList("M1", null, "M3")));


        // TEST 12: Null expense object

        expectException("Null expense object", () ->
                service.calculateBalance(
                        Arrays.asList(grocery, null), splits, members));


        // TEST 13: Null expense split object

        expectException("Null expense split object", () ->
                service.calculateBalance(
                        expenses, Arrays.asList(g1, null), members));


        // TEST 14: Unknown expense payer

        Expense invalidPayer = new Expense(
                ExpenseType.GROCERY,
                "M99",
                "Invalid payer",
                new BigDecimal("100")
        );

        expectException("Unknown expense payer", () ->
                service.calculateBalance(
                        List.of(invalidPayer), List.of(), members));


        // TEST 15: Unknown split member

        ExpenseSplit invalidMember = new ExpenseSplit(
                grocery.getExpenseId(),
                "M99",
                new BigDecimal("90")
        );

        expectException("Unknown split member", () ->
                service.calculateBalance(
                        List.of(grocery),
                        List.of(invalidMember),
                        members));


        // TEST 16: Duplicate expense

        expectException("Duplicate expense", () ->
                service.calculateBalance(
                        List.of(grocery, grocery), splits, members));


        // TEST 17: Unknown expense ID

        ExpenseSplit invalidExpense = new ExpenseSplit(
                "INVALID_EXPENSE_ID",
                "M1",
                new BigDecimal("90")
        );

        expectException("Unknown expense ID", () ->
                service.calculateBalance(
                        List.of(grocery),
                        List.of(invalidExpense),
                        members));


        // TEST 18: Duplicate expense split

        expectException("Duplicate expense split", () ->
                service.calculateBalance(
                        List.of(grocery),
                        List.of(g1, g1, g2),
                        members));


        // TEST 19: Missing expense splits

        expectException("Missing expense splits", () ->
                service.calculateBalance(
                        List.of(grocery), List.of(), members));


        // TEST 20: Incorrect individual expense totals
        // Grocery: 90 expected, 80 distributed
        // WiFi: 60 expected, 70 distributed
        // Overall total is correct, individual totals are wrong.

        List<ExpenseSplit> incorrectSplits = List.of(
                new ExpenseSplit(grocery.getExpenseId(),
                        "M1", new BigDecimal("40")),
                new ExpenseSplit(grocery.getExpenseId(),
                        "M2", new BigDecimal("40")),
                new ExpenseSplit(wifi.getExpenseId(),
                        "M1", new BigDecimal("35")),
                new ExpenseSplit(wifi.getExpenseId(),
                        "M2", new BigDecimal("35"))
        );

        expectException("Incorrect individual expense totals", () ->
                service.calculateBalance(
                        expenses, incorrectSplits, members));


        System.out.println("\n------------------------------");
        System.out.println("Total tests passed: " + passed + "/20");
        System.out.println("All BalanceService tests passed!");
    }


    private static void checkBalance(
            List<MemberBalance> balances,
            String memberId,
            String expectedPaid,
            String expectedShare,
            String expectedNet) {

        for (MemberBalance balance : balances) {

            if (balance.getMemberId().equals(memberId)) {

                checkAmount(balance.getTotalPaid(),
                        expectedPaid, memberId + " total paid");

                checkAmount(balance.getTotalShare(),
                        expectedShare, memberId + " total share");

                checkAmount(balance.getNetBalance(),
                        expectedNet, memberId + " net balance");

                return;
            }
        }

        throw new AssertionError("Member not found: " + memberId);
    }


    private static void checkAmount(
            BigDecimal actual,
            String expected,
            String message) {

        if (actual.compareTo(new BigDecimal(expected)) != 0) {

            throw new AssertionError(
                    message + " | Expected: " + expected
                            + " | Actual: " + actual
            );
        }
    }


    private static void expectException(String testName, Runnable action) {

        try {
            action.run();

            throw new AssertionError(
                    testName + " FAILED: Expected IllegalArgumentException"
            );

        } catch (IllegalArgumentException e) {
            pass(testName);
        }
    }


    private static void pass(String testName) {
        passed++;
        System.out.println("PASSED: " + testName);
    }
}
