import java.util.List;
import java.util.Scanner;
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static final String SEPARATOR = "========================================";
    public static void main(String[] args) {
        DatabaseManager.connect();
        DatabaseManager.createTables();
        mainMenu();
    }
    private static void mainMenu() {
        while (true) {
            System.out.println("\n" + SEPARATOR);
            System.out.println("     SMART INTERNSHIP MATCHER");
            System.out.println(SEPARATOR);
            System.out.println("1. Candidate Register");
            System.out.println("2. Candidate Login");
            System.out.println("3. Company Register");
            System.out.println("4. Company Login");
            System.out.println("5. Exit");
            System.out.println(SEPARATOR);
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
        System.out.println("\n--- Candidate Registration ---");
        try {
            Candidate c = new Candidate();
            System.out.print("Name: "); c.setName(sc.nextLine());
            System.out.print("Email (e.g., user@gmail.com): "); String email = sc.nextLine();
            System.out.print("Password: "); String pass = sc.nextLine();
            System.out.print("Retype Password: "); String rePass = sc.nextLine();
            System.out.print("Phone (10 digits): "); String phone = sc.nextLine();
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
            if (!email.toLowerCase().endsWith("@gmail.com")) {
                System.out.println("Invalid email. Must end with '@gmail.com'."); return;
            }
            if (!pass.equals(rePass)) {
                System.out.println("Passwords do not match."); return;
            }
            if (phone.length() != 10 || !phone.matches("\\d+")) {
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
            System.out.println("Invalid input. Please try again. Error: " + e.getMessage());
        }
    }
    private static void candidateLogin() {
        System.out.println("\n--- Candidate Login ---");
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        Candidate c = DatabaseManager.checkCandidateLogin(email, pass);
        if (c != null) {
            System.out.println(SEPARATOR + "\nWelcome, " + c.getName() + "!");
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
            System.out.println("5. Edit My Profile");
            System.out.println("6. Logout");
            System.out.println(SEPARATOR);
            System.out.print("Choose: ");
            String ch = sc.nextLine();
            switch (ch) {
                case "1": viewRecommendations(c); break;
                case "2": viewAllJobs(); break;
                case "3": applyToJob(c); break;
                case "4": viewMyApplications(c); break;
                case "5": editCandidateProfile(c); break;
                case "6": System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private static void editCandidateProfile(Candidate c) {
        System.out.println("\n--- Edit Your Profile ---");
        System.out.println("Note: Email and Aadhar cannot be changed.");
        try {
            System.out.print("Name [" + c.getName() + "]: ");
            c.setName(sc.nextLine());
            System.out.print("Phone (10 digits) [" + c.getPhone() + "]: ");
            String phone = sc.nextLine();
            System.out.print("School Name [" + c.getSchool() + "]: ");
            c.setSchool(sc.nextLine());
            System.out.print("College Name [" + c.getCollege() + "]: ");
            c.setCollege(sc.nextLine());
            System.out.print("CGPA (0.0 - 10.0) [" + c.getCgpa() + "]: ");
            double cgpa = Double.parseDouble(sc.nextLine());
            System.out.print("Hobbies (comma-separated) [" + c.getHobbies() + "]: ");
            c.setHobbies(sc.nextLine());
            System.out.print("Description (max 100 chars) [" + c.getDescription() + "]: ");
            String desc = sc.nextLine();
            System.out.print("Interested Area [" + c.getInterestedArea() + "]: ");
            c.setInterestedArea(sc.nextLine());
            System.out.print("Specialization [" + c.getSpecialization() + "]: ");
            c.setSpecialization(sc.nextLine());
            System.out.print("Position [" + c.getPosition() + "]: ");
            c.setPosition(sc.nextLine());
            System.out.print("Education [" + c.getEducation() + "]: ");
            c.setEducation(sc.nextLine());
            System.out.print("Location Type (Rural/Urban) [" + c.getLocationType() + "]: ");
            c.setLocationType(sc.nextLine());
            System.out.print("Pass Out Year [" + c.getPassOutYear() + "]: ");
            c.setPassOutYear(Integer.parseInt(sc.nextLine()));
            System.out.print("Language [" + c.getLanguage() + "]: ");
            c.setLanguage(sc.nextLine());
            if (phone.length() != 10 || !phone.matches("\\d+")) {
                System.out.println("Phone number must be exactly 10 digits."); return;
            }
            if (cgpa < 0 || cgpa > 10) {
                System.out.println("CGPA must be between 0 and 10."); return;
            }
            if (desc.length() > 100) {
                System.out.println("Description must be 100 characters or less."); return;
            }
            c.setPhone(phone);
            c.setCgpa(cgpa);
            c.setDescription(desc);
            if (DatabaseManager.updateCandidateProfile(c)) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Profile update failed.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again. Error: " + e.getMessage());
        }
    }
    private static void viewRecommendations(Candidate c) {
        System.out.println("\n--- Recommended Jobs ---");
        List<MatchResult> results = MatchEngine.getRecommendations(c);
        if (results.isEmpty()) {
            System.out.println("No recommendations found based on your profile.");
        } else {
            for (MatchResult res : results) {
                System.out.println(SEPARATOR);
                System.out.println(res);
            }
            System.out.println(SEPARATOR);
        }
    }
    private static String getJobDisplayString(Job j) {
        return "ID: " + j.getJobId() + " | Title: " + j.getTitle()
                + "\n    Area: " + j.getArea() + " | Stipend: $" + j.getStipend()
                + " | Duration: " + j.getTimePeriod()
                + "\n    Skills: " + j.getRequiredSkills();
    }
    private static void viewAllJobs() {
        System.out.println("\n--- All Available Jobs ---");
        List<Job> jobs = DatabaseManager.getJobs();
        for (Job j : jobs) {
            System.out.println(SEPARATOR);
            System.out.println(getJobDisplayString(j));
        }
        System.out.println(SEPARATOR);
    }
    private static void applyToJob(Candidate c) {
        System.out.println("\n--- Apply to Job ---");
        viewAllJobs(); // Show them the jobs first
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
                System.out.println(SEPARATOR);
                System.out.println(getJobDisplayString(j));
            }
            System.out.println(SEPARATOR);
        }
    }
    private static void companyRegister() {
        System.out.println("\n--- Company Registration ---");
        try {
            System.out.print("Company Name: "); String name = sc.nextLine();
            System.out.print("Company Email (e.g., user@gmail.com): "); String email = sc.nextLine();
            System.out.print("Password: "); String pass = sc.nextLine();
            System.out.print("Retype Password: "); String rePass = sc.nextLine();
            System.out.print("Contact Phone (10 digits): "); String contact = sc.nextLine();
            System.out.print("Company Code (from provided list): "); String code = sc.nextLine();
            if (!email.toLowerCase().endsWith("@gmail.com")) {
                System.out.println("Invalid email. Must end with '@gmail.com'."); return;
            }
            if (!pass.equals(rePass)) {
                System.out.println("Passwords do not match."); return;
            }
            if (contact.length() != 10 || !contact.matches("\\d+")) {
                System.out.println("Phone number must be exactly 10 digits."); return;
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
            System.out.println("Invalid input. Please try again. Error: " + e.getMessage());
        }
    }
    private static void companyLogin() {
        System.out.println("\n--- Company Login ---");
        System.out.print("Company Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        Company c = DatabaseManager.checkCompanyLogin(email, pass);
        if (c != null) {
            System.out.println(SEPARATOR + "\nWelcome, " + c.getName() + "!");
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
            System.out.println("4. Edit a Job Posting");
            System.out.println("5. Delete a Job Posting");
            System.out.println("6. Edit Company Profile");
            System.out.println("7. Logout");
            System.out.println(SEPARATOR);
            System.out.print("Choose: ");
            String ch = sc.nextLine();
            switch (ch) {
                case "1": postJob(c); break;
                case "2": viewMyJobs(c); break;
                case "3": viewApplicants(c); break;
                case "4": editJob(c); break;
                case "5": deleteJob(c); break;
                case "6": editCompanyProfile(c); break;
                case "7": System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private static void editCompanyProfile(Company c) {
        System.out.println("\n--- Edit Company Profile ---");
        System.out.println("Note: Email and Company Code cannot be changed.");
        try {
            System.out.print("Company Name [" + c.getName() + "]: ");
            String name = sc.nextLine();
            System.out.print("Contact Phone (10 digits) [" + c.getContact() + "]: ");
            String contact = sc.nextLine();

            if (contact.length() != 10 || !contact.matches("\\d+")) {
                System.out.println("Phone number must be exactly 10 digits."); return;
            }
            c.setName(name);
            c.setContact(contact);
            if (DatabaseManager.updateCompanyProfile(c)) {
                System.out.println("Company profile updated successfully!");
            } else {
                System.out.println("Profile update failed.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again. Error: " + e.getMessage());
        }
    }
    private static void postJob(Company c) {
        System.out.println("\n--- Post a New Job ---");
        try {
            System.out.print("Job Title: "); String title = sc.nextLine();
            System.out.print("Required Skills (comma-separated): "); String skills = sc.nextLine();
            System.out.print("Job Area (e.g., Web, AI, Marketing): "); String area = sc.nextLine();
            System.out.print("Stipend (per month): "); double stipend = Double.parseDouble(sc.nextLine());
            System.out.print("Time Period (e.g., 3 Months): "); String period = sc.nextLine();
            Job j = new Job();
            j.setTitle(title);
            j.setRequiredSkills(skills);
            j.setArea(area);
            j.setStipend(stipend);
            j.setTimePeriod(period);
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
                System.out.println(SEPARATOR);
                System.out.println(getJobDisplayString(j));
            }
            System.out.println(SEPARATOR);
        }
    }
    private static void viewApplicants(Company c) {
        System.out.println("\n--- View Applicants ---");
        viewMyJobs(c); // Show their jobs first
        if (DatabaseManager.getCompanyJobs(c.getId()).isEmpty()) {
            return; // No jobs to view applicants for
        }
        try {
            System.out.print("\nEnter Job ID to view applicants: ");
            int jobId = Integer.parseInt(sc.nextLine());
            List<Candidate> candidates = DatabaseManager.getApplicantsForJob(jobId);
            if (candidates.isEmpty()) {
                System.out.println("No applicants for this job yet.");
            } else {
                System.out.println("\nApplicants for Job ID " + jobId + ":");
                for (Candidate cand : candidates) {
                    System.out.println(SEPARATOR);
                    System.out.println("  - Name: " + cand.getName() + " | Email: " + cand.getEmail() + "  | Score: " + cand.getScore() + " | Phone: " + cand.getPhone());
                    System.out.println("    Skills: " + cand.getInterestedArea() + ", " + cand.getSpecialization());
                    System.out.println("    CGPA: " + cand.getCgpa() + " | Education: " + cand.getEducation());
                }
                System.out.println(SEPARATOR);
            }
        } catch (Exception e) {
            System.out.println("Invalid Job ID.");
        }
    }
    private static void editJob(Company c) {
        System.out.println("\n--- Edit Job Posting ---");
        viewMyJobs(c);
        if (DatabaseManager.getCompanyJobs(c.getId()).isEmpty()) {
            return;
        }
        try {
            System.out.print("\nEnter Job ID to edit: ");
            int jobId = Integer.parseInt(sc.nextLine());
            Job jobToEdit = null;
            for(Job j : DatabaseManager.getCompanyJobs(c.getId())) {
                if(j.getJobId() == jobId) {
                    jobToEdit = j;
                    break;
                }
            }
            if(jobToEdit == null) {
                System.out.println("Invalid Job ID or this job does not belong to you.");
                return;
            }
            System.out.print("Job Title [" + jobToEdit.getTitle() + "]: "); 
            String title = sc.nextLine();
            System.out.print("Required Skills [" + jobToEdit.getRequiredSkills() + "]: "); 
            String skills = sc.nextLine();
            System.out.print("Job Area [" + jobToEdit.getArea() + "]: "); 
            String area = sc.nextLine();
            System.out.print("Stipend [" + jobToEdit.getStipend() + "]: "); 
            double stipend = Double.parseDouble(sc.nextLine());
            System.out.print("Time Period [" + jobToEdit.getTimePeriod() + "]: "); 
            String period = sc.nextLine();
            Job updatedJob = new Job();
            updatedJob.setJobId(jobId);
            updatedJob.setCompanyId(c.getId());
            updatedJob.setTitle(title.isEmpty() ? jobToEdit.getTitle() : title);
            updatedJob.setRequiredSkills(skills.isEmpty() ? jobToEdit.getRequiredSkills() : skills);
            updatedJob.setArea(area.isEmpty() ? jobToEdit.getArea() : area);
            updatedJob.setStipend(stipend); // Assuming they must re-enter number
            updatedJob.setTimePeriod(period.isEmpty() ? jobToEdit.getTimePeriod() : period);
            if (DatabaseManager.updateJob(updatedJob)) {
                System.out.println("Job updated successfully!");
            } else {
                System.out.println("Failed to update job.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Error: " + e.getMessage());
        }
    }
    private static void deleteJob(Company c) {
        System.out.println("\n--- Delete Job Posting ---");
        viewMyJobs(c);
        if (DatabaseManager.getCompanyJobs(c.getId()).isEmpty()) {
            return;
        }
        try {
            System.out.print("\nEnter Job ID to DELETE: ");
            int jobId = Integer.parseInt(sc.nextLine());
            
            System.out.print("Are you sure you want to delete Job ID " + jobId + "? This cannot be undone. (y/n): ");
            String confirmation = sc.nextLine();
            if(confirmation.equalsIgnoreCase("y")) {
                if (DatabaseManager.deleteJob(jobId, c.getId())) {
                    System.out.println("Job " + jobId + " and all associated applications have been deleted.");
                } else {
                    System.out.println("Failed to delete job. (It may not exist or belong to you).");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("Invalid Job ID.");
        }
    }
}