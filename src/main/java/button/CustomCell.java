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

public class CustomCell extends JPanel {

    public static final Color DISABLED = new Color(214, 217, 223);
    public static final Color PRIMARY = new Color(0, 122, 204);
    public static final Color SECONDARY = new Color(238, 238, 242);



    protected boolean selected;
    protected VerticalLabel selectionLabel;



    protected Color primaryColor;
    protected Color secondaryColor;
    protected Color disabledColor;
    protected Color borderColor = new Color(147, 147, 147);
    protected JPopupMenu popupMenu;
    private Action action;


    public CustomCell(String text) {
        this(text, false);
    }

    public CustomCell(String text, boolean vertical) {
        this(text, vertical, PRIMARY, SECONDARY, DISABLED);
    }


    public CustomCell(String text, boolean vertical, Color primaryColor, Color secondaryColor, Color disabledColor) {
        super();

        this.setBorder(new MatteBorder(1, 1, 1, 1, borderColor));
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.disabledColor = disabledColor;

        VerticalLabel verticalLabel = new VerticalLabel(text);
        this.selectionLabel = verticalLabel;
        setVertical(vertical);
        this.selectionLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        setLayout(new BorderLayout());


        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0,5,0,5);
        p2.add(this.selectionLabel, gc);

        this.add(p2, BorderLayout.CENTER);

        addMouseListener();
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
            public void mouseExited(MouseEvent e) {
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
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        selectionLabel.setEnabled(enabled);
        updateCell();
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
        updateCell();
    }

    public boolean isSelected() {
        return selected;
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

        } else {
            setComponentPopupMenu(null);
            setBackground(disabledColor);
            selectionLabel.setForeground(Color.BLACK);
        }
    }

    public void setAction(Action action) {
        this.action = action;
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
                CustomCell cell = new CustomCell((i+1000) + " Diyarbakır");
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
