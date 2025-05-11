import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.*;

// i will start explaining the code from the main method
public class UserGUI extends JFrame { // UserGUI class extends JFrame
  // JFrame is a top-level container that provides a window on the screen
  // We used swing components to create a GUI application and JFrame is a part
  // of the Swing library It provides a window on the screen where we can add
  // components like buttons, labels, etc.
  // JFrame is a class in the javax.swing package
  private UserModule userModule;
  public UserGUI(String username) {           // constructor of UserGUI class
    this.userModule = new UserModule(username);
    setTitle("User Dashboard - " + username); // set title of the window
    setSize(800, 700);                        // set size of the window
    setDefaultCloseOperation(
        JFrame.EXIT_ON_CLOSE); // set default close operation means
    // when the user clicks the close button, the application will exit
    setLocationRelativeTo(null); // center the window on the screen

    JLabel welcomeLabel =
        new JLabel("Welcome, " + username + "! You are logged in as a user.");
    // create a label with welcome message
    // the username is passed as a parameter to the constructor
    // the label is created using JLabel class
    // JLabel is a class in the javax.swing package
    // JLabel is used to display a short string or an image icon
    add(welcomeLabel,
        BorderLayout.CENTER); // add the label to the center of the window
    // BorderLayout is a layout manager that arranges components in five
    // regions: North, South, East, West, and center the label will be added to
    // the center of the window

    JButton logoutButton =
        new JButton("Logout"); // create a button with logout text
    // the button is created using JButton class
    // JButton is a class in the javax.swing package
    // JButton is used to create a push button
    // the button will be used to logout the user
    // the button will be added to the south of the window
    logoutButton.addActionListener(e -> { // add action listener to the button
      dispose();                          // Close dashboard
      new LoginGUI().setVisible(true);    // Return to login
      // create a new instance of LoginGUI class and set it visible means
      // the login window will be displayed again
      // the dispose() method is used to close the current window
      // the action listener is an interface that receives action events
      // the action event is generated when the user clicks the button called
      // actionPerformed methodof the ActionListener interface
      // the actionPerformed method is called when the button is clicked
      // the ActionListener interface is implemented by the anonymous class
    });
    // the anonymous class is a class that does not have a name
    // it is used to create an instance of a class that implements an interface
    // the anonymous class is used to create an instance of the ActionListener
    // interface
    // the ActionListener interface is used to handle action events
    // the action event is generated when the user clicks the button
    add(logoutButton,
        BorderLayout.SOUTH); // add the button to the south of the window
    // the button will be added to the south of the window
  }
}
