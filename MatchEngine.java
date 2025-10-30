import java.util.*;

public class MatchEngine {

    // Calculates the match score between a job and a candidate
    public static int calculateMatch(Job job, Candidate candidate) {
        double score = 0.0;

        // --- 1. Job Skills ---
        Set<String> jobSkills = new HashSet<>();
        String jobReqSkills = (job.getRequiredSkills() != null) ? job.getRequiredSkills().toLowerCase() : "";
        String jobArea = (job.getArea() != null) ? job.getArea().toLowerCase() : "";
        jobSkills.addAll(Arrays.asList(jobReqSkills.split("\\s*,\\s*")));
        jobSkills.addAll(Arrays.asList(jobArea.split("\\s*,\\s*")));
        jobSkills.remove(""); // remove empty entries

        // --- 2. Candidate Skills ---
        Set<String> candidateSkills = new HashSet<>();
        String candInterested = (candidate.getInterestedArea() != null) ? candidate.getInterestedArea().toLowerCase() : "";
        String candSpecial = (candidate.getSpecialization() != null) ? candidate.getSpecialization().toLowerCase() : "";
        candidateSkills.addAll(Arrays.asList(candInterested.split("\\s*,\\s*")));
        candidateSkills.addAll(Arrays.asList(candSpecial.split("\\s*,\\s*")));
        candidateSkills.remove("");

        // --- 3. Skill Match Calculation ---
        Set<String> commonSkills = new HashSet<>(jobSkills);
        commonSkills.retainAll(candidateSkills);

        if (!jobSkills.isEmpty()) {
            double matchRatio = (double) commonSkills.size() / jobSkills.size();
            score += matchRatio * 100;  // base percentage
        }

        // --- 4. Education Match ---
        String candEducation = (candidate.getEducation() != null) ? candidate.getEducation().toLowerCase() : "";
        if (!candEducation.isEmpty() && jobReqSkills.contains(candEducation)) {
            score += 15;
        }

        // --- 5. CGPA Bonus ---
        if (candidate.getCgpa() >= 8.0) {
            score += 15;
        } else if (candidate.getCgpa() >= 7.0) {
            score += 10;
        }

        // --- 6. Position Match ---
        String candPosition = (candidate.getPosition() != null) ? candidate.getPosition().toLowerCase() : "";
        String jobTitle = (job.getTitle() != null) ? job.getTitle().toLowerCase() : "";
        if (!candPosition.isEmpty() && jobTitle.contains(candPosition)) {
            score += 10;
        }

        // --- 7. Region Bonus (+10% for Rural) ---
        String region = (candidate.getLocationType() != null) ? candidate.getLocationType().toLowerCase() : "";
        if (region.contains("rural")) {
            score *= 1.10; // add 10% bonus
        }

        // --- 8. Cap final score at 100 ---
        return (int) Math.min(Math.round(score), 100);
    }

    // Generates a ranked list of job recommendations for a given candidate
    public static List<MatchResult> getRecommendations(Candidate c) {
        List<MatchResult> results = new ArrayList<>();
        List<Job> allJobs = DatabaseManager.getJobs(); // fetch all jobs from data source

        for (Job job : allJobs) {
            int score = calculateMatch(job, c);
            if (score > 30) { // threshold to consider a match
                results.add(new MatchResult(job, c, score));
            }
        }

        // Sort results in descending order of match score
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
        this.candidate.setScore(score); // assign calculated score to candidate
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
