package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.dao.AdminDAO;
import com.discoverethiopia.dao.HeritageDAO;
import com.discoverethiopia.dao.QuizDAO;
import com.discoverethiopia.dao.SuggestionDAO;
import com.discoverethiopia.model.ArchaeologicalSite;
import com.discoverethiopia.model.ChurchSite;
import com.discoverethiopia.model.CitySite;
import com.discoverethiopia.model.HeritageSite;
import com.discoverethiopia.model.NaturalSite;
import com.discoverethiopia.model.Question;
import com.discoverethiopia.model.Suggestion;
import com.discoverethiopia.model.User;
import com.discoverethiopia.ui.auth.AuthFrame;
import com.discoverethiopia.ui.components.RoundedButton;
import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final User admin;
    private final HeritageDAO heritageDAO = new HeritageDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final SuggestionDAO suggestionDAO = new SuggestionDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private DefaultTableModel sitesModel;
    private JComboBox<HeritageSiteItem> questionSiteCombo;
    private DefaultTableModel questionsModel;
    private DefaultTableModel suggestionsModel;

    public AdminDashboard(User admin) {
        this.admin = admin;
        setTitle("Discover Ethiopia Admin");
        setSize(1250, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        contentPanel.add(createOverviewPanel(), "OVERVIEW");
        contentPanel.add(createSitesPanel(), "SITES");
        contentPanel.add(createQuestionsPanel(), "QUESTIONS");
        contentPanel.add(createSuggestionsPanel(), "SUGGESTIONS");
        add(contentPanel, BorderLayout.CENTER);

        refreshAll();
        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setPreferredSize(new Dimension(230, 0));
        panel.setBackground(new Color(45, 0, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        RoundedButton overview = new RoundedButton("Overview");
        RoundedButton sites = new RoundedButton("Manage Sites");
        RoundedButton questions = new RoundedButton("Quiz Questions");
        RoundedButton suggestions = new RoundedButton("Suggestions");
        RoundedButton userView = new RoundedButton("User Dashboard");
        RoundedButton logout = new RoundedButton("Logout");

        overview.addActionListener(e -> cardLayout.show(contentPanel, "OVERVIEW"));
        sites.addActionListener(e -> cardLayout.show(contentPanel, "SITES"));
        questions.addActionListener(e -> cardLayout.show(contentPanel, "QUESTIONS"));
        suggestions.addActionListener(e -> cardLayout.show(contentPanel, "SUGGESTIONS"));
        userView.addActionListener(e -> new MainDashboard(admin));
        logout.addActionListener(e -> {
            dispose();
            new AuthFrame();
        });

        JLabel title = new JLabel("Admin", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(UIConstants.TITLE_FONT);
        panel.add(title);
        panel.add(overview);
        panel.add(sites);
        panel.add(questions);
        panel.add(suggestions);
        panel.add(userView);
        panel.add(logout);
        return panel;
    }

    private JPanel createOverviewPanel() {
        JPanel panel = page("Admin Overview");
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(UIConstants.NORMAL_FONT);
        area.setText("Admin can add/delete heritage sites, add/delete quiz questions, and approve/reject user suggestions.");
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSitesPanel() {
        JPanel panel = page("Manage Heritage Sites");
        sitesModel = new DefaultTableModel(new Object[]{"ID", "Name", "Type", "Region", "Image"}, 0);
        JTable table = new JTable(sitesModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField name = new JTextField();
        JComboBox<String> type = new JComboBox<>(new String[]{"church", "natural", "archaeological", "city"});
        JTextField region = new JTextField();
        JTextArea description = new JTextArea(3, 20);
        JTextArea facts = new JTextArea(3, 20);
        JTextField image = new JTextField("example.jpg");
        form.add(new JLabel("Name"));
        form.add(name);
        form.add(new JLabel("Type"));
        form.add(type);
        form.add(new JLabel("Region"));
        form.add(region);
        form.add(new JLabel("Description"));
        form.add(new JScrollPane(description));
        form.add(new JLabel("Facts, separated by semicolon"));
        form.add(new JScrollPane(facts));
        form.add(new JLabel("Image filename"));
        form.add(image);

        RoundedButton add = new RoundedButton("Add Site");
        RoundedButton delete = new RoundedButton("Delete Selected");
        add.addActionListener(e -> {
            try {
                HeritageSite site = createSite(0, (String) type.getSelectedItem(), name.getText(), region.getText(),
                        description.getText(), facts.getText(), image.getText(), admin.getUserId());
                heritageDAO.addSite(site);
                adminDAO.logAction(admin.getUserId(), "Added heritage site: " + name.getText().trim());
                clearText(name, region, description, facts);
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a site first.");
                return;
            }
            try {
                int siteId = (int) sitesModel.getValueAt(row, 0);
                heritageDAO.deleteSite(siteId);
                adminDAO.logAction(admin.getUserId(), "Deleted heritage site #" + siteId);
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(add);
        buttons.add(delete);
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createQuestionsPanel() {
        JPanel panel = page("Manage Quiz Questions");
        questionsModel = new DefaultTableModel(new Object[]{"ID", "Question", "Correct"}, 0);
        JTable table = new JTable(questionsModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionSiteCombo = new JComboBox<>();
        questionSiteCombo.addActionListener(e -> refreshQuestions());
        top.add(new JLabel("Site:"));
        top.add(questionSiteCombo);
        panel.add(top, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField question = new JTextField();
        JTextField a = new JTextField();
        JTextField b = new JTextField();
        JTextField c = new JTextField();
        JTextField d = new JTextField();
        JComboBox<String> correct = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        JTextField explanation = new JTextField();
        form.add(new JLabel("Question"));
        form.add(question);
        form.add(new JLabel("Option A"));
        form.add(a);
        form.add(new JLabel("Option B"));
        form.add(b);
        form.add(new JLabel("Option C"));
        form.add(c);
        form.add(new JLabel("Option D"));
        form.add(d);
        form.add(new JLabel("Correct"));
        form.add(correct);
        form.add(new JLabel("Explanation"));
        form.add(explanation);

        RoundedButton add = new RoundedButton("Add Question");
        RoundedButton delete = new RoundedButton("Delete Selected");
        add.addActionListener(e -> {
            HeritageSiteItem selected = (HeritageSiteItem) questionSiteCombo.getSelectedItem();
            if (selected == null) {
                return;
            }
            try {
                quizDAO.addQuestion(new Question(0, selected.id, question.getText(), a.getText(), b.getText(),
                        c.getText(), d.getText(), correct.getSelectedItem().toString().charAt(0), explanation.getText()));
                adminDAO.logAction(admin.getUserId(), "Added quiz question for site #" + selected.id);
                clearText(question, a, b, c, d, explanation);
                refreshQuestions();
            } catch (Exception ex) {
                showError(ex);
            }
        });
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a question first.");
                return;
            }
            try {
                int questionId = (int) questionsModel.getValueAt(row, 0);
                quizDAO.deleteQuestion(questionId);
                adminDAO.logAction(admin.getUserId(), "Deleted quiz question #" + questionId);
                refreshQuestions();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(add);
        buttons.add(delete);
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSuggestionsPanel() {
        JPanel panel = page("Review User Suggestions");
        suggestionsModel = new DefaultTableModel(new Object[]{"ID", "Name", "Region", "Reason"}, 0);
        JTable table = new JTable(suggestionsModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JComboBox<String> type = new JComboBox<>(new String[]{"church", "natural", "archaeological", "city"});
        JTextField facts = new JTextField();
        JTextField image = new JTextField("example.jpg");
        form.add(new JLabel("Approved site type"));
        form.add(type);
        form.add(new JLabel("Amazing facts"));
        form.add(facts);
        form.add(new JLabel("Image filename"));
        form.add(image);

        RoundedButton approve = new RoundedButton("Approve Selected");
        RoundedButton reject = new RoundedButton("Reject Selected");
        approve.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a suggestion first.");
                return;
            }
            try {
                int suggestionId = (int) suggestionsModel.getValueAt(row, 0);
                suggestionDAO.approveSuggestion(suggestionId, admin.getUserId(), (String) type.getSelectedItem(),
                        facts.getText(), image.getText());
                adminDAO.logAction(admin.getUserId(), "Approved suggestion #" + suggestionId);
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });
        reject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a suggestion first.");
                return;
            }
            try {
                int suggestionId = (int) suggestionsModel.getValueAt(row, 0);
                suggestionDAO.rejectSuggestion(suggestionId);
                adminDAO.logAction(admin.getUserId(), "Rejected suggestion #" + suggestionId);
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(approve);
        buttons.add(reject);
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel page(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(UIConstants.TITLE_FONT);
        label.setForeground(UIConstants.PRIMARY_COLOR);
        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    private HeritageSite createSite(int id, String type, String name, String region, String description,
            String facts, String image, Integer adminId) {
        return switch (type) {
            case "church" -> new ChurchSite(id, name.trim(), region.trim(), description.trim(), facts.trim(), image.trim(), adminId);
            case "natural" -> new NaturalSite(id, name.trim(), region.trim(), description.trim(), facts.trim(), image.trim(), adminId);
            case "archaeological" -> new ArchaeologicalSite(id, name.trim(), region.trim(), description.trim(), facts.trim(), image.trim(), adminId);
            default -> new CitySite(id, name.trim(), region.trim(), description.trim(), facts.trim(), image.trim(), adminId);
        };
    }

    private void refreshAll() {
        refreshSites();
        refreshQuestionSites();
        refreshSuggestions();
    }

    private void refreshSites() {
        sitesModel.setRowCount(0);
        for (HeritageSite site : heritageDAO.findAll()) {
            sitesModel.addRow(new Object[]{site.getSiteId(), site.getName(), site.getType(), site.getRegion(), site.getImagePath()});
        }
    }

    private void refreshQuestionSites() {
        Object selected = questionSiteCombo.getSelectedItem();
        questionSiteCombo.removeAllItems();
        for (HeritageSite site : heritageDAO.findAll()) {
            questionSiteCombo.addItem(new HeritageSiteItem(site.getSiteId(), site.getName()));
        }
        if (selected != null) {
            questionSiteCombo.setSelectedItem(selected);
        }
        refreshQuestions();
    }

    private void refreshQuestions() {
        if (questionsModel == null || questionSiteCombo == null) {
            return;
        }
        questionsModel.setRowCount(0);
        HeritageSiteItem selected = (HeritageSiteItem) questionSiteCombo.getSelectedItem();
        if (selected == null) {
            return;
        }
        List<Question> questions = quizDAO.getQuestionsForSite(selected.id);
        for (Question question : questions) {
            questionsModel.addRow(new Object[]{question.getQuestionId(), question.getQuestionText(), question.getCorrectOption()});
        }
    }

    private void refreshSuggestions() {
        suggestionsModel.setRowCount(0);
        for (Suggestion suggestion : suggestionDAO.getPendingSuggestions()) {
            suggestionsModel.addRow(new Object[]{
                    suggestion.getSuggestionId(),
                    suggestion.getSuggestedName(),
                    suggestion.getSuggestedRegion(),
                    suggestion.getReason()
            });
        }
    }

    private void clearText(JTextComponent... fields) {
        for (JTextComponent field : fields) {
            field.setText("");
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private record HeritageSiteItem(int id, String name) {
        @Override
        public String toString() {
            return name;
        }
    }
}
