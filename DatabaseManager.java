import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static Connection conn = null;
    private static final String DB_URL = "jdbc:sqlite:internship.db";

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            System.out.println("DB Connection error: " + e.getMessage());
        }
    }

    public static void createTables() {
        String candidatesTable = "CREATE TABLE IF NOT EXISTS candidates ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "email TEXT UNIQUE NOT NULL,"
                + "password TEXT NOT NULL,"
                + "phone TEXT,"
                + "adhar TEXT,"
                + "school TEXT,"
                + "college TEXT,"
                + "cgpa REAL,"
                + "hobbies TEXT,"
                + "description TEXT,"
                + "interestedArea TEXT,"
                + "specialization TEXT,"
                + "position TEXT,"
                + "education TEXT,"
                + "locationType TEXT,"
                + "passOutYear INTEGER,"
                + "language TEXT"
                + ");";

        String companiesTable = "CREATE TABLE IF NOT EXISTS companies ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "email TEXT UNIQUE NOT NULL,"
                + "password TEXT NOT NULL,"
                + "contact TEXT,"
                + "companyCode TEXT UNIQUE NOT NULL"
                + ");";

        String jobsTable = "CREATE TABLE IF NOT EXISTS jobs ("
                + "jobId INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT NOT NULL,"
                + "companyId INTEGER NOT NULL,"
                + "requiredSkills TEXT,"
                + "FOREIGN KEY (companyId) REFERENCES companies(id)"
                + ");";

        String applicationsTable = "CREATE TABLE IF NOT EXISTS applications ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "jobId INTEGER NOT NULL,"
                + "candidateId INTEGER NOT NULL,"
                + "FOREIGN KEY (jobId) REFERENCES jobs(jobId),"
                + "FOREIGN KEY (candidateId) REFERENCES candidates(id)"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(candidatesTable);
            stmt.execute(companiesTable);
            stmt.execute(jobsTable);
            stmt.execute(applicationsTable);
        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static boolean registerCandidate(Candidate c) {
        String sql = "INSERT INTO candidates(name, email, password, phone, adhar, school, college, cgpa, hobbies, description, interestedArea, specialization, position, education, locationType, passOutYear, language) "
                   + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getEmail());
            pstmt.setString(3, c.getPassword());
            pstmt.setString(4, c.getPhone());
            pstmt.setString(5, c.getAdhar());
            pstmt.setString(6, c.getSchool());
            pstmt.setString(7, c.getCollege());
            pstmt.setDouble(8, c.getCgpa());
            pstmt.setString(9, c.getHobbies());
            pstmt.setString(10, c.getDescription());
            pstmt.setString(11, c.getInterestedArea());
            pstmt.setString(12, c.getSpecialization());
            pstmt.setString(13, c.getPosition());
            pstmt.setString(14, c.getEducation());
            pstmt.setString(15, c.getLocationType());
            pstmt.setInt(16, c.getPassOutYear());
            pstmt.setString(17, c.getLanguage());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error registering candidate: " + e.getMessage());
            return false;
        }
    }

    public static Candidate checkCandidateLogin(String email, String password) {
        String sql = "SELECT * FROM candidates WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCandidate(rs);
            }
        } catch (Exception e) {
            System.out.println("Error during candidate login: " + e.getMessage());
        }
        return null;
    }

    public static boolean registerCompany(Company c) {
        String sql = "INSERT INTO companies(name, email, password, contact, companyCode) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getEmail());
            pstmt.setString(3, c.getPassword());
            pstmt.setString(4, c.getContact());
            pstmt.setString(5, c.getCompanyCode());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error registering company: " + e.getMessage());
            return false;
        }
    }

    public static Company checkCompanyLogin(String email, String password) {
        String sql = "SELECT * FROM companies WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Company c = new Company();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setContact(rs.getString("contact"));
                c.setCompanyCode(rs.getString("companyCode"));
                return c;
            }
        } catch (Exception e) {
            System.out.println("Error during company login: " + e.getMessage());
        }
        return null;
    }
    
    public static List<Job> getJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                jobs.add(mapResultSetToJob(rs));
            }
        } catch (Exception e) {
            System.out.println("Error fetching jobs: " + e.getMessage());
        }
        return jobs;
    }

    public static boolean applyToJob(int jobId, int candidateId) {
        String sql = "INSERT INTO applications(jobId, candidateId) VALUES(?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, candidateId);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error applying to job: " + e.getMessage());
            return false;
        }
    }

    public static List<Job> getMyApplications(int candidateId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT j.* FROM jobs j JOIN applications a ON j.jobId = a.jobId WHERE a.candidateId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, candidateId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobs.add(mapResultSetToJob(rs));
            }
        } catch (Exception e) {
            System.out.println("Error fetching applications: " + e.getMessage());
        }
        return jobs;
    }

    public static List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidates";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                candidates.add(mapResultSetToCandidate(rs));
            }
        } catch (Exception e) {
            System.out.println("Error fetching all candidates: " + e.getMessage());
        }
        return candidates;
    }

    public static boolean postJob(Job j) {
        String sql = "INSERT INTO jobs(title, companyId, requiredSkills) VALUES(?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, j.getTitle());
            pstmt.setInt(2, j.getCompanyId());
            pstmt.setString(3, j.getRequiredSkills());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error posting job: " + e.getMessage());
            return false;
        }
    }

    public static List<Job> getCompanyJobs(int companyId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE companyId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobs.add(mapResultSetToJob(rs));
            }
        } catch (Exception e) {
            System.out.println("Error fetching company jobs: " + e.getMessage());
        }
        return jobs;
    }
    
    public static List<Candidate> getApplicantsForJob(int jobId) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT c.* FROM candidates c JOIN applications a ON c.id = a.candidateId WHERE a.jobId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jobId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                candidates.add(mapResultSetToCandidate(rs));
            }
        } catch (Exception e) {
            System.out.println("Error fetching applicants: " + e.getMessage());
        }
        return candidates;
    }

    private static Candidate mapResultSetToCandidate(ResultSet rs) throws Exception {
        Candidate c = new Candidate();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setAdhar(rs.getString("adhar"));
        c.setSchool(rs.getString("school"));
        c.setCollege(rs.getString("college"));
        c.setCgpa(rs.getDouble("cgpa"));
        c.setHobbies(rs.getString("hobbies"));
        c.setDescription(rs.getString("description"));
        c.setInterestedArea(rs.getString("interestedArea"));
        c.setSpecialization(rs.getString("specialization"));
        c.setPosition(rs.getString("position"));
        c.setEducation(rs.getString("education"));
        c.setLocationType(rs.getString("locationType"));
        c.setPassOutYear(rs.getInt("passOutYear"));
        c.setLanguage(rs.getString("language"));
        return c;
    }
    
    private static Job mapResultSetToJob(ResultSet rs) throws Exception {
        Job j = new Job();
        j.setJobId(rs.getInt("jobId"));
        j.setTitle(rs.getString("title"));
        j.setCompanyId(rs.getInt("companyId"));
        j.setRequiredSkills(rs.getString("requiredSkills"));
        return j;
    }
}