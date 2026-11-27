import java.math.BigDecimal;

public class MemberBalance {
    private final String memberId;
    private final BigDecimal totalPaid;
    private final BigDecimal totalShare;


    public MemberBalance(String memberId, BigDecimal totalPaid, BigDecimal totalShare) {

        if(memberId == null || totalPaid == null || totalShare == null ) {
            throw new IllegalArgumentException("Not valid");
        }
        if(memberId.isBlank() || totalPaid.compareTo(BigDecimal.ZERO) < 0 || totalShare.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Not valid");
        }
        this.memberId = memberId;
        this.totalPaid = totalPaid;
        this.totalShare = totalShare;
    }



    //getters

    public String getMemberId() {return memberId;}
    public BigDecimal getTotalPaid() {return totalPaid;}
    public BigDecimal getTotalShare() {return totalShare;}



    public BigDecimal getNetBalance() {
        return totalShare.subtract(totalPaid);
    }



}
