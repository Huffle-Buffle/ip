import java.util.Scanner;

public class UiMasterList {
    private final Scanner sc = new Scanner(System.in);

    public void sayhi() { System.out.println("Hello! I'm MiMi \n What can I do for you?"); }

    public String readCommand() { return sc.hasNextLine() ? sc.nextLine() : ""; }

    public void showLine(String s) { System.out.println(s); }

    public void showError(String msg) { System.out.println(msg); }

    public void byebye() {System.out.println("Bye. Hope to see you again soon!");}

    public void showLoadingError() {
        System.out.println("Could not load your tasks, starting with an empty list.");
    }
}
