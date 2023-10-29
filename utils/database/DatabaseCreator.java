package utils.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import individuals.Class2;
import utils.helpers.Transaction;

/**
 * NOTE : If you make changes to this DatabaseCreator class you might have to
 * reinitialise the program
 */

public class DatabaseCreator {

    /**
     * This is private variable which represents whether the program is in
     * intialised mode or in the normal run
     */
    private static boolean initialising = false;
    final static String databaseName = "oops_mini_project";

    public static boolean isInitialising() {
        return initialising;
    }

    public static void setInitialising(boolean initialising) {
        DatabaseCreator.initialising = initialising;
    }

    public static void programInit(String args[]) {
        setInitialising(true);
        final String createDatabaseQuery = "CREATE DATABASE " + databaseName;

        Connection con = ConnectionFactory.getConnection(args[1], args[2]);
        dropDataBaseIfExists(databaseName, args);
        try (PreparedStatement statement = con.prepareStatement(createDatabaseQuery)) {
            statement.execute(createDatabaseQuery);
            System.out.println("Database created....");
            createTables();
            addDummyDataToDataBase();
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Unexpected error occur in creating database....");
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * This method addDummyDataToDataBase will add the dummy data from two files
     * 1st is from intialUserData.csv which contains the data of the users
     * (By default password for every user is admin)
     * 2nd is from initialTransactionData.csv which contains data of the transaction
     * done by the users
     * used java nio for file I/O
     */
    private static void addDummyDataToDataBase() {
        Path path = Paths.get("data/initialUserData.csv");
        if (Files.exists(path)) {
            List<String> lines;
            try {
                lines = Files.readAllLines(path);
                for (String line : lines) {
                    String usersData[] = line.split(",");
                    usersData[1] = Class2.encryptString(usersData[1]);
                    DataBaseModifier.addDataToAccountTable(usersData);
                }
            } catch (IOException e) {
                if (Class2.inDevelopment) {
                    e.printStackTrace();
                } else {
                    System.out.println("Unable to read the dummy data file");
                }
            }
        }

        path = Paths.get("data/initialLoanData.csv");
        if(Files.exists(path)) {
            List<String> lines;
            try {
                lines = Files.readAllLines(path);
                for (String line : lines) {
                    String usersData[] = line.split(",");
                    DataBaseModifier.addDataToLoanTable(usersData);   
                }
            } catch (IOException e) {
                if(Class2.inDevelopment) {
                    e.printStackTrace();
                } else {
                    System.out.println("Unable to read the dummy data file");
                }
            }
        }

        path = Paths.get("data/initialTransactionData.csv");
        if (Files.exists(path)) {
            List<String> lines;
            try {
                lines = Files.readAllLines(path);
                for (String line : lines) {
                    String usersData[] = line.split(",");
                    if (usersData[1].equals("w")) {
                        if(DataBaseModifier.withdraw(usersData)) {
                            Transaction transaction = new Transaction(Integer.valueOf(usersData[4]), usersData[2], "SELF", "W");
                            DataBaseModifier.addTransaction(transaction);
                        }
                    } else if (usersData[1].equals("d")) {
                        if(DataBaseModifier.deposit(usersData)) {
                            Transaction transaction = new Transaction(Integer.valueOf(usersData[3]), usersData[2], "SELF", "D");
                            DataBaseModifier.addTransaction(transaction);
                        }
                    } else if (usersData[1].equals("t")) {
                        if(DataBaseModifier.transfer(usersData)) {
                            Transaction transaction = new Transaction(Integer.valueOf(usersData[4]), usersData[2], usersData[5], "T");
                            DataBaseModifier.addTransaction(transaction);
                        }
                    }
                }
            } catch (IOException e) {
                if (Class2.inDevelopment) {
                    e.printStackTrace();
                } else {
                    System.out.println("Unable to read the dummy data file");
                }
                return;
            }
        }
        System.out.println("Dummy data added....");
    }

    /**
     * This method is basically used for development purposes it will drop database
     * if it exists
     */
    private static void dropDataBaseIfExists(String databaseName, String args[]) {
        Connection con = ConnectionFactory.getConnection(args[1], args[2]);
        final String dropDataBaseQuery = "DROP DATABASE IF EXISTS " + databaseName;
        try (PreparedStatement stmt = con.prepareStatement(dropDataBaseQuery)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some internal error occurred");
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * This method createTables is used to create tables in the database
     * 1st is account table which will store the customers data
     * 2nd is transaction table which will store transaction records
     */
    private static void createTables() {
        final Connection con = ConnectionFactory.getConnection();
        final String accountTableQuery = "CREATE TABLE account(username VARCHAR(25) NOT NULL PRIMARY KEY,"
                + "password VARCHAR(10) NOT NULL,"
                + "account_number VARCHAR(10) NOT NULL,"
                + "account_type VARCHAR(5) NOT NULL,"
                + "account_holder_name VARCHAR(30) NOT NULL,"
                + "account_balance DOUBLE(20,3) NOT NULL,"
                + "gender VARCHAR(2) NOT NULL)";

        try (PreparedStatement stmt = con.prepareStatement(accountTableQuery)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while creating the tables");
            }
        }

        final String loanTableQuery = "CREATE TABLE loan(loan_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "username varchar(25) NOT NULL,"
                + "borrower_name VARCHAR(30) NOT NULL,"
                + "loan_type VARCHAR(5) NOT NULL,"
                + "loan_amount INT NOT NULL,"
                + "FOREIGN KEY(username) REFERENCES account(username))"
                + "AUTO_INCREMENT = 1001";
        try(PreparedStatement stmt = con.prepareStatement(loanTableQuery)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            if(Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occured while creating the tables");
            }
        }

        final String transactionTableQuery = "CREATE TABLE transaction(sender VARCHAR(10) NOT NULL,"
                + "receiver VARCHAR(10) NOT NULL,"
                + "transaction_id INT NOT NULL AUTO_INCREMENT,"
                + "transaction_date DATETIME NOT NULL,"
                + "amount INT NOT NULL,"
                + "type VARCHAR(2) NOT NULL,"
                + "PRIMARY KEY(transaction_id))";

        try (PreparedStatement stmt = con.prepareStatement(transactionTableQuery)) {
            stmt.executeUpdate();
            System.out.println("Tables Created....");
        } catch (Exception e) {
            if (Class2.inDevelopment) {
                e.printStackTrace();
            } else {
                System.out.println("Some error occurred while creating the tables");
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        }
    }
}
