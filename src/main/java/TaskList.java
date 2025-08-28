import java.util.ArrayList;

public record TaskList(ArrayList<Task> tasks) {
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task remove(int index0) {
        return tasks.remove(index0);
    }

    public Task get(int index0) {
        return tasks.get(index0);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> asArrayList() {
        return tasks;
    }

    public Task mark(int index0) {
        Task t = tasks.get(index0);
        t.mark();
        return t;
    }

    public Task unmark(int index0) {
        Task t = tasks.get(index0);
        t.unmark();
        return t;
    }
}
