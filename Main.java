
import java.io.IOException;
import individuals.Class1;
import individuals.Class2;
import utils.database.DataBaseModifier;
import utils.database.DatabaseCreator;
import utils.database.SearchDataBase;
import utils.helpers.Helps;

/**
 * Main
 */

 
public class Main {
    public static void main(String[] args) throws IOException {
        /**
         * Print Help
         */
        if (args.length == 0) {
            Helps.printHelp();
            return;
        }

        switch (args[0]) {
            /**
             * Print Help
             */
            case "-h":
                Helps.printHelp();
                break;

            /**
             * Create a new Account
             */
            case "-c":
                if (args.length < 7) {
                    System.out.println("Invalid number of arguments entered, use help for more details");
                    Helps.createNewAccountHelp();
                    return;
                }

                Class2.createNewAccount(args);
                break;

            /**
             * Update Password
             */
            case "-up":
                if (args.length < 4) {
                    System.out.println("Invalid number of arguments for updating password");
                    Helps.updatePasswordHelp();
                    return;
                }
                Class2.updatePassword(args);
                break;

            /**
             * Search records of user and transactions
             */
            case "-s":
                if (args.length < 3) {
                    System.out.println("Invalid number of arguments for searching");
                    Helps.searchHelp();
                    return;
                }
                Class2.search(args);
                return;

            /**
             * Deleting the account
             */
            case "-d":
                if (args.length < 3) {
                    System.out.println("Invalid number of arguments");
                    Helps.deleteAccountHelp();
                    return;
                }
                DataBaseModifier.deleteAccount(args);
                break;

            /**
             * Performing the transaction
             */
            case "-tr":
                if (args.length < 2) {
                    System.out.println("Invalid number of arguments");
                    Helps.transactionHelp();
                    return;
                }
                Class2.executeTransaction(args);
                break;

            /**
             * Loan Facility
             */
            case "-l":
                if (args.length < 5 || args.length > 8) {
                    System.out.println("Invalid number of arguments");
                    Helps.createLoanAccountHelp();
                    return;
                }
                Class1.createLoanAccount(args);
                break;

            
            /**
             * Search records of loans using username, loanID 
             */
            case "-sl":
                    if(args.length < 3){
                        Helps.searchLoanHelp();
                        return;
                    }
                    if(args[1].equals("-u")){
                        SearchDataBase.searchLoan(args[2]);
                    } else if(args[1].equals("-id")){
                        SearchDataBase.searchLoanByLoanID(Integer.valueOf(args[2]));
                    } else{
                        Helps.searchLoanHelp();
                    }
                    break;

            /**
             * Printing details in sorted order
             */
            case "-sort":
                SearchDataBase.printSortedUserList();
                break;

            /**
             * Initializing the program
             */
            case "--init":
                DatabaseCreator.programInit(args);
                break;

            /**
             * Default for printing help if wrong input is given
             */
            default:
                Helps.printHelp();
                break;
        }
    }
}