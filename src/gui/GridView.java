package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Hakim on 19/04/14.
 */
public class GridView extends JPanel implements CellChangedListener {
    private JLabel cells[][];
    private ImageIcon deadCell = new ImageIcon("resources/dead.jpg");
    private ImageIcon livingCell = new ImageIcon("resources/alive.jpg");

    public GridView(int width, int height) {
        setLayout(new GridLayout(width, height, 0 , 0));
        setVisible(true);
        cells = new JLabel[width][height];

        for(int i = 0; i<width; i++) {
            for(int j = 0; j < height; j++ ) {
                cells[i][j] = new JLabel();
                cells[i][j].setIcon(deadCell);
                cells[i][j].setVisible(true);
                this.add(cells[i][j]);
            }
        }
        this.setVisible(true);
    }

    @Override
    public void cellChanged(CellChangedEvent cellChange) {
        cells[cellChange.getX()][cellChange.getY()].setIcon(cellChange.isAlive()? livingCell:deadCell);
    }
}
