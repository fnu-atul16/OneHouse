import java.math.BigDecimal;
import java.util.HashMap;

public class ExpenseService {
    private HashMap<String,Expense> expenses; // hashmap to store expenses with expense id

    public ExpenseService(){
        this.expenses = new HashMap<>();
    }
    // addExpense add the expense to the hashmap with their respective expense id
    public void addExpense(Expense expense) {

        if (expense == null) {
            throw new IllegalArgumentException("Not valid");
        }

        if (expenses.containsKey(expense.getExpenseId())) {
            throw new IllegalArgumentException("Already exist");
        }

        expenses.put(expense.getExpenseId(), expense);
    }
    //find expense through expenseId and then return the expense
    public Expense findExpense(String expenseId) {
        Expense expense = expenses.get(expenseId);

        if (expense == null) {
            throw new IllegalArgumentException("Not found");
        }


        return expense;
    }
    // remove expense
    public void removeExpense(String expenseId){
        Expense expense = expenses.remove(expenseId);
        if(expense == null){
            throw new IllegalArgumentException("Not found");
        }

    }

    // return total amount of expenses
    public BigDecimal getTotalExpenses(){
        BigDecimal total = BigDecimal.ZERO;
        for(Expense expense: expenses.values()){
           total = total.add(expense.getAmount());
        }
        return total;
    }


    // return total amount of expenses of particular month
    public BigDecimal getMonthlyTotal(int year,int month){
        BigDecimal total = BigDecimal.ZERO;

        if(month<1 || month>12){
            throw new IllegalArgumentException("Not a valid month");
        }

        for(Expense expense: expenses.values()){
            int expenseYear = expense.getExpenseDate().getYear();
            int expenseMonth = expense.getExpenseDate().getMonthValue();

            if(expenseMonth == month && expenseYear == year){
                total = total.add(expense.getAmount());
            }

        }
        return total;
    }

    public BigDecimal getTotalPaidByMember(String memberId){
        BigDecimal total = BigDecimal.ZERO;
        if(memberId==null || memberId.isBlank()){
            throw new IllegalArgumentException("Not valid");
        }

        for(Expense expense: expenses.values()){
            if(expense.getPaidByMemberId().equals(memberId)){
                total = total.add(expense.getAmount());
            }
        }
        return total;
    }




}