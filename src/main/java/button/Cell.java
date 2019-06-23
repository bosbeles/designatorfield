package button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cell extends JPanel  {
    public static final Color COLOR = new Color(147, 147, 147);
    public static final Color PRIMARY = new Color(0, 122, 204);
    public static final Color SECONDARY = new Color(238, 238, 242);
    private final Image blackFilter;
    private final Image whiteFilter;
    private  JLabel label;
    private int col;
    private int row;
    private int borderWidth = 1 ;
    private Color borderColor = COLOR;
    private boolean selected;
    private boolean enabled = true;
    private boolean filtered;
    private Color primary = PRIMARY;
    private Color secondary = SECONDARY;
    private Color disabledColor = new Color(214, 217, 223);

    public Cell(int row, int col, int size) {
        this.col = col;
        this.row = row;
        this.setPreferredSize(new Dimension(size, size));
        this.setMinimumSize(this.getPreferredSize());


        blackFilter = new ImageIcon(getClass().getResource("/filled-filter-16-black.png")).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
        whiteFilter = new ImageIcon(getClass().getResource("/filled-filter-16-white.png")).getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);


        JPopupMenu pop = new JPopupMenu("Deneme");
        pop.add(new JMenuItem("Deneme2"));
        this.setComponentPopupMenu(pop);

        label = new JLabel("");
        label.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(enabled && e.getButton() == MouseEvent.BUTTON1) {
                    setSelected(!selected);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(enabled) {
                    if(selected) {
                        setBackground(primary.darker());
                    }
                    else {
                        setBackground(secondary.darker());
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(enabled) {
                    if(selected) {
                        setBackground(primary);
                    }
                    else {
                        setBackground(secondary);
                    }
                }
            }
        });

        updateBorders();

        setSelected(false);

        this.add(label);
    }

    private void updateBorders() {
        if (row == 0) {
            if (col == 0) {
                // Top left corner, draw all sides
                setBorder(BorderFactory.createLineBorder(borderColor));

            } else {
                // Top edge, draw all sides except left edge
                setBorder(BorderFactory.createMatteBorder(borderWidth,
                        0,
                        borderWidth,
                        borderWidth,
                        borderColor));
            }
        } else {
            if (col == 0) {
                // Left-hand edge, draw all sides except top
                setBorder(BorderFactory.createMatteBorder(0,
                        borderWidth,
                        borderWidth,
                        borderWidth,
                        borderColor));
            } else {
                // Neither top edge nor left edge, skip both top and left lines
                setBorder(BorderFactory.createMatteBorder(0,
                        0,
                        borderWidth,
                        borderWidth,
                        borderColor));
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(enabled) {
            if(selected) {
                setBackground(primary);
                label.setText("\u2714");
                label.setForeground(Color.WHITE);
            }
            else {
                setBackground(secondary);
                label.setForeground(Color.BLACK);
                label.setText("");
            }
        }
        else {
            label.setForeground(Color.BLACK);
            setBackground(disabledColor);
            if(selected) {
                label.setText("\u2714");
            }
            else {
                label.setText("");
            }
        }


    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        label.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    @Override
    public void paintComponent(Graphics g) {

        Dimension size = getSize();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(enabled) {
            g2.setColor(getBackground());

        }
        else {
            g2.setColor(disabledColor);

        }

        super.paintComponent(g);

        g2.fillRoundRect(1,1, size.width-2 , size.height-2 , 0, 0);

        if(filtered) {
            if(selected && enabled) {
                g.drawImage(whiteFilter, size.width -14, 2, null);
            }
            else {
                g.drawImage(blackFilter, size.width -14, 2, null);
            }
        }

        repaint();


    }
}
