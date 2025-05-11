import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
// This is our magic box to help us with JSON (like a toy box for data!)

public class AdminGUI extends JFrame {
  private AdminModule adminModule; // This is our magic admin helper!
  private String adminUsername;

  public AdminGUI(String username) {
    this.adminUsername = username;
    this.adminModule = new AdminModule(); // We get our magic admin helper!

    setTitle("Admin Dashboard - " + username);
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    // This is a big label to say hello to the admin
    JLabel label = new JLabel("Welcome, " + username + "! You are an Admin.");
    label.setHorizontalAlignment(SwingConstants.CENTER);

    // Button to see all projects with bugs
    JButton viewProjectsBtn = new JButton("See Projects");
    viewProjectsBtn.addActionListener(e -> {
      Set<String> projectSet = new HashSet<>();
      try {
        JSONObject data = readJsonFile("data.json");
        JSONArray bugs = (JSONArray) data.get("bugs");
        if (bugs == null) throw new Exception("No 'bugs' array in data.json");
        for (Object obj : bugs) {
          JSONObject bug = (JSONObject) obj;
          String project = (String) bug.get("project");
          if (project != null && !project.isEmpty()) {
            projectSet.add(project);
          }
        }
      } catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(this, "data.json file not found! Please make sure it exists.");
        return;
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error reading data.json: " + ex.getMessage());
        return;
      }
      if (projectSet.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No projects with bugs found!");
        return;
      }
      String[] projects = projectSet.toArray(new String[0]);
      String chosenProject = (String) JOptionPane.showInputDialog(
        this,
        "Pick a project to see its bugs:",
        "Projects with Bugs",
        JOptionPane.PLAIN_MESSAGE,
        null,
        projects,
        projects[0]
      );
      if (chosenProject != null) {
        java.util.List<JSONObject> bugs = adminModule.viewProjectBugs(chosenProject);
        if (bugs.isEmpty()) {
          JOptionPane.showMessageDialog(this, "No bugs found for project '" + chosenProject + "'.");
        } else {
          StringBuilder sb = new StringBuilder();
          for (JSONObject bug : bugs) {
            sb.append("Name: ").append(bug.getOrDefault("name", "")).append("\n");
            sb.append("Type: ").append(bug.getOrDefault("type", "")).append("\n");
            sb.append("Priority: ").append(bug.getOrDefault("priority", "")).append("\n");
            sb.append("Status: ").append(bug.getOrDefault("status", "")).append("\n");
            sb.append("Assigned To: ").append(bug.getOrDefault("assignedTo", "")).append("\n");
            sb.append("Reported By: ").append(bug.getOrDefault("reportedBy", "")).append("\n");
            sb.append("Project: ").append(bug.getOrDefault("project", "")).append("\n");
            sb.append("-----------------------------\n");
          }
          JOptionPane.showMessageDialog(this, sb.toString(), "Bugs in Project '" + chosenProject + "'", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });

    // Button to view all users
    JButton viewUsersBtn = new JButton("View Users");
    viewUsersBtn.addActionListener(e -> {
      java.util.List<String> userList = new java.util.ArrayList<>();
      try {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        java.io.File file = new java.io.File("users.json");
        org.json.simple.JSONArray users;
        Object parsed = parser.parse(new java.io.FileReader(file));
        if (parsed instanceof org.json.simple.JSONArray) {
          users = (org.json.simple.JSONArray) parsed;
        } else if (parsed instanceof org.json.simple.JSONObject) {
          org.json.simple.JSONObject dataObj = (org.json.simple.JSONObject) parsed;
          users = (org.json.simple.JSONArray) dataObj.get("users");
        } else {
          users = new org.json.simple.JSONArray();
        }
        for (Object obj : users) {
          org.json.simple.JSONObject user = (org.json.simple.JSONObject) obj;
          String uname = (String) user.get("username");
          String role = (String) user.get("role");
          if (uname != null && !uname.isEmpty()) {
            userList.add(uname + " (" + role + ")");
          }
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error reading users.json: " + ex.getMessage());
        return;
      }
      if (userList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No users found!");
        return;
      }
      JOptionPane.showMessageDialog(this, String.join("\n", userList), "All Users", JOptionPane.INFORMATION_MESSAGE);
    });

    // Button to delete a user
    JButton deleteUserBtn = new JButton("Delete User");
    deleteUserBtn.addActionListener(e -> {
      java.util.List<String> userList = new java.util.ArrayList<>();
      try {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        java.io.File file = new java.io.File("users.json");
        org.json.simple.JSONArray users;
        Object parsed = parser.parse(new java.io.FileReader(file));
        org.json.simple.JSONObject dataObj = new org.json.simple.JSONObject();
        if (parsed instanceof org.json.simple.JSONArray) {
          users = (org.json.simple.JSONArray) parsed;
          dataObj.put("users", users);
        } else if (parsed instanceof org.json.simple.JSONObject) {
          dataObj = (org.json.simple.JSONObject) parsed;
          users = (org.json.simple.JSONArray) dataObj.get("users");
        } else {
          users = new org.json.simple.JSONArray();
          dataObj.put("users", users);
        }
        for (Object obj : users) {
          org.json.simple.JSONObject user = (org.json.simple.JSONObject) obj;
          String uname = (String) user.get("username");
          if (uname != null && !uname.isEmpty()) {
            userList.add(uname);
          }
        }
      } catch (FileNotFoundException ex) {
        JOptionPane.showMessageDialog(this, "users.json file not found! Please make sure it exists.");
        return;
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error reading users.json: " + ex.getMessage());
        return;
      }
      if (userList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No users found!");
        return;
      }
      String[] usersArr = userList.toArray(new String[0]);
      String chosenUser = (String) JOptionPane.showInputDialog(
        this,
        "Pick a user to say bye-bye:",
        "Delete User",
        JOptionPane.PLAIN_MESSAGE,
        null,
        usersArr,
        usersArr[0]
      );
      if (chosenUser != null) {
        boolean deleted = adminModule.deleteUser(chosenUser);
        JOptionPane.showMessageDialog(this, deleted ? ("Bye-bye, " + chosenUser + "!") : ("Oops, couldn't delete " + chosenUser));
        try {
          org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
          java.io.File file = new java.io.File("users.json");
          org.json.simple.JSONArray users;
          org.json.simple.JSONObject dataObj = new org.json.simple.JSONObject();
          Object parsed = parser.parse(new java.io.FileReader(file));
          if (parsed instanceof org.json.simple.JSONArray) {
            users = (org.json.simple.JSONArray) parsed;
            dataObj.put("users", users);
          } else if (parsed instanceof org.json.simple.JSONObject) {
            dataObj = (org.json.simple.JSONObject) parsed;
            users = (org.json.simple.JSONArray) dataObj.get("users");
          } else {
            users = new org.json.simple.JSONArray();
            dataObj.put("users", users);
          }
          users.remove(chosenUser);
          try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(dataObj.toJSONString());
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage());
        }
      }
    });

    // Button to add a user
    JButton addUserBtn = new JButton("Add User");
    addUserBtn.addActionListener(e -> {
      JTextField usernameField = new JTextField();
      JTextField passwordField = new JTextField();
      String[] roles = {"admin", "tester", "dev", "pm", "user"};
      JComboBox<String> roleBox = new JComboBox<>(roles);
      JPanel panel = new JPanel(new GridLayout(0, 1));
      panel.add(new JLabel("Username:"));
      panel.add(usernameField);
      panel.add(new JLabel("Password:"));
      panel.add(passwordField);
      panel.add(new JLabel("Role:"));
      panel.add(roleBox);
      int result = JOptionPane.showConfirmDialog(this, panel, "Add User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
      if (result == JOptionPane.OK_OPTION) {
        String newUsername = usernameField.getText();
        String password = passwordField.getText();
        String role = (String) roleBox.getSelectedItem();
        if (newUsername.isEmpty() || password.isEmpty()) {
          JOptionPane.showMessageDialog(this, "Username and password required.");
          return;
        }
        org.json.simple.JSONObject newUser = new org.json.simple.JSONObject();
        newUser.put("username", newUsername);
        newUser.put("password", password);
        newUser.put("role", role);
        try {
          org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
          java.io.File file = new java.io.File("users.json");
          org.json.simple.JSONArray users;
          org.json.simple.JSONObject dataObj = new org.json.simple.JSONObject();
          Object parsed = parser.parse(new java.io.FileReader(file));
          if (parsed instanceof org.json.simple.JSONArray) {
            users = (org.json.simple.JSONArray) parsed;
            dataObj.put("users", users);
          } else if (parsed instanceof org.json.simple.JSONObject) {
            dataObj = (org.json.simple.JSONObject) parsed;
            users = (org.json.simple.JSONArray) dataObj.get("users");
          } else {
            users = new org.json.simple.JSONArray();
            dataObj.put("users", users);
          }
          users.add(newUser);
          try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(users.toJSONString());
          }
          JOptionPane.showMessageDialog(this, "User '" + newUsername + "' added!");
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage());
        }
      }
    });

    // Button to update a user
    JButton updateUserBtn = new JButton("Update User");
    updateUserBtn.addActionListener(e -> {
      java.util.List<String> userList = new java.util.ArrayList<>();
      try {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        java.io.File file = new java.io.File("users.json");
        org.json.simple.JSONArray users;
        org.json.simple.JSONObject dataObj = new org.json.simple.JSONObject();
        Object parsed = parser.parse(new java.io.FileReader(file));
        if (parsed instanceof org.json.simple.JSONArray) {
          users = (org.json.simple.JSONArray) parsed;
          dataObj.put("users", users);
        } else if (parsed instanceof org.json.simple.JSONObject) {
          dataObj = (org.json.simple.JSONObject) parsed;
          users = (org.json.simple.JSONArray) dataObj.get("users");
        } else {
          users = new org.json.simple.JSONArray();
          dataObj.put("users", users);
        }
        for (Object obj : users) {
          org.json.simple.JSONObject user = (org.json.simple.JSONObject) obj;
          String uname = (String) user.get("username");
          if (uname != null && !uname.isEmpty()) {
            userList.add(uname);
          }
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error reading users.json: " + ex.getMessage());
        return;
      }
      if (userList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No users found!");
        return;
      }
      String[] usersArr = userList.toArray(new String[0]);
      String chosenUser = (String) JOptionPane.showInputDialog(
        this,
        "Pick a user to update:",
        "Update User",
        JOptionPane.PLAIN_MESSAGE,
        null,
        usersArr,
        usersArr[0]
      );
      if (chosenUser != null) {
        JTextField passwordField = new JTextField();
        String[] roles = {"admin", "tester", "dev", "pm", "user"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("New Password (leave blank to keep current):"));
        panel.add(passwordField);
        panel.add(new JLabel("New Role:"));
        panel.add(roleBox);
        int result = JOptionPane.showConfirmDialog(this, panel, "Update User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
          String newPassword = passwordField.getText();
          String newRole = (String) roleBox.getSelectedItem();
          // Find and update user in JSON
          try {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            java.io.File file = new java.io.File("users.json");
            org.json.simple.JSONArray users;
            org.json.simple.JSONObject dataObj = new org.json.simple.JSONObject();
            Object parsed = parser.parse(new java.io.FileReader(file));
            if (parsed instanceof org.json.simple.JSONArray) {
              users = (org.json.simple.JSONArray) parsed;
              dataObj.put("users", users);
            } else if (parsed instanceof org.json.simple.JSONObject) {
              dataObj = (org.json.simple.JSONObject) parsed;
              users = (org.json.simple.JSONArray) dataObj.get("users");
            } else {
              users = new org.json.simple.JSONArray();
              dataObj.put("users", users);
            }
            for (Object obj : users) {
              org.json.simple.JSONObject user = (org.json.simple.JSONObject) obj;
              if (chosenUser.equals(user.get("username"))) {
                if (!newPassword.isEmpty()) user.put("password", newPassword);
                user.put("role", newRole);
                break;
              }
            }
            try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
              writer.write(users.toJSONString());
            }
            JOptionPane.showMessageDialog(this, "User updated!");
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage());
          }
        }
      }
    });

    JButton logout = new JButton("Logout");
    logout.addActionListener(e -> {
      dispose();
      new LoginGUI().setVisible(true);
    });

    // Put all our buttons in a row, like toys on a shelf
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 6, 10, 10));
    buttonPanel.add(viewProjectsBtn);
    buttonPanel.add(viewUsersBtn);
    buttonPanel.add(addUserBtn);
    buttonPanel.add(updateUserBtn);
    buttonPanel.add(deleteUserBtn);
    buttonPanel.add(logout);

    setLayout(new BorderLayout());
    add(label, BorderLayout.NORTH);
    add(buttonPanel, BorderLayout.CENTER);
    // Now our admin can play with all the buttons!
    setVisible(true);
  }

  // Helper to read JSON file (like opening our toy box)
  private JSONObject readJsonFile(String filename) throws Exception {
    FileReader reader = new FileReader(filename);
    JSONParser parser = new JSONParser();
    return (JSONObject) parser.parse(reader);
  }
}
