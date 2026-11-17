
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.UUID;

public class Expense {
    private final ExpenseType expenseType;  // type of expense grocery,electricity,gas,wifi,other
    private final String expenseId;  // expense id
    private final String paidByMemberId; //id of member who paid for expense
    private String description;  // brief description of expense
    private final LocalDate expenseDate;  // date on which expense made
    private BigDecimal amount; // total amount of expense


    public Expense(ExpenseType expenseType,String paidByMemberId,String description, BigDecimal amount ){
        this.expenseId =  UUID.randomUUID().toString();
        if(expenseType == null){
            throw new IllegalArgumentException("Not valid");
        }else{
            this.expenseType = expenseType;
        }


        if(paidByMemberId == null || paidByMemberId.isBlank()){
            throw new IllegalArgumentException("Not valid memberId");
        }else{
            this.paidByMemberId = paidByMemberId;
        }

        setDescription(description);
        this.expenseDate = LocalDate.now();
       setAmount(amount);
    }


    //setters

    public void setDescription(String description){
        if(description==null || description.isBlank()){
            throw new IllegalArgumentException("Not valid");
        } else{
            this.description = description;
        }
    }

    public void setAmount(BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO )<= 0 ) {
            throw new IllegalArgumentException("Not valid");

        }
        else{
            this.amount = amount;
        }
    }

    //Getters

    public String getExpenseId(){return expenseId;}
    public String getPaidByMemberId(){return paidByMemberId;}
    public String getDescription(){return description;}
    public LocalDate getExpenseDate(){return expenseDate;}
    public BigDecimal getAmount(){return amount;}
    public ExpenseType getExpenseType(){return expenseType;}


}
