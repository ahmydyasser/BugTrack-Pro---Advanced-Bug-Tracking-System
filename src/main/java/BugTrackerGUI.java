import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BugTrackerGUI extends JFrame {
  private TesterModule tester;
  private DefaultListModel<String> bugListModel;
  private JList<String> bugList;
  private static final String DATA_FILE = "data.json";

  private JTextField createStyledTextField() {
    JTextField field = new JTextField(20);
    field.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    field.setBackground(Color.WHITE);
    return field;
  }

  private JLabel createStyledLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
    return label;
  }

  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
    button.setBackground(new Color(255, 182, 193));
    button.setForeground(new Color(139, 0, 0));
    return button;
  }

  public BugTrackerGUI(TesterModule tester) {
    this.tester = tester;
    setTitle("Bug Tracker - Logged in as: " + tester.getName());
    setSize(1200, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    getContentPane().setBackground(new Color(240, 248, 255));
    
    // Load bugs from JSON first
    loadBugsFromJson();
    
    // Setup UI components
    setupUI();
    
    // Refresh the bug list to show loaded bugs
    refreshBugList();

    System.out.println("Working directory: " + new java.io.File(".").getAbsolutePath());
    System.out.println("users.json exists? " + new java.io.File("users.json").exists());
  }

  private void loadBugsFromJson() {
    try (FileReader reader = new FileReader(DATA_FILE)) {
      JSONParser parser = new JSONParser();
      JSONObject data = (JSONObject) parser.parse(reader);
      JSONArray bugs = (JSONArray) data.get("bugs");
      
      // Create a new ArrayList instead of clearing the existing one
      List<Bug> newBugs = new ArrayList<>();
      
      for (Object obj : bugs) {
        JSONObject bugObj = (JSONObject) obj;
        String name = (String) bugObj.get("name");
        String type = (String) bugObj.get("type");
        String priority = (String) bugObj.get("priority");
        String level = (String) bugObj.get("level");
        String projectName = (String) bugObj.get("project");
        String status = (String) bugObj.get("status");
        String assignedDev = (String) bugObj.get("assignedTo");
        
        Bug bug = new Bug(name, type, priority, level, projectName);
        if (status != null && status.equals("closed")) {
          bug.status = "closed";
        }
        if (assignedDev != null) {
          bug.assignedDev = assignedDev;
        }
        newBugs.add(bug);
      }
      
      // Update the tester's bug list with the new list
      tester.setBugs(newBugs);
      
    } catch (IOException | ParseException e) {
      // If file doesn't exist or is empty, ignore
      System.out.println("No existing bugs found or error loading bugs: " + e.getMessage());
    }
  }

  private void saveBugsToJson() {
    JSONObject data = new JSONObject();
    JSONArray bugs = new JSONArray();
    
    for (Bug bug : tester.getBugs()) {
      JSONObject bugObj = new JSONObject();
      bugObj.put("name", bug.name);
      bugObj.put("type", bug.type);
      bugObj.put("priority", bug.priority);
      bugObj.put("level", bug.level);
      bugObj.put("project", bug.projectName);
      bugObj.put("status", bug.status);
      // Always write assignedTo, even if null
      bugObj.put("assignedTo", bug.assignedDev != null ? bug.assignedDev : "");
      // Add reportedBy field
      bugObj.put("reportedBy", tester.getName());
      if (bug.screenshotPath != null) {
        JSONArray screenshots = new JSONArray();
        screenshots.add(bug.screenshotPath);
        bugObj.put("screenshots", screenshots);
      }
      // Debug print
      System.out.println("Saving bug: " + bug.name + ", assignedTo: " + bug.assignedDev + ", reportedBy: " + tester.getName());
      bugs.add(bugObj);
    }
    
    data.put("bugs", bugs);
    
    try (FileWriter writer = new FileWriter(DATA_FILE)) {
      writer.write(data.toJSONString());
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error saving bugs to JSON: " + e.getMessage());
    }
  }

  private void setupUI() {
    // Main layout: left (forms), right (bug list)
    setLayout(new BorderLayout(15, 15));

    // LEFT SIDE: Stack Create New Bug and Bug Actions vertically
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBackground(new Color(240, 248, 255)); // Match main bg

    // --- Create New Bug Panel ---
    JPanel inputPanel = new JPanel();
    inputPanel.setBorder(BorderFactory.createTitledBorder("Create New Bug"));
    inputPanel.setBackground(new Color(230, 255, 230)); // Light green
    inputPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(8, 8, 8, 8);

    JLabel nameLabel = createStyledLabel("Bug Name:");
    JTextField nameField = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 0;
    inputPanel.add(nameLabel, gbc);
    gbc.gridx = 1;
    inputPanel.add(nameField, gbc);

    JLabel typeLabel = createStyledLabel("Type:");
    JTextField typeField = createStyledTextField();
    gbc.gridy = 1;
    inputPanel.add(typeLabel, gbc);
    gbc.gridx = 1;
    inputPanel.add(typeField, gbc);

    JLabel priorityLabel = createStyledLabel("Priority:");
    JTextField priorityField = createStyledTextField();
    gbc.gridy = 2;
    inputPanel.add(priorityLabel, gbc);
    gbc.gridx = 1;
    inputPanel.add(priorityField, gbc);

    JLabel levelLabel = createStyledLabel("Level:");
    JTextField levelField = createStyledTextField();
    gbc.gridy = 3;
    inputPanel.add(levelLabel, gbc);
    gbc.gridx = 1;
    inputPanel.add(levelField, gbc);

    JLabel projectLabel = createStyledLabel("Project:");
    JTextField projectField = createStyledTextField();
    gbc.gridy = 4;
    inputPanel.add(projectLabel, gbc);
    gbc.gridx = 1;
    inputPanel.add(projectField, gbc);

    JButton addButton = createStyledButton("✨ Add New Bug ✨");
    addButton.setBackground(new Color(144, 238, 144));
    addButton.setForeground(new Color(0, 100, 0));
    addButton.addActionListener(e -> {
      String name = nameField.getText();
      String type = typeField.getText();
      String priority = priorityField.getText();
      String level = levelField.getText();
      String project = projectField.getText();
      if (!name.isEmpty() && !type.isEmpty()) {
        tester.initBug(name, type, priority, level, project);
        refreshBugList();
        JOptionPane.showMessageDialog(this, "Bug added!");
      } else {
        JOptionPane.showMessageDialog(this, "Please fill required fields.");
      }
    });
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    inputPanel.add(addButton, gbc);

    // --- Bug Actions Panel ---
    JPanel actionsPanel = new JPanel();
    actionsPanel.setBorder(BorderFactory.createTitledBorder("Bug Actions"));
    actionsPanel.setBackground(new Color(255, 228, 225)); // Misty rose
    actionsPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc2 = new GridBagConstraints();
    gbc2.anchor = GridBagConstraints.WEST;
    gbc2.insets = new Insets(8, 8, 8, 8);

    JButton closeButton = createStyledButton("Close Selected Bug");
    closeButton.addActionListener(e -> {
      int selectedIndex = bugList.getSelectedIndex();
      if (selectedIndex != -1) {
        String selectedBugName = tester.getBugs().get(selectedIndex).name;
        tester.closeBug(selectedBugName);
        refreshBugList();
        JOptionPane.showMessageDialog(this, "Bug closed!");
      } else {
        JOptionPane.showMessageDialog(this, "Select a bug to close.");
      }
    });
    gbc2.gridx = 0;
    gbc2.gridy = 0;
    actionsPanel.add(closeButton, gbc2);

    JLabel assignLabel = createStyledLabel("Developer Name:");
    JComboBox<String> assignDevDropdown = new JComboBox<>();
    loadUsersForDropdown(assignDevDropdown, "dev");
    JButton addDevButton = createStyledButton("Add New Developer");
    addDevButton.addActionListener(e -> {
      JTextField newDevField = new JTextField();
      JTextField newDevPassField = new JTextField();
      JPanel panel = new JPanel(new GridLayout(0, 1));
      panel.add(new JLabel("Developer Username:"));
      panel.add(newDevField);
      panel.add(new JLabel("Password:"));
      panel.add(newDevPassField);
      int result = JOptionPane.showConfirmDialog(this, panel, "Add Developer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (result == JOptionPane.OK_OPTION) {
        String devName = newDevField.getText();
        String devPass = newDevPassField.getText();
        if (!devName.isEmpty() && !devPass.isEmpty()) {
          org.json.simple.JSONObject newUser = new org.json.simple.JSONObject();
          newUser.put("username", devName);
          newUser.put("password", devPass);
          newUser.put("role", "dev");
          try {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            Object parsed = parser.parse(new java.io.FileReader("users.json"));
            org.json.simple.JSONArray usersArray = null;
            org.json.simple.JSONObject dataObj = null;

            if (parsed instanceof org.json.simple.JSONObject) {
              dataObj = (org.json.simple.JSONObject) parsed;
              usersArray = (org.json.simple.JSONArray) dataObj.get("users");
              usersArray.add(newUser);
              try (java.io.FileWriter writer = new java.io.FileWriter("users.json")) {
                dataObj.put("users", usersArray);
                writer.write(dataObj.toJSONString());
              }
            } else if (parsed instanceof org.json.simple.JSONArray) {
              usersArray = (org.json.simple.JSONArray) parsed;
              usersArray.add(newUser);
              try (java.io.FileWriter writer = new java.io.FileWriter("users.json")) {
                writer.write(usersArray.toJSONString());
              }
            }
            assignDevDropdown.removeAllItems();
            loadUsersForDropdown(assignDevDropdown, "dev");
            JOptionPane.showMessageDialog(this, "Developer added!");
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding developer: " + ex.getMessage());
          }
        } else {
          JOptionPane.showMessageDialog(this, "Please enter username and password.");
        }
      }
    });
    JButton assignButton = createStyledButton("Assign Developer");
    assignButton.addActionListener(e -> {
      int selectedIndex = bugList.getSelectedIndex();
      if (selectedIndex != -1) {
        String selectedBugName = tester.getBugs().get(selectedIndex).name;
        String devName = (String) assignDevDropdown.getSelectedItem();
        if (devName != null && !devName.isEmpty()) {
          // Only update the selected bug's assignedDev
          for (Bug bug : tester.getBugs()) {
            if (bug.name.equals(selectedBugName)) {
              bug.assignedDev = devName;
              break;
            }
          }
          saveBugsToJson(); // Ensure assignment is saved to data.json
          JOptionPane.showMessageDialog(this, "Bug assigned to: " + devName);
        } else {
          JOptionPane.showMessageDialog(this, "Select a developer.");
        }
      } else {
        JOptionPane.showMessageDialog(this, "Select a bug to assign.");
      }
    });
    gbc2.gridy = 1;
    actionsPanel.add(assignLabel, gbc2);
    gbc2.gridx = 1;
    actionsPanel.add(assignDevDropdown, gbc2);
    gbc2.gridx = 2;
    actionsPanel.add(assignButton, gbc2);
    gbc2.gridx = 3;
    actionsPanel.add(addDevButton, gbc2);

    JLabel screenshotLabel = createStyledLabel("Screenshot Path:");
    JTextField screenshotField = createStyledTextField();
    JButton browseButton = createStyledButton("Browse");
    browseButton.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      int result = fileChooser.showOpenDialog(this);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        screenshotField.setText(selectedFile.getAbsolutePath());
      }
    });
    JButton attachButton = createStyledButton("Attach Screenshot");
    attachButton.addActionListener(e -> {
      int selectedIndex = bugList.getSelectedIndex();
      if (selectedIndex != -1) {
        String selectedBugName = tester.getBugs().get(selectedIndex).name;
        String path = screenshotField.getText();
        if (!path.isEmpty()) {
          tester.attachScreenshotForBug(selectedBugName, path);
          JOptionPane.showMessageDialog(this, "Screenshot attached.");
        } else {
          JOptionPane.showMessageDialog(this, "Enter screenshot path.");
        }
      } else {
        JOptionPane.showMessageDialog(this,
                                      "Select a bug to attach screenshot.");
      }
    });
    gbc2.gridy = 2;
    gbc2.gridx = 0;
    actionsPanel.add(screenshotLabel, gbc2);
    gbc2.gridx = 1;
    actionsPanel.add(screenshotField, gbc2);
    gbc2.gridx = 2;
    actionsPanel.add(browseButton, gbc2);
    gbc2.gridx = 3;
    actionsPanel.add(attachButton, gbc2);

    // --- Send Message to Any User (Email User) ---
    JLabel emailLabel = createStyledLabel("User to Email:");
    JComboBox<String> userEmailDropdown = new JComboBox<>();
    loadUsersForDropdown(userEmailDropdown, null);
    JButton emailButton = createStyledButton("Send Email");
    emailButton.addActionListener(e -> {
      int selectedIndex = bugList.getSelectedIndex();
      if (selectedIndex != -1) {
        String selectedBugName = tester.getBugs().get(selectedIndex).name;
        String userName = (String) userEmailDropdown.getSelectedItem();
        if (userName != null && !userName.isEmpty()) {
          tester.mailDev(selectedBugName, tester.getName(), tester.getEmail(), userName);
          JOptionPane.showMessageDialog(this, "Email sent to user: " + userName);
        } else {
          JOptionPane.showMessageDialog(this, "Select a user to email.");
        }
      } else {
        JOptionPane.showMessageDialog(this, "Select a bug to send email.");
      }
    });
    gbc2.gridy = 4;
    gbc2.gridx = 0;
    actionsPanel.add(emailLabel, gbc2);
    gbc2.gridx = 1;
    actionsPanel.add(userEmailDropdown, gbc2);
    gbc2.gridx = 2;
    actionsPanel.add(emailButton, gbc2);

    // Add both panels to the leftPanel (stacked)
    leftPanel.add(inputPanel);
    leftPanel.add(Box.createVerticalStrut(15)); // Space between panels
    leftPanel.add(actionsPanel);

    // RIGHT SIDE: My Bug Collection (takes full height)
    JPanel listPanel = new JPanel(new BorderLayout());
    listPanel.setBorder(BorderFactory.createTitledBorder("My Bug Collection"));
    listPanel.setBackground(new Color(255, 255, 240)); // Cream color
    bugListModel = new DefaultListModel<>();
    bugList = new JList<>(bugListModel);
    bugList.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
    bugList.setBackground(new Color(255, 255, 240));
    bugList.setCellRenderer(new BugCellRenderer());
    JScrollPane scrollPane = new JScrollPane(bugList);
    scrollPane.setBorder(
        BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
    listPanel.add(scrollPane, BorderLayout.CENTER);

    // Add left and right panels to the main frame
    add(leftPanel, BorderLayout.WEST);
    add(listPanel, BorderLayout.CENTER);

    // Bottom: Exit button (centered)
    JPanel logoutPanel = new JPanel();
    JButton logoutButton = createStyledButton("🚪 Exit Room");
    logoutButton.addActionListener(e -> {
      dispose();
      new LoginGUI().setVisible(true);
    });
    logoutPanel.setBackground(new Color(240, 248, 255));
    logoutPanel.add(logoutButton);

    // Add View Notifications button next to Exit Room
    JButton notificationsButton = createStyledButton("View Notifications");
    notificationsButton.addActionListener(e -> {
        try {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            java.io.File file = new java.io.File("users.json");
            org.json.simple.JSONArray usersArray;
            Object parsed = parser.parse(new java.io.FileReader(file));
            if (parsed instanceof org.json.simple.JSONArray) {
                usersArray = (org.json.simple.JSONArray) parsed;
            } else if (parsed instanceof org.json.simple.JSONObject) {
                org.json.simple.JSONObject dataObj = (org.json.simple.JSONObject) parsed;
                usersArray = (org.json.simple.JSONArray) dataObj.get("users");
            } else {
                usersArray = new org.json.simple.JSONArray();
            }
            java.util.List<String> notifications = new java.util.ArrayList<>();
            for (Object obj : usersArray) {
                org.json.simple.JSONObject user = (org.json.simple.JSONObject) obj;
                String uname = (String) user.get("username");
                String role = (String) user.get("role");
                if (tester.getName().equals(uname) && "tester".equals(role)) {
                    org.json.simple.JSONArray notifArr = (org.json.simple.JSONArray) user.get("notifications");
                    if (notifArr != null) {
                        for (Object n : notifArr) notifications.add(n.toString());
                    }
                    break;
                }
            }
            if (notifications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No notifications.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String n : notifications) sb.append("- ").append(n).append("\n");
                JOptionPane.showMessageDialog(this, sb.toString(), "Your Notifications", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + ex.getMessage());
        }
    });
    logoutPanel.add(notificationsButton);
    add(logoutPanel, BorderLayout.PAGE_END);
  }

  private void refreshBugList() {
    bugListModel.clear();
    for (Bug bug : tester.getBugs()) {
      bugListModel.addElement(bug.toString());
    }
    saveBugsToJson(); // Save after refreshing to ensure consistency
  }

  private void loadUsersForDropdown(JComboBox<String> dropdown, String role) {
    dropdown.removeAllItems();
    try {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        Object parsed = parser.parse(new java.io.FileReader("users.json"));
        org.json.simple.JSONArray usersArray = null;

        if (parsed instanceof org.json.simple.JSONObject) {
            org.json.simple.JSONObject data = (org.json.simple.JSONObject) parsed;
            usersArray = (org.json.simple.JSONArray) data.get("users");
        } else if (parsed instanceof org.json.simple.JSONArray) {
            usersArray = (org.json.simple.JSONArray) parsed;
        }

        if (usersArray != null) {
            for (Object obj : usersArray) {
                org.json.simple.JSONObject user = (org.json.simple.JSONObject) obj;
                String userRole = (String) user.get("role");
                String uname = (String) user.get("username");
                if (role == null || role.equals(userRole)) {
                    dropdown.addItem(uname);
                    System.out.println("Added to dropdown: " + uname + " (" + userRole + ")");
                }
            }
        } else {
            System.out.println("No users found in users.json!");
        }
        dropdown.revalidate();
        dropdown.repaint();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading users for dropdown: " + e.getMessage());
    }
  }
}
