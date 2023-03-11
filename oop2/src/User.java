import java.util.Scanner;

class User {

    enum AccountType implements ApproveLoan, CalculateInterest 
    {
        Savings,
        Current;
        

        @Override
        public void calculateInterest(Scanner sc) {
            switch (this) {
                case Savings -> {
                    System.out.println("Enter Amount: ");
                    long input_amount = sc.nextLong();
                    System.out.println("Enter Time In Years: ");
                    int input_time = sc.nextInt();
                    long interest = input_amount * input_time * 5 / 100;
                    System.out.println("Interest: " + interest);
                    long total = interest + input_amount;
                    System.out.println("Total Balance: " + total);
                }
                case Current -> System.out.println("No Interest for Current Account");
            }
        }

        @Override
        public void approveLoan(double balance) {
            switch (this) {
                case Savings -> {
                    if (balance > 1_00_000) {
                        double amount = 1.5 * balance;
                        System.out.println("Approved Amount: " + amount);
                    } else System.out.println("Loan Not Approved!!!");
                }
                case Current -> {
                    if (balance > 5_00_000) {
                        double amount = 3 * balance;
                        System.out.println("Approved Amount: " + amount);
                    } else System.out.println("Loan Not Approved!!!");
                }
            }
        }

        
    }

    String username;
    String password;
    String fullName;
    Long accountNumber;
    Long balance;

    AccountType type;

    User(String username, String password, AccountType type) {
        this.username = username;
        this.password = password;
        this.balance = 0L;
        this.type = type;
    }

    public void approveLoan() {
        this.type.approveLoan(this.balance);
    }

    public void calculateInterest(Scanner sc) {
        this.type.calculateInterest(sc);
    }
}
