package exceptions;

/**
 * Created by Hakim on 19/04/14.
 */
public class InitializingPatternOutOfBoundsException extends Exception {
    public InitializingPatternOutOfBoundsException () {
        super("Error initializing the grid with the given pattern");
    }
}
