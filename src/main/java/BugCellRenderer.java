import java.awt.*;
import javax.swing.*;

public class BugCellRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value,
                                                int index, boolean isSelected,
                                                boolean cellHasFocus) {

    JLabel label = (JLabel)super.getListCellRendererComponent(
        list, value, index, isSelected, cellHasFocus);

    if (value instanceof String) {
      String[] parts = ((String)value).split(",");
      label.setText(
          String.format("Name: %s | Type: %s | Priority: %s | Status: %s",
                        parts[0], parts[1], parts[2], parts[3]));
      label.setFont(new Font("Arial", Font.PLAIN, 14));
      label.setForeground(isSelected ? Color.WHITE : Color.BLACK);
      label.setBackground(isSelected ? new Color(0x3498db) : Color.WHITE);
    }

    return label;
  }
}
