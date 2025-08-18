import java.util.Scanner;

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
        sayhi();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.trim().equals("bye")) {
                byebye();
                break;
            }
            System.out.println(LINE);
            System.out.println("       " + input);
            System.out.println(LINE);
        }
        sc.close();
    }
}
