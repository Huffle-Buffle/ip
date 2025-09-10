package mimi;

import java.util.ArrayList;

/**
 * Entry point of the MiMi chatbot.
 * Wires UI, storage and task list, then runs a simple REPL loop.
 */
public class MiMi {
    // Due to this week's topic on code quality, I had to change the whole
    // structure cause if not we have a lot of magic strings and numbers haizzz
    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";
    private static final String CMD_MARK = "mark";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_FIND = "find";
    private static final String MSG_NEED_INDEX = "Please provide a valid task number.";
    private static final String MSG_UNKNOWN_CMD = "Alamak what is this?";
    private static final String MSG_FIND_USAGE = "Please provide a keyword: find <word>";
    private static final String DEFAULT_SAVE_PATH = "data/MiMi.txt";

    private final UiMasterList ui;
    private final Storage storage;
    private final TaskList tasks;

    /** Creates MiMi and loads tasks from disk (folder/file are created if missing). */
    public MiMi() {
        this.ui = new UiMasterList();
        this.storage = new Storage(DEFAULT_SAVE_PATH);
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
                case CMD_BYE -> {
                    ui.byebye();
                    return;
                }
                case CMD_LIST -> ui.showList(tasks);

                case CMD_TODO -> {
                    Todo t = new Todo(Parser.parseTodo(keyword));
                    add(t);
                    ui.showAdded(t);
                }

                case CMD_DEADLINE -> {
                    String[] a = Parser.parseDeadline(keyword);
                    Deadline d = new Deadline(a[0], a[1]);
                    add(d);
                    ui.showAdded(d);
                }

                case CMD_EVENT -> {
                    String[] a = Parser.parseEvent(keyword);
                    Event ev = new Event(a[0], a[1], a[2]);
                    add(ev);
                    ui.showAdded(ev);
                }

                case CMD_MARK -> {
                    int idx = Parser.parseIndex(keyword);
                    Task t = tasks.mark(idx);
                    save();
                    ui.showMarked(t);
                }

                case CMD_UNMARK -> {
                    int idx = Parser.parseIndex(keyword);
                    Task t = tasks.unmark(idx);
                    save();
                    ui.showUnmarked(t);
                }

                case CMD_DELETE -> {
                    int idx = Parser.parseIndex(keyword);
                    Task removed = tasks.remove(idx);
                    save();
                    ui.showRemoved(removed);
                }

                case CMD_FIND -> {
                    if (keyword.isEmpty()) {
                        ui.showError(MSG_FIND_USAGE);
                    } else {
                        var matches = tasks.find(keyword);
                        ui.showFind(matches);
                    }
                }

                default -> ui.showError(MSG_UNKNOWN_CMD);
                }
            } catch (MiMiException e) {
                ui.showError(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                ui.showError(MSG_NEED_INDEX);
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
