import java.util.*;
public class MatchEngine {
    public static int calculateMatch(Job job, Candidate candidate) {
        double score = 0.0;
        Set<String> jobSkills = new HashSet<>();
        String jobReqSkills = (job.getRequiredSkills() != null) ? job.getRequiredSkills().toLowerCase() : "";
        String jobArea = (job.getArea() != null) ? job.getArea().toLowerCase() : "";
        for (String skill : jobReqSkills.split("\\s*,\\s*")) {
            jobSkills.add(skill.trim());
        }
        jobSkills.remove(""); 
        Set<String> candidateSkills = new HashSet<>();
        String candInterested = (candidate.getInterestedArea() != null) ? candidate.getInterestedArea().toLowerCase() : "";
        String candSpecial = (candidate.getSpecialization() != null) ? candidate.getSpecialization().toLowerCase() : "";
        
        for (String skill : candInterested.split("\\s*,\\s*")) {
            candidateSkills.add(skill.trim());
        }
        for (String skill : candSpecial.split("\\s*,\\s*")) {
            candidateSkills.add(skill.trim());
        }
        candidateSkills.remove("");
        if (jobSkills.isEmpty()) {
            return 0;
        }
        Set<String> commonSkills = new HashSet<>(jobSkills);
        commonSkills.retainAll(candidateSkills);
        double matchRatio = (double) commonSkills.size() / jobSkills.size();
        score = matchRatio * 100; 
        String locationType = (candidate.getLocationType() != null) ? candidate.getLocationType().toLowerCase() : "";
        if (locationType.equals("rural")) {
            score = score + 10; 
        }
        return (int) Math.min(Math.round(score), 100);
    }
    public static List<MatchResult> getRecommendations(Candidate c) {
        List<MatchResult> results = new ArrayList<>();
        List<Job> allJobs = DatabaseManager.getJobs(); 
        for (Job job : allJobs) {
            int score = calculateMatch(job, c);
            if (score > 30) { 
                results.add(new MatchResult(job, c, score));
            }
        }
        results.sort((m1, m2) -> Integer.compare(m2.getScore(), m1.getScore()));
        return results;
    }
}
class MatchResult {
    private Job job;
    private Candidate candidate;
    private int score;
    public MatchResult(Job job, Candidate candidate, int score) {
        this.job = job;
        this.candidate = candidate;
        this.score = score;
        this.candidate.setScore(score); 
    }
    public Job getJob() { return job; }
    public Candidate getCandidate() { return candidate; }
    public int getScore() { return score; }
    @Override
    public String toString() {
        return "Job: " + job.getTitle() + " (ID: " + job.getJobId() + ")" +
               " - Match Score: " + score + "/100" +
               "\n    Area: " + job.getArea() +
               " | Stipend: $" + job.getStipend() +
               " | Duration: " + job.getTimePeriod();
    }
}