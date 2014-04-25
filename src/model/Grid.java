package model;

import exceptions.InitializingPatternOutOfBoundsException;

import java.util.ArrayList;

/**
 * Created by Hakim on 19/04/14.
 */
public class Grid {
    private int width;
    private int height;
    private Cell grid[][];
    private long generation;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        generation = 0;
        setup();
    }

    public void start() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                new Thread(grid[x][y]).start();
            }
        }
    }

    public void initializeWithPattern(Pattern pattern)
            throws InitializingPatternOutOfBoundsException {
        int xOffset = (width - pattern.getDimension())/2;
        int yOffset = (height - pattern.getDimension())/2;
        initializeWithPattern(pattern, xOffset, yOffset);
    }

    public void initializeWithPattern(Pattern pattern, int xOffset, int yOffset)
            throws InitializingPatternOutOfBoundsException{
        if(pattern.getDimension() + xOffset > width || pattern.getDimension() + yOffset > height) {
            throw new InitializingPatternOutOfBoundsException();
        }

        else{
            for(int x = 0; x < pattern.getDimension(); x++) {
                for(int y = 0; y < pattern.getDimension(); y++) {
                    grid[x + xOffset][y + yOffset].setAlive(pattern.read(x, y));
                }
            }
        }
    }

    public void setup() {
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++) {
                grid[x][y] = new Cell();
                grid[x][y].setCoordinates(x, y);
            }
        }

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++) {
                grid[x][y].setNeighbours(determineNeighbours(x, y));
            }
        }
    }

    private ArrayList<Cell> determineNeighbours(int x, int y) {
        ArrayList<Cell> neighbours = new ArrayList<Cell>();
        if(x!=0)  {
            neighbours.add(grid[x-1][y]);
            neighbours.add(grid[x-1][(y+1)%height]);
            if(y!=0) neighbours.add(grid[x-1][y-1]);
            else neighbours.add(grid[x-1][height-1]);
        }
        else {
            neighbours.add(grid[width-1][y]);
            neighbours.add(grid[width-1][(y+1)%height]);
            if(y!=0) neighbours.add(grid[width-1][y-1]);
            else neighbours.add(grid[width-1][height-1]);
        }

        if(y!=0) {
            neighbours.add(grid[x][y-1]);
            neighbours.add(grid[(x+1)%width][y-1]);
        }
        else {
            neighbours.add(grid[x][height-1]);
            neighbours.add(grid[(x+1)%width][height-1]);
        }

        neighbours.add(grid[(x+1)%width][y]);
        neighbours.add(grid[(x+1)%width][(y+1)%height]);
        neighbours.add(grid[x][(y+1)%height]);

        return neighbours;
    }

    public int getWidth()  { return width; }
    public int getHeight() { return height; }
    public Cell getCell(int x, int y) { return grid[x][y]; }
}
