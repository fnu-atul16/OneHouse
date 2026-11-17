import java.math.BigDecimal;

public class HouseTest {
    static void main(String[] args) {
        // 1. Create House
        House house = new House(
                "Queens House",
                new BigDecimal("2400")
        );

        System.out.println("House: " + house.getHouseName());
        System.out.println("Rent: $" + house.getMonthlyRent());
        System.out.println("House ID: " + house.getHouseId());

        // 2. Create Members
        HouseMember atul = new HouseMember(
                "Atul", "atul@gmail.com",
                "1234567890", "2003-09-16"
        );

        HouseMember rahul = new HouseMember(
                "Rahul", "rahul@gmail.com",
                "2345678901", "2002-05-12"
        );

        HouseMember aman = new HouseMember(
                "Aman", "aman@gmail.com",
                "3456789012", "2001-08-20"
        );

        // 3. Add Members
        house.addMember(atul);
        house.addMember(rahul);
        house.addMember(aman);

        System.out.println("\nTotal members: "
                + house.getMemberCount());

        // 4. Find Member
        HouseMember found = house.findMember(
                rahul.getMemberId()
        );

        System.out.println("Found member: "
                + found.getMemberName());

        // 5. Remove Member
        house.removeMember(aman);

        System.out.println("Members after removal: "
                + house.getMemberCount());

        // 6. Test Duplicate Member
        try {
            house.addMember(atul);
            System.out.println("FAIL: Duplicate accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 7. Test Invalid Member ID
        try {
            house.findMember("invalid-id");
            System.out.println("FAIL: Invalid ID accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 8. Test Null Member
        try {
            house.addMember(null);
            System.out.println("FAIL: Null member accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 9. Test Invalid Rent
        try {
            house.setMonthlyRent(new BigDecimal("-500"));
            System.out.println("FAIL: Negative rent accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        System.out.println("\nFinal rent: $"
                + house.getMonthlyRent());
    }
    }

