import java.util.List;
import java.util.Scanner;
public class CandidateUI {
    private static Scanner sc;
    public static void init(Scanner scanner) {
        sc = scanner;
    }


    public static void register() {
        UIHelper.printMenuTitle("Candidate Registration");
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

    public static void login() {
        UIHelper.printMenuTitle("Candidate Login");
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        Candidate c = DatabaseManager.checkCandidateLogin(email, pass);
        if (c != null) {
            System.out.println(UIHelper.SEPARATOR + "\nWelcome, " + c.getName() + "!");
            showMenu(c);
        } else {
            System.out.println("Invalid login.");
        }
    }

    private static void showMenu(Candidate c) {
        while (true) {
            UIHelper.printMenuTitle("Candidate Menu");
            System.out.println("1. View Recommended Jobs");
            System.out.println("2. View All Jobs");
            System.out.println("3. Apply to Job");
            System.out.println("4. View My Applications");
            System.out.println("5. Edit My Profile");
            System.out.println("6. Logout");
            UIHelper.printSeparator();
            System.out.print("Choose: ");
            String ch = sc.nextLine();
            switch (ch) {
                case "1": viewRecommendations(c); break;
                case "2": viewAllJobs(); break;
                case "3": applyToJob(c); break;
                case "4": viewMyApplications(c); break;
                case "5": editProfile(c); break;
                case "6": System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private static void editProfile(Candidate c) {
        UIHelper.printMenuTitle("Edit Your Profile");
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
        UIHelper.printMenuTitle("Recommended Jobs");
        List<MatchResult> results = MatchEngine.getRecommendations(c);
        if (results.isEmpty()) {
            System.out.println("No recommendations found based on your profile.");
        } else {
            for (MatchResult res : results) {
                UIHelper.printSeparator();
                System.out.println(res);
            }
            UIHelper.printSeparator();
        }
    }

    private static void viewAllJobs() {
        UIHelper.printMenuTitle("All Available Jobs");
        List<Job> jobs = DatabaseManager.getJobs();
        UIHelper.displayJobs(jobs);
    }

    private static void applyToJob(Candidate c) {
        UIHelper.printMenuTitle("Apply to Job");
        viewAllJobs(); // Show them the jobs first
        List<Job> jobs = DatabaseManager.getJobs();
        if (jobs.isEmpty()) {
            return;
        }
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
        UIHelper.printMenuTitle("My Applications");
        List<Job> jobs = DatabaseManager.getMyApplications(c.getId());
        UIHelper.displayJobs(jobs);
    }
}