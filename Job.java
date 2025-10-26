public class Job {
    int jobId;
    String title;
    int companyId;
    String requiredSkills;

    public int getJobId() { return jobId; }
    public void setJobId(int id) { this.jobId = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int id) { this.companyId = id; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String skills) { this.requiredSkills = skills; }
}