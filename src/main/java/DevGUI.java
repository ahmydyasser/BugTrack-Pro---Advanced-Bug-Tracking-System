import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class DevGUI extends JFrame {
    private String devName;
    private DefaultListModel<String> bugListModel;
    private JList<String> bugList;
    private DevModule devModule;
    private List<Bug> assignedBugs;

    public DevGUI(String devName, List<Bug> allBugs) {
        this.devName = devName;
        this.devModule = new DevModule(devName);
        setTitle("Developer Dashboard - " + devName);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 255));

        // Load assigned bugs from DevModule
        assignedBugs = devModule.getAssignedBugs();

        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Assigned Bugs"));
        listPanel.setBackground(new Color(255, 255, 240));
        bugListModel = new DefaultListModel<>();
        bugList = new JList<>(bugListModel);
        bugList.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        bugList.setBackground(new Color(255, 255, 240));
        JScrollPane scrollPane = new JScrollPane(bugList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        add(listPanel, BorderLayout.CENTER);

        // Populate bug list
        refreshBugList();

        // Bottom: Change status button
        JPanel bottomPanel = new JPanel();
        JButton completeButton = new JButton("Mark as Complete");
        completeButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        completeButton.setBackground(new Color(144, 238, 144));
        completeButton.setForeground(new Color(0, 100, 0));
        completeButton.addActionListener(e -> {
            int selectedIndex = bugList.getSelectedIndex();
            if (selectedIndex != -1) {
                Bug selectedBug = assignedBugs.get(selectedIndex);
                devModule.markBugAsComplete(selectedBug);
                devModule.notifyTester(selectedBug);
                JOptionPane.showMessageDialog(this, "Bug marked as complete! Tester has been notified.");
                // Reload assigned bugs and refresh list
                assignedBugs = devModule.getAssignedBugs();
                refreshBugList();
            } else {
                JOptionPane.showMessageDialog(this, "Select a bug to mark as complete.");
            }
        });
        bottomPanel.add(completeButton);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        logoutButton.setBackground(new Color(255, 182, 193));
        logoutButton.setForeground(new Color(139, 0, 0));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginGUI().setVisible(true);
        });
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshBugList() {
        bugListModel.clear();
        for (Bug bug : assignedBugs) {
            bugListModel.addElement(bug.toString());
        }
    }
}
