package mimi;

import java.util.ArrayList;

/**
 * Entry point of the MiMi chatbot.
 * Wires UI, storage and task list, then runs a simple REPL loop.
 */
public class MiMi {
    // Due to this week's topic on code quality, I had to change the whole
    // structure cause if not we have a lot of magic strings and numbers haizzz
    private static final String bye = "bye";
    private static final String list = "list";
    private static final String todo = "todo";
    private static final String deadline = "deadline";
    private static final String event = "event";
    private static final String mark = "mark";
    private static final String unmark = "unmark";
    private static final String delete = "delete";
    private static final String find = "find";
    private static final String do_within_period = "within";
    private static final String index_error = "Please provide a valid task number.";
    private static final String unknown = "Alamak what is this?";
    private static final String find_error = "Please provide a keyword: find <word>";
    private static final String save_path = "data/MiMi.txt";

    private final UiMasterList ui;
    private final Storage storage;
    private final TaskList tasks;

    /** Creates MiMi and loads tasks from disk (folder/file are created if missing). */
    public MiMi() {
        this.ui = new UiMasterList();
        this.storage = new Storage(save_path);
        ArrayList<Task> loaded = storage.load();
        this.tasks = new TaskList(loaded);
    }

    /** Runs the read–eval–print loop until the user types "bye". */
    public void run() {
        ui.sayhi();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                input = "";
            }
            input = input.trim();
            if (input.isEmpty()) {
                continue;
            }

            String cmd = Parser.commandWord(input);
            String keyword = Parser.afterWord(input);

            try {
                switch (cmd) {
                case bye -> {
                    ui.byebye();
                    return;
                }
                case list -> ui.showList(tasks);

                case todo -> {
                    Todo t = new Todo(Parser.parseTodo(keyword));
                    add(t);
                    ui.showAdded(t);
                }

                case deadline -> {
                    String[] a = Parser.parseDeadline(keyword);
                    Deadline d = new Deadline(a[0], a[1]);
                    add(d);
                    ui.showAdded(d);
                }

                case event -> {
                    String[] a = Parser.parseEvent(keyword);
                    Event ev = new Event(a[0], a[1], a[2]);
                    add(ev);
                    ui.showAdded(ev);
                }

                case mark -> {
                    int idx = Parser.parseIndex(keyword);
                    Task t = tasks.mark(idx);
                    save();
                    ui.showMarked(t);
                }

                case unmark -> {
                    int idx = Parser.parseIndex(keyword);
                    Task t = tasks.unmark(idx);
                    save();
                    ui.showUnmarked(t);
                }

                case delete -> {
                    int idx = Parser.parseIndex(keyword);
                    Task removed = tasks.remove(idx);
                    save();
                    ui.showRemoved(removed);
                }

                case find -> {
                    if (keyword.isEmpty()) {
                        ui.showError(find_error);
                    } else {
                        var matches = tasks.find(keyword);
                        ui.showFind(matches);
                    }
                }
                case do_within_period -> {
                    String[] a = Parser.parseWithin(keyword);
                    DoWithinPeriodTasks w = new DoWithinPeriodTasks(a[0], a[1], a[2]);
                    tasks.add(w);
                    storage.save(tasks.asArrayList());
                    ui.showAdded(w);
                }
                default -> ui.showError(unknown);
                }
            } catch (MiMiException e) {
                ui.showError(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                ui.showError(index_error);
            } catch (Exception e) {
                ui.showError("Something went wrong: " + e.getMessage());
            }
        }
    }

    /** Generates a response for the user's chat message (used by GUI). */
    public String getResponse(String input) {
        return "MiMi heard: " + input;
    }

    /** Program entry point. */
    public static void main(String[] args) {
        new MiMi().run();
    }

    /** Making helps as per the code quality rules to reduce duplicates */
    private void add(Task t) {
        tasks.add(t);
        save();
    }

    private void save() {
        storage.save(tasks.asArrayList());
    }
}
