package mimi;

/**
 * A task that must be done within a certain period.
 * This is the extension
 */
public class DoWithinPeriodTasks extends Task {
    private final String from;
    private final String to;

    /**
     * Creates a new within-period task.
     * @param description short text describing the task
     * @param from starting date (as text)
     * @param to ending date (as text)
     */
    public DoWithinPeriodTasks(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[W]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
