package mimi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected String by;
    private LocalDate date;

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        try {
            this.date = (by == null) ? null : LocalDate.parse(by.trim());
        } catch (Exception ignore) {
            this.date = null; // not parseable -> fall back to original string
        }
    }

    @Override
    public String toString() {
        String nice = (date != null) ? date.format(OUT) : by;
        return "[D]" + super.toString() + " (by: " + nice + ")";
    }
}
