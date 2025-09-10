package mimi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that has a due date/time. Pretty-prints ISO dates for Level-8.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter IN = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String by;
    private final LocalDate date;

    /**
     * Creates a deadline task.
     * @param description short text
     * @param by user input for date/time (e.g., "2019-10-15")
     */
    public Deadline(String description, String by) {
        super(description);
        String cleaned = (by == null) ? "" : by.trim();
        this.by = cleaned;

        LocalDate parsed = null;
        if (!cleaned.isEmpty()) {
            try {
                parsed = LocalDate.parse(cleaned, IN);
            } catch (Exception ignored) {
                // not parseable -> fall back to original string
            }
        }
        this.date = parsed;
    }

    /** Raw 'by' string as entered by the user (possibly empty). */
    public String getBy() {
        return by;
    }

    /** @return string form like {@code [D][ ] desc (by: Oct 15 2019)} */
    @Override
    public String toString() {
        String nice = (date != null) ? date.format(OUT) : by;
        return "[D]" + super.toString() + " (by: " + nice + ")";
    }
}
