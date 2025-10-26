import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        CompanyAPIManager.fetchAndSaveCompanies();
        DatabaseManager.connect();
        DatabaseManager.createTables();
        mainMenu();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n===== SMART INTERNSHIP MATCHER =====");
            System.out.println("1. Candidate Register");
            System.out.println("2. Candidate Login");
            System.out.println("3. Company Register");
            System.out.println("4. Company Login");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1": candidateRegister(); break;
                case "2": candidateLogin(); break;
                case "3": companyRegister(); break;
                case "4": companyLogin(); break;
                case "5": System.out.println("Bye!"); System.exit(0);
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void candidateRegister() {
        try {
            Candidate c = new Candidate();
            System.out.print("Name: "); c.setName(sc.nextLine());
            System.out.print("Email: "); String email = sc.nextLine();
            System.out.print("Password: "); String pass = sc.nextLine();
            System.out.print("Retype Password: "); String rePass = sc.nextLine();
            System.out.print("Phone: "); String phone = sc.nextLine();
            System.out.print("Aadhar Number: "); c.setAdhar(sc.nextLine());
            System.out.print("School Name: "); c.setSchool(sc.nextLine());
            System.out.print("College Name: "); c.setCollege(sc.nextLine());
            System.out.print("CGPA (0.0 - 10.0): "); double cgpa = Double.parseDouble(sc.nextLine());
            System.out.print("Hobbies (comma-separated): "); c.setHobbies(sc.nextLine());
            System.out.print("Description (max 100 chars): "); String desc = sc.nextLine();
            System.out.print("Interested Area (e.g., Java, Python): "); c.setInterestedArea(sc.nextLine());
            System.out.print("Specialization (e.g., AI, Web): "); c.setSpecialization(sc.nextLine());
            System.out.print("Position (e.g., Developer): "); c.setPosition(sc.nextLine());
            System.out.print("Education (e.g., BE, BTech): "); c.setEducation(sc.nextLine());
            System.out.print("Location Type (Rural/Urban): "); c.setLocationType(sc.nextLine());
            System.out.print("Pass Out Year: "); c.setPassOutYear(Integer.parseInt(sc.nextLine()));
            System.out.print("Language (e.g., English): "); c.setLanguage(sc.nextLine());

            if (!email.contains("@")) {
                System.out.println("Invalid email. Must contain '@'."); return;
            }
            if (!pass.equals(rePass)) {
                System.out.println("Passwords do not match."); return;
            }
            if (phone.length() != 10) {
                System.out.println("Phone number must be exactly 10 digits."); return;
            }
            if (cgpa < 0 || cgpa > 10) {
                System.out.println("CGPA must be between 0 and 10."); return;
            }
            if (desc.length() > 100) {
                System.out.println("Description must be 100 characters or less."); return;
            }

            c.setEmail(email);
            c.setPassword(pass);
            c.setPhone(phone);
            c.setCgpa(cgpa);
            c.setDescription(desc);

            if (DatabaseManager.registerCandidate(c)) {
                System.out.println("Candidate registered successfully!");
            } else {
                System.out.println("Registration failed (Email or Adhar might already exist).");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void candidateLogin() {
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();

        Candidate c = DatabaseManager.checkCandidateLogin(email, pass);
        if (c != null) {
            System.out.println("Welcome, " + c.getName() + "!");
            candidateMenu(c);
        } else {
            System.out.println("Invalid login.");
        }
    }

    private static void candidateMenu(Candidate c) {
        while (true) {
            System.out.println("\n--- Candidate Menu ---");
            System.out.println("1. View Recommended Jobs");
            System.out.println("2. View All Jobs");
            System.out.println("3. Apply to Job");
            System.out.println("4. View My Applications");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1": viewRecommendations(c); break;
                case "2": viewAllJobs(); break;
                case "3": applyToJob(c); break;
                case "4": viewMyApplications(c); break;
                case "5": System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewRecommendations(Candidate c) {
        System.out.println("\n--- Recommended Jobs ---");
        List<MatchResult> results = MatchEngine.getRecommendations(c);
        if (results.isEmpty()) {
            System.out.println("No recommendations found based on your profile.");
        } else {
            for (MatchResult res : results) {
                System.out.println(res);
            }
        }
    }

    private static void viewAllJobs() {
        System.out.println("\n--- All Available Jobs ---");
        List<Job> jobs = DatabaseManager.getJobs();
        for (Job j : jobs) {
            System.out.println("ID: " + j.getJobId() + " | Title: " + j.getTitle() + " | Skills: " + j.getRequiredSkills());
        }
    }

    private static void applyToJob(Candidate c) {
        try {
            System.out.print("Enter Job ID to apply: ");
            int jobId = Integer.parseInt(sc.nextLine());
            if (DatabaseManager.applyToJob(jobId, c.getId())) {
                System.out.println("Successfully applied to job ID " + jobId);
            } else {
                System.out.println("Failed to apply (maybe already applied or job ID is invalid).");
            }
        } catch (Exception e) {
            System.out.println("Invalid Job ID.");
        }
    }

    private static void viewMyApplications(Candidate c) {
        System.out.println("\n--- My Applications ---");
        List<Job> jobs = DatabaseManager.getMyApplications(c.getId());
        if (jobs.isEmpty()) {
            System.out.println("You have not applied to any jobs.");
        } else {
            for (Job j : jobs) {
                System.out.println("ID: " + j.getJobId() + " | Title: " + j.getTitle() + " | Skills: " + j.getRequiredSkills());
            }
        }
    }

    private static void companyRegister() {
        try {
            System.out.print("Company Name: "); String name = sc.nextLine();
            System.out.print("Company Email: "); String email = sc.nextLine();
            System.out.print("Password: "); String pass = sc.nextLine();
            System.out.print("Retype Password: "); String rePass = sc.nextLine();
            System.out.print("Contact Phone: "); String contact = sc.nextLine();
            System.out.print("Company Code (from provided list): "); String code = sc.nextLine();

            if (!email.contains("@")) {
                System.out.println("Invalid email. Must contain '@'."); return;
            }
            if (!pass.equals(rePass)) {
                System.out.println("Passwords do not match."); return;
            }

            if (!CompanyAPIManager.checkCompanyExists(code)) {
                System.out.println("Company Code not found in our records. Registration failed.");
                return;
            }

            Company c = new Company();
            c.setName(name);
            c.setEmail(email);
            c.setPassword(pass);
            c.setContact(contact);
            c.setCompanyCode(code);

            if (DatabaseManager.registerCompany(c)) {
                System.out.println("Company registered successfully!");
            } else {
                System.out.println("Registration failed (Email or Company Code might already be registered).");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void companyLogin() {
        System.out.print("Company Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();

        Company c = DatabaseManager.checkCompanyLogin(email, pass);
        if (c != null) {
            System.out.println("Welcome, " + c.getName() + "!");
            companyMenu(c);
        } else {
            System.out.println("Invalid login.");
        }
    }

    private static void companyMenu(Company c) {
        while (true) {
            System.out.println("\n--- Company Menu ---");
            System.out.println("1. Post a New Job");
            System.out.println("2. View My Posted Jobs");
            System.out.println("3. View Applicants for a Job");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            String ch = sc.nextLine();

            switch (ch) {
                case "1": postJob(c); break;
                case "2": viewMyJobs(c); break;
                case "3": viewApplicants(c); break;
                case "4": System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void postJob(Company c) {
        try {
            System.out.print("Job Title: "); String title = sc.nextLine();
            System.out.print("Required Skills (comma-separated): "); String skills = sc.nextLine();

            Job j = new Job();
            j.setTitle(title);
            j.setRequiredSkills(skills);
            j.setCompanyId(c.getId());

            if (DatabaseManager.postJob(j)) {
                System.out.println("Job posted successfully!");
            } else {
                System.out.println("Failed to post job.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewMyJobs(Company c) {
        System.out.println("\n--- My Posted Jobs ---");
        List<Job> jobs = DatabaseManager.getCompanyJobs(c.getId());
        if (jobs.isEmpty()) {
            System.out.println("You have not posted any jobs.");
        } else {
            for (Job j : jobs) {
                System.out.println("ID: " + j.getJobId() + " | Title: " + j.getTitle() + " | Skills: " + j.getRequiredSkills());
            }
        }
    }

    private static void viewApplicants(Company c) {
        System.out.println("\n--- View Applicants ---");
        viewMyJobs(c); 
        try {
            System.out.print("\nEnter Job ID to view applicants: ");
            int jobId = Integer.parseInt(sc.nextLine());

            List<Candidate> candidates = DatabaseManager.getApplicantsForJob(jobId);
            if (candidates.isEmpty()) {
                System.out.println("No applicants for this job yet.");
            } else {
                System.out.println("\nApplicants for Job ID " + jobId + ":");
                for (Candidate cand : candidates) {
                    System.out.println("  - Name: " + cand.getName() + " | Email: " + cand.getEmail() + " | Phone: " + cand.getPhone());
                    System.out.println("    Skills: " + cand.getInterestedArea() + ", " + cand.getSpecialization() + " | CGPA: " + cand.getCgpa());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid Job ID.");
        }
    }
}