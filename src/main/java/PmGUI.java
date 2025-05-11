import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.FileReader;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class PmGUI extends JFrame {
  private DefaultListModel<String> bugListModel;
  private JList<String> bugList;
  private PmModule pmModule;
  private java.util.List<Bug> allBugs;
  private JComboBox<String> projectFilter;
  private JComboBox<String> statusFilter;
  private JLabel statsLabel;

  public PmGUI(String username) {
    setTitle("Project Manager Dashboard - " + username);
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    pmModule = new PmModule();
    allBugs = pmModule.getAllBugs();

    setupUI();
    refreshBugList();
    updateStats();
    setVisible(true);
  }

  private void setupUI() {
    setLayout(new BorderLayout(10, 10));
    JPanel filterPanel = new JPanel();
    filterPanel.add(new JLabel("Project:"));
    projectFilter = new JComboBox<>();
    projectFilter.addItem("All");
    Set<String> projects = pmModule.getAllProjects();
    for (String project : projects) projectFilter.addItem(project);
    filterPanel.add(projectFilter);

    filterPanel.add(new JLabel("Status:"));
    statusFilter = new JComboBox<>(new String[] {"All", "open", "closed"});
    filterPanel.add(statusFilter);

    JButton filterButton = new JButton("Apply Filter");
    filterButton.addActionListener(e -> {
      refreshBugList();
      updateStats();
    });
    filterPanel.add(filterButton);
    add(filterPanel, BorderLayout.NORTH);

    bugListModel = new DefaultListModel<>();
    bugList = new JList<>(bugListModel);
    bugList.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    JScrollPane scrollPane = new JScrollPane(bugList);
    add(scrollPane, BorderLayout.CENTER);

    statsLabel = new JLabel();
    add(statsLabel, BorderLayout.SOUTH);

    JButton logoutButton = new JButton("Logout");
    logoutButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
    logoutButton.setBackground(new Color(255, 182, 193));
    logoutButton.setForeground(new Color(139, 0, 0));
    logoutButton.addActionListener(e -> {
      dispose();
      new LoginGUI().setVisible(true);
    });
    JPanel bottomPanel = new JPanel();
    bottomPanel.add(logoutButton);
    add(bottomPanel, BorderLayout.PAGE_END);
  }

  private void refreshBugList() {
    bugListModel.clear();
    String selectedProject = (String) projectFilter.getSelectedItem();
    String selectedStatus = (String) statusFilter.getSelectedItem();
    java.util.List<Bug> filtered = pmModule.filterBugs(selectedProject, selectedStatus);
    for (Bug bug : filtered) {
      bugListModel.addElement(bug.toString());
    }
  }

  private void updateStats() {
    int open = pmModule.getOpenCount();
    int closed = pmModule.getClosedCount();
    Map<String, Integer> devBugs = pmModule.getDevBugStats();
    StringBuilder stats = new StringBuilder();
    stats.append("Open: ").append(open).append(" | Closed: ").append(closed);
    stats.append(" | Bugs per Developer: ");
    for (Map.Entry<String, Integer> entry : devBugs.entrySet()) {
      stats.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
    }
    statsLabel.setText(stats.toString());
  }
}
