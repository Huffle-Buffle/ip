import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class MiMi {
    public static final String LINE = "____________________________________________________________";

    public static void sayhi() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm MiMi");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    public static void byebye() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();

        sayhi();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String user_input = input.trim();

            if (user_input.equals("bye")) {
                byebye();
                break;

            } else if (user_input.equals("list")) {
                System.out.println(LINE);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
                System.out.println(LINE);

            } else if (user_input.startsWith("mark ")) {
                int idx = parseIndex(user_input.substring(5));
                if (isValidIndex(idx, tasks.size())) {
                    Task t = tasks.get(idx - 1);
                    t.mark();
                    System.out.println(LINE);
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("  " + t);
                    System.out.println(LINE);
                } else {
                    printIndexError(tasks.size());
                }

            } else if (user_input.startsWith("unmark ")) {
                int idx = parseIndex(user_input.substring(7));
                if (isValidIndex(idx, tasks.size())) {
                    Task t = tasks.get(idx - 1);
                    t.unmark();
                    System.out.println(LINE);
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("  " + t);
                    System.out.println(LINE);
                } else {
                    printIndexError(tasks.size());
                }

            } else {
                String s = input.length() >= 5 ? input.substring(5).trim() : "";
                if (user_input.startsWith("todo")) {
                    if (s.isEmpty()) {
                        printSimpleError(" The description of a todo cannot be empty.");
                    } else {
                        Task t = new Todo(s);
                        tasks.add(t);
                        printAdded(t, tasks.size());
                    }
    
                } else if (user_input.startsWith("deadline")) {
                    String rest = input.length() >= 9 ? input.substring(9).trim() : "";
                    int p = rest.indexOf("/by");
                    if (p == -1) {
                        printSimpleError(" Please provide '/by <deadline>' (e.g., deadline return book /by Sunday).");
                    } else {
                        String desc = rest.substring(0, p).trim();
                        String by = rest.substring(p + 3).trim();
                        if (desc.isEmpty() || by.isEmpty()) {
                            printSimpleError(" Please provide both description and '/by ...'.");
                        } else {
                            Task t = new Deadline(desc, by);
                            tasks.add(t);
                            printAdded(t, tasks.size());
                        }
                    }
    
                } else if (user_input.startsWith("event")) {
                    int pf = s.indexOf("/from");
                    int pt = (pf == -1) ? -1 : s.indexOf("/to", pf + 5);
    
                    if (pf == -1 || pt == -1) {
                        printSimpleError(" Please provide '/from ... /to ...' (e.g., event meeting /from Mon 2pm /to 4pm).");
                    } else {
                        String desc = s.substring(0, pf).trim();
                        String from = s.substring(pf + 5, pt).trim();
                        String to = s.substring(pt + 3).trim();
                        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                            printSimpleError(" Please provide description, '/from ...', and '/to ...'.");
                        } else {
                            Task t = new Event(desc, from, to);
                            tasks.add(t);
                            printAdded(t, tasks.size());
                        }
                    }
    
                } else { // if user doesn't specify the type of activity, by default we set it as todo
                    Task t = new Todo(input);
                    tasks.add(t);
                    printAdded(t, tasks.size());
                }
            }
        }
        sc.close();
    }

    // These are helpers for MiMi
    private static void printAdded(Task t, int total) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println(" Now you have " + total + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printSimpleError(String msg) {
        System.out.println(LINE);
        System.out.println(msg);
        System.out.println(LINE);
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
