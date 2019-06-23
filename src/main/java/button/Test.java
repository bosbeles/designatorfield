package button;

import com.bsbls.deneme.designatorfield.GUITester;

import javax.swing.*;
import java.awt.*;

public class Test {

    public static void main(String[] args) {
        GUITester.test(Test::test, "Nimbus");
    }

    private static JPanel test() {
        final int borderWidth = 1;
        final Color color = new Color(147,147,147);
        final int rows = 5;
        final int cols = 5;
        final int size = 40;
        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));



        GridBagConstraints gc = new GridBagConstraints();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                final Cell cell = new Cell(row, col, size);
                if(row == col) {
                    cell.setEnabled(false);
                }
                if(row == 4 && col == 1) {
                    cell.setEnabled(false);
                    cell.setSelected(true);
                }

                if(row == 4 && col == 2) {
                    cell.setEnabled(true);
                    cell.setSelected(true);
                    cell.setFiltered(true);
                }
                if(row == 4 && col == 3) {
                    cell.setEnabled(false);
                    cell.setSelected(true);
                    cell.setFiltered(true);
                }
                gc.gridx = col + 2;
                gc.gridy = row + 2;
                panel.add(cell, gc);
            }
        }

        return panel;
    }
}
