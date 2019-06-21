package com.bsbls.deneme.designatorfield;

import org.apache.commons.lang3.tuple.Pair;
import sun.swing.DefaultLookup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JREDesignatorField extends JPanel {


    private final ListCellRenderer<Pair<Link, String>> renderer = new DesignatorRender();
    private final DefaultComboBoxModel<Pair<Link, String>> model;
    JComboBox<Pair<Link, String>> comboBox;
    JButton button;

    private MultipleDesignatorPanel multipleDesignatorPanel;
    private final JPopupMenu popupMenu;


    public JREDesignatorField(List<Pair<Link, String>> data) {

        button = new JButton("...");

        multipleDesignatorPanel = new MultipleDesignatorPanel();


        popupMenu = new JPopupMenu();
        popupMenu.add(multipleDesignatorPanel);

        button.setComponentPopupMenu(popupMenu);
        button.addActionListener(e -> {

            showPopup();


        });


        comboBox = new JComboBox<>();
        model = new DefaultComboBoxModel<Pair<Link, String>>(data.toArray(new Pair[0])) {

        };
        comboBox.setModel(model);

        comboBox.setRenderer(renderer);
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width + 20, comboBox.getPreferredSize().height));
        comboBox.addActionListener(e -> updateButton(true));


        updateButton(false);
        add(comboBox);
        add(button);
    }

    private void showPopup() {
        Link link = getLink();
        if (link != null) {
            multipleDesignatorPanel.setLink(link);

        }

        popupMenu.show(this, 0, 40);
    }

    private void updateButton(boolean open) {
        Link link = getLink();
        if (link != null) {
            button.setEnabled(link.isMulticast);
            if (link.isMulticast && open) {
                showPopup();
            }
            if(link.isMulticast) {
                comboBox.setToolTipText(((Pair<Link, String>)comboBox.getSelectedItem()).getRight());

            } else {
                comboBox.setToolTipText(null);
            }
        }

    }

    private Link getLink() {
        Object item = comboBox.getSelectedItem();
        if (item instanceof Pair) {
            return (Link) ((Pair) item).getLeft();
        }
        return null;
    }


    static class Link {
        int id;
        List<Integer> designators;
        boolean isMulticast;

        public Link(int id, List<Integer> designators, boolean isMulticast) {
            this.id = id;
            this.designators = designators;
            this.isMulticast = isMulticast;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    private static class DesignatorRender implements ListCellRenderer<Pair<Link, String>> {

        private JPanel panel;
        private JLabel linkLabel;
        private JLabel jreLabel;
        private JButton button;
        private Color bg;
        private Color fg;

        public DesignatorRender() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.RED);
            linkLabel = new JLabel("");
            linkLabel.setPreferredSize(new Dimension(60, 16));

            jreLabel = new JLabel("");
            jreLabel.setPreferredSize(new Dimension(30, 16));

            button = new JButton("...");
            button.setVisible(false);

            panel.add(linkLabel);
            panel.add(new JLabel(" - "));
            panel.add(jreLabel);
            panel.add(button);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Pair<Link, String>> list, Pair<Link, String> value, int index, boolean isSelected, boolean cellHasFocus) {
            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                bg = DefaultLookup.getColor(panel, panel.getUI(), "List.dropCellBackground");
                fg = DefaultLookup.getColor(panel, panel.getUI(), "List.dropCellForeground");

                isSelected = true;
            }


            if (isSelected) {
                panel.setBackground(bg == null ? list.getSelectionBackground() : bg);
                panel.setForeground(fg == null ? list.getSelectionForeground() : fg);
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());

            }

            boolean multicast = value.getKey() != null && value.getKey().isMulticast;

            linkLabel.setText("Link " + value.getLeft());
            jreLabel.setText(value.getRight());

            if (index >= 0)
                button.setVisible(multicast);

            if(multicast) {
                panel.setToolTipText(value.getRight());

            } else {
                panel.setToolTipText(null);
            }

            return panel;
        }


    }

    private class MultipleDesignatorPanel extends JPanel {

        private final JScrollPane scrollPanel;
        private Link link;

        private final JButton selectAll;
        private final JButton close;
        private JButton apply;
        private JTextField jre;

        private JList<Integer> list;

        public MultipleDesignatorPanel() {
            apply = new JButton("Apply");
            selectAll = new JButton("Select All");
            close = new JButton("Close");

            list = new JList<>();
            scrollPanel = new JScrollPane(list);

            list.addListSelectionListener(e-> {
                String collect = list.getSelectedValuesList().stream().map(String::valueOf).collect(Collectors.joining(" "));
                jre.setText(collect);
            });

            apply.addActionListener(e -> {
                Object selectedItem = comboBox.getSelectedItem();
                if (selectedItem instanceof Pair) {
                    String collect = jre.getText();
                    int selectedIndex = comboBox.getSelectedIndex();
                    model.removeElementAt(selectedIndex);
                    Pair<Link, String> pair = (Pair<Link, String>) selectedItem;
                    model.insertElementAt(Pair.of(pair.getLeft(), collect), selectedIndex);
                    comboBox.setSelectedIndex(selectedIndex);
                    popupMenu.setVisible(false);
                }
            });

            jre = new JTextField();


            selectAll.addActionListener(e -> {
                int start = 0;
                int end = list.getModel().getSize() - 1;
                if (end >= 0) {
                    list.setSelectionInterval(start, end);
                }
            });

            close.addActionListener(e -> {
                popupMenu.setVisible(false);
            });
        }

        public void setLink(Link link) {
            this.link = link;
            updatePanel();
        }

        private void updatePanel() {
            this.removeAll();
            this.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();

            gc.insets = new Insets(5, 5, 5, 5);
            gc.gridx = 0;
            gc.gridy = 0;
            gc.gridwidth = 3;
            gc.fill = GridBagConstraints.HORIZONTAL;
            this.add(new JLabel(" Select JRE designators for Link " + link.id), gc);

            gc.gridx = 0;
            gc.gridy = 2;
            gc.gridwidth = 3;
            gc.fill = GridBagConstraints.BOTH;
            list.setListData(link.designators.toArray(new Integer[0]));
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem instanceof Pair) {
                Pair<Link, String> pair = (Pair<Link, String>) selectedItem;
                String value = pair.getValue();

                String[] strings = value.split(" ");
                int[] indices = Arrays.stream(strings).mapToInt(s -> {
                    if ("".equals(s)) {
                        return -1;
                    }
                    return link.designators.indexOf(Integer.valueOf(s));
                }).filter(i -> i != -1).toArray();
                list.setSelectedIndices(indices);
                jre.setText(value);
            }

            this.add(scrollPanel, gc);

            gc.fill = GridBagConstraints.NONE;
            gc.gridx = 0;
            gc.gridy = 3;
            gc.gridwidth = 1;
            this.add(new JLabel("JRE:"), gc);

            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.gridx = 1;
            gc.gridy = 3;
            gc.gridwidth = 2;
            this.add(jre, gc);



            gc.fill = GridBagConstraints.NONE;
            gc.gridx = 0;
            gc.gridy = 4;
            gc.gridwidth = 1;
            this.add(apply, gc);

            gc.gridx = 1;
            gc.gridy = 4;
            gc.gridwidth = 1;
            this.add(selectAll, gc);

            gc.gridx = 2;
            gc.gridy = 4;
            gc.gridwidth = 1;
            this.add(close, gc);


        }
    }
}
