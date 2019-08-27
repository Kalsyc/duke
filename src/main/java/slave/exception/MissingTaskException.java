package slave.exception;

/**
 * Missing Task exception, for when task is missing from input
 */
public class MissingTaskException extends DukeException {

    /**
     * Constructor for MissingTaskException
     */
    public MissingTaskException() {
        super("Task is missing! Please specify the task!");
    }
}
