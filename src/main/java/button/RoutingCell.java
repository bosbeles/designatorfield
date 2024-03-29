package button;

import com.bsbls.deneme.designatorfield.GUITester;
import label.VerticalLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class RoutingCell<T> extends CustomCell<T> {


    private static final String TICK = "\u2714";




    public RoutingCell(T data, int size) {
        this(data, size, PRIMARY, SECONDARY, DISABLED);
    }


    public RoutingCell(T data, int size, Color primaryColor, Color secondaryColor, Color disabledColor) {
        super(data, "", false, primaryColor, secondaryColor, disabledColor);









        setPreferredSize(new Dimension(size, size));
        setMinimumSize(getPreferredSize());
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
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

        } else {
            setComponentPopupMenu(null);


            if (isSelected()) {
                selectionLabel.setText(TICK);
            } else {
                selectionLabel.setText("");
            }
        }
    }




    public static void main(String[] args) {
        GUITester.test(RoutingCell::test, "Nimbus");
    }

    private static JComponent test() {
        JPanel panel = new JPanel(new GridBagLayout());
        int size = 46;
        int N = 5;

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        int xoffset = 2;
        int yoffset = 2;
        Random r = new Random();
        RoutingCell[][] routingCells = new RoutingCell[N][N];
        CustomCell[] txLinks = new CustomCell[N];
        CustomCell[] rxLinks = new CustomCell[N];
        for (int i = 0; i < N; i++) {
            boolean enabled = r.nextBoolean();
            for (int j = 0; j < N; j++) {
                final int x = j;
                final int y = i;
                RoutingCell cell = new RoutingCell(null, size);
                routingCells[i][j] = cell;
                if (i == j) {
                    cell.setEnabled(i != j);

                } else {
                    cell.setEnabled(enabled);
                    cell.setFiltered(r.nextBoolean());
                    cell.setSelected(r.nextBoolean());

                }

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        rxLinks[y].highlight();
                        txLinks[x].highlight();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        rxLinks[y].dehighlight();
                        txLinks[x].dehighlight();
                    }
                });

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
            CustomCell cell = new CustomCell(null, i == 0 ? "HOST" :  "" + (i + 1000));
            cell.setFiltered(r.nextBoolean());
            rxLinks[i] = cell;
            cell.setEnabled(true);
            final int cellNo = i;

            cell.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int index = 0; index < N; index++) {
                        if (index != cellNo) {
                            routingCells[cellNo][index].setEnabled(rxLinks[cellNo].isSelected() && txLinks[index].isSelected());
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
            CustomCell cell = new CustomCell(null, i == 0 ? "HOST" : "" + (i + 1000), true);
            cell.setFiltered(r.nextBoolean());
            cell.setEnabled(true);
            txLinks[i] = cell;
            final int cellNo = i;
            cell.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int index = 0; index < N; index++) {
                        if (index != cellNo) {
                            routingCells[index][cellNo].setEnabled((rxLinks[index].isSelected() && txLinks[cellNo].isSelected()));
                        }
                    }


                }
            });
            // cell.setPreferredSize(new Dimension(30, 120));
            panel.add(cell, gc);
        }

        gc.gridx = 0;
        gc.gridy = yoffset+ 1;
        gc.gridheight = N-1;
        gc.gridwidth = 1;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        VerticalLabel label = new VerticalLabel("FROM (Rx)", JLabel.CENTER);

        label.setOpaque(true);
        label.setRotation(VerticalLabel.ROTATE_LEFT);
        CustomCell fromCell = new CustomCell(null, "Rx", true);
        panel.add(fromCell, gc);


        gc.gridx = xoffset+1;
        gc.gridy = 0;
        gc.gridheight = 1;
        gc.gridwidth = N-1;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        JLabel toLabel = new JLabel("TO (Tx)", JLabel.CENTER);
        toLabel.setBackground(Color.lightGray);

        CustomCell toCell = new CustomCell(null, "Tx");
        toCell.setPreferredSize(new Dimension(toCell.getPreferredSize().width, toCell.getPreferredSize().height - 6));
        panel.add(toCell, gc);


        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;

        JPanel p = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font("TimesRoman",  Font.PLAIN, 20));
                g2d.drawString("To", 60, 60);
                g2d.drawString("From", 5, this.getHeight() - 40 );
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawLine(0,0, this.getWidth(), this.getHeight());

            }
        };
        panel.add(p, gc);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                routingCells[i][j].setEnabled((rxLinks[i].isSelected() && txLinks[j].isSelected()));
            }
        }

        JPanel legend = new JPanel();

        legend.add(createLegendButton(null, CustomCell.blackFilterIcon));
        legend.add(new JLabel("Filtered"));

        legend.add(createLegendButton(CustomCell.PRIMARY, null));
        legend.add(new JLabel("Active"));

        gc.gridy = N+2;
        gc.gridx = 1;
        gc.gridwidth = N + 1;
        panel.add(legend, gc);

        /*
        for (int i = 1; i < N; i++) {
            gc.gridx = xoffset - 2;
            gc.gridy = i + yoffset;
            gc.weighty = 0;
            gc.fill = GridBagConstraints.NONE;
            RoutingCell cell = new RoutingCell(null, size);
            final int cellNo = i;
            cell.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int index = 0; index < N; index++) {
                        if (index != cellNo) {
                            routingCells[cellNo][index].setEnabled(cell.isSelected());
                            rxLinks[cellNo].setSelected(cell.isSelected());
                        }
                    }


                }
            });
            panel.add(cell, gc);
        }

        for (int i = 1; i < N; i++) {
            gc.gridx = i + xoffset;
            gc.gridy = yoffset - 2;
            gc.fill = GridBagConstraints.NONE;
            RoutingCell cell = new RoutingCell(null, size);
            final int cellNo = i;
            cell.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    for (int index = 0; index < N; index++) {
                        if (index != cellNo) {
                            routingCells[index][cellNo].setEnabled(cell.isSelected());
                            txLinks[cellNo].setSelected(cell.isSelected());
                        }
                    }


                }
            });
            panel.add(cell, gc);
        }
*/

        JScrollPane pane = new JScrollPane(panel);
        pane.getVerticalScrollBar().setUnitIncrement(size / 2);
        return pane;

    }

    private static JComponent createLegendButton(Color color, ImageIcon icon) {
        JPanel button = new JPanel();
        button.setPreferredSize(new Dimension(20, 20));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if(color != null) {
            button.setBackground(color);
        }

        button.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        if(icon != null) {
            button.add(new JLabel(icon, SwingConstants.LEADING));
        }


        return button;
    }
}
