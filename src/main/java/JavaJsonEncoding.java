import org.json.simple.JSONObject;

public class JavaJsonEncoding {
  public static void main(String args[]) {
    JSONObject file = new JSONObject();
    file.put("Full Name", "Ritu Sharma");
    file.put("Roll No.", Integer.valueOf(1704310046));
    file.put("Tuition Fees", Double.valueOf(65400));
    System.out.print(file);
  }
}
