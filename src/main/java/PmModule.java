import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class PmModule {
  private List<Bug> allBugs = new ArrayList<>();

  public PmModule() { loadAllBugs(); }

  public List<Bug> getAllBugs() { return allBugs; }

  public void loadAllBugs() {
    allBugs.clear();
    try (FileReader reader = new FileReader("data.json")) {
      JSONParser parser = new JSONParser();
      JSONObject data = (JSONObject)parser.parse(reader);
      JSONArray bugs = (JSONArray)data.get("bugs");
      for (Object obj : bugs) {
        JSONObject bugObj = (JSONObject)obj;
        String name = (String)bugObj.get("name");
        String type = (String)bugObj.get("type");
        String priority = (String)bugObj.get("priority");
        String level = (String)bugObj.get("level");
        String projectName = (String)bugObj.get("project");
        String status = (String)bugObj.get("status");
        String assignedDev = (String)bugObj.get("assignedTo");
        Bug bug = new Bug(name, type, priority, level, projectName);
        if (status != null && status.equals("closed")) {
          bug.status = "closed";
        }
        bug.assignedDev = (assignedDev != null) ? assignedDev : "";
        allBugs.add(bug);
      }
    } catch (Exception e) {
      System.out.println("Error loading bugs for PM: " + e.getMessage());
    }
  }

  public Set<String> getAllProjects() {
    Set<String> projects = new HashSet<>();
    for (Bug bug : allBugs) {
      if (bug.projectName != null)
        projects.add(bug.projectName);
    }
    return projects;
  }

  public List<Bug> filterBugs(String project, String status) {
    List<Bug> filtered = new ArrayList<>();
    for (Bug bug : allBugs) {
      boolean matchesProject =
          project.equals("All") ||
          (bug.projectName != null && bug.projectName.equals(project));
      boolean matchesStatus = status.equals("All") || bug.status.equals(status);
      if (matchesProject && matchesStatus) {
        filtered.add(bug);
      }
    }
    return filtered;
  }

  public Map<String, Integer> getDevBugStats() {
    Map<String, Integer> devBugs = new HashMap<>();
    for (Bug bug : allBugs) {
      if (bug.assignedDev != null && !bug.assignedDev.isEmpty()) {
        devBugs.put(bug.assignedDev,
                    devBugs.getOrDefault(bug.assignedDev, 0) + 1);
      }
    }
    return devBugs;
  }

  public int getOpenCount() {
    int open = 0;
    for (Bug bug : allBugs) {
      if ("closed".equals(bug.status))
        continue;
      open++;
    }
    return open;
  }

  public int getClosedCount() {
    int closed = 0;
    for (Bug bug : allBugs) {
      if ("closed".equals(bug.status))
        closed++;
    }
    return closed;
  }

  public void exportToPDF(String jsonPath, String pdfPath) {
    Printer.main(new String[] {jsonPath, pdfPath});
  }
}
