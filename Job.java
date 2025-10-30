public class Job {
    int jobId;
    String title;
    int companyId;
    String requiredSkills;
    double stipend;
    String timePeriod;
    String area;

    public int getJobId() { return jobId; }
    public void setJobId(int id) { this.jobId = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int id) { this.companyId = id; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String skills) { this.requiredSkills = skills; }
    public double getStipend() { return stipend; }
    public void setStipend(double stipend) { this.stipend = stipend; }
    public String getTimePeriod() { return timePeriod; }
    public void setTimePeriod(String timePeriod) { this.timePeriod = timePeriod; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
}