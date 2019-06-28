package button;

import com.bsbls.deneme.designatorfield.GUITester;
import label.VerticalLabel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class CustomCell<T> extends JPanel {

    public static final Color DISABLED = new Color(214, 217, 223);
    public static final Color PRIMARY = new Color(0, 122, 204);
    public static final Color SECONDARY = new Color(238, 238, 242);
    public static final Color BORDER_COLOR = new Color(147, 147, 147);


    public static ImageIcon whiteFilterIcon;
    public static ImageIcon blackFilterIcon;


    protected T data;

    protected boolean selected;
    protected VerticalLabel selectionLabel;

    protected JLabel filterLabel;
    protected boolean filtered;


    protected Color primaryColor;
    protected Color secondaryColor;
    protected Color disabledColor;
    protected Color borderColor = BORDER_COLOR;
    protected JPopupMenu popupMenu;
    private Action action;


    public CustomCell(T data, String text) {
        this(data, text, false);
    }

    public CustomCell(T data, String text, boolean vertical) {
        this(data, text, vertical, PRIMARY, SECONDARY, DISABLED);
    }


    public CustomCell(T data, String text, boolean vertical, Color primaryColor, Color secondaryColor, Color disabledColor) {
        super();

        loadIcons();
        filterLabel = new JLabel();

        this.data = data;

        this.setBorder(new MatteBorder(1, 1, 1, 1, borderColor));
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.disabledColor = disabledColor;

        VerticalLabel verticalLabel = new VerticalLabel(text);
        this.selectionLabel = verticalLabel;
        setVertical(vertical);
        this.selectionLabel.setFont(new Font("TimesRoman", Font.PLAIN, 16));

        setLayout(new BorderLayout());


        JPanel panel = empty(getWidth(), 12);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        panel.add(filterLabel);
        this.add(panel, BorderLayout.NORTH);

        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0,5,0,5);
        p2.add(this.selectionLabel, gc);

        this.add(p2, BorderLayout.CENTER);

        JPanel empty = empty(getWidth(), 12);
        this.add(empty, BorderLayout.SOUTH);


        addMouseListener();
    }

    protected synchronized void loadIcons() {
        if (blackFilterIcon == null) {
            blackFilterIcon = new ImageIcon(new ImageIcon(getClass().getResource("/filled-filter-16-black.png")).getImage().getScaledInstance(8, 8, Image.SCALE_SMOOTH));
        }

        if (whiteFilterIcon == null) {
            whiteFilterIcon = new ImageIcon(new ImageIcon(getClass().getResource("/filled-filter-16-white.png")).getImage().getScaledInstance(8, 8, Image.SCALE_SMOOTH));
        }

    }

    public void setVertical(boolean vertical) {
        if(vertical) {
            selectionLabel.setRotation(VerticalLabel.ROTATE_LEFT);
        }
        else {
            selectionLabel.setRotation(VerticalLabel.DONT_ROTATE);
        }
    }

    protected void addMouseListener() {

        popupMenu = new JPopupMenu();
        popupMenu.add("Go to Filter Window");
        this.setComponentPopupMenu(popupMenu);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setSelected(!selected);
                    if(action != null) {
                        action.actionPerformed(new ActionEvent(e.getSource(), e.getID(), String.valueOf(action.getValue(Action.NAME)), e.getWhen(), e.getModifiers()));
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                highlight();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dehighlight();
            }
        });
    }

    public void dehighlight() {
        if (isEnabled()) {
            if (isSelected()) {
                setBackground(primaryColor);
            } else {
                setBackground(secondaryColor);
            }
        }
        else {

        }
    }

    public void highlight() {
        if (isEnabled()) {
            if (isSelected()) {
                setBackground(primaryColor.darker());
            } else {
                setBackground(secondaryColor.darker());
            }
        } else {

        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        selectionLabel.setEnabled(enabled);
        filterLabel.setEnabled(enabled);
        updateCell();
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
        updateCell();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
        updateCell();
    }

    public boolean isFiltered() {
        return filtered;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void updateCell() {
        if (isEnabled()) {
            if (isSelected()) {
                selectionLabel.setForeground(Color.WHITE);
                setBackground(primaryColor);
            } else {
                selectionLabel.setForeground(Color.BLACK);
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
        }
    }

    public void setAction(Action action) {
        this.action = action;
    }

    protected JPanel empty(int width, int height) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(panel.getPreferredSize());
        return panel;
    }


    public static void main(String[] args) {
        GUITester.test(CustomCell::test, "Nimbus");
    }

    private static JComponent test() {
        JPanel panel = new JPanel(new GridBagLayout());
        int size = 54;
        int N = 8;

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2,2,2, 2);
        gc.fill = GridBagConstraints.BOTH;
        int xoffset = 2;
        int yoffset = 2;
        Random r = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                CustomCell cell = new CustomCell(null, (i+1000) + " Diyarbakır");
                if(i == j) {
                    cell.setEnabled(i != j);

                }
                else {
                    cell.setEnabled(r.nextBoolean());
                    cell.setSelected(r.nextBoolean());
                }

                gc.gridx = j + xoffset;
                gc.gridy = i + yoffset;
                if(i != j) {
                    panel.add(cell, gc);
                }
                else {
                    JButton a = new JButton("A");
                    a.setPreferredSize(new Dimension(200,54));
                    panel.add(a, gc);
                }

            }
        }


        JScrollPane pane = new JScrollPane(panel);
        pane.getVerticalScrollBar().setUnitIncrement(size/2);
        return pane;

    }
}
