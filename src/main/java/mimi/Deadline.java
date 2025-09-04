package mimi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that has a due date/time. Pretty-prints ISO dates for Level-8.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");

    protected String by;
    private LocalDate date;

    /**
     * Creates a deadline task.
     * @param description short text
     * @param by user input for date/time (e.g., "2019-10-15")
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        try {
            this.date = (by == null) ? null : LocalDate.parse(by.trim());
        } catch (Exception ignore) {
            this.date = null; // not parseable -> fall back to original string
        }
    }

    /** @return string form like {@code [D][ ] desc (by: Oct 15 2019)} */
    @Override
    public String toString() {
        String nice = (date != null) ? date.format(OUT) : by;
        return "[D]" + super.toString() + " (by: " + nice + ")";
    }
}
