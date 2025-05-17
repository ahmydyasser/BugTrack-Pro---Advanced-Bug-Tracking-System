import com.aspose.cells.*;
import java.io.File;
import java.nio.file.Files;
import org.json.JSONArray;
import org.json.JSONObject;

public class Printer {
  public static void main(String[] args) {
    try {
      // 1. Read the entire JSON file into a String
      String jsonStr = Files.readString(new File("data.json").toPath());

      // 2. Parse the root object
      JSONObject root = new JSONObject(jsonStr);

      // 3. Extract the array under "bugs"
      JSONArray bugsArray = root.getJSONArray("bugs");

      // 4. Create a new Workbook and get the first sheet
      Workbook workbook = new Workbook();
      Worksheet sheet = workbook.getWorksheets().get(0);
      Cells cells = sheet.getCells();

      // 5. Write header row (use the keys of the first bug object)
      JSONObject firstBug = bugsArray.getJSONObject(0);
      int headerCol = 0;
      for (String key : firstBug.keySet()) {
        cells.get(0, headerCol).setValue(key);
        headerCol++;
      }

      // 6. Populate each row with bug data
      for (int i = 0; i < bugsArray.length(); i++) {
        JSONObject bug = bugsArray.getJSONObject(i);
        int col = 0;
        for (String key : firstBug.keySet()) {
          // write the value as string
          cells.get(i + 1, col).setValue(bug.optString(key));
          col++;
        }
      }

      // 7. Save the populated workbook as PDF
      workbook.save("data.pdf", SaveFormat.PDF);
      System.out.println("data.pdf generated successfully.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
