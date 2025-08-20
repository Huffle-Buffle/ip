import java.util.Scanner;
import java.util.ArrayList;

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
        ArrayList<Task> tasks = new ArrayList<>();

        sayhi();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String user_input = input.trim();

            try {
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
                    int index = parseIndex(user_input.substring(5));
                    if (isValidIndex(index, tasks.size())) {
                        Task t = tasks.get(index - 1);
                        t.mark();
                        System.out.println(LINE);
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("  " + t);
                        System.out.println(LINE);
                    } else {
                        printIndexError(tasks.size());
                    }

                } else if (user_input.startsWith("unmark ")) {
                    int index = parseIndex(user_input.substring(7));
                    if (isValidIndex(index, tasks.size())) {
                        Task t = tasks.get(index - 1);
                        t.unmark();
                        System.out.println(LINE);
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("  " + t);
                        System.out.println(LINE);
                    } else {
                        printIndexError(tasks.size());
                    }

                } else if (user_input.startsWith("delete ")) { // deleteing stuff
                    int index = parseIndex(user_input.substring(7));
                    if (isValidIndex(index, tasks.size())) {
                        Task deleted = tasks.remove(index - 1);
                        System.out.println(LINE);
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("  " + deleted);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println(LINE);
                    } else {
                        printIndexError(tasks.size());
                    }

                } else if (user_input.startsWith("todo")) { // handling todo stuff
                    String thingtodo = user_input.substring(4).trim();
                    if (thingtodo.isEmpty()) {
                        throw new MiMiException("How can there be nothing to do, there is always something to do!");
                    } else {
                        Task t = new Todo(thingtodo);
                        tasks.add(t);
                        printAdded(t, tasks.size());
                    }

                } else if (user_input.startsWith("deadline")) { // handling deadline stuff
                    String deadline = input.length() >= 9 ? input.substring(9).trim() : "";
                    int afterby = deadline.indexOf("/by");
                    if (deadline.isEmpty()) {
                        throw new MiMiException("Chop chop what's the deadline? Please provide '/by <deadline>' (e.g., deadline return book /by Sunday).");
                    } else {
                        String desc = deadline.substring(0, afterby).trim();
                        String by = deadline.substring(afterby + 3).trim();
                        if (desc.isEmpty() || by.isEmpty()) {
                            printSimpleError();
                        } else {
                            Task t = new Deadline(desc, by);
                            tasks.add(t);
                            printAdded(t, tasks.size());
                        }
                    }

                } else if (user_input.startsWith("event")) { // handing my events stuff
                    String event = user_input.length() > 5 ? user_input.substring(5).trim() : "";
                    if (event.isEmpty()) {
                        throw new MiMiException("What is the event??? Please provide description, '/from ...', and '/to ...'. If its a good event we should celebrate!");
                    }
                    int pf = event.indexOf("/from");
                    int pt = (pf == -1) ? -1 : event.indexOf("/to", pf + 5);

                    if (pf == -1 || pt == -1) {
                        throw new MiMiException("Please provide '/from ... /to ...' (e.g., event meeting /from Mon 2pm /to 4pm).");
                    }

                    String desc = event.substring(0, pf).trim();
                    String from = event.substring(pf + 5, pt).trim();
                    String to = event.substring(pt + 3).trim();

                    if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        throw new MiMiException("Please provide description, '/from ...', and '/to ...'.");
                    }

                    Task t = new Event(desc, from, to);
                    tasks.add(t);
                    printAdded(t, tasks.size());
                } else { // Catching empty or random inouts
                    throw new MiMiException("Alamak, what is this ?");
                }
            } catch (MiMiException e) {
                System.out.println(LINE);
                System.out.println(e.getMessage());
                System.out.println(LINE);
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

    private static void printSimpleError() {
        System.out.println(LINE);
        System.out.println(" Please provide both description and '/by ...'.");
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