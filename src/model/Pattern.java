package model;

/**
 * Created by Hakim on 19/04/14.
 */
public class Pattern {
    private byte[][] pattern;

    public Pattern(byte pattern[][]) { this.pattern = pattern; }

    public int getDimension() { return pattern.length; }

    public boolean read(int x, int y) {
        switch (pattern[x][y]) {
            case 0: return false;
            default: return true;
        }
    }
}
