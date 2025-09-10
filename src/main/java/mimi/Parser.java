package mimi;

/**
 * Stateless helpers that parse user input into command parts.
 * Throws {@link MiMiException} for malformed input.
 */
public class Parser {
    // Due to this week's topic on code quality, I had to change the whole
    // structure cause if not we have a lot of magic strings and numbers again haizz
    // so troublesome...
    private static final String BY = "/by";
    private static final String FROM = "/from";
    private static final String TO = "/to";
    private static final int BY_LEN = BY.length();
    private static final int FROM_LEN = FROM.length();
    private static final int TO_LEN = TO.length();

    /** Returns the first word (command) from the input. */
    public static String commandWord(String input) {
        String s = (input == null) ? "" : input.trim();
        int sp = s.indexOf(' ');
        return (sp == -1) ? s : s.substring(0, sp);
    }

    /** Returns everything after the first word (trimmed, may be empty). */
    public static String afterWord(String input) {
        String s = (input == null) ? "" : input.trim();
        int sp = s.indexOf(' ');
        return (sp == -1) ? "" : s.substring(sp + 1).trim();
    }

    /**
     * Parses a 1-based index and returns a 0-based index.
     * @throws MiMiException if not a positive integer
     */
    public static int parseIndex(String arg) throws MiMiException {
        try {
            int i = Integer.parseInt(arg.trim());
            if (i <= 0) {
                throw new NumberFormatException();
            }
            return i - 1;
        } catch (Exception e) {
            throw new MiMiException("Please give a valid positive number.");
        }
    }

    /** Parses a todo command and returns a non-empty description. */
    public static String parseTodo(String rest) throws MiMiException {
        String desc = (rest == null) ? "" : rest.trim();
        if (desc.isEmpty()) {
            throw new MiMiException("How can there be nothing to do, there is always something to do!");
        }
        return desc;
    }

    /**
     * Parses a deadline command of the form: {@code deadline <desc> /by <when>}.
     * @return {desc, when}
     */
    public static String[] parseDeadline(String rest) throws MiMiException {
        int pos = rest.indexOf(BY);
        if (pos == -1) {
            throw new MiMiException("Use /by for deadlines (e.g., deadline return book /by 2019-10-15)");
        }
        String desc = rest.substring(0, pos).trim();
        String when = rest.substring(pos + BY_LEN).trim();
        if (desc.isEmpty() || when.isEmpty()) {
            throw new MiMiException(
                    "Please provide both a description and a deadline, e.g., 'deadline return book /by 2019-10-15'");
        }
        return new String[] { desc, when };
    }

    /**
     * Parses an event command of the form: {@code event <desc> /from <a> /to <b>}.
     * @return {desc, from, to}
     */
    public static String[] parseEvent(String rest) throws MiMiException {
        String desc = rest;
        String from = "";
        String to = "";
        int f = rest.indexOf(FROM);
        if (f != -1) {
            desc = rest.substring(0, f).trim();
            String afterFrom = rest.substring(f + FROM_LEN).trim();
            int t = afterFrom.indexOf(TO);
            if (t == -1) {
                from = afterFrom.trim();
            } else {
                from = afterFrom.substring(0, t).trim();
                to = afterFrom.substring(t + TO_LEN).trim();
            }
        }
        if (desc.isEmpty()) {
            throw new MiMiException(
                    "Please provide an event description and optionally '/from ...' and '/to ...'.");
        }
        return new String[] { desc, from, to };
    }
}
