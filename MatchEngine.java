import java.util.*;

public class MatchEngine {

    /**
     * Calculates the match score between a job and a candidate
     * This version ONLY bases the score on matching skills.
     */
    public static int calculateMatch(Job job, Candidate candidate) {
        double score = 0.0;

        // --- 1. Job Skills ---
        Set<String> jobSkills = new HashSet<>();
        String jobReqSkills = (job.getRequiredSkills() != null) ? job.getRequiredSkills().toLowerCase() : "";
        String jobArea = (job.getArea() != null) ? job.getArea().toLowerCase() : "";
        
        for (String skill : jobReqSkills.split("\\s*,\\s*")) {
            jobSkills.add(skill.trim());
        }
        // This loop is commented out, as you requested
        // for (String skill : jobArea.split("\\s*,\\s*")) {
        //     jobSkills.add(skill.trim());
        // }

        jobSkills.remove(""); // remove empty entries

        // --- 2. Candidate Skills ---
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

        // --- 3. Skill Match Calculation ---
        
        if (jobSkills.isEmpty()) {
            return 0;
        }

        Set<String> commonSkills = new HashSet<>(jobSkills);
        commonSkills.retainAll(candidateSkills);

        double matchRatio = (double) commonSkills.size() / jobSkills.size();
        score = matchRatio * 100; // This will be 80 for the (4/5) match

        
        // --- 4. RURAL BONUS (NEWLY ADDED) ---
        String locationType = (candidate.getLocationType() != null) ? candidate.getLocationType().toLowerCase() : "";
        
        // Check if the location is 'rural'
        if (locationType.equals("rural")) {
            // --- THIS LINE IS CHANGED ---
            score = score + 10; // Apply +10 point bonus
        }
        // --- END OF NEW SECTION ---


        // --- 5. Return the final score ---
        // Round to the nearest whole number and ensure it doesn't go over 100.
        return (int) Math.min(Math.round(score), 100);
    }

    // Generates a ranked list of job recommendations for a given candidate
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

// --- MatchResult class ---
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