package model;

import gui.CellChangedEvent;
import gui.CellChangedListener;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Created by Hakim on 19/04/14.
 */
public class Cell implements Runnable{
    private int xCoordinate;
    private int yCoordinate;
    private boolean alive;
    private boolean willLive = false;
    private ArrayList<Cell> neighbours;
    private CellChangedListener listener;
    private int reads = 0;
    private int generation = 0;

    @Override
    public void run() {
        while(true) {
            try {
                calculateNextStep();
                System.out.println(this);
                applyNextStep();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int livingNeighboursCount() throws InterruptedException {
        int livingNeighbours = 0;
        for(Cell c : neighbours) {
            if (c.isAlive()) livingNeighbours++;
        }
        return livingNeighbours;
    }

    public void calculateNextStep() throws InterruptedException {
        /*Determine the next step (whether the cell will live or not) based on the
          number of living neighbours and applying the rules of the Game of Life. */
        switch(livingNeighboursCount()) {
            case 2: willLive = alive; break;
            case 3: willLive = true; break;
            default: willLive = false;
        }
    }

    public  void applyNextStep() throws InterruptedException {

        setAlive(willLive);
        //System.out.print("Cell [" + xCoordinate + ", " + yCoordinate +"] is now ");
        //if(alive) System.out.println("alive"); else System.out.println("dead");

        generation++;
        sleep(100);
    }

    public boolean getStatus() {
        return this.alive;
    }
    public void setStatus(boolean alive) { this.alive = alive; listener.cellChanged(new CellChangedEvent(this)); }

    public synchronized boolean isAlive() throws InterruptedException {
        while(reads == 8) wait();
        reads++;
        notifyAll();
        return this.alive;

    }
    public synchronized void setAlive(boolean alive) throws InterruptedException {
        while(reads < 8) wait();

        if(this.alive != alive) {
        this.alive = alive;
        listener.cellChanged(new CellChangedEvent(this));
        }

        reads = 0;
        notifyAll();

    }

    public void setCoordinates(int x, int y)     {xCoordinate = x; yCoordinate = y;}
    public int getxCoordinate() { return xCoordinate; }
    public int getyCoordinate() { return yCoordinate; }

    public ArrayList<Cell> getNeighbours() { return neighbours; }
    public void setNeighbours(ArrayList<Cell> neighbours) {this.neighbours = neighbours;}
    public void setListener(CellChangedListener listener) { this.listener = listener; }

    @Override
    public String toString(){
        String id = Integer.toString(xCoordinate) + ", " + Integer.toString(yCoordinate)
                  + "\treads: " + reads + "\tgeneration: " + generation;
        if(alive) return  id + " - alive";
        else      return  id + " - dead";
    }
}
