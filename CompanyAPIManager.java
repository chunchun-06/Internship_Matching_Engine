import java.io.BufferedReader;
import java.io.FileReader;

public class CompanyAPIManager {

    private static final String FILE_NAME = "companies.json";

    public static void fetchAndSaveCompanies() {
 
    }

    public static boolean checkCompanyExists(String companyCode) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"companyCode\": \"" + companyCode + "\"")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading " + FILE_NAME + ": " + e.getMessage());
        }
        return false;
    }
}