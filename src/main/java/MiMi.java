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
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        List<String> tasks = new ArrayList<>();

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
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("      " + (i + 1) + ". " + tasks.get(i));
                }
                System.out.println(LINE);
            } else {
                tasks.add(input);
                System.out.println(LINE);
                System.out.println("        added: " + input);
                System.out.println(LINE);
            }
        }
        sc.close();
    }
}
