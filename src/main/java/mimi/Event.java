package mimi;

/**
 * Represents an event task that occurs at a specific time.
 */
public class Event extends Task {
    private final String from; // never null; may be ""
    private final String to;

    /**
     * Returns a string suitable for display.
     * @param from start time/date
     * @param to end time/date
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = (from == null) ? "" : from.trim();
        this.to = (to == null) ? "" : to.trim();
    }

    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
