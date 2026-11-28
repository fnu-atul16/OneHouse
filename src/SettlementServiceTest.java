import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettlementServiceTest {
    public static void main(String[] args) {

        SettlementService service = new SettlementService();

        // Test 1: One debtor, one creditor
        checkSettlements(
                "One debtor and one creditor",
                service,
                List.of(
                        member("M1", "60"),
                        member("M2", "-60")
                ),
                1
        );

        // Test 2: One debtor, multiple creditors
        checkSettlements(
                "One debtor and multiple creditors",
                service,
                List.of(
                        member("M1", "100"),
                        member("M2", "-30"),
                        member("M3", "-70")
                ),
                2
        );

        // Test 3: Multiple debtors, one creditor
        checkSettlements(
                "Multiple debtors and one creditor",
                service,
                List.of(
                        member("M1", "40"),
                        member("M2", "60"),
                        member("M3", "-100")
                ),
                2
        );

        // Test 4: Multiple debtors and creditors
        checkSettlements(
                "Multiple debtors and creditors",
                service,
                List.of(
                        member("M1", "60"),
                        member("M2", "40"),
                        member("M3", "-70"),
                        member("M4", "-30")
                ),
                3
        );

        // Test 5: Decimal amounts
        checkSettlements(
                "Decimal amounts",
                service,
                List.of(
                        member("M1", "33.33"),
                        member("M2", "33.34"),
                        member("M3", "-66.67")
                ),
                2
        );

        // Test 6: One cent settlement
        checkSettlements(
                "One cent settlement",
                service,
                List.of(
                        member("M1", "0.01"),
                        member("M2", "-0.01")
                ),
                1
        );

        // Test 7: All balances zero
        checkSettlements(
                "All balances zero",
                service,
                List.of(
                        member("M1", "0"),
                        member("M2", "0"),
                        member("M3", "0")
                ),
                0
        );

        // Test 8: Zero balance member with other members
        checkSettlements(
                "Ignore zero balance member",
                service,
                List.of(
                        member("M1", "50"),
                        member("M2", "-50"),
                        member("M3", "0")
                ),
                1
        );

        // Test 9: Empty list
        checkSettlements(
                "Empty list",
                service,
                List.of(),
                0
        );

        // Test 10: Null list
        expectException(
                "Null list",
                () -> service.calculateSettlement(null)
        );

        // Test 11: Null MemberBalance object
        List<MemberBalance> nullMemberList = new ArrayList<>();
        nullMemberList.add(null);

        expectException(
                "Null member object",
                () -> service.calculateSettlement(nullMemberList)
        );

        // Test 12: Duplicate member ID
        expectException(
                "Duplicate member ID",
                () -> service.calculateSettlement(
                        List.of(
                                member("M1", "50"),
                                member("M1", "-50")
                        )
                )
        );

        // Test 13: Duplicate zero balance member
        expectException(
                "Duplicate zero balance member",
                () -> service.calculateSettlement(
                        List.of(
                                member("M1", "0"),
                                member("M1", "0")
                        )
                )
        );

        // Test 14: Unequal total balances
        expectException(
                "Unequal total balances",
                () -> service.calculateSettlement(
                        List.of(
                                member("M1", "100"),
                                member("M2", "-80")
                        )
                )
        );

        // Test 15: No debtors
        expectException(
                "No debtors with outstanding credit",
                () -> service.calculateSettlement(
                        List.of(
                                member("M1", "-50"),
                                member("M2", "-30")
                        )
                )
        );

        System.out.println();
        System.out.println("All 15 SettlementService tests passed!");
    }


    // Helper method to create MemberBalance objects
    private static MemberBalance member(
            String memberId, String amount) {

        BigDecimal netBalance = new BigDecimal(amount);

        if (netBalance.compareTo(BigDecimal.ZERO) >= 0) {

            return new MemberBalance(
                    memberId,
                    BigDecimal.ZERO,
                    netBalance
            );

        } else {

            return new MemberBalance(
                    memberId,
                    netBalance.negate(),
                    BigDecimal.ZERO
            );
        }
    }


    // Verify settlement results
    private static void checkSettlements(
            String testName,
            SettlementService service,
            List<MemberBalance> balances,
            int expectedCount) {

        List<Settlement> settlements =
                service.calculateSettlement(balances);

        if (settlements.size() != expectedCount) {

            throw new AssertionError(
                    testName + ": Expected " + expectedCount
                            + " settlements but got "
                            + settlements.size()
            );
        }

        HashMap<String, BigDecimal> remainingBalances =
                new HashMap<>();

        HashMap<String, BigDecimal> originalBalances =
                new HashMap<>();

        for (MemberBalance balance : balances) {

            remainingBalances.put(
                    balance.getMemberId(),
                    balance.getNetBalance()
            );

            originalBalances.put(
                    balance.getMemberId(),
                    balance.getNetBalance()
            );
        }

        for (Settlement settlement : settlements) {

            String from = settlement.getFromMemberId();
            String to = settlement.getToMemberId();
            BigDecimal amount = settlement.getAmount();

            // Amount must be positive
            if (amount == null ||
                    amount.compareTo(BigDecimal.ZERO) <= 0) {

                throw new AssertionError(
                        testName + ": Invalid settlement amount"
                );
            }

            // A member cannot pay themselves
            if (from.equals(to)) {

                throw new AssertionError(
                        testName + ": Self settlement detected"
                );
            }

            // Both members must exist
            if (!remainingBalances.containsKey(from) ||
                    !remainingBalances.containsKey(to)) {

                throw new AssertionError(
                        testName + ": Unknown member detected"
                );
            }

            BigDecimal debtorBalance =
                    remainingBalances.get(from);

            BigDecimal creditorBalance =
                    remainingBalances.get(to);

            // Verify payment direction
            if (debtorBalance.compareTo(BigDecimal.ZERO) <= 0 ||
                    creditorBalance.compareTo(BigDecimal.ZERO) >= 0) {

                throw new AssertionError(
                        testName + ": Incorrect payment direction"
                );
            }

            // Update debtor balance
            BigDecimal remainingDebt =
                    debtorBalance.subtract(amount);

            // Update creditor balance
            BigDecimal remainingCredit =
                    creditorBalance.add(amount);

            // Verify no overpayment
            if (remainingDebt.compareTo(BigDecimal.ZERO) < 0 ||
                    remainingCredit.compareTo(BigDecimal.ZERO) > 0) {

                throw new AssertionError(
                        testName + ": Overpayment detected"
                );
            }

            remainingBalances.put(from, remainingDebt);
            remainingBalances.put(to, remainingCredit);
        }

        // Every member must be fully settled
        for (String memberId : remainingBalances.keySet()) {

            if (remainingBalances.get(memberId)
                    .compareTo(BigDecimal.ZERO) != 0) {

                throw new AssertionError(
                        testName + ": Member " + memberId
                                + " is not fully settled"
                );
            }
        }

        // Verify original MemberBalance objects are unchanged
        for (MemberBalance balance : balances) {

            BigDecimal original =
                    originalBalances.get(balance.getMemberId());

            if (balance.getNetBalance().compareTo(original) != 0) {

                throw new AssertionError(
                        testName + ": Original balance was modified"
                );
            }
        }

        System.out.println("PASSED: " + testName);
    }


    // Verify invalid inputs
    private static void expectException(
            String testName, Runnable action) {

        try {

            action.run();

            throw new AssertionError(
                    testName + ": Expected exception was not thrown"
            );

        } catch (IllegalArgumentException e) {

            System.out.println("PASSED: " + testName);
        }
    }
}
