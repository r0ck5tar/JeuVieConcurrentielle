package gui;

import model.Cell;

import java.util.EventObject;

/**
 * Created by Hakim on 19/04/14.
 */
public class CellChangedEvent extends EventObject {
    private int x;
    private int y;
    private boolean alive;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CellChangedEvent(Object source) {
        super(source);
        Cell cell = (Cell) source;
        x = cell.getxCoordinate();
        y = cell.getyCoordinate();
        alive = cell.getStatus();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }

}
