import java.math.BigDecimal;

public class ExpenseSplit {
    private final String expenseId;
    private final String memberId;
    private final BigDecimal shareAmount;

    public ExpenseSplit(String expenseId, String memberId, BigDecimal shareAmount) {
        if (expenseId == null || expenseId.isBlank()) {
            throw new IllegalArgumentException("Expense ID cannot be null or empty");
        }
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be null or empty");
        }
        if (shareAmount == null || shareAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Share amount cannot be negative or zero");
        }

        this.expenseId = expenseId;
        this.memberId = memberId;
        this.shareAmount = shareAmount;
    }


    // getters
    public String getExpenseId() { return expenseId; }
    public String getMemberId() { return memberId; }
    public BigDecimal getShareAmount() { return shareAmount; }


}
