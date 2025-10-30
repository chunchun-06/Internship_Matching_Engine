import java.util.List;
import java.util.Scanner;
public class CompanyUI {
    private static Scanner sc;
    public static void init(Scanner scanner) {
        sc = scanner;
    }
    public static void register() {
        UIHelper.printMenuTitle("Company Registration");
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
    public static void login() {
        UIHelper.printMenuTitle("Company Login");
        System.out.print("Company Email: "); String email = sc.nextLine();
        System.out.print("Password: "); String pass = sc.nextLine();
        Company c = DatabaseManager.checkCompanyLogin(email, pass);
        if (c != null) {
            System.out.println(UIHelper.SEPARATOR + "\nWelcome, " + c.getName() + "!");
            showMenu(c);
        } else {
            System.out.println("Invalid login.");
        }
    }
    private static void showMenu(Company c) {
        while (true) {
            UIHelper.printMenuTitle("Company Menu");
            System.out.println("1. Post a New Job");
            System.out.println("2. View My Posted Jobs");
            System.out.println("3. View Applicants for a Job");
            System.out.println("4. Edit a Job Posting");
            System.out.println("5. Delete a Job Posting");
            System.out.println("6. Edit Company Profile");
            System.out.println("7. Logout");
            UIHelper.printSeparator();
            System.out.print("Choose: ");
            String ch = sc.nextLine();
            switch (ch) {
                case "1": postJob(c); break;
                case "2": viewMyJobs(c); break;
                case "3": viewApplicants(c); break;
                case "4": editJob(c); break;
                case "5": deleteJob(c); break;
                case "6": editProfile(c); break;
                case "7": System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    private static void editProfile(Company c) {
        UIHelper.printMenuTitle("Edit Company Profile");
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
        UIHelper.printMenuTitle("Post a New Job");
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
        UIHelper.printMenuTitle("My Posted Jobs");
        List<Job> jobs = DatabaseManager.getCompanyJobs(c.getId());
        UIHelper.displayJobs(jobs);
    }
    private static void viewApplicants(Company c) {
        UIHelper.printMenuTitle("View Applicants");
        viewMyJobs(c); 
        if (DatabaseManager.getCompanyJobs(c.getId()).isEmpty()) {
            return; 
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
                    UIHelper.printSeparator();
                    System.out.println("  - Name: " + cand.getName() + " | Email: " + cand.getEmail() + "  | Score: " + cand.getScore() + " | Phone: " + cand.getPhone());
                    System.out.println("    Skills: " + cand.getInterestedArea() + " | Specialization: " + cand.getSpecialization());
                    System.out.println("    CGPA: " + cand.getCgpa() + " | Education: " + cand.getEducation());
                }
                UIHelper.printSeparator();
            }
        } catch (Exception e) {
            System.out.println("Invalid Job ID.");
        }
    }
    private static void editJob(Company c) {
        UIHelper.printMenuTitle("Edit Job Posting");
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
            updatedJob.setCompanyId(c.getId()); // Set company ID for the update query
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
        UIHelper.printMenuTitle("Delete Job Posting");
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