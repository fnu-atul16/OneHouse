import java.math.BigDecimal;

public class Settlement {
    private final String  fromMemberId;
    private final String toMemberId;
    private final BigDecimal amount;


    public Settlement(String fromMemberId, String toMemberId, BigDecimal amount) {
        if(fromMemberId == null || toMemberId == null || fromMemberId.isBlank() || toMemberId.isBlank()){
            throw new IllegalArgumentException("fromMemberId and toMemberId cannot be null or blank");
        }

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero and cannot be null.");
        }
        if(fromMemberId.equals(toMemberId)){
            throw new IllegalArgumentException("fromMemberId and toMemberId cannot be the same");
        }


        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.amount = amount;
    }

    public String getFromMemberId() { return fromMemberId; }


    public String getToMemberId() {  return toMemberId; }


    public BigDecimal getAmount() { return amount; }




}
