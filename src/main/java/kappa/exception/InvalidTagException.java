package kappa.exception;

/**
 * Represents an invalid tag exception.
 */
public class InvalidTagException extends KappaException {

    /**
     * Constructor for invalid tag.
     */
    public InvalidTagException() {
        super("Tag is invalid!");
    }
}
