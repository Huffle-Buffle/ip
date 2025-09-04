package mimi;

/**
 * Represents an event task that occurs at a specific time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Returns a string suitable for display.
     *
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
