import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class SettlementService {

    public List<Settlement> calculateSettlement(List<MemberBalance> memberBalanceList ){

        if(memberBalanceList == null){
            throw new IllegalArgumentException("Member Balance List cannot be null");
        }
        if(memberBalanceList.isEmpty()){
            return new ArrayList<>();
        }



        HashMap<String, BigDecimal> debtorsMap = new HashMap<>();
        HashMap<String, BigDecimal> creditorsMap = new HashMap<>();
        ArrayList<Settlement> settlements = new ArrayList<>();
        HashSet<String> memberIds = new HashSet<>();

        BigDecimal totalNetBalance = BigDecimal.ZERO;

        for(MemberBalance balance : memberBalanceList){

            if(balance == null){
                throw new IllegalArgumentException("object is null");
            }

            boolean result = memberIds.add(balance.getMemberId());
            if(!result){
                throw new IllegalArgumentException("duplicate member id");
            }
            BigDecimal netBalance = balance.getNetBalance();

            totalNetBalance = totalNetBalance.add(netBalance);


            if(netBalance.compareTo(BigDecimal.ZERO) < 0){
                creditorsMap.put(balance.getMemberId(), netBalance.negate());
            }
            else if(netBalance.compareTo(BigDecimal.ZERO) > 0){
                debtorsMap.put(balance.getMemberId(), netBalance);
            }
        }

        if(totalNetBalance.compareTo(BigDecimal.ZERO) !=0){
            throw new IllegalArgumentException("total net balance is incorrect");
        }

        while(!debtorsMap.isEmpty() && !creditorsMap.isEmpty()){
            String debtorId = debtorsMap.keySet().iterator().next();
            String creditorId = creditorsMap.keySet().iterator().next();
            BigDecimal debtorBalance = debtorsMap.get(debtorId);
            BigDecimal creditorBalance = creditorsMap.get(creditorId);
            BigDecimal minAmount = debtorBalance.min(creditorBalance);

            settlements.add(new Settlement(debtorId, creditorId, minAmount));

            BigDecimal remainingDebtorBalance = debtorBalance.subtract(minAmount);
            BigDecimal remainingCreditorBalance = creditorBalance.subtract(minAmount);

            if(remainingCreditorBalance.compareTo(BigDecimal.ZERO) == 0){
                creditorsMap.remove(creditorId);
            }
            else{
                creditorsMap.put(creditorId, remainingCreditorBalance);
            }
            if(remainingDebtorBalance.compareTo(BigDecimal.ZERO) == 0){
                debtorsMap.remove(debtorId);
            }
            else {
                debtorsMap.put(debtorId, remainingDebtorBalance);
            }



        }

        return settlements;
    }


}
