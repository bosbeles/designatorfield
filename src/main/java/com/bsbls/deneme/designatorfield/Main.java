package com.bsbls.deneme.designatorfield;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        GUITester.test(Main::test, "Nimbus"); // Nimbus, Windows, Metal
    }

    private static JPanel test() {
        JPanel panel = new JPanel();

        List<Pair<JREDesignatorField.Link, String>> data = new ArrayList<>();

        data.add(Pair.of(new JREDesignatorField.Link(1001, Arrays.asList(1), false), "1"));
        data.add(Pair.of(new JREDesignatorField.Link(1002, Arrays.asList(2), false), "2"));
        data.add(Pair.of(new JREDesignatorField.Link(1003, Arrays.asList(3, 4, 5), true), ""));
        data.add(Pair.of(new JREDesignatorField.Link(1004, Arrays.asList(6), false), "6"));


        JREDesignatorField field = new JREDesignatorField(data);


        panel.add(new JLabel("Remote JRE: "));
        panel.add(field);
        panel.add(new JButton("Send"));

        return panel;
    }

}
