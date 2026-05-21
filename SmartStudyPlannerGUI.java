import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// --- Model ---
class StudySession {
    private String subject;
    private int duration;
    private String difficulty;

    public StudySession(String subject, int duration, String difficulty) {
        this.subject = subject;
        this.duration = duration;
        this.difficulty = difficulty;
    }

    public String getSubject() {
        return subject;
    }

    public int getDuration() {
        return duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String toString() {
        return subject + " - " + duration + " mins (" + difficulty + ")";
    }
}

class StudyPlanner {
    private ArrayList<StudySession> sessions;

    public StudyPlanner() {
        sessions = new ArrayList<>();
    }

    public void addSession(StudySession session) {
        sessions.add(session);
    }

    public ArrayList<StudySession> getSessions() {
        return sessions;
    }

    public int getTotalTime() {
        return sessions.stream().mapToInt(StudySession::getDuration).sum();
    }

    public String getFeedback() {
        int totalTime = getTotalTime();
        long hardCount = sessions.stream().filter(s -> s.getDifficulty().equalsIgnoreCase("Hard")).count();

        if (totalTime > 300) {
            return "You've studied over 5 hours today. Great job, but don't forget to rest!";
        } else if (hardCount > 3) {
            return "Consider mixing in some easier sessions to avoid burnout.";
        } else if (totalTime < 60) {
            return "Try to dedicate at least an hour to studying.";
        } else {
            return "You're maintaining a balanced study plan. Keep it up!";
        }
    }

    public Map<String, Integer> getDifficultyCount() {
        Map<String, Integer> map = new HashMap<>();
        for (StudySession s : sessions) {
            map.put(s.getDifficulty(), map.getOrDefault(s.getDifficulty(), 0) + 1);
        }
        return map;
    }
}

// --- Motivational Quote ---
class MotivationalQuote {
    private static final String[] quotes = {
        "Keep pushing forward!",
        "Success is built on consistency.",
        "Small steps lead to big results.",
        "Study hard, dream big!",
        "Stay focused and never give up!"
    };

    public static String getQuote() {
        Random rand = new Random();
        return quotes[rand.nextInt(quotes.length)];
    }
}

// --- Custom Pie Chart Panel ---
class StudyChartPanel extends JPanel {
    private Map<String, Integer> difficultyCount;

    public void setDifficultyData(Map<String, Integer> data) {
        this.difficultyCount = data;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (difficultyCount == null || difficultyCount.isEmpty()) return;

        int total = difficultyCount.values().stream().mapToInt(Integer::intValue).sum();
        int startAngle = 0;

        Map<String, Color> colorMap = new HashMap<>();
        colorMap.put("Easy", Color.GREEN);
        colorMap.put("Medium", Color.ORANGE);
        colorMap.put("Hard", Color.RED);

        for (String key : difficultyCount.keySet()) {
            int value = difficultyCount.get(key);
            int angle = (int) Math.round(360.0 * value / total);
            g.setColor(colorMap.getOrDefault(key, Color.GRAY));
            g.fillArc(10, 10, 150, 150, startAngle, angle);
            startAngle += angle;
        }

        // Legend
        int y = 170;
        for (String key : difficultyCount.keySet()) {
            g.setColor(colorMap.getOrDefault(key, Color.GRAY));
            g.fillRect(10, y, 15, 15);
            g.setColor(Color.BLACK);
            g.drawString(key + ": " + difficultyCount.get(key), 30, y + 12);
            y += 20;
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(180, 220);
    }
}

// --- GUI with Progress Bars and Chart ---
public class SmartStudyPlannerGUI extends JFrame {
    private StudyPlanner planner;
    private JTextField subjectField, durationField;
    private JComboBox<String> difficultyBox;
    private JTextArea sessionArea;
    private JLabel feedbackLabel, quoteLabel;
    private JProgressBar timeProgress, easyBar, mediumBar, hardBar;
    private StudyChartPanel chartPanel;

    public SmartStudyPlannerGUI() {
        planner = new StudyPlanner();
        setTitle("Smart Study Planner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        subjectField = new JTextField();
        durationField = new JTextField();
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        JButton addButton = new JButton("Add Session");
        addButton.addActionListener(e -> addSession());

        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Duration (mins):"));
        inputPanel.add(durationField);
        inputPanel.add(new JLabel("Difficulty:"));
        inputPanel.add(difficultyBox);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center Session Display
        sessionArea = new JTextArea();
        sessionArea.setEditable(false);
        add(new JScrollPane(sessionArea), BorderLayout.CENTER);

        // Right Stats Panel
        JPanel statsPanel = new JPanel(new BorderLayout());
        JPanel progressPanel = new JPanel(new GridLayout(4, 1));
        timeProgress = new JProgressBar(0, 300);
        easyBar = new JProgressBar(0, 10);
        mediumBar = new JProgressBar(0, 10);
        hardBar = new JProgressBar(0, 10);
        timeProgress.setStringPainted(true);
        easyBar.setStringPainted(true);
        mediumBar.setStringPainted(true);
        hardBar.setStringPainted(true);
        progressPanel.add(labeledBar("Total Study Time", timeProgress));
        progressPanel.add(labeledBar("Easy Sessions", easyBar));
        progressPanel.add(labeledBar("Medium Sessions", mediumBar));
        progressPanel.add(labeledBar("Hard Sessions", hardBar));

        statsPanel.add(progressPanel, BorderLayout.NORTH);

        chartPanel = new StudyChartPanel();
        statsPanel.add(chartPanel, BorderLayout.SOUTH);

        add(statsPanel, BorderLayout.EAST);

        // Bottom Feedback Panel
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        feedbackLabel = new JLabel("Feedback will appear here.", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
        feedbackLabel.setForeground(Color.BLUE);

        quoteLabel = new JLabel(MotivationalQuote.getQuote(), SwingConstants.CENTER);
        quoteLabel.setFont(new Font("Serif", Font.ITALIC, 14));
        quoteLabel.setForeground(new Color(0, 128, 0));

        bottomPanel.add(feedbackLabel);
        bottomPanel.add(quoteLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel labeledBar(String label, JProgressBar bar) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        return panel;
    }

    private void addSession() {
        try {
            String subject = subjectField.getText();
            int duration = Integer.parseInt(durationField.getText());
            String difficulty = (String) difficultyBox.getSelectedItem();

            if (subject.isEmpty() || duration <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter valid inputs.");
                return;
            }

            planner.addSession(new StudySession(subject, duration, difficulty));
            updateUIData();

            subjectField.setText("");
            durationField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duration must be a valid number.");
        }
    }

    private void updateUIData() {
        sessionArea.setText("");
        for (StudySession s : planner.getSessions()) {
            sessionArea.append(s.toString() + "\n");
        }

        feedbackLabel.setText(planner.getFeedback());
        quoteLabel.setText(MotivationalQuote.getQuote());

        int totalTime = planner.getTotalTime();
        timeProgress.setValue(Math.min(totalTime, 300));

        Map<String, Integer> counts = planner.getDifficultyCount();
        easyBar.setValue(counts.getOrDefault("Easy", 0));
        mediumBar.setValue(counts.getOrDefault("Medium", 0));
        hardBar.setValue(counts.getOrDefault("Hard", 0));

        chartPanel.setDifficultyData(counts);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmartStudyPlannerGUI().setVisible(true));
    }
}
