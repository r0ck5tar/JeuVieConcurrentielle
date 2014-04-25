package gui;

import sun.applet.Main;

import javax.swing.*;

/**
 * Created by Hakim on 19/04/14.
 */
public class MainView extends JFrame {
    private GridView gridView;

    public MainView(int width, int height) {
        super("Game of Life");
        gridView = new GridView(width, height);
        this.add(gridView);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public GridView getGridView() { return gridView; }
}
