import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() { this.tasks = new ArrayList<>(); }

    public TaskList(ArrayList<Task> initial) { this.tasks = new ArrayList<>(initial); }

    public void add(Task t) { tasks.add(t); }

    public Task remove(int index0) { return tasks.remove(index0); }

    public Task get(int index0) { return tasks.get(index0); }

    public int size() { return tasks.size(); }

    public ArrayList<Task> asArrayList() { return tasks; }

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

    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> result = new ArrayList<>();
        String k = keyword == null ? "" : keyword.toLowerCase();
        for (Task t : tasks) {
            String d = t.getDescription();
            if (d != null && d.toLowerCase().contains(k)) result.add(t);
        }
        return result;
    }
}
