import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class DevModule {
    private final String devName;
    private List<Bug> assignedBugs = new ArrayList<>();

    public DevModule(String devName) {
        this.devName = devName;
        loadAssignedBugs();
    }

    public List<Bug> getAssignedBugs() {
        return assignedBugs;
    }

    public void loadAssignedBugs() {
        assignedBugs.clear();
        try (FileReader reader = new FileReader("data.json")) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(reader);
            JSONArray bugs = (JSONArray) data.get("bugs");
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
                if (devName.equals(assignedDev)) {
                    assignedBugs.add(bug);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading bugs for developer: " + e.getMessage());
        }
    }

    public void markBugAsComplete(Bug updatedBug) {
        try (FileReader reader = new FileReader("data.json")) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(reader);
            JSONArray bugs = (JSONArray) data.get("bugs");
            for (Object obj : bugs) {
                JSONObject bugObj = (JSONObject) obj;
                String name = (String) bugObj.get("name");
                String assignedDev = (String) bugObj.get("assignedTo");
                if (name.equals(updatedBug.name) && devName.equals(assignedDev)) {
                    bugObj.put("status", "closed");
                    break;
                }
            }
            try (FileWriter writer = new FileWriter("data.json")) {
                writer.write(data.toJSONString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Update in-memory list
        loadAssignedBugs();
    }

    public void notifyTester(Bug bug) {
        // Notify only the tester who reported this bug (using reportedBy field in data.json)
        try {
            JSONParser parser = new JSONParser();
            File file = new File("users.json");
            JSONArray usersArray;
            Object parsed = parser.parse(new FileReader(file));
            if (parsed instanceof JSONArray) {
                usersArray = (JSONArray) parsed;
            } else if (parsed instanceof JSONObject) {
                JSONObject dataObj = (JSONObject) parsed;
                usersArray = (JSONArray) dataObj.get("users");
            } else {
                usersArray = new JSONArray();
            }
            // Find the reportedBy field for this bug in data.json
            String reportedBy = null;
            try (FileReader bugReader = new FileReader("data.json")) {
                JSONObject data = (JSONObject) parser.parse(bugReader);
                JSONArray bugs = (JSONArray) data.get("bugs");
                for (Object obj : bugs) {
                    JSONObject bugObj = (JSONObject) obj;
                    String name = (String) bugObj.get("name");
                    String assignedDev = (String) bugObj.get("assignedTo");
                    if (name.equals(bug.name) && devName.equals(assignedDev)) {
                        reportedBy = (String) bugObj.get("reportedBy");
                        break;
                    }
                }
            }
            boolean notified = false;
            if (reportedBy != null) {
                for (Object obj : usersArray) {
                    JSONObject user = (JSONObject) obj;
                    String uname = (String) user.get("username");
                    String role = (String) user.get("role");
                    if (reportedBy.equals(uname) && "tester".equals(role)) {
                        JSONArray notifications = (JSONArray) user.get("notifications");
                        if (notifications == null) {
                            notifications = new JSONArray();
                            user.put("notifications", notifications);
                        }
                        String message = "Bug '" + bug.name + "' has been marked as complete by developer '" + devName + "'.";
                        notifications.add(message);
                        notified = true;
                        break;
                    }
                }
            }
            // Save back to users.json
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(usersArray.toJSONString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
} 