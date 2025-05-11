import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// here we are going to explain the code of the LoginGUI class
public class LoginGUI extends JFrame { // LoginGUI class extends JFrame
  private JTextField
      usernameField; // JTextField is a class in the javax.swing package
  private JPasswordField
      passwordField; // JPasswordField is a class in the javax.swing package

  public LoginGUI() {                // constructor of LoginGUI class
    setTitle("Bug Tracker - Login"); // set title of the window
    setSize(400, 200);               // set size of the window
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null); // Center window on screen
    // setLocationRelativeTo(null) method is used to center the window on the
    // screen

    JPanel panel =
        new JPanel(new GridLayout(3, 2)); // create a panel with grid layout
    // grid layout is a layout manager that arranges components in a grid of
    // rows and columns the grid layout is created using GridLayout class
    // GridLayout is a class in the java.awt package
    panel.add(new JLabel("Username:")); // add a label to the panel
    // panel here is like a container that holds the components
    // we could use JPanel class to create a panel
    // and grid here means the layout of the panel which is like a grid like a
    // table  rows and columns

    usernameField =
        new JTextField(); // JTextField is a class in the javax.swing package
    panel.add(usernameField); // add a text field to the panel

    panel.add(new JLabel("Password:"));   // add a label to the panel
    passwordField = new JPasswordField(); // JPasswordField is a class in the
    // javax.swing package
    panel.add(passwordField); // add a password field to the panel

    JButton loginButton =
        new JButton("Login"); // create a button with login text
    JButton cancelButton =
        new JButton("Cancel"); // create a button with cancel text

    loginButton.addActionListener(e -> { // add action listener to the button
      String username = usernameField.getText(); // get text from username field
      String password = new String(
          passwordField.getPassword()); // get password from password field
      // getPassword() method returns a char array
      if (username.isEmpty() ||
          password.isEmpty()) {        // check if username or password is empty
        JOptionPane.showMessageDialog( // show message dialog Joption here is to
                                       // show a message
            this, "Please enter both username and password.");
        return;
      }

      if (UserDatabase.isValidUser(username, password)) {
        User user = UserDatabase.getUser(username); // get user from database
        String role = user.getRole();               // get role of the user

        if ("tester".equals(role)) {
          TesterModule tester = new TesterModule(username, "tester@example.com");
          new BugTrackerGUI(tester).setVisible(true);
        } else if ("user".equals(role)) {
          openUserDashboard(username); // User -> User Dashboard
        } else if ("admin".equals(role)) {
          openAdminGUIDashboard(username); // Admin -> Admin Dashboard
        } else if ("dev".equals(role)) {
          openDevDashboard(username); // Dev -> Dev Dashboard
        } else if ("pm".equals(role)) {
          openPmDashboard(username); // PM -> Project Manager Dashboard
        } else {
          JOptionPane.showMessageDialog(this, "Unknown role: " + role);
        }
        dispose(); // Close login window
      } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password.");
      }
    });
    cancelButton.addActionListener(e -> System.exit(0));

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(loginButton);
    buttonPanel.add(cancelButton);

    add(panel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  // Methods to open different dashboards

  private void openUserDashboard(String username) {
    SwingUtilities.invokeLater(
        () -> { new UserGUI(username).setVisible(true); });
  }

  // create a method to open the user dashboard
  private void openTesterDashboard(String username) {
    SwingUtilities.invokeLater(
        () -> { new TesterGUI(username).setVisible(true); });
  }

  private void openDevDashboard(String username) {
    // Load all bugs from data.json
    java.util.List<Bug> allBugs = new java.util.ArrayList<>();
    try (java.io.FileReader reader = new java.io.FileReader("data.json")) {
      org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
      org.json.simple.JSONObject data = (org.json.simple.JSONObject) parser.parse(reader);
      org.json.simple.JSONArray bugs = (org.json.simple.JSONArray) data.get("bugs");
      for (Object obj : bugs) {
        org.json.simple.JSONObject bugObj = (org.json.simple.JSONObject) obj;
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
        allBugs.add(bug);
      }
    } catch (Exception e) {
      System.out.println("Error loading bugs for developer: " + e.getMessage());
    }
    javax.swing.SwingUtilities.invokeLater(() -> {
      new DevGUI(username, allBugs).setVisible(true);
    });
  }

  private void openAdminGUIDashboard(String username) {
    SwingUtilities.invokeLater(
        () -> { new AdminGUI(username).setVisible(true); });
  }

  private void openPmDashboard(String username) {
    SwingUtilities.invokeLater(() -> { new PmGUI(username).setVisible(true); });
  }

  private void openBugTracker(String username) {
    TesterModule tester = new TesterModule(username, username + ("@example."
                                                                 + "com"));
    tester.initBug("Login Error", "UI", "High", "P1", "ProjectX");
    tester.initBug("Slow Performance", "Performance", "Medium", "P2",
                   "ProjectY");

    SwingUtilities.invokeLater(() -> {
      BugTrackerGUI bugTrackerGUI = new BugTrackerGUI(tester);
      bugTrackerGUI.setVisible(true);
    });
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
  }
}
