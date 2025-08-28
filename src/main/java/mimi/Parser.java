package mimi;

/**
 * Stateless helpers that parse user input into command parts.
 * Throws {@link MiMiException} for malformed input.
 */
public class Parser {
    /**
     * Returns the first word (command) from the input.
     * @param input full user input line
     * @return command word (maybe empty)
     */
    public static String commandWord(String input) {
        String s = input == null ? "" : input.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? s : s.substring(0, sp);
    }

    /**
     * Returns everything after the first word.
     * @param input full user input line
     * @return remainder after the command (trimmed, may be empty)
     */
    public static String afterWord(String input) {
        String s = input == null ? "" : input.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? "" : s.substring(sp + 1).trim();
    }

    /**
     * Parses a 1-based index and returns a 0-based index.
     * @param arg user-provided index text
     * @return 0-based index
     * @throws MiMiException if not a positive integer
     */
    public static int parseIndex(String arg) throws MiMiException {
        try {
            int i = Integer.parseInt(arg.trim());
            if (i <= 0) throw new NumberFormatException();
            return i - 1;
        } catch (Exception e) {
            throw new MiMiException("Please give a valid positive number.");
        }
    }

    /**
     * Parses a todo command.
     * @param rest text after "todo"
     * @return non-empty description
     * @throws MiMiException if description is empty
     */
    public static String parseTodo(String rest) throws MiMiException {
        String desc = (rest == null) ? "" : rest.trim();
        if (desc.isEmpty()) {
            throw new MiMiException("How can there be nothing to do, there is always something to do!");
        }
        return desc;
    }

    /**
     * Parses a deadline command of the form:
     * {@code deadline <desc> /by <when>}.
     * @param rest text after "deadline"
     * @return {desc, when}
     * @throws MiMiException if /by or parts are missing
     */
    public static String[] parseDeadline(String rest) throws MiMiException {
        int pos = rest.indexOf("/by");
        if (pos == -1) throw new MiMiException("Use /by for deadlines (e.g., ... /by 2019-10-15)");
        String desc = rest.substring(0, pos).trim();
        String when = rest.substring(pos + 3).trim(); // after '/by'
        if (desc.isEmpty() || when.isEmpty())
            throw new MiMiException("Chop chop what's the deadline? Please provide '/by <deadline>' (e.g., deadline return book /by Sunday)");
        return new String[]{desc, when};
    }

    /**
     * Parses an event command of the form:
     * {@code event <desc> /from <a> /to <b>}.
     * @param rest text after "event"
     * @return {desc, from, to} (to may be empty)
     * @throws MiMiException if description is missing
     */
    public static String[] parseEvent(String rest) throws MiMiException {
        String desc = rest, from = "", to = "";
        int f = rest.indexOf("/from");
        if (f != -1) {
            desc = rest.substring(0, f).trim();
            String afterFrom = rest.substring(f + 5).trim();
            int t = afterFrom.indexOf("/to");
            if (t == -1) from = afterFrom.trim();
            else {
                from = afterFrom.substring(0, t).trim();
                to = afterFrom.substring(t + 3).trim();
            }
        }
        if (desc.isEmpty()) throw new MiMiException("What is the event??? Please provide description, '/from ...', and '/to ...'. If its a good event we should celebrate!");
        return new String[]{desc, from, to};
    }
}

