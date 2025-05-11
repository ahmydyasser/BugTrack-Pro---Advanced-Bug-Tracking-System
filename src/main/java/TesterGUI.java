import java.awt.*;
import javax.swing.*;

// i will start explaining the code from the main method
public class TesterGUI extends JFrame { // TesterGUI class extends JFrame
  // JFrame is a top-level container that provides a window on the screen
  // We used swing components to create a GUI application and JFrame is a part
  // of the Swing library It provides a window on the screen where we can add
  // components like buttons, labels, etc.
  // JFrame is a class in the javax.swing package
  public TesterGUI(String testername) { // constructor of TesterGUI class
    setTitle("tester Dashboard - " + testername); // set title of the window
    setSize(800, 700);                            // set size of the window
    setDefaultCloseOperation(
        JFrame.EXIT_ON_CLOSE); // set default close operation means
    // when the tester clicks the close button, the application will exit
    setLocationRelativeTo(null); // center the window on the screen

    // Main panel with BorderLayout
    JPanel mainPanel = new JPanel(new BorderLayout());

    // Top panel for notifications button
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton notificationsButton = new JButton("View Notifications");
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
                if (testername.equals(uname) && "tester".equals(role)) {
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
    topPanel.add(notificationsButton);
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // Welcome label in the center
    JLabel welcomeLabel = new JLabel("Welcome, " + testername + "! You are logged in as a tester.", SwingConstants.CENTER);
    mainPanel.add(welcomeLabel, BorderLayout.CENTER);

    // Bottom panel for logout and notifications
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(e -> {
        dispose();
        new LoginGUI().setVisible(true);
    });
    bottomPanel.add(logoutButton);
    bottomPanel.add(notificationsButton);
    mainPanel.add(bottomPanel, BorderLayout.SOUTH);

    setContentPane(mainPanel);
  }
}
