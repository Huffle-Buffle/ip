import java.util.ArrayList;

public class MiMi {

    private final UiMasterList ui;
    private final Storage storage;
    private final TaskList tasks;

    public MiMi() {
        this.ui = new UiMasterList();
        this.storage = new Storage("data/MiMi.txt");
        ArrayList<Task> loaded = storage.load();
        this.tasks = new TaskList(loaded);
    }

    public void run() {
        ui.sayhi();

        while (true) {
            String input = ui.readCommand();
            if (input == null) input = "";
            input = input.trim();
            if (input.isEmpty()) continue;

            String cmd = Parser.commandWord(input);
            String rest = Parser.afterWord(input);

            try {
                switch (cmd) {
                    case "bye" -> {
                        ui.byebye();
                        return;
                    }
                    case "list" -> {
                        ui.printLine();
                        ui.showList(tasks);
                        ui.printLine();
                    }
                    case "todo" -> {
                        Todo t = new Todo(Parser.parseTodo(rest));
                        tasks.add(t);
                        storage.save(tasks.asArrayList());
                        ui.showAdded(t);

                    }
                    case "deadline" -> {
                        String[] a = Parser.parseDeadline(rest);    // [desc, when]

                        Deadline d = new Deadline(a[0], a[1]);      // Level-8 pretty-print if yyyy-MM-dd

                        tasks.add(d);
                        storage.save(tasks.asArrayList());
                        ui.showAdded(d);

                    }
                    case "event" -> {
                        String[] a = Parser.parseEvent(rest);       // [desc, from, to]

                        Event ev = new Event(a[0], a[1], a[2]);
                        tasks.add(ev);
                        storage.save(tasks.asArrayList());
                        ui.showAdded(ev);

                    }
                    case "mark" -> {
                        int idx = Parser.parseIndex(rest);          // 1-based -> 0-based

                        Task t = tasks.mark(idx);
                        storage.save(tasks.asArrayList());
                        ui.showMarked(t);

                    }
                    case "unmark" -> {
                        int idx = Parser.parseIndex(rest);
                        Task t = tasks.unmark(idx);
                        storage.save(tasks.asArrayList());
                        ui.showUnmarked(t);

                    }
                    case "delete" -> {
                        int idx = Parser.parseIndex(rest);
                        Task removed = tasks.remove(idx);
                        storage.save(tasks.asArrayList());
                        ui.showRemoved(removed);

                    }
                    default -> ui.showError("I don't understand that command.");
                }
            } catch (MiMiException e) {
                ui.showError(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                ui.showError("Please provide a valid task number.");
            } catch (Exception e) {
                ui.showError("Something went wrong: " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
            new MiMi().run();
    }
}