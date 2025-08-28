import java.util.Scanner;

public class UiMasterList {

    private final Scanner sc = new Scanner(System.in);

    public void sayhi() { System.out.println("Hello! I'm MiMi \n What can I do for you?"); }

    public String readCommand() { return sc.hasNextLine() ? sc.nextLine() : ""; }

    public void showError(String msg) { System.out.println(msg); }

    public void byebye() {System.out.println("Bye. Hope to see you again soon!");}

    public void showList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showAdded(Task t) {
        System.out.println("added: " + t);
    }

    public void showRemoved(Task t) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
    }

    public void showMarked(Task t) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
    }

    public void showUnmarked(Task t) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
    }
}
