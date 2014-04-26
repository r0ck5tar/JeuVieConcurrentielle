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

    /*
        Compte le nombre de cellules voisines vivantes.
     */
    private int livingNeighboursCount() throws InterruptedException {
        int livingNeighbours = 0;

        for(Cell c : neighbours) {
            if (c.isAlive()) livingNeighbours++;
        }
        return livingNeighbours;
    }

    /*Determine the next step (whether the cell will live or not) based on the
      number of living neighbours and applying the rules of the Game of Life. */
    public void calculateNextStep() throws InterruptedException {
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
        /*  ça semble marcher correctement avec sleep 400 (et même 100 si on a un Pattern plus petit
            comme l'oscilateur (voir resources/Patterns.txt)
            Si on diminue cette valeur, on verra dans la sortie du console que la valeur de generation
            ne sont plus synchro (certaines cellules peuvent être au n+3iem pas, et d'autres au nieme pas
            par exemple
        */
        sleep(400);
    }

    /*
        getStatus and setStatus sont les équivalentes de isAlive et setAlive, à part qu'elles sont pas synchro.
        Elles sont appelées dans Grid (pour l'initialisation - d'où la raison pour la quelle elles sont pas
        synchro)
     */
    public boolean getStatus() { return this.alive;}
    public void setStatus(boolean alive) { this.alive = alive; listener.cellChanged(new CellChangedEvent(this)); }

    /*
     Ne permet la lecture de l'état d'une cellule que si read != 8 (la raison m'échappe actuellement)
    */
    public synchronized boolean isAlive() throws InterruptedException {
        while(reads == 8) wait();
        reads++;
        notifyAll();
        return this.alive;

    }

    /*
    Ne permet la modification de l'état d'une cellule que si read == 8 (c-à-d que la cellule a été lue
    8 fois - donc par tous ses voisins)
    */
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
