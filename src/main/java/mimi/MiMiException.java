package mimi;

/**
 * Simple unchecked/checked exception used for user input errors.
 */
public class MiMiException extends Exception {
    public MiMiException(String message) {
        super(message);
    }
}
