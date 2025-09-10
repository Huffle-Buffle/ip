package mimi;

import java.util.ArrayList;

/**
 * A thin wrapper around an {@code ArrayList<Task>}.
 * Provides basic operations used by MiMi.
 */
public record TaskList(ArrayList<Task> tasks) {

    /** Creates a new empty task list. */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes and returns the task at a 0-based index.
     * @param index0 0-based index
     * @return removed task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task remove(int index0) {
        return tasks.remove(index0);
    }

    /**
     * Returns the task at a 0-based index.
     * @param index0 0-based index
     * @return task at index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task get(int index0) {
        return tasks.get(index0);
    }

    /** @return number of tasks stored. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list (used for saving).
     * @return mutable backing list
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
    }


    /**
     * Marks a task as done and returns it.
     * @param index0 0-based index
     * @return the same task after marking
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task mark(int index0) {
        Task t = tasks.get(index0);
        t.mark();
        return t;
    }

    /**
     * Marks a task as not done and returns it.
     * @param index0 0-based index
     * @return the same task after unmarking
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task unmark(int index0) {
        Task t = tasks.get(index0);
        t.unmark();
        return t;
    }

    /**
     * Case-insensitive substring search over task descriptions.
     * @param keyword word/phrase to look for (ignored if null/blank)
     * @return list of matching tasks
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> result = new ArrayList<>();
        String k = (keyword == null) ? "" : keyword.trim().toLowerCase();
        if (k.isEmpty()) {
            return result; // empty search -> no matches
        }
        for (Task t : tasks) {
            String d = t.getDescription();
            if (d != null && d.toLowerCase().contains(k)) {
                result.add(t);
            }
        }
        return result;
    }
}
