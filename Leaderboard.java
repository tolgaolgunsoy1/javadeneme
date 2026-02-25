import java.io.*;
import java.util.*;

public class Leaderboard {
    private List<Score> scores;
    private String filename = "leaderboard.txt";
    
    public Leaderboard() {
        scores = new ArrayList<>();
        loadScores();
    }
    
    public void addScore(String playerName, int score) {
        scores.add(new Score(playerName, score));
        Collections.sort(scores);
        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        saveScores();
    }
    
    public List<Score> getTopScores() {
        return new ArrayList<>(scores);
    }
    
    private void loadScores() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    scores.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
            }
            Collections.sort(scores);
        } catch (IOException e) {
            // File doesn't exist yet
        }
    }
    
    private void saveScores() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Score score : scores) {
                pw.println(score.name + "," + score.score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class Score implements Comparable<Score> {
        String name;
        int score;
        
        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }
        
        @Override
        public int compareTo(Score other) {
            return Integer.compare(other.score, this.score);
        }
    }
}
