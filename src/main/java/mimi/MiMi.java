package mimi;

import java.util.ArrayList;

/**
 * Entry point of the MiMi chatbot.
 * Wires UI, storage and task list, then runs a simple REPL loop.
 */
public class MiMi {
    // Due to this week's topic on code quality, I had to change the whole
    // structure cause if not we have a lot of magic strings and numbers haizzz
    // ================================
    // Command keywords and constants
    // ================================
    /** Command keyword to exit the chatbot. */
    private static final String bye = "bye";
    /** Command keyword to list all tasks. */
    private static final String list = "list";
    /** Command keyword to add a todo task. */
    private static final String todo = "todo";
    /** Command keyword to add a deadline task. */
    private static final String deadline = "deadline";
    /** Command keyword to add an event task. */
    private static final String event = "event";
    /** Command keyword to mark a task as done. */
    private static final String mark = "mark";
    /** Command keyword to unmark a task. */
    private static final String unmark = "unmark";
    /** Command keyword to delete a task. */
    private static final String delete = "delete";
    /** Command keyword to find matching tasks. */
    private static final String find = "find";
    /** Command keyword to add a do-within-period task. */
    private static final String do_within_period = "within";

    /** Error message when an invalid task index is given. */
    private static final String index_error = "Please provide a valid task number.";
    /** Error message when an unknown command is entered. */
    private static final String unknown = "Alamak what is this?";
    /** Error message when no keyword is provided for find. */
    private static final String find_error = "Please provide a keyword: find <word>";
    /** Path to the data file for task persistence. */
    private static final String save_path = "data/MiMi.txt";

    // ================================
    // Core components
    // ================================
    private final UiMasterList ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a MiMi chatbot instance and loads saved tasks from disk.
     * If no data file exists, one will be created automatically.
     */
    public MiMi() {
        this.ui = new UiMasterList();
        this.storage = new Storage(save_path);
        ArrayList<Task> loaded = storage.load();
        this.tasks = new TaskList(loaded);
    }

    /**
     * Runs the chatbot's main REPL (read–eval–print loop) until the user types "bye".
     * This method repeatedly reads input, parses it, executes the corresponding command,
     * and displays the result to the user.
     */
    public void run() {
        ui.sayhi();
        while (true) {
            String input = nullSafety(ui.readCommand());
            if (input.isEmpty()) {
                continue;
            }

            String cmd = Parser.commandWord(input);
            String args = Parser.afterWord(input);

            try {
                if (commandBook(cmd, args)) {
                    return;
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

    /**
     * Handles a user command by dispatching it to the appropriate handler method.
     *
     * @param cmd  The command keyword entered by the user.
     * @param args The arguments or details following the command keyword.
     * @return true if the command was "bye" and the chatbot should exit; false otherwise.
     * @throws MiMiException if a command fails to execute properly.
     */
    private boolean commandBook(String cmd, String args) throws MiMiException {
        switch (cmd) {
        case bye -> {
            ui.byebye();
            return true;
        }
        case list -> showList();
        case todo -> addTodo(args);
        case deadline -> addDeadline(args);
        case event -> addEvent(args);
        case mark -> markTask(args);
        case unmark -> unmarkTask(args);
        case delete -> deleteTask(args);
        case find -> findTasks(args);
        case do_within_period -> addWithinPeriod(args);
        default -> ui.showError(unknown);
        }
        return false;
    }

    /**
     * Returns an empty string if the input is null, otherwise trims it.
     *
     * @param s The input string from the user.
     * @return A non-null, trimmed string.
     */
    private String nullSafety(String s) {
        if (s == null) {
            return "";
        }
        return s.trim();
    }

    /** Shows the list of all tasks to the user. */
    private void showList() {
        ui.showList(tasks);
    }

    /**
     * Adds a new todo task.
     *
     * @param args The description of the todo task.
     * @throws MiMiException if parsing fails.
     */
    private void addTodo(String args) throws MiMiException {
        Todo t = new Todo(Parser.parseTodo(args));
        add(t);
        ui.showAdded(t);
    }

    /**
     * Adds a new deadline task.
     *
     * @param args The description and deadline details.
     * @throws MiMiException if parsing fails.
     */
    private void addDeadline(String args) throws MiMiException {
        String[] a = Parser.parseDeadline(args);
        Deadline d = new Deadline(a[0], a[1]);
        add(d);
        ui.showAdded(d);
    }

    /**
     * Adds a new event task.
     *
     * @param args The description, start, and end details of the event.
     * @throws MiMiException if parsing fails.
     */
    private void addEvent(String args) throws MiMiException {
        String[] a = Parser.parseEvent(args);
        Event ev = new Event(a[0], a[1], a[2]);
        add(ev);
        ui.showAdded(ev);
    }

    /**
     * Marks a task as completed.
     *
     * @param args The index of the task to mark.
     * @throws MiMiException if parsing fails.
     */
    private void markTask(String args) throws MiMiException {
        int idx = Parser.parseIndex(args);
        Task t = tasks.mark(idx);
        save();
        ui.showMarked(t);
    }

    /**
     * Unmarks a task (marks as not done).
     *
     * @param args The index of the task to unmark.
     * @throws MiMiException if parsing fails.
     */
    private void unmarkTask(String args) throws MiMiException {
        int idx = Parser.parseIndex(args);
        Task t = tasks.unmark(idx);
        save();
        ui.showUnmarked(t);
    }

    /**
     * Deletes a task.
     *
     * @param args The index of the task to delete.
     * @throws MiMiException if parsing fails.
     */
    private void deleteTask(String args) throws MiMiException {
        int idx = Parser.parseIndex(args);
        Task removed = tasks.remove(idx);
        save();
        ui.showRemoved(removed);
    }

    /**
     * Finds tasks matching a given keyword.
     *
     * @param args The keyword to search for.
     */
    private void findTasks(String args) {
        if (args.isEmpty()) {
            ui.showError(find_error);
            return;
        }
        var matches = tasks.find(args);
        ui.showFind(matches);
    }

    /**
     * Adds a do-within-period task.
     *
     * @param args The description, start, and end period details.
     * @throws MiMiException if parsing fails.
     */
    private void addWithinPeriod(String args) throws MiMiException {
        String[] a = Parser.parseWithin(args);
        DoWithinPeriodTasks w = new DoWithinPeriodTasks(a[0], a[1], a[2]);
        tasks.add(w);
        storage.save(tasks.asArrayList());
        ui.showAdded(w);
    }

    /**
     * Generates a response for the user's chat message (used by the GUI).
     *
     * @param input The user's input message.
     * @return A response string from MiMi.
     */
    public String getResponse(String input) {
        return "MiMi heard: " + input;
    }

    /**
     * Entry point for the program.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new MiMi().run();
    }

    /**
     * Adds a task to the list and saves the updated task list to disk.
     *
     * @param t The task to add.
     */
    private void add(Task t) {
        tasks.add(t);
        save();
    }

    /** Saves the current task list to disk. */
    private void save() {
        storage.save(tasks.asArrayList());
    }
}
