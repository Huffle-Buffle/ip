package mimi;

import java.util.ArrayList;

/**
 * Entry point of the MiMi chatbot.
 * Wires UI, storage and task list, then runs a simple REPL loop.
 */
public class MiMi {

    private final UiMasterList ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates MiMi and loads tasks from disk (folder/file are created if missing).
     */
    public MiMi() {
        this.ui = new UiMasterList();
        this.storage = new Storage("data/MiMi.txt");
        ArrayList<Task> loaded = storage.load();
        this.tasks = new TaskList(loaded);
    }

    /**
     * Runs the read–eval–print loop until the user types "bye".
     */
    public void run() {
        ui.sayhi();

        while (true) {
            String input = ui.readCommand();
            if (input == null) input = "";
            input = input.trim();
            if (input.isEmpty()) continue;

            String cmd = Parser.commandWord(input);
            String keyword = Parser.afterWord(input);

            try {
                switch (cmd) {
                    case "bye" -> {
                        ui.byebye();
                        return;
                    }
                    case "list" -> ui.showList(tasks);
                    case "todo" -> {
                        Todo t = new Todo(Parser.parseTodo(keyword));
                        tasks.add(t);
                        storage.save(tasks.asArrayList());
                        ui.showAdded(t);

                    }
                    case "deadline" -> {
                        String[] a = Parser.parseDeadline(keyword);

                        Deadline d = new Deadline(a[0], a[1]);

                        tasks.add(d);
                        storage.save(tasks.asArrayList());
                        ui.showAdded(d);

                    }
                    case "event" -> {
                        String[] a = Parser.parseEvent(keyword);

                        Event ev = new Event(a[0], a[1], a[2]);
                        tasks.add(ev);
                        storage.save(tasks.asArrayList());
                        ui.showAdded(ev);

                    }
                    case "mark" -> {
                        int idx = Parser.parseIndex(keyword);

                        Task t = tasks.mark(idx);
                        storage.save(tasks.asArrayList());
                        ui.showMarked(t);

                    }
                    case "unmark" -> {
                        int idx = Parser.parseIndex(keyword);
                        Task t = tasks.unmark(idx);
                        storage.save(tasks.asArrayList());
                        ui.showUnmarked(t);

                    }
                    case "delete" -> {
                        int idx = Parser.parseIndex(keyword);
                        Task removed = tasks.remove(idx);
                        storage.save(tasks.asArrayList());
                        ui.showRemoved(removed);

                    }
                case "find" -> {
                    if (keyword.isEmpty()) {
                        ui.showError("Please provide a keyword: find <word>");
                    } else {
                        var matches = tasks.find(keyword);
                        ui.showFind(matches);
                    }
                }
                    default -> ui.showError("Alamak what is this?");
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

    /**
     * Program entry point.
     * @param args unused CLI args
     */
    public static void main(String[] args) {
            new MiMi().run();
    }
}