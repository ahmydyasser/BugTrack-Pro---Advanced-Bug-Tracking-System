public class Bug {
  String name;
  String type;
  String priority;
  String level;
  String projectName;
  String date;
  String status; // "open" or "closed"
  String assignedDev;
  String screenshotPath;

  Bug(String name, String type, String priority, String level,
      String projectName) {
    this.name = name;
    this.type = type;
    this.priority = priority;
    this.level = level;
    this.projectName = projectName;
    this.date = java.time.LocalDate.now().toString();
    this.status = "open";
  }

  Bug(String name, String type, String priority, String level,
      String projectName, String screenshotPath) {
    this.name = name;
    this.type = type;
    this.priority = priority;
    this.level = level;
    this.projectName = projectName;
    this.date = java.time.LocalDate.now().toString();
    this.status = "open";
    this.screenshotPath = screenshotPath;
  }

  @Override
  public String toString() {
    return String.format(
        "Bug{name='%s', type='%s', priority='%s', status='%s', project='%s'}",
        name, type, priority, status, projectName);
  }
}
