package mimi;

/**
 * Base class for all tasks stored by MiMi.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description non-null task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as done. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns "X" if done, or a single space if not done.
     * @return status icon used in text UI
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** @return the (non-null) description of this task */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
