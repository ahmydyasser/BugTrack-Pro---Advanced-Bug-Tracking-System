import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import org.json.simple.*;
import org.json.simple.parser.*;

public class UserDatabase {
  private static final List<User> users = new ArrayList<>();

  // Static initializer loads users when class is loaded
  static { loadUsersFromJson("users.json"); }

  public static void addUser(User user) { users.add(user); }

  public static void removeUser(User user) { users.remove(user); }

  public static List<User> getAllUsers() { return new ArrayList<>(users); }

  public static List<User> getUsersByRole(String role) {
    return users.stream()
        .filter(user -> Objects.equals(user.getRole(), role))
        .collect(Collectors.toList());
  }

  public static boolean isValidUser(String username, String password) {
    return users.stream().anyMatch(u
                                   -> u.getUsername().equals(username) &&
                                          u.getPassword().equals(password));
  }

  public static User getUser(String username) {
    return users.stream()
        .filter(u -> u.getUsername().equals(username))
        .findFirst()
        .orElse(null);
  }

  public static void saveUsersToJson(String filePath) {
    try {
      JSONArray jsonArray = new JSONArray();

      for (User user : users) {
        JSONObject userJson = new JSONObject();
        userJson.put("username", user.getUsername());
        userJson.put("password", user.getPassword());
        userJson.put("role", user.getRole());
        jsonArray.add(userJson);
      }

      try (FileWriter writer = new FileWriter(filePath)) {
        writer.write(jsonArray.toJSONString());
      }

      System.out.println("Users saved to JSON");

    } catch (IOException e) {
      System.err.println("Error saving users to JSON: " + e.getMessage());
    }
  }

  private static void loadUsersFromJson(String filename) {
    try {
      // Try to load from classpath first
      File file = new File(filename);
      if (!file.exists()) {
        throw new RuntimeException("File not found: " + filename);
      }
      JSONParser parser = new JSONParser();
      JSONArray jsonArray =
          (JSONArray)parser.parse(new FileReader(file));

      System.out.println("Raw JSON Content: " + jsonArray.toJSONString());
      System.out.println("Loaded " + jsonArray.size() + " users from JSON");

      for (Object obj : jsonArray) {
        if (!(obj instanceof JSONObject)) {
          System.err.println("Invalid JSON object in array: " + obj);
          continue;
        }

        JSONObject userJson = (JSONObject)obj;
        String username = getStringValue(userJson, "username");
        String password = getStringValue(userJson, "password");
        String role = getStringValue(userJson, "role");

        if (username == null || password == null || role == null) {
          System.err.println("Missing required fields in user: " + userJson);
          continue;
        }

        users.add(new User(username, password, role));
        System.out.println("Loaded: " + username + " | " + password + " | " +
                           role);
      }

    } catch (FileNotFoundException e) {
      System.err.println("File not found: " +
                         new File(filename).getAbsolutePath());
    } catch (ParseException e) {
      System.err.println("Invalid JSON format: " + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Unexpected error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  // Helper to safely extract String values from JSON
  private static String getStringValue(JSONObject json, String key) {
    Object value = json.get(key);
    if (value == null)
      return null;
    return value.toString(); // Always convert to string for consistency
  }
}
