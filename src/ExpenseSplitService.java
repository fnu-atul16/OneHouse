import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseSplitService {

    public List<ExpenseSplit> splitEqually(Expense expense,List<String> memberIds){
        if(memberIds == null){
            throw new IllegalArgumentException("memberIds cannot be null");
        }
        if (expense==null){
            throw new IllegalArgumentException("expense cannot be null");
        }
        for (String memberId : memberIds) {
            if (memberId == null || memberId.isBlank()) {
                throw new IllegalArgumentException("Member ID cannot be null or blank");
            }
        }

        Set<String> uniqueMemberIds = new HashSet<String>(memberIds);
        if(uniqueMemberIds.size() != memberIds.size()){
            throw new IllegalArgumentException("duplicated member ids");
        }
        BigDecimal amount = expense.getAmount();
        int numOfMembers = memberIds.size();
        List<ExpenseSplit> splitListOfMembers = new ArrayList<ExpenseSplit>();

        if(numOfMembers==0){
            throw new IllegalArgumentException("Expense must have at least one member");
        }
        BigDecimal shareAmount = amount.divide(
                BigDecimal.valueOf(numOfMembers),
                2,
                RoundingMode.DOWN);

        BigDecimal distributed = shareAmount.multiply(BigDecimal.valueOf(numOfMembers));

        BigDecimal remaining = amount.subtract(distributed);

        int remainingCent = remaining.movePointRight(2).intValueExact();

        for(int i=0; i<numOfMembers; i++){
            BigDecimal currentShareAmount = shareAmount;

            String memberId = memberIds.get(i);
            if(remainingCent>i){
                currentShareAmount = currentShareAmount.add(new BigDecimal("0.01"));


            }
            ExpenseSplit split = new ExpenseSplit(expense.getExpenseId(), memberId, currentShareAmount);
            splitListOfMembers.add(split);

        }

        return splitListOfMembers;



    }
}
