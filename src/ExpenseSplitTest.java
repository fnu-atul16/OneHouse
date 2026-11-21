import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ExpenseSplitTest {
    public static void main(String[] args) {

        ExpenseSplitService service = new ExpenseSplitService();

        // TEST 1: $100 split among 3 members
        Expense expense1 = new Expense(
                ExpenseType.GROCERY,
                "Atul",
                "Groceries",
                new BigDecimal("100.00"),
                LocalDate.now()
        );

        List<ExpenseSplit> splits1 = service.splitEqually(
                expense1,
                Arrays.asList("Atul", "Rahul", "Aman")
        );

        checkSplits(
                "Test 1: $100 / 3",
                splits1,
                "33.34", "33.33", "33.33"
        );

        // TEST 2: $100 split among 6 members
        Expense expense2 = new Expense(
                ExpenseType.ELECTRICITY,
                "Atul",
                "Electricity bill",
                new BigDecimal("100.00"),
                LocalDate.now()
        );

        List<ExpenseSplit> splits2 = service.splitEqually(
                expense2,
                Arrays.asList("A", "B", "C", "D", "E", "F")
        );

        checkSplits(
                "Test 2: $100 / 6",
                splits2,
                "16.67", "16.67", "16.67",
                "16.67", "16.66", "16.66"
        );

        // TEST 3: $0.01 split among 3 members
        Expense expense3 = new Expense(
                ExpenseType.OTHER,
                "Atul",
                "Small expense",
                new BigDecimal("0.01"),
                LocalDate.now()
        );

        List<ExpenseSplit> splits3 = service.splitEqually(
                expense3,
                Arrays.asList("Atul", "Rahul", "Aman")
        );

        checkSplits(
                "Test 3: $0.01 / 3",
                splits3,
                "0.01", "0.00", "0.00"
        );

        // TEST 4: Duplicate member IDs
        expectException("Test 4: Duplicate IDs", () ->
                service.splitEqually(
                        expense1,
                        Arrays.asList("Atul", "Rahul", "Atul")
                )
        );

        // TEST 5: Empty member list
        expectException("Test 5: Empty list", () ->
                service.splitEqually(expense1, List.of())
        );

        // TEST 6: Null member list
        expectException("Test 6: Null list", () ->
                service.splitEqually(expense1, null)
        );

        // TEST 7: Blank member ID
        expectException("Test 7: Blank ID", () ->
                service.splitEqually(
                        expense1,
                        Arrays.asList("Atul", " ", "Aman")
                )
        );

        // TEST 8: Null member ID
        expectException("Test 8: Null ID", () ->
                service.splitEqually(
                        expense1,
                        Arrays.asList("Atul", null, "Aman")
                )
        );

        // TEST 9: Null expense
        expectException("Test 9: Null expense", () ->
                service.splitEqually(
                        null,
                        Arrays.asList("Atul", "Rahul")
                )
        );

        // TEST 10: Invalid currency precision
        expectException("Test 10: Invalid precision", () ->
                new Expense(
                        ExpenseType.GROCERY,
                        "Atul",
                        "Invalid amount",
                        new BigDecimal("10.555"),
                        LocalDate.now()
                )
        );

        System.out.println("\nAll ExpenseSplit tests passed!");
    }

    private static void checkSplits(
            String testName,
            List<ExpenseSplit> splits,
            String... expectedAmounts
    ) {
        if (splits.size() != expectedAmounts.length) {
            throw new AssertionError(
                    testName + ": Incorrect number of splits"
            );
        }

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal expectedTotal = BigDecimal.ZERO;

        for (int i = 0; i < splits.size(); i++) {
            BigDecimal actual = splits.get(i).getShareAmount();
            BigDecimal expected = new BigDecimal(expectedAmounts[i]);

            if (actual.compareTo(expected) != 0) {
                throw new AssertionError(
                        testName + ": Member " + (i + 1)
                                + " expected $" + expected
                                + " but got $" + actual
                );
            }

            total = total.add(actual);
            expectedTotal = expectedTotal.add(expected);
        }

        if (total.compareTo(expectedTotal) != 0) {
            throw new AssertionError(
                    testName + ": Total mismatch"
            );
        }

        System.out.println(testName + " PASSED");
    }

    private static void expectException(
            String testName,
            Runnable action
    ) {
        try {
            action.run();
            throw new AssertionError(
                    testName + ": Expected IllegalArgumentException"
            );
        } catch (IllegalArgumentException expected) {
            System.out.println(testName + " PASSED");
        }
    }
}
