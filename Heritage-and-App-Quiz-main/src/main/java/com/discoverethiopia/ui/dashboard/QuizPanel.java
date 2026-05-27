package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.dao.HeritageDAO;
import com.discoverethiopia.dao.QuizDAO;
import com.discoverethiopia.model.HeritageSite;
import com.discoverethiopia.model.Question;
import com.discoverethiopia.model.User;
import com.discoverethiopia.ui.components.RoundedButton;
import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizPanel extends JPanel {

    // =========================
    // DATA & DAO
    // =========================
    private final User currentUser;
    private final HeritageDAO heritageDAO;
    private final QuizDAO quizDAO;

    // Reference to the main dashboard (to open suggestion panel)
    private MainDashboard dashboard;

    // UI components
    private JComboBox<HeritageSiteComboItem> siteComboBox;
    private RoundedButton startButton;
    private JPanel centerPanel;
    private CardLayout cardLayout;

    // Quiz state
    private List<Question> currentQuestions;
    private int currentQuestionIndex;
    private List<Character> userAnswers; // index -> answer (A/B/C/D) or null
    private int siteIdForQuiz;

    // Question view widgets
    private JLabel questionNumberLabel;
    private JTextArea questionTextArea;
    private JRadioButton[] optionRadios;
    private ButtonGroup optionGroup;
    private RoundedButton nextButton;
    private JLabel feedbackLabel;


    /**
     * Called by MainDashboard to give this panel a reference to the dashboard.
     */
    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }


    // =========================
    // CONSTRUCTOR & SETUP
    // =========================
    public QuizPanel(User user) {
        this.currentUser = user;
        this.heritageDAO = new HeritageDAO();
        this.quizDAO = new QuizDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // NORTH: Site selection panel
        add(createTopPanel(), BorderLayout.NORTH);

        // CENTER: CardLayout for welcome / question / result
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        centerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        centerPanel.add(createWelcomePanel(), "WELCOME");
        add(centerPanel, BorderLayout.CENTER);

        // SOUTH: Suggestion row (always visible)
        add(createBottomSuggestionPanel(), BorderLayout.SOUTH);

        // Load heritage sites into combo box
        loadHeritageSites();
    }

    // =========================
    // TOP PANEL (heritage selection)
    // =========================
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(UIConstants.PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel selectLabel = new JLabel("Select Heritage Site:");
        selectLabel.setFont(UIConstants.NORMAL_FONT);
        selectLabel.setForeground(Color.WHITE);

        siteComboBox = new JComboBox<>();
        siteComboBox.setFont(UIConstants.COMBOBOX_FONT);
        siteComboBox.setPreferredSize(new Dimension(250, 35));

        startButton = new RoundedButton("Start Quiz");
        startButton.addActionListener(e -> startQuiz());

        topPanel.add(selectLabel);
        topPanel.add(siteComboBox);
        topPanel.add(startButton);

        return topPanel;
    }

    // =========================
    // WELCOME PANEL (shown before quiz starts)
    // =========================
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel welcomeLabel = new JLabel("Choose a heritage site and click 'Start Quiz'");
        welcomeLabel.setFont(UIConstants.TITLE_FONT);
        welcomeLabel.setForeground(UIConstants.PRIMARY_COLOR);
        panel.add(welcomeLabel);
        return panel;
    }

    // =========================
    // BOTTOM SUGGESTION ROW (same as HeritagePanel)
    // =========================
    private JPanel createBottomSuggestionPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JLabel suggestLabel = new JLabel("Want to suggest something?");
        suggestLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        suggestLabel.setForeground(new Color(80, 80, 80));

        RoundedButton suggestButton = new RoundedButton("Submit Suggestion");
        suggestButton.addActionListener(e -> {
            if (dashboard != null) {
                dashboard.showSuggestionPanel();
            }
        });

        bottomPanel.add(suggestLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomPanel.add(suggestButton);

        return bottomPanel;
    }

    // =========================
    // LOAD HERITAGE SITES INTO COMBO BOX
    // =========================
    private void loadHeritageSites() {
        List<HeritageSite> sites = heritageDAO.findAll();
        siteComboBox.removeAllItems();
        for (HeritageSite site : sites) {
            siteComboBox.addItem(new HeritageSiteComboItem(site.getSiteId(), site.getName()));
        }
        if (sites.isEmpty()) {
            siteComboBox.addItem(new HeritageSiteComboItem(-1, "No sites available"));
            startButton.setEnabled(false);
        }
    }

    // =========================
    // START QUIZ (load questions)
    // =========================
    private void startQuiz() {
        HeritageSiteComboItem selected = (HeritageSiteComboItem) siteComboBox.getSelectedItem();
        if (selected == null || selected.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a valid heritage site.");
            return;
        }
        siteIdForQuiz = selected.getId();

        try {
            currentQuestions = quizDAO.getQuestionsForSite(siteIdForQuiz);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + ex.getMessage());
            return;
        }

        if (currentQuestions == null || currentQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No quiz questions available for this site yet.");
            return;
        }

        // Reset quiz state
        currentQuestionIndex = 0;
        userAnswers = new ArrayList<>(Collections.nCopies(currentQuestions.size(), null));

        // Show first question
        showQuestion(0);
    }

    // =========================
    // DISPLAY ONE QUESTION
    // =========================
    private void showQuestion(int index) {
        if (index >= currentQuestions.size()) {
            finishQuiz();
            return;
        }

        Question q = currentQuestions.get(index);

        JPanel questionPanel = new JPanel(new GridBagLayout());
        questionPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Question number
        questionNumberLabel = new JLabel("Question " + (index + 1) + " of " + currentQuestions.size());
        questionNumberLabel.setFont(UIConstants.HERITAGE_SECTION_FONT);
        questionNumberLabel.setForeground(UIConstants.PRIMARY_COLOR);
        gbc.gridy = 0;
        questionPanel.add(questionNumberLabel, gbc);

        // Question text
        questionTextArea = new JTextArea(q.getQuestionText());
        questionTextArea.setFont(UIConstants.HERITAGE_TEXT_FONT);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBackground(UIConstants.BACKGROUND_COLOR);
        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.5;
        questionPanel.add(scrollPane, gbc);

        // Options as radio buttons
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        String[] options = {q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()};
        char[] optionChars = {'A', 'B', 'C', 'D'};
        optionRadios = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionRadios[i] = new JRadioButton(options[i]);
            optionRadios[i].setFont(UIConstants.NORMAL_FONT);
            optionRadios[i].setBackground(UIConstants.BACKGROUND_COLOR);
            optionRadios[i].setActionCommand(String.valueOf(optionChars[i]));
            optionGroup.add(optionRadios[i]);
            optionsPanel.add(optionRadios[i]);
            optionsPanel.add(Box.createVerticalStrut(5));

            if (userAnswers.get(index) != null && userAnswers.get(index) == optionChars[i]) {
                optionRadios[i].setSelected(true);
            }
        }
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        questionPanel.add(optionsPanel, gbc);

        // Next / Submit button
        boolean isLast = (index == currentQuestions.size() - 1);
        String buttonText = isLast ? "Submit Quiz" : "Next Question";
        nextButton = new RoundedButton(buttonText);
        nextButton.addActionListener(e -> saveAndContinue());
        gbc.gridy = 3;
        questionPanel.add(nextButton, gbc);

        // Feedback label
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(UIConstants.SMALL_FONT);
        feedbackLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        questionPanel.add(feedbackLabel, gbc);

        // Show the question card
        centerPanel.removeAll();
        centerPanel.add(questionPanel, "QUESTION");
        cardLayout.show(centerPanel, "QUESTION");
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // =========================
    // SAVE CURRENT ANSWER & CONTINUE
    // =========================
    private void saveAndContinue() {
        if (optionGroup.getSelection() == null) {
            feedbackLabel.setText("Please select an answer before continuing.");
            return;
        }
        feedbackLabel.setText(" ");

        char selectedChar = optionGroup.getSelection().getActionCommand().charAt(0);
        userAnswers.set(currentQuestionIndex, selectedChar);

        currentQuestionIndex++;
        if (currentQuestionIndex < currentQuestions.size()) {
            showQuestion(currentQuestionIndex);
        } else {
            finishQuiz();
        }
    }

    // =========================
    // FINISH QUIZ – SHOW SCORE & WRONG ANSWERS
    // =========================
    private void finishQuiz() {
        int score = 0;
        for (int i = 0; i < currentQuestions.size(); i++) {
            Character answer = userAnswers.get(i);
            if (answer != null && currentQuestions.get(i).isCorrect(answer)) {
                score++;
            }
        }
        int total = currentQuestions.size();
        int percentage = (int) Math.round((score * 100.0) / total);

        // Result panel
        JPanel resultPanel = new JPanel(new BorderLayout(15, 15));
        resultPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel scoreLabel = new JLabel(String.format("Your Score: %d / %d  (%d%%)", score, total, percentage));
        scoreLabel.setFont(UIConstants.TITLE_FONT);
        scoreLabel.setForeground(UIConstants.PRIMARY_COLOR);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultPanel.add(scoreLabel, BorderLayout.NORTH);

        // Wrong answers summary
        JTextArea summaryArea = new JTextArea();
        summaryArea.setFont(UIConstants.NORMAL_FONT);
        summaryArea.setEditable(false);
        summaryArea.setBackground(UIConstants.BACKGROUND_COLOR);
        summaryArea.setMargin(new Insets(10, 10, 10, 10));
        StringBuilder sb = new StringBuilder();
        sb.append("Incorrect Answers:\n\n");
        boolean hasWrong = false;
        for (int i = 0; i < currentQuestions.size(); i++) {
            Character answer = userAnswers.get(i);
            if (answer == null || !currentQuestions.get(i).isCorrect(answer)) {
                hasWrong = true;
                Question q = currentQuestions.get(i);
                sb.append(i + 1).append(". ").append(q.getQuestionText()).append("\n");
                sb.append("   Your answer: ").append(answer == null ? "None" : q.getAnswerText(answer)).append("\n");
                sb.append("   Correct: ").append(q.getCorrectAnswerText()).append("\n");
                sb.append("   Explanation: ").append(q.getExplanation()).append("\n\n");
            }
        }
        if (!hasWrong) {
            sb.append("Perfect! All answers correct.\n");
        }
        summaryArea.setText(sb.toString());
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setPreferredSize(new Dimension(600, 300));
        resultPanel.add(summaryScroll, BorderLayout.CENTER);

        // Buttons below summary
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        RoundedButton newQuizBtn = new RoundedButton("Take Another Quiz");
        newQuizBtn.addActionListener(e -> resetToWelcome());
        RoundedButton closeBtn = new RoundedButton("Close");
        closeBtn.addActionListener(e -> resetToWelcome());
        buttonPanel.add(newQuizBtn);
        buttonPanel.add(closeBtn);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Save attempt to database
        if (currentUser != null && currentUser.getUserId() > 0) {
            try {
                Map<Integer, Character> answersMap = new HashMap<>();
                for (int i = 0; i < currentQuestions.size(); i++) {
                    Character ans = userAnswers.get(i);
                    if (ans != null) {
                        answersMap.put(currentQuestions.get(i).getQuestionId(), ans);
                    }
                }
                quizDAO.saveQuizAttempt(currentUser.getUserId(), siteIdForQuiz, currentQuestions, answersMap);
            } catch (Exception ex) {
                System.err.println("Failed to save quiz attempt: " + ex.getMessage());
            }
        }

        centerPanel.removeAll();
        centerPanel.add(resultPanel, "RESULT");
        cardLayout.show(centerPanel, "RESULT");
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // =========================
    // RESET TO WELCOME SCREEN
    // =========================
    private void resetToWelcome() {
        centerPanel.removeAll();
        centerPanel.add(createWelcomePanel(), "WELCOME");
        cardLayout.show(centerPanel, "WELCOME");
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // =========================
    // HELPER CLASS FOR COMBO BOX ITEMS
    // =========================
    private static class HeritageSiteComboItem {
        private final int id;
        private final String name;

        public HeritageSiteComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}