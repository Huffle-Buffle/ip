import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class MiMi {
    private static final String LINE = "____________________________________________________________";

    private static void sayhi() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm MiMi");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    private static void byebye() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        List<TaskDone> tasks = new ArrayList<>();

        sayhi();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String cmd = input.trim();

            if (cmd.equals("bye")) {
                byebye();
                break;

            } else if (cmd.equals("list")) {
                System.out.println(LINE);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    TaskDone t = tasks.get(i);
                    System.out.println(" " + (i + 1) + ".[" + t.getStatusIcon() + "] " + t.getDescription());
                }
                System.out.println(LINE);

            } else if (cmd.startsWith("mark ")) {
                int idx = parseIndex(cmd.substring(5));
                if (isValidIndex(idx, tasks.size())) {
                    TaskDone t = tasks.get(idx - 1);
                    t.mark();
                    System.out.println(LINE);
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("  " + t);
                    System.out.println(LINE);
                } else {
                    printIndexError(tasks.size());
                }

            } else if (cmd.startsWith("unmark ")) {
                int idx = parseIndex(cmd.substring(7));
                if (isValidIndex(idx, tasks.size())) {
                    TaskDone t = tasks.get(idx - 1);
                    t.unmark();
                    System.out.println(LINE);
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("  " + t);
                    System.out.println(LINE);
                } else {
                    printIndexError(tasks.size());
                }

            } else {
                tasks.add(new TaskDone(input));
                System.out.println(LINE);
                System.out.println(" added: " + input);
                System.out.println(LINE);
            }
        }
        sc.close();
    }

    private static int parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean isValidIndex(int idx, int size) {
        return idx >= 1 && idx <= size;
    }

    private static void printIndexError(int size) {
        System.out.println(LINE);
        if (size == 0) {
            System.out.println(" Your list is empty.");
        } else {
            System.out.println(" Please provide a valid task number between 1 and " + size + ".");
        }
        System.out.println(LINE);
    }
}
