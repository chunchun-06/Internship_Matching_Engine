import java.util.Scanner;
public class Main {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        DatabaseManager.connect();
        DatabaseManager.createTables();
        CandidateUI.init(sc);
        CompanyUI.init(sc);
        mainMenu();
    }
    private static void mainMenu() {
        while (true) {
            UIHelper.printSectionTitle("SMART INTERNSHIP MATCHER");
            System.out.println("1. Candidate Register");
            System.out.println("2. Candidate Login");
            System.out.println("3. Company Register");
            System.out.println("4. Company Login");
            System.out.println("5. Exit");
            UIHelper.printSeparator();
            System.out.print("Choose: ");
            String ch = sc.nextLine();
            switch (ch) {
                case "1": CandidateUI.register(); break;
                case "2": CandidateUI.login(); break;
                case "3": CompanyUI.register(); break;
                case "4": CompanyUI.login(); break;
                case "5": 
                    System.out.println("Bye!"); 
                    System.exit(0);
                default: 
                    System.out.println("Invalid choice.");
            }
        }
    }
}