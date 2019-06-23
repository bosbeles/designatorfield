package button;

import com.bsbls.deneme.designatorfield.GUITester;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class RoutingCell extends CustomCell {


    private static final String TICK = "\u2714";

    private static ImageIcon whiteFilterIcon;
    private static ImageIcon blackFilterIcon;


    private JLabel filterLabel;
    private boolean filtered;


    public RoutingCell(int size) {
        this(size, PRIMARY, SECONDARY, DISABLED);
    }


    public RoutingCell(int size, Color primaryColor, Color secondaryColor, Color disabledColor) {
        super("", false, primaryColor, secondaryColor, disabledColor);

        loadIcons();
        filterLabel = new JLabel();


        JPanel panel = empty(size, 16);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        panel.add(filterLabel);
        this.add(panel, BorderLayout.NORTH);


        JPanel empty = empty(size, 16);
        this.add(empty, BorderLayout.SOUTH);


        setPreferredSize(new Dimension(size, size));
        setMinimumSize(getPreferredSize());
    }


    @Override
    public void setEnabled(boolean enabled) {
        filterLabel.setEnabled(enabled);
        super.setEnabled(enabled);
    }


    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
        updateCell();
    }

    public boolean isFiltered() {
        return filtered;
    }

    @Override
    public void updateCell() {
        super.updateCell();
        if (isEnabled()) {

            if (isSelected()) {
                selectionLabel.setText(TICK);

            } else {
                selectionLabel.setText("");
            }

            if (isFiltered()) {
                setComponentPopupMenu(popupMenu);
                filterLabel.setIcon(selected ? whiteFilterIcon : blackFilterIcon);
            } else {
                setComponentPopupMenu(null);
                filterLabel.setIcon(null);
            }

        } else {
            setComponentPopupMenu(null);
            if (isFiltered()) {
                filterLabel.setIcon(blackFilterIcon);
            } else {
                filterLabel.setIcon(null);
            }

            if (isSelected()) {
                selectionLabel.setText(TICK);
            } else {
                selectionLabel.setText("");
            }
        }
    }

    private synchronized void loadIcons() {
        if (blackFilterIcon == null) {
            blackFilterIcon = new ImageIcon(new ImageIcon(getClass().getResource("/filled-filter-16-black.png")).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
        }

        if (whiteFilterIcon == null) {
            whiteFilterIcon = new ImageIcon(new ImageIcon(getClass().getResource("/filled-filter-16-white.png")).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH));
        }

    }

    private JPanel empty(int width, int height) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(panel.getPreferredSize());
        return panel;
    }

    public static void main(String[] args) {
        GUITester.test(RoutingCell::test, "Nimbus");
    }

    private static JComponent test() {
        JPanel panel = new JPanel(new GridBagLayout());
        int size = 54;
        int N = 8;

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        int xoffset = 2;
        int yoffset = 2;
        Random r = new Random();
        RoutingCell[][] routingCells = new RoutingCell[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                RoutingCell cell = new RoutingCell(size);
                routingCells[i][j] = cell;
                if (i == j) {
                    cell.setEnabled(i != j);

                } else {
                    cell.setEnabled(r.nextBoolean());
                    cell.setFiltered(r.nextBoolean());
                    cell.setSelected(r.nextBoolean());

                }

                gc.gridx = j + xoffset;
                gc.gridy = i + yoffset;
                if (i != j) {
                    panel.add(cell, gc);
                }

            }
        }

        for (int i = 0; i < N; i++) {
            gc.gridx = xoffset - 1;
            gc.gridy = i + yoffset;
            gc.fill = GridBagConstraints.BOTH;
            CustomCell cell = new CustomCell("" + (i + 1000));
            cell.setEnabled(true);
            final int cellNo = i;
            cell.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int index = 0; index < N; index++) {
                        if (index != cellNo) {
                            routingCells[cellNo][index].setSelected(cell.isSelected());
                        }
                    }

                }
            });
            cell.setPreferredSize(new Dimension(120, 30));
            panel.add(cell, gc);
        }

        for (int i = 0; i < N; i++) {
            gc.gridy = yoffset - 1;
            gc.gridx = i + xoffset;
            gc.weighty = 1;
            gc.fill = GridBagConstraints.BOTH;
            CustomCell cell = new CustomCell("" + (i + 1000), true);
            cell.setEnabled(true);
            final int cellNo = i;
            cell.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int index = 0; index < N; index++) {
                        if (index != cellNo) {
                            routingCells[index][cellNo].setSelected(cell.isSelected());
                        }
                    }

                }
            });
            // cell.setPreferredSize(new Dimension(30, 120));
            panel.add(cell, gc);
        }


        JScrollPane pane = new JScrollPane(panel);
        pane.getVerticalScrollBar().setUnitIncrement(size / 2);
        return pane;

    }
}
