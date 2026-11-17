import java.util.UUID;

public class HouseMember {
    private final String  memberId;
    private String memberName;
    private String memberEmailId;
    private String memberPhoneNumber;
    private String memberDOB;

    public HouseMember(String memberName, String memberEmailId,String memberPhoneNumber,String memberDOB){
        this.memberId = UUID.randomUUID().toString();
        setMemberName(memberName);
        setMemberEmailId(memberEmailId);
        setMemberPhoneNumber(memberPhoneNumber);
        setMemberDOB(memberDOB);

    }



    // Setters

    public void setMemberName(String memberName){

        if( memberName == null||memberName.isBlank()){
            throw new IllegalArgumentException("Not a valid name");
        }
        this.memberName = memberName;
    }

    public void setMemberEmailId(String memberEmailId){
        if (memberEmailId == null || memberEmailId.isBlank()){
            throw new IllegalArgumentException("Not a valid email");
        }

        this.memberEmailId = memberEmailId;
    }

    public  void setMemberPhoneNumber(String memberPhoneNumber){
        if(memberPhoneNumber == null || memberPhoneNumber.isBlank()){
            throw new IllegalArgumentException("not a valid phone number");
        }
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public  void setMemberDOB(String memberDOB){
        if(memberDOB == null || memberDOB.isBlank()){
            throw new IllegalArgumentException("Not a valid date of birth");
        }
        this.memberDOB = memberDOB;
    }

    //getters

    public String getMemberId(){return memberId;}
    public String getMemberName(){return memberName;}
    public String getMemberEmailId(){return memberEmailId;}
    public String getMemberPhoneNumber(){return memberPhoneNumber;}
    public String getMemberDOB(){return memberDOB;}

}
