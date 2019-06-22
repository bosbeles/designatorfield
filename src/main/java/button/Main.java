package button;

import com.bsbls.deneme.designatorfield.GUITester;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {


    public static void main(String[] args) {
        GUITester.test(Main::test, "Nimbus");
    }

    public static JPanel test() {
        Color primary = new Color(0, 122, 204);
        Color secondary = new Color(238, 238, 242);

        JPanel p = new JPanel();
        p.setBackground(Color.white);

        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 1; i <= 5; i++) {
            final JButton button = new JButton("Button #" + i);

            //button.setFont(new Font("Calibri", Font.PLAIN, 14));


            button.addMouseListener(new java.awt.event.MouseAdapter() {
                Color old;
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if(button.isSelected()) {
                        button.setBackground(primary.darker());
                    }
                    else {
                        button.setBackground(secondary.darker());
                    }

                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if(button.isSelected()) {
                        button.setBackground(primary);
                    }
                    else {
                        button.setBackground(secondary);
                    }
                }
            });

            button.addActionListener(e-> {
                button.setSelected(!button.isSelected());
                if(button.isSelected()) {
                    button.setBackground(primary);
                    button.setForeground(Color.WHITE);
                }
                else {
                    button.setBackground(secondary);
                    button.setForeground(Color.BLACK);
                }

            });

            // customize the button with your own look
            button.setUI(new StyledButtonUI());
            p.add(button);
        }

        return p;
    }
}
