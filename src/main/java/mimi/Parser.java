package mimi;

public class Parser {
    public static String commandWord(String input) {
        String s = input == null ? "" : input.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? s : s.substring(0, sp);
    }

    public static String afterWord(String input) {
        String s = input == null ? "" : input.trim();
        int sp = s.indexOf(' ');
        return sp == -1 ? "" : s.substring(sp + 1).trim();
    }

    public static int parseIndex(String arg) throws MiMiException {
        try {
            int i = Integer.parseInt(arg.trim());
            if (i <= 0) throw new NumberFormatException();
            return i - 1;
        } catch (Exception e) {
            throw new MiMiException("Please give a valid positive number.");
        }
    }

    public static String parseTodo(String rest) throws MiMiException {
        String desc = (rest == null) ? "" : rest.trim();
        if (desc.isEmpty()) {
            throw new MiMiException("How can there be nothing to do, there is always something to do!");
        }
        return desc;
    }

    public static String[] parseDeadline(String rest) throws MiMiException {
        int pos = rest.indexOf("/by");
        if (pos == -1) throw new MiMiException("Use /by for deadlines (e.g., ... /by 2019-10-15)");
        String desc = rest.substring(0, pos).trim();
        String when = rest.substring(pos + 3).trim(); // after '/by'
        if (desc.isEmpty() || when.isEmpty())
            throw new MiMiException("Chop chop what's the deadline? Please provide '/by <deadline>' (e.g., deadline return book /by Sunday)");
        return new String[]{desc, when};
    }

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

