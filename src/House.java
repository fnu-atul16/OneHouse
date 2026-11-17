import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class House {

    private  final String houseId;
    private String houseName;
    private BigDecimal monthlyRent;
    private HashMap<String,HouseMember> membersList;

    // constructor
    public House(String houseName, BigDecimal monthlyRent){
        this.houseId = UUID.randomUUID().toString();
        this.houseName = houseName;
        setMonthlyRent(monthlyRent);
        this.membersList = new HashMap<>();

    }

    //getters
    public String getHouseId(){return houseId;}
    public  String getHouseName(){return houseName;}
    public BigDecimal getMonthlyRent(){return monthlyRent;}

    //setters
    public void setHouseName(String houseName){this.houseName = houseName;}

    // setMonthlyRent sets the rent
    public void setMonthlyRent(BigDecimal monthlyRent)
    {

        if(monthlyRent==null||monthlyRent.compareTo(BigDecimal.ZERO) <=0){
            System.out.println("Not valid");
            throw new IllegalArgumentException("Not valid amount");
        }

            this.monthlyRent = monthlyRent;

    }


    //add members to house

    public void addMember(HouseMember member){

        if(member!=null) {

            if(membersList.containsKey(member.getMemberId())){
                throw new IllegalArgumentException("Already exist");
            } else{
                membersList.put(member.getMemberId(),member);
            }


        }
        else{
            throw new IllegalArgumentException("Not valid");
        }



    }
    // get number of members n house
    public int getMemberCount(){
        return membersList.size();
    }

    // remove members from house

    public void removeMember(HouseMember member){
        if(member!=null){
            if(membersList.containsKey(member.getMemberId())){
                membersList.remove(member.getMemberId());
            }
            else {
                throw new IllegalArgumentException("Not found");
            }
        }
        else {
            throw new IllegalArgumentException("Not valid");
        }
    }

    public HouseMember findMember(String memberId){
        if(memberId!=null && (!memberId.isBlank())){
            if(membersList.containsKey(memberId)){
                return membersList.get(memberId);
            }
            else{
                throw new IllegalArgumentException("Not found");
            }
        }
        else{
            throw new IllegalArgumentException("not valid");
        }
    }













}
