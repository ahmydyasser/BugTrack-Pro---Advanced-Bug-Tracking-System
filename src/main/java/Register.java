import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Register extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JComboBox<String> roleComboBox;

  public Register() {
    // Frame Setup
    setTitle("Register");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(6, 2));

    // Components
    JLabel usernameLabel = new JLabel("Username:");
    usernameField = new JTextField();

    JLabel passwordLabel = new JLabel("Password:");
    passwordField = new JPasswordField();

    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    confirmPasswordField = new JPasswordField();

    JLabel roleLabel = new JLabel("Role:");
    String[] roles = {"admin", "user", "dev", "pm"};
    roleComboBox = new JComboBox<>(roles);

    JButton registerButton = new JButton("Register");
    JButton cancelButton = new JButton("Cancel");

    // Add components
    add(usernameLabel);
    add(usernameField);
    add(passwordLabel);
    add(passwordField);
    add(confirmPasswordLabel);
    add(confirmPasswordField);
    add(roleLabel);
    add(roleComboBox);
    add(registerButton);
    add(cancelButton);

    // Register Action
    registerButton.addActionListener(this::registerUser);

    // Cancel Action
    cancelButton.addActionListener(e -> {
      dispose();
      new LoginGUI().setVisible(true);
    });

    setVisible(true);
  }

  private void registerUser(ActionEvent e) {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());
    String role = (String)roleComboBox.getSelectedItem();

    // Validation
    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      JOptionPane.showMessageDialog(this, "All fields are required.");
      return;
    }

    if (!password.equals(confirmPassword)) {
      JOptionPane.showMessageDialog(this, "Passwords do not match.");
      return;
    }

    if (UserDatabase.getUser(username) != null) {
      JOptionPane.showMessageDialog(this, "Username already exists.");
      return;
    }

    // Create and add user
    User newUser = new User(username, password, role);
    UserDatabase.addUser(newUser);
    UserDatabase.saveUsersToJson("users.json");

    JOptionPane.showMessageDialog(this, "Registration successful!");
    dispose();
    new LoginGUI().setVisible(true);
  }

  // Main method for standalone testing
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Register().setVisible(true));
  }
}
