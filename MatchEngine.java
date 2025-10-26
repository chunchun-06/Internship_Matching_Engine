import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatchEngine {

    public static int calculateMatch(Job job, Candidate candidate) {
        int score = 0;
        String skills = job.getRequiredSkills().toLowerCase();
        
        if (skills.contains(candidate.getInterestedArea().toLowerCase())) {
            score += 50;
        }
        if (skills.contains(candidate.getSpecialization().toLowerCase())) {
            score += 50;
        }
        if (skills.contains(candidate.getEducation().toLowerCase())) {
            score += 10;
        }
        return score;
    }

    public static List<MatchResult> getRecommendations(Candidate c) {
        List<MatchResult> results = new ArrayList<>();
        List<Job> allJobs = DatabaseManager.getJobs();

        for (Job job : allJobs) {
            int score = calculateMatch(job, c);
            if (score > 0) {
                results.add(new MatchResult(job, score));
            }
        }

        Collections.sort(results, new Comparator<MatchResult>() {
            public int compare(MatchResult m1, MatchResult m2) {
                return Integer.compare(m2.getScore(), m1.getScore());
            }
        });

        return results;
    }
}

class MatchResult {
    Job job;
    int score;

    public MatchResult(Job job, int score) {
        this.job = job;
        this.score = score;
    }

    public Job getJob() { return job; }
    public int getScore() { return score; }

    @Override
    public String toString() {
        return "Job: " + job.getTitle() + " (ID: " + job.getJobId() + ") - Match Score: " + score + "%";
    }
}