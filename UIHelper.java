import java.util.List;


public class UIHelper {

    public static final String SEPARATOR = "========================================";

    public static void printSectionTitle(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("     " + title);
        System.out.println(SEPARATOR);
    }

    public static void printMenuTitle(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public static void printSeparator() {
        System.out.println(SEPARATOR);
    }

    public static String getJobDisplayString(Job j) {
        return "ID: " + j.getJobId() + " | Title: " + j.getTitle()
                + "\n    Area: " + j.getArea() + " | Stipend: $" + j.getStipend()
                + " | Duration: " + j.getTimePeriod()
                + "\n    Skills: " + j.getRequiredSkills();
    }

    public static void displayJobs(List<Job> jobs) {
        if (jobs.isEmpty()) {
            System.out.println("No jobs found.");
        } else {
            for (Job j : jobs) {
                printSeparator();
                System.out.println(getJobDisplayString(j));
            }
            printSeparator();
        }
    }
}