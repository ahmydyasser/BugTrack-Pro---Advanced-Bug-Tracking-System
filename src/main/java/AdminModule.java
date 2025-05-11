import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class AdminModule {

    // 1. View all bugs for a project
    public List<JSONObject> viewProjectBugs(String projectName) {
        List<JSONObject> projectBugs = new ArrayList<>();
        try {
            JSONObject data = readJsonFile("data.json");
            JSONArray bugs = (JSONArray) data.get("bugs");
            for (Object obj : bugs) {
                JSONObject bug = (JSONObject) obj;
                if (projectName.equals(bug.get("project"))) {
                    projectBugs.add(bug);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectBugs;
    }

    // 2. Update user data
    public boolean updateUser(String username, JSONObject newUserData) {
        try {
            JSONObject data = readJsonFile("users.json");
            JSONArray users = (JSONArray) data.get("users");
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                if (username.equals(user.get("username"))) {
                    users.set(i, newUserData);
                    writeJsonFile("users.json", data);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Add user
    public boolean addUser(JSONObject newUser) {
        try {
            JSONObject data = readJsonFile("users.json");
            JSONArray users = (JSONArray) data.get("users");
            users.add(newUser);
            writeJsonFile("users.json", data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Delete user
    public boolean deleteUser(String username) {
        try {
            JSONObject data = readJsonFile("users.json");
            JSONArray users = (JSONArray) data.get("users");
            Iterator<Object> it = users.iterator();
            while (it.hasNext()) {
                JSONObject user = (JSONObject) it.next();
                if (username.equals(user.get("username"))) {
                    it.remove();
                    writeJsonFile("users.json", data);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // --- Helper methods for JSON ---
    private JSONObject readJsonFile(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(reader);
    }

    private void writeJsonFile(String filename, JSONObject data) throws Exception {
        FileWriter writer = new FileWriter(filename);
        writer.write(data.toJSONString());
        writer.flush();
        writer.close();
    }
}
