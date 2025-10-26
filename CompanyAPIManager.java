import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CompanyAPIManager {

    private static final String API_URL = "https://api.opencorporates.com/v0.4/companies/search?q=COMPANY_NAME";
    private static final String FILE_NAME = "companies.json";

    public static void fetchAndSaveCompanies() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            System.out.println("companies.json already exists. Skipping download.");
            return;
        }

        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            String rawJson = content.toString();
            StringBuilder simplifiedJson = new StringBuilder();
            simplifiedJson.append("[\n");

            String[] users = rawJson.split("\"company\":");
            for (int i = 1; i < users.length; i++) {
                String userBlock = users[i];
                String name = parseValue(userBlock, "\"name\":\"");
                String email = parseValue(rawJson.split("\"email\":")[i], "\"");
                String phone = parseValue(rawJson.split("\"phone\":")[i], "\"");
                String code = "C" + (100 + i);

                simplifiedJson.append("  {\n");
                simplifiedJson.append("    \"name\": \"").append(name).append("\",\n");
                simplifiedJson.append("    \"email\": \"").append(email).append("\",\n");
                simplifiedJson.append("    \"contact\": \"").append(phone).append("\",\n");
                simplifiedJson.append("    \"companyCode\": \"").append(code).append("\"\n");
                simplifiedJson.append("  }");
                if (i < users.length - 1) {
                    simplifiedJson.append(",\n");
                }
            }
            simplifiedJson.append("\n]");

            try (FileWriter fw = new FileWriter(FILE_NAME)) {
                fw.write(simplifiedJson.toString());
            }

            System.out.println("Successfully downloaded and saved mock companies to " + FILE_NAME);

        } catch (Exception e) {
            System.out.println("Error fetching company data: " + e.getMessage());
        }
    }

    private static String parseValue(String text, String key) {
        try {
            return text.split(key)[1].split("\"")[0];
        } catch (Exception e) {
            return "";
        }
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