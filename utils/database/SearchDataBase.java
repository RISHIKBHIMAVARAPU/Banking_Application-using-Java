package utils.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import individuals.Class2;
import utils.CurrentAccountUser;
import utils.SavingAccountUser;
import utils.helpers.LoanAccount;
import utils.helpers.Transaction;

public class SearchDataBase {

    /**
     * This function searchUser will search for a user by their username and create
     * appropriate object from that details
     * The Object can be of two types - currentAccountUser and savingAccountUser
     * that's why the return type of function is Object as it is a superclass of
     * every class so we can return that way
     */
    public static Object searchUser(String username) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM account";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("username").equals(username)) {
                    if (rs.getString("account_type").equalsIgnoreCase("CA")) {
                        CurrentAccountUser user = new CurrentAccountUser(rs.getString("account_holder_name"),
                                rs.getString("gender"),
                                rs.getString("username"),
                                rs.getString("password"),
                                Double.valueOf(rs.getString("account_balance")));
                        user.getAccount().setAccountNumber(rs.getString("account_number"));
                        return user;
                    } else if (rs.getString("account_type").equalsIgnoreCase("SA")) {
                        SavingAccountUser user = new SavingAccountUser(rs.getString("account_holder_name"),
                                rs.getString("gender"),
                                rs.getString("username"),
                                rs.getString("password"),
                                Double.valueOf(rs.getString("account_balance")));
                        user.getAccount().setAccountNumber(rs.getString("account_number"));
                        return user;
                    }
                }
            }
            System.out.println("Record not found");
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while accessing the database");
            }
        }
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Method for searching loan details using username.
     */

    public static void searchLoan (String username) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM loan";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) { 
                if (rs.getString("username").equals(username)) {
                    isFound = true;
                    LoanAccount user = new LoanAccount(username, rs.getString("borrower_name"), rs.getString("loan_type"), Double.valueOf(rs.getString("loan_amount")));
                    System.out.println(user);
                }
            }
            if(!isFound){
                System.out.println("No records found.");
            }
            
        } catch (Exception e) {
            if(Class2.inDevelopment){
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while accessing the database");
            }
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for searching loan details using loan_id.
     */

    public static void searchLoanByLoanID (int loanID) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM loan";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            boolean isFound = false;
            while (rs.next()) {
                if (Integer.valueOf(rs.getString("loan_id")) == loanID) {
                    isFound = true;
                    LoanAccount user = new LoanAccount(rs.getString("username"), rs.getString("borrower_name"), rs.getString("loan_type"), Double.valueOf(rs.getString("loan_amount")));
                    System.out.println(user);
                }            
            }
            if(!isFound){
                System.out.println("No records found.");
            }
            
        } catch (Exception e) {
            if(Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while accessing the database");
            }
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method searchUserByAccountNumber will simply search for a user by their
     * account number
     * This function prints the details of the user by using the toString method of
     * that class which we have overriden
     */
    public static void searchUserByAccountNumber(String number) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM account";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("account_number").equals(number)) {
                    if (rs.getString("account_type").equalsIgnoreCase("CA")) {
                        CurrentAccountUser user = new CurrentAccountUser(rs.getString("account_holder_name"),
                                rs.getString("gender"),
                                rs.getString("username"),
                                rs.getString("password"),
                                Double.valueOf(rs.getString("account_balance")));
                        user.getAccount().setAccountNumber(rs.getString("account_number"));
                        System.out.println(user);
                        return;
                    } else if (rs.getString("account_type").equalsIgnoreCase("SA")) {
                        SavingAccountUser user = new SavingAccountUser(rs.getString("account_holder_name"),
                                rs.getString("gender"),
                                rs.getString("username"),
                                rs.getString("password"),
                                Double.valueOf(rs.getString("account_balance")));
                        user.getAccount().setAccountNumber(rs.getString("account_number"));
                        System.out.println(user);
                        return;
                    }
                }
            }
            System.out.println("Record not found");
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while accessing the database");
            }
        }
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * This method will search user by their name and returns all the users with
     * that name
     * Name can be typed in any format (Varun, VARUN, varun, varuN, VaRuN) all are
     * considered as same
     */
    public static void searchUserByName(String name) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM account";
        boolean isFound = false;
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("account_holder_name").contains(name.toLowerCase())) {
                    isFound = true;
                    if (rs.getString("account_type").equalsIgnoreCase("CA")) {
                        CurrentAccountUser user = new CurrentAccountUser(rs.getString("account_holder_name"),
                                rs.getString("gender"),
                                rs.getString("username"),
                                rs.getString("password"),
                                Double.valueOf(rs.getString("account_balance")));
                        user.getAccount().setAccountNumber(rs.getString("account_number"));
                        System.out.println(user);
                    } else if (rs.getString("account_type").equalsIgnoreCase("SA")) {
                        SavingAccountUser user = new SavingAccountUser(rs.getString("account_holder_name"),
                                rs.getString("gender"),
                                rs.getString("username"),
                                rs.getString("password"),
                                Double.valueOf(rs.getString("account_balance")));
                        user.getAccount().setAccountNumber(rs.getString("account_number"));
                        System.out.println(user);
                    }
                }
            }
            if (!isFound) {
                System.out.println("Record not found");
            }
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while accessing the database");
            }
        }
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * This method will simply search for a transaction using its transaction id and
     * create a Transaction object from it
     * Then it prints the object by using its own toString method which is
     * overridden
     */
    public static void searchTransaction(String string) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM transaction";
        boolean isFound = false;
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (Integer.toString(rs.getInt("transaction_id")).equals(string)) {
                    String id = Integer.toString(rs.getInt("transaction_id"));
                    String dateTime = rs.getString("transaction_date");
                    String sender = rs.getString("sender");
                    String receiver = rs.getString("receiver");
                    int amount = rs.getInt("amount");
                    String type = rs.getString("type");
                    Transaction transaction = new Transaction(id, dateTime, amount, sender, receiver, type);
                    System.out.println(transaction);
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                System.out.println("No record found for this transaction ID");
            }
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some internal error occurred");
            }
        }
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * This method will print the name and account number of the customers which are
     * sorted according to their names
     */
    public static void printSortedUserList() {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT account_holder_name, account_number FROM account ORDER BY account_holder_name";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("+-----------------------+---------------+");
            System.out.println("|\t   NAME   \t|    Acc. No.\t|");
            System.out.println("|-----------------------+---------------|");
            while (rs.next()) {
                System.out.printf("|\t%10s\t|\t%s\t|\n", rs.getString("account_holder_name"),
                        rs.getString("account_number"));
            }
            System.out.println("+-----------------------+---------------+");
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some internal error occurred");
            }
        }
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to print the transaction done by a particular user
     * either it is a withdrawal, deposit or transfer
     * This method is called from Varun.java file whenever user is asking for
     * detailed description of the user
     */
    public static void printTransactions(String string) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM transaction";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println(
                    "+---------------------------------------------------------------------------------------+");
            System.out.println(
                    "|                                 Transactions                                          |");
            System.out.println(
                    "|---------------------------------------------------------------------------------------|");
            System.out.println("|\tID\t|\t Date and Time \t\t|  Amount\t| \t Remark \t|");
            System.out.println(
                    "|---------------+---------------------------------+---------------+---------------------|");
            while (rs.next()) {
                if (rs.getString("sender").equals(string)) {
                    String result = "|\t" + rs.getInt("transaction_id")
                            + "\t|\t" + rs.getString("transaction_date")
                            + "\t|\t" + rs.getInt("amount");

                    if (rs.getString("type").equals("W") || rs.getString("type").equals("T")) {
                        result = result + "\t|\tWithdraw\t|";
                    } else if (rs.getString("type").equals("D")) {
                        result = result + "\t|\tDeposit \t|";
                    }
                    System.out.println(result);
                } else if (rs.getString("receiver").equals(string)) {
                    String result = "|\t" + rs.getInt("transaction_id")
                            + "\t|\t" + rs.getString("transaction_date")
                            + "\t|\t" + rs.getInt("amount");

                    if (rs.getString("type").equals("W")) {
                        result = result + "\t|\tWithdraw\t|";
                    } else if (rs.getString("type").equals("D") || rs.getString("type").equals("T")) {
                        result = result + "\t|\tDeposit \t|";
                    }
                    System.out.println(result);
                }
            }
            System.out.println(
                    "+---------------------------------------------------------------------------------------+");
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred");
            }
        }
    }

    public static void searchTransaction(Integer amount, boolean isGreater) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM transaction";
        if(isGreater) {
            query = query + " HAVING amount >= ?";
        } else {
            query = query + " HAVING amount <= ?";
        }
        boolean isFound = false;
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, amount);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = Integer.toString(rs.getInt("transaction_id"));
                String dateTime = rs.getString("transaction_date");
                String sender = rs.getString("sender");
                String receiver = rs.getString("receiver");
                String type = rs.getString("type");
                Transaction transaction = new Transaction(id, dateTime, rs.getInt("amount"), sender, receiver, type);
                System.out.println(transaction);
                isFound = true;
            }
            if (!isFound) {
                System.out.println("No record found");
            }
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some internal error occurred");
            }
        }
        if (con != null) {
            try {
                con.commit();
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }
}
