import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.Console;

public class BankingSystem {
    //Scanner and Console for input.
    Scanner sc = new Scanner(System.in);
    Console cnsl = System.console();
    
    //For maintaining current logged-in session.
    User currentUser = null;

    //To store registered user list.
    List<User> users = new ArrayList<>();
    //String state = null;

    public static void main(String[] args){
        BankingSystem bs = new BankingSystem();
        while(true){
            int opt = bs.takeInput("home");
            
            //Sign-In
            if(opt == 1){
                opt = bs.signIn();

                //Login Successful
                if(opt == 1){
                    System.out.println("\nSign-In Successful");
                    while(opt != 7){

                        opt = bs.takeInput("MainMenu");
                        switch(opt){
                            case 1: bs.enterDetails();
                                    break;
                            case 2: bs.transaction();
                                    break;
                            case 3: if(bs.currentUser.fullName != null){
                                        bs.displayDetails();
                                    }
                                    else{
                                        System.out.println("User Details Yet to Be Filled");
                                    }
                                    break;
                            case 4: bs.displayBalance();
                                    break;

                            case 5: {
                                bs.currentUser.calculateInterest(bs.sc);
                                break;
                            }
                            case 6: {
                                bs.currentUser.approveLoan();
                                break;
                            }
                        }
                    }
                }

                //Wrong Password
                else if(opt == 0){
                    opt = bs.takeInput("Password");

                    //Reset Password
                    if(opt == 1){
                        int res = bs.resetPassword();
                        if(res == 1)
                            System.out.println("Password Changed Successfully");
                        else System.out.println("User Doesn't Exist");
                    }
                }

                //User Doesn't Exist
                else if(opt == -1){
                    System.out.println("User Doesn't Exist");
                }
            }

            //Register
            else if(opt == 2){
                bs.register();
            }

            //Exit
            else if(opt == 3){
                break;
            }
            else{
                System.out.println("Not a Valid Option");
            }
        }
    }

    //Common Function to take input.
    public int takeInput(String state){
        int opt;
        switch (state) {
            case "MainMenu" -> {
                System.out.println("-------------------------------");
                System.out.println("Enter Option: \n1. Enter Details: \n2. Transaction: \n3. Display Details: \n4. Display Balance: \n5. Calculate Interest\n6. Approve Loan\n7. Logout");
                opt = sc.nextInt();
                sc.nextLine();
                return opt;
            }
            case "Transaction" -> {
                System.out.println("-------------------------------");
                System.out.println("Enter Operation: \n1. Withdraw\n2. Deposit");
                opt = sc.nextInt();
                sc.nextLine();
                return opt;
            }
            case "Password" -> {
                System.out.println("Wrong Password");
                System.out.println("-------------------------------");
                System.out.println("1. Reset Password\n2. Retry");
                opt = sc.nextInt();
                sc.nextLine();
                return opt;
            }
            case "home" -> {
                System.out.println("-------------------------------");
                System.out.println("Enter Option: \n1. Sign In\n2. Register\n3. Exit");
                opt = sc.nextInt();
                sc.nextLine();
                return opt;
            }
        }

        return -1;
    }

    //Function to take input of details.
    public void enterDetails(){
        System.out.println("Enter FullName: ");
        String name = sc.nextLine();
        System.out.println("Enter Account Number");
        long accNo = sc.nextLong();
        sc.nextLine();
        currentUser.fullName = name;
        currentUser.accountNumber = accNo;

        System.out.println("Details Updated Successfully");
        
    }

    //Function to perform transactions.
    public void transaction(){
        System.out.println("Enter Operation: \n1. Withdraw\n2. Deposit");
        int operation = sc.nextInt();
        sc.nextLine();
        switch (operation) {
            case 1 -> withdrawMoney();
            case 2 -> depositMoney();
        }
    }

    public void displayDetails(){
        System.out.println("FullName: " + currentUser.fullName + "\nAccount Number: " + currentUser.accountNumber + "\nUsername: " + currentUser.username);
    }

    public void displayBalance(){
        System.out.println("Account Number: " + currentUser.accountNumber + "\nCurrent Balance: " + currentUser.balance);
    }

    public void withdrawMoney(){
        System.out.println("Enter Amount to Withdraw: ");
        long amt = sc.nextLong();
        sc.nextLine();
        if(amt > currentUser.balance){
            System.out.println("Insufficient Funds: ");
        }
        else{
            currentUser.balance -= amt;
            System.out.println("Amount Rs." + amt + " Withdrawn Successfully");
        }
    }

    public void depositMoney(){
        System.out.println("Enter Amount to Deposit: ");
        long amt = sc.nextLong();
        sc.nextLine();
        currentUser.balance += amt;
        System.out.println("Amount Rs." + amt + " Deposited Successfully");
    }

    public void register(){
        System.out.println("Enter Username: ");
        String uname = sc.nextLine();
        System.out.println("Enter Savings/Current (0/1): ");
        int input_type = sc.nextInt();
        User.AccountType type;
        if (input_type == 0)
            type = User.AccountType.Savings;
        else if (input_type == 1)
            type = User.AccountType.Current;
        else {
            System.out.println("Invalid Input Type. Please try again!!!");
            return;
        }

        System.out.print("Enter Password: ");
        String passwd = String.valueOf(cnsl.readPassword());
        for(int i = 0; i < passwd.length(); i++) System.out.print("*");
        System.out.println();
        users.add(new User(uname, passwd, type));

        System.out.println("User Created Successfully");
    }

    public int signIn(){
        System.out.println("Enter Username: ");
        String uname = sc.nextLine();
        System.out.print("Enter Password: ");
        String passwd = String.valueOf(cnsl.readPassword());
        for(int i = 0; i < passwd.length(); i++)    System.out.print("*");
        System.out.println();
        for(User user: users){
            if(user.username.equals(uname) && user.password.equals(passwd)){
                //If Authorization Successful.
                currentUser = user;
                return 1;
            }
            else if(user.username.equals(uname)){
                //If Wrong Password.
                return 0;
            }
        }
        //Authorization Failed.
        return -1;
    }

    public int resetPassword(){
        System.out.println("Enter Username: ");
        String uname = sc.nextLine();
        for(User user: users){
            if(user.username.equals(uname)){
                System.out.print("Enter New Password: ");
                String passwd = String.valueOf(cnsl.readPassword());
                for(int i = 0; i < passwd.length(); i++)    System.out.print("*");
                System.out.println();
                user.password = passwd;
                //Successful change of password.
                return 1;
            }
        }
        //If User Doesn't Exist.
        return 0;
    }
}
