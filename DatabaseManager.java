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
                + "stipend REAL,"
                + "timePeriod TEXT,"
                + "area TEXT,"
                + "FOREIGN KEY (companyId) REFERENCES companies(id)"
                + ");";
        String applicationsTable = "CREATE TABLE IF NOT EXISTS applications ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "jobId INTEGER NOT NULL,"
                +"score INTEGER DEFAULT 0,"
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
    public static boolean updateCandidateProfile(Candidate c) {
        String sql = "UPDATE candidates SET "
                + "name = ?, phone = ?, school = ?, college = ?, cgpa = ?, "
                + "hobbies = ?, description = ?, interestedArea = ?, specialization = ?, "
                + "position = ?, education = ?, locationType = ?, passOutYear = ?, language = ? "
                + "WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getPhone());
            pstmt.setString(3, c.getSchool());
            pstmt.setString(4, c.getCollege());
            pstmt.setDouble(5, c.getCgpa());
            pstmt.setString(6, c.getHobbies());
            pstmt.setString(7, c.getDescription());
            pstmt.setString(8, c.getInterestedArea());
            pstmt.setString(9, c.getSpecialization());
            pstmt.setString(10, c.getPosition());
            pstmt.setString(11, c.getEducation());
            pstmt.setString(12, c.getLocationType());
            pstmt.setInt(13, c.getPassOutYear());
            pstmt.setString(14, c.getLanguage());
            pstmt.setInt(15, c.getId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            System.out.println("Error updating candidate profile: " + e.getMessage());
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
    public static boolean updateCompanyProfile(Company c) {
        String sql = "UPDATE companies SET name = ?, contact = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getContact());
            pstmt.setInt(3, c.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            System.out.println("Error updating company profile: " + e.getMessage());
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
        String sql = "INSERT INTO jobs(title, companyId, requiredSkills, stipend, timePeriod, area) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, j.getTitle());
            pstmt.setInt(2, j.getCompanyId());
            pstmt.setString(3, j.getRequiredSkills());
            pstmt.setDouble(4, j.getStipend());
            pstmt.setString(5, j.getTimePeriod());
            pstmt.setString(6, j.getArea());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error posting job: " + e.getMessage());
            return false;
        }
    }
    public static boolean updateJob(Job j) {
        String sql = "UPDATE jobs SET title = ?, requiredSkills = ?, stipend = ?, timePeriod = ?, area = ? "
                   + "WHERE jobId = ? AND companyId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, j.getTitle());
            pstmt.setString(2, j.getRequiredSkills());
            pstmt.setDouble(3, j.getStipend());
            pstmt.setString(4, j.getTimePeriod());
            pstmt.setString(5, j.getArea());
            pstmt.setInt(6, j.getJobId());
            pstmt.setInt(7, j.getCompanyId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            System.out.println("Error updating job: " + e.getMessage());
            return false;
        }
    }
    public static boolean deleteJob(int jobId, int companyId) {
        String sqlApps = "DELETE FROM applications WHERE jobId = ?";
        String sqlJob = "DELETE FROM jobs WHERE jobId = ? AND companyId = ?";
        try (PreparedStatement pstmtApps = conn.prepareStatement(sqlApps);
             PreparedStatement pstmtJob = conn.prepareStatement(sqlJob)) {
            pstmtApps.setInt(1, jobId);
            pstmtApps.executeUpdate();
            pstmtJob.setInt(1, jobId);
            pstmtJob.setInt(2, companyId);
            int affectedRows = pstmtJob.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            System.out.println("Error deleting job: " + e.getMessage());
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
        j.setStipend(rs.getDouble("stipend"));
        j.setTimePeriod(rs.getString("timePeriod"));
        j.setArea(rs.getString("area"));
        return j;
    }
}