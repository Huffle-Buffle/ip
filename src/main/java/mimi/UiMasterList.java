package mimi;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Very small UI helper around System.in/out used by MiMi.
 * Prints messages and reads one line of user input.
 */
public class UiMasterList {

    private final Scanner sc = new Scanner(System.in);

    /** Prints the greeting shown when MiMi starts. */
    public void sayhi() { System.out.println("Hello! I'm MiMi \n What can I do for you?"); }

    /**
     * Reads the next line from input.
     * @return the line read, or an empty string if none
     */
    public String readCommand() { return sc.hasNextLine() ? sc.nextLine() : ""; }

    /** Prints an error message to the console. */
    public void showError(String msg) { System.out.println(msg); }

    /** Prints the goodbye shown when MiMi ends. */
    public void byebye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints the current task list with 1-based indices.
     * @param tasks the tasks to display
     */
    public void showList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Prints a confirmation that a task was added. */
    public void showAdded(Task t) {
        System.out.println("added: " + t);
    }

    /** Prints a confirmation that a task was removed. */
    public void showRemoved(Task t) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
    }

    /** Prints a confirmation that a task was marked done. */
    public void showMarked(Task t) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
    }

    /** Prints a confirmation that a task was marked not done. */
    public void showUnmarked(Task t) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
    }

    /** Prints matching task to the find word. */
    public void showFind(ArrayList<Task> matches) {
        System.out.println("Here are the matching tasks in your list:");
        if (matches == null || matches.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            for (int i = 0; i < matches.size(); i++) {
                System.out.println((i + 1) + "." + matches.get(i));
            }
        }
    }
}
