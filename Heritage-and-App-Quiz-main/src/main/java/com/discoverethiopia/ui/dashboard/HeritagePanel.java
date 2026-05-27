package com.discoverethiopia.ui.dashboard;

import com.discoverethiopia.dao.HeritageDAO;
import com.discoverethiopia.model.HeritageSite;
import com.discoverethiopia.ui.components.RoundedButton;
import com.discoverethiopia.ui.utils.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HeritagePanel extends JPanel {

    private JComboBox<String> heritageComboBox;
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel typeLabel;
    private JLabel regionLabel;
    private JTextArea descriptionArea;
    private JTextArea factsArea;
    private final List<HeritageSite> heritageSites;

    // Reference to the main dashboard (to open suggestion panel)
    private MainDashboard dashboard;

    public void setDashboard(MainDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public HeritagePanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Load heritage data from database
        HeritageDAO heritageDAO = new HeritageDAO();
        heritageSites = heritageDAO.findAll();

        // =========================
        // TOP PANEL (purple bar with combo box) – same style as QuizPanel
        // =========================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(UIConstants.PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel selectLabel = new JLabel("Select Heritage Site:");
        selectLabel.setFont(UIConstants.NORMAL_FONT);
        selectLabel.setForeground(Color.WHITE);

        heritageComboBox = new JComboBox<>();
        heritageComboBox.setFont(UIConstants.COMBOBOX_FONT);
        heritageComboBox.setPreferredSize(new Dimension(250, 35));
        heritageComboBox.addItem("Select Heritage Site");
        for (HeritageSite site : heritageSites) {
            heritageComboBox.addItem(site.getName());
        }

        // No extra button – selection immediately shows details
        topPanel.add(selectLabel);
        topPanel.add(heritageComboBox);

        add(topPanel, BorderLayout.NORTH);

        // =========================
        // CONTENT PANEL (scrollable area with details)
        // =========================
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 40, 40, 40));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image
        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        titleLabel = new JLabel();
        titleLabel.setFont(UIConstants.HERITAGE_TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Type
        typeLabel = new JLabel();
        typeLabel.setFont(UIConstants.NORMAL_FONT);
        typeLabel.setForeground(UIConstants.PRIMARY_COLOR);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Region
        regionLabel = new JLabel();
        regionLabel.setFont(UIConstants.NORMAL_FONT);
        regionLabel.setForeground(UIConstants.SUBTEXT_COLOR);
        regionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description title
        JLabel descTitle = new JLabel("About This Heritage");
        descTitle.setFont(UIConstants.HERITAGE_SECTION_FONT);
        descTitle.setForeground(UIConstants.PRIMARY_COLOR);

        // Description area
        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(UIConstants.HERITAGE_TEXT_FONT);
        descriptionArea.setBackground(UIConstants.LIGHT_BACKGROUND);
        descriptionArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Amazing Facts title
        JLabel factsTitle = new JLabel("Amazing Facts");
        factsTitle.setFont(UIConstants.HERITAGE_SECTION_FONT);
        factsTitle.setForeground(UIConstants.PRIMARY_COLOR);

        // Facts area
        factsArea = new JTextArea();
        factsArea.setEditable(false);
        factsArea.setWrapStyleWord(true);
        factsArea.setLineWrap(true);
        factsArea.setFont(UIConstants.HERITAGE_TEXT_FONT);
        factsArea.setBackground(UIConstants.LIGHT_BACKGROUND);
        factsArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        // =========================
        // SUGGESTION SECTION (bottom)
        // =========================
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel suggestionRow = new JPanel();
        suggestionRow.setLayout(new BoxLayout(suggestionRow, BoxLayout.X_AXIS));
        suggestionRow.setBackground(Color.WHITE);
        suggestionRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel suggestLabel = new JLabel("Want to suggest something? ");
        suggestLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        suggestLabel.setForeground(new Color(80, 80, 80));

        RoundedButton suggestButton = new RoundedButton("Submit Suggestion");
        suggestButton.addActionListener(e -> {
            if (dashboard != null) {
                dashboard.showSuggestionPanel();
            }
        });

        suggestionRow.add(suggestLabel);
        suggestionRow.add(Box.createRigidArea(new Dimension(10, 0)));
        suggestionRow.add(suggestButton);

        contentPanel.add(suggestionRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // =========================
        // ADD ALL DETAIL COMPONENTS
        // =========================
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(typeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(regionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        contentPanel.add(descTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(descriptionArea);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        contentPanel.add(factsTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(factsArea);

        // Wrap into scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // =========================
        // COMBOBOX EVENT – load selected heritage
        // =========================
        heritageComboBox.addActionListener(e -> {
            int index = heritageComboBox.getSelectedIndex();
            if (index <= 0) return;
            HeritageSite selectedSite = heritageSites.get(index - 1);
            showHeritageDetails(selectedSite);
        });
    }

    /**
     * Displays the details of the selected heritage site in the UI.
     */
    private void showHeritageDetails(HeritageSite site) {
        titleLabel.setText(site.getName());
        typeLabel.setText("Type: " + site.getType().toUpperCase());
        regionLabel.setText("Region: " + site.getRegion());
        descriptionArea.setText(site.getDescription());

        // Format amazing facts with bullet points
        StringBuilder facts = new StringBuilder();
        for (String fact : site.getAmazingFactsList()) {
            facts.append("* ").append(fact).append("\n\n");
        }
        factsArea.setText(facts.toString());

        // Load and scale image
        String imagePath = site.getImagePath();
        String fullImagePath = imagePath.startsWith("images/") || imagePath.startsWith("images\\")
                ? imagePath
                : "images/" + imagePath;
        ImageIcon originalIcon = new ImageIcon(fullImagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(750, 380, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }
}
