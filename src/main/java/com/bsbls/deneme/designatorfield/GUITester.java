package com.bsbls.deneme.designatorfield;

import javax.swing.*;
import java.util.function.Supplier;

public class GUITester {


    public static void test(JPanel panel) {
        test(()-> panel, null);
    }

    public static void test(Supplier<JPanel> panelSupplier) {
        test(panelSupplier, "Nimbus");
    }

    public static void test(Supplier<JPanel> panelSupplier, String lookAndFeel) {
        setLookAndFeel(lookAndFeel);

        JFrame frame = new JFrame();
        frame.setContentPane(panelSupplier.get());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void setLookAndFeel(String lookAndFeel) {
        try {
            UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo lookAndFeelInfo : lookAndFeelInfos) {
                if(lookAndFeelInfo.getName().contains(lookAndFeel)) {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName().toString());
                }

            }
        }
        catch (Exception ex) {

        }
    }
}
