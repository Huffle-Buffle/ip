package mimi;

import java.util.ArrayList;

/**
 * Represents a list of {@link Task} objects.
 * Provides basic operations for adding, removing, marking, and searching tasks.
 * Some programmer assumptions are documented with Java {@code assert} statements.
 * Enable assertions during development with {@code -ea}.
 */
public record TaskList(ArrayList<Task> tasks) {

    /** Creates a new empty task list. */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "TaskList must and cannot be null!";
        this.tasks = new ArrayList<>(tasks);
        for (Task taskL : this.tasks) {
            assert taskL != null : "TaskList cannot contain null tasks";
        }
    }

    /** Adds a task to the end of the list. */
    public void add(Task t) {
        assert t != null : "Cannot add null Task";
        tasks.add(t);
    }

    /**
     * Removes and returns the task at a 0-based index.
     * @param index0 0-based index
     * @return removed task
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task remove(int index0) {
        Task removed = tasks.remove(index0);
        assert removed != null : "Removed task cannot be null";
        return removed;
    }

    /**
     * Returns the task at a 0-based index.
     * @param index0 0-based index
     * @return task at index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task get(int index0) {
        Task t = tasks.get(index0);
        assert t != null : "Task at index cannot be null";
        return t;
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
        assert index0 >= 0 && index0 < tasks.size() : "Index out of bounds: " + index0 + ". Choose a valid index!";
        Task t = tasks.get(index0);
        assert t != null : "Task cannot be null";
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
        assert index0 >= 0 && index0 < tasks.size() : "Index out of bounds: " + index0 + ". Choose a valid index!";
        Task t = tasks.get(index0);
        assert t != null : "Task cannot be null";
        t.unmark();
        return t;
    }

    /**
     * Finds all tasks whose descriptions contain the given keyword, ignoring case.
     *
     * @param keyword The word or phrase to look for (ignored if null/blank).
     * @return A list of matching tasks.
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> result = new ArrayList<>();
        String k = (keyword == null) ? "" : keyword.trim().toLowerCase();
        if (k.isEmpty()) {
            return result; // empty search -> no matches
        }
        for (Task t : tasks) {
            assert t != null : "Task cannot be null";
            String d = t.getDescription();
            assert d != null : "Task description cannot be null";
            if (d.toLowerCase().contains(k)) {
                result.add(t);
            }
        }
        return result;
    }
}
