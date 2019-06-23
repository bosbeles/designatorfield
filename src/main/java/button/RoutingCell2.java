package button;

import com.bsbls.deneme.designatorfield.GUITester;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class RoutingCell2 extends JPanel {

    public static final Color DISABLED = new Color(214, 217, 223);
    public static final Color PRIMARY = new Color(0, 122, 204);
    public static final Color SECONDARY = new Color(238, 238, 242);

    private static final String TICK = "\u2714";


    private static ImageIcon whiteFilterIcon;
    private static ImageIcon blackFilterIcon;


    private JLabel filterLabel;
    private JLabel selectionLabel;
    private boolean selected;
    private boolean filtered;

    private Color primaryColor;
    private Color secondaryColor;
    private Color disabledColor;
    private Color borderColor = new Color(147, 147, 147);
    private JPopupMenu popupMenu;

    public RoutingCell2(int size) {
        this(size, PRIMARY, SECONDARY, DISABLED);
    }


    public RoutingCell2(int size, Color primaryColor, Color secondaryColor, Color disabledColor) {
        super();

        this.setBorder(new MatteBorder(1, 1, 1, 1, borderColor));
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.disabledColor = disabledColor;

        loadIcons();
        filterLabel = new JLabel();
        selectionLabel = new JLabel();
        selectionLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        setLayout(new BorderLayout());


        JPanel panel = empty(size, 16);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        panel.add(filterLabel);
        this.add(panel, BorderLayout.NORTH);


        JPanel empty = empty(size, 16);
        this.add(empty, BorderLayout.SOUTH);


        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p2.setOpaque(false);
        p2.add(selectionLabel);

        this.add(p2, BorderLayout.CENTER);

        setPreferredSize(new Dimension(size, size));
        setMinimumSize(getPreferredSize());

        updateLabels();

        addMouseListener();
    }

    private void addMouseListener() {

        popupMenu = new JPopupMenu();
        popupMenu.add("Go to Filter Window");
        this.setComponentPopupMenu(popupMenu);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && e.getButton() == MouseEvent.BUTTON1) {
                    setSelected(!selected);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    if (isSelected()) {
                        setBackground(primaryColor.darker());
                    } else {
                        setBackground(secondaryColor.darker());
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    if (isSelected()) {
                        setBackground(primaryColor);
                    } else {
                        setBackground(secondaryColor);
                    }
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        selectionLabel.setEnabled(enabled);
        filterLabel.setEnabled(enabled);
        updateLabels();
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
        updateLabels();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
        updateLabels();
    }

    public boolean isFiltered() {
        return filtered;
    }

    private void updateLabels() {
        if (isEnabled()) {

            if (isSelected()) {
                selectionLabel.setForeground(Color.WHITE);
                selectionLabel.setText(TICK);
                setBackground(primaryColor);

            } else {
                selectionLabel.setForeground(Color.BLACK);
                selectionLabel.setText("");
                setBackground(secondaryColor);
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

            setBackground(disabledColor);
            selectionLabel.setForeground(Color.BLACK);


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
        GUITester.test(RoutingCell2::test, "Nimbus");
    }

    private static JComponent test() {
        JPanel panel = new JPanel(new GridBagLayout());
        int size = 54;
        int N = 8;

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2,2,2, 2);
        int xoffset = 2;
        int yoffset = 2;
        Random r = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                RoutingCell2 cell = new RoutingCell2(size);
                if(i == j) {
                    cell.setEnabled(i != j);

                }
                else {
                    cell.setEnabled(r.nextBoolean());
                    cell.setFiltered(r.nextBoolean());
                    cell.setSelected(r.nextBoolean());

                }

                gc.gridx = j + xoffset;
                gc.gridy = i + yoffset;
                if(i != j) {
                    panel.add(cell, gc);
                }

            }
        }


        JScrollPane pane = new JScrollPane(panel);
        pane.getVerticalScrollBar().setUnitIncrement(size/2);
        return pane;

    }
}
