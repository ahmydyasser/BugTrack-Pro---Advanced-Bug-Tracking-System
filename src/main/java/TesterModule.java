import java.util.*;

public class TesterModule {
  // create tester constructor  give him name and email and notifications list

  private final String name;
  private final String email;
  private final List<Bug> bugs = new ArrayList<>(); // list of bugs
  private final List<String> notifications;
  private final List<String> inbox; // created empty inbox

  public TesterModule(String name, String email) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Name cannot be empty"); // IllegalArgumentException means what ?
      // why not   just printlN ?
      // // because we are using exception handling to handle the error
    }
    if (email == null || !email.contains("@")) {
      throw new IllegalArgumentException("Invalid email");
    }
    this.name = name;
    this.email = email;
    this.notifications = new ArrayList<>();
    this.inbox = new ArrayList<>(); // created empty inbox
  }

  // create method for tester.RecieveMail() into his emails_recieved
  public void RecieveMail(String email, String subject, String body,
                          String screenshotPath) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }
    if (subject == null || subject.trim().isEmpty()) {
      throw new IllegalArgumentException("Subject cannot be empty");
    }
    if (body == null || body.trim().isEmpty()) {
      throw new IllegalArgumentException("Body cannot be empty");
    }
    String emailContent =
        "From: " + email + "\nSubject: " + subject + "\nBody: " + body;
    if (screenshotPath != null) {
      emailContent += "\nScreenshot: " + screenshotPath;
    }
    inbox.add(emailContent);
  }

  public String getName() { return name; }

  public String getEmail() { return email; }

  public void addNotification(String message) { notifications.add(message); }

  public List<String> getNotifications() {
    return Collections.unmodifiableList(
        notifications); // to prevent modification not just retrun notifications
    // because
    // it can prevent modification from outside like from other classes example
    //
  }

  // get inbox method to get the inbox of the tester
  public List<String> getInbox() {
    return Collections.unmodifiableList(inbox); // to prevent modification
  }

  // get bugs method to get the bugs of the tester
  public List<Bug> getBugs() {
    return Collections.unmodifiableList(bugs); // to prevent modification
  }

  // set bugs method to update the bug list
  public void setBugs(List<Bug> newBugs) {
    bugs.clear();
    bugs.addAll(newBugs);
  }

  // get all screenshots of the bugs
  public List<String> getAllScreenshots() {
    List<String> screenshots = new ArrayList<>();
    for (Bug b : bugs) {
      if (b.screenshotPath != null) {
        screenshots.add(b.screenshotPath);
      }
    }
    return Collections.unmodifiableList(screenshots); // to prevent modification
  }

  // get all bugs of the tester
  public List<Bug> getAllBugs() {
    return Collections.unmodifiableList(bugs); // to prevent modification
  }

  // ###############################################################################################################################

  // get all testers data
  public String getAllData() {
    StringBuilder data = new StringBuilder();
    data.append("Tester Name: ").append(name).append("\n");
    data.append("Tester Email: ").append(email).append("\n");
    data.append("Notifications: ").append(notifications.size()).append("\n");
    data.append("Inbox: ").append(inbox.size()).append("\n");
    data.append("Bugs: ").append(bugs.size()).append("\n");

    for (Bug bug : bugs) {
      data.append(" - ").append(bug.toString()).append("\n");
    }

    return data.toString();
  }

  // list of class bug in new object bugs created in form of arraylist

  public void initBug(String name, String type, String priority, String level,
                      String projectName) {
    Bug newBug = new Bug(name, type, priority, level, projectName);
    bugs.add(newBug);
    System.out.println("Bug initialized: " + name);
  }

  public void initBug(String name, String type, String priority, String level,
                      String projectName, String screenshotPath) {
    Bug newBug =
        new Bug(name, type, priority, level, projectName, screenshotPath);
    bugs.add(newBug);
    System.out.println("Bug initialized: " + name);
  }

  public void assignBugToDev(String bugName, String devName) {
    for (Bug b : bugs) {
      if (b.name.equals(bugName)) {
        // didnot use = instead used equals because diff is equals => compare
        // strings while == => compare references ( diff objects or same object)
        b.assignedDev = devName;
        System.out.println("Bug " + b.name + "assigned to: " +
                           devName); // edited to contain bug name
        return;
      }
    }
    System.out.println("Bug not found");
  }

  public void attachScreenshotForBug(String bugName, String screenshotPath) {
    for (Bug b : bugs) {
      if (b.name.equals(bugName)) {
        b.screenshotPath = screenshotPath; // soon i will get the path of
        // screenshot from the user
        System.out.println("Screenshot attached for bug: " + bugName);
        return;
      }
    }
    System.out.println("Bug not found");
  }

  public void RecieveNotification(String message, String testerName) {
    notifications.add(message);
    System.out.println("Tester : " + testerName + "\n Recieved: " + message);
  }

  public void mailDev(String bugName, String testerName, String testerEmail,
                      String DevEmail) {
    for (Bug b : bugs) {
      if (b.name.equals(bugName) && b.assignedDev != null) {
        String details = "Bug: " + b.name + ", Project: " + b.projectName +
                         ", Priority: " + b.priority;
        // add screenshot path to details if it exists
        if (b.screenshotPath != null) {
          details += ", Screenshot: " + b.screenshotPath;
        }
        // ############ send email to developer ############ waiting for khaled
        // send email to developer
        // div should hav email attached with his name
        // create object dev and send email to him
        // Developer dev = new Developer(b.assignedDev, DevEmail);
        // dev.RecieveMail(testerEmail, testerName, details);

        // here we are just printing the email to console
        System.out.println("Email sent to " + b.assignedDev + ": " + details +
                           " [From Tester: " + testerName + "]");
        return;
      }
    }
    System.out.println("Bug not found or not assigned");
  }

  public void monitorBugs() {
    System.out.println("Open Bugs:");
    for (Bug b : bugs) {
      if ("open".equalsIgnoreCase(b.status)) {
        System.out.println("- " + b.name);
      }
    }

    System.out.println("Closed Bugs:");
    for (Bug b : bugs) {
      if ("closed".equalsIgnoreCase(b.status)) {
        System.out.println("- " + b.name);
      }
    }
  }

  public void closeBug(String bugName) {
    for (Bug b : bugs) {
      if (b.name.equals(bugName)) {
        b.status = "closed";
        System.out.println("Bug closed: " + bugName);
        return;
      }
    }
    System.out.println("Bug not found");
  }
}
