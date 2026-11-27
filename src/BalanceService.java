
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BalanceService {

    public List<MemberBalance> calculateBalance(List<Expense> expenses, List<ExpenseSplit> expenseSplits, List<String> memberIds    ) {


        if (expenses == null ||  expenseSplits == null || memberIds == null || memberIds.isEmpty())  {
            throw new IllegalArgumentException("Input lists cannot be null and members cannot be empty.");
        }
        HashSet<String> membersIdSet = new HashSet<>(memberIds);

        if(membersIdSet.size() != memberIds.size()) {
            throw new IllegalArgumentException("Duplicate Id detected.");
        }
        for(String memberId : memberIds) {
            if(memberId == null || memberId.isBlank()){
                throw new IllegalArgumentException("Member Id cannot be null or blank.");
            }
        }
        HashSet<String> expenseIdsSet = new HashSet<>();
        HashSet<String> splitIdsSet = new HashSet<>();

        HashMap<String, BigDecimal> totalPaidMap = new HashMap<>();
        HashMap<String, BigDecimal> totalShareMap = new HashMap<>();
        HashMap<String, BigDecimal> expenseAmountMap = new HashMap<>();

        for (Expense expense : expenses) {
            if(expense == null){
                throw new IllegalArgumentException("Expense cannot be null.");
            }
            if(!membersIdSet.contains(expense.getPaidByMemberId())){
                throw new IllegalArgumentException("Member Id does not belong to members list.");
            }

            boolean result = expenseIdsSet.add(expense.getExpenseId());
            if(!result){
                throw new IllegalArgumentException("Duplicate expense detected.");
            }

            expenseAmountMap.put(expense.getExpenseId(), expense.getAmount());


            BigDecimal previousAmount = totalPaidMap.getOrDefault(expense.getPaidByMemberId(), BigDecimal.ZERO);
            previousAmount = previousAmount.add(expense.getAmount());
            totalPaidMap.put(expense.getPaidByMemberId(), previousAmount);


        }

        HashMap<String, BigDecimal> totalShareByExpenseMap = new HashMap<>();



        for(ExpenseSplit split : expenseSplits){
            if(split == null){
                throw new IllegalArgumentException("ExpenseSplit cannot be null.");
            }
            if(!membersIdSet.contains(split.getMemberId())){
                throw new IllegalArgumentException("Member Id does not belong to members list.");
            }

            if(!expenseIdsSet.contains(split.getExpenseId())){
                throw new IllegalArgumentException("ExpenseId mismatch.");
            }
            boolean result = splitIdsSet.add((split.getMemberId() + ":"  + split.getExpenseId()));
            if(!result){
                throw new IllegalArgumentException("Duplicate expense split detected");
            }

           BigDecimal prevAmount = totalShareByExpenseMap.getOrDefault(split.getExpenseId(), BigDecimal.ZERO);
            prevAmount = prevAmount.add(split.getShareAmount());
            totalShareByExpenseMap.put(split.getExpenseId(), prevAmount);
            BigDecimal previousShareAmount = totalShareMap.getOrDefault(split.getMemberId(), BigDecimal.ZERO);
            previousShareAmount = previousShareAmount.add(split.getShareAmount());
            totalShareMap.put(split.getMemberId(), previousShareAmount);

        }

            for(String id : expenseIdsSet){
                if(expenseAmountMap.get(id).compareTo(totalShareByExpenseMap.getOrDefault(id,BigDecimal.ZERO))!=0){

                    throw new IllegalArgumentException("Total split amount does not match expense amount for expense ID: " + id);
                }
            }







        ArrayList<MemberBalance> memberBalances = new ArrayList<>();
        for(String memberId : memberIds){
            BigDecimal totalPaid = totalPaidMap.getOrDefault(memberId, BigDecimal.ZERO);
            BigDecimal totalShare = totalShareMap.getOrDefault(memberId, BigDecimal.ZERO);
            memberBalances.add(new MemberBalance(memberId, totalPaid, totalShare));

        }
        return memberBalances;

    }
}
