package mimi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Very small persistence helper.
 * Saves/loads tasks to a TSV-like text file at {@code data/MiMi.txt}.
 * Creates the folder/file if they do not exist.
 */
public class Save {
    // constants (avoid magic strings)
    private static final String DATA_DIR = "data";
    private static final String FILE_NAME = "MiMi.txt";
    private static final String SEP = "\t";
    private static final String HEADER = "Task Type\tDone/Not Done\tDescription";
    private static final String FOOTER_NOTE = "Note to reader: 1: Done while 0: Not Done";
    private static final String FOOTER_THANKS = "Thank you for using MiMi";

    private final File file = Paths.get(DATA_DIR, FILE_NAME).toFile();

    /** Prepares the folder/file used for saving. */
    public Save() {
        try {
            Path path = file.toPath();
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.out.println("Could not save file :p : " + e.getMessage());
        }
    }

    /**
     * Loads tasks from disk. Ignores header/footer lines.
     * @return list of tasks (empty if file is missing or unreadable)
     */
    public ArrayList<Task> load() {
        ArrayList<Task> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || isMetaLine(trimmed)) {
                    continue;
                }
                Task t = parseLine(trimmed);
                if (t != null) {
                    list.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("load problem: " + e.getMessage());
        }
        return list;
    }

    /**
     * Writes the header, all tasks, and a short footer.
     * Skips invalid/blank rows.
     * @param list tasks to save
     */
    public void save(ArrayList<Task> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            writeHeader(bw);
            for (Task t : list) {
                String line = encodeRow(t);
                if (line == null || line.isBlank()) {
                    continue;
                }
                bw.write(line);
                bw.newLine();
            }
            writeFooter(bw);
        } catch (IOException e) {
            System.out.println("There was a problem when saving MiMi.txt: " + e.getMessage());
        }
    }

    // Helpers for Save.java, mostly using these for formatting u know
    private Task parseLine(String line) {
        if (!line.isEmpty()) {
            char c = line.charAt(0);
            if ((c == 'T' || c == 'D' || c == 'E') && line.indexOf('\t') > 0) {
                return parseTsv(line);
            }
        }
        return null;
    }

    private String encodeRow(Task t) {
        if (t == null) {
            return null;
        }
        String desc = t.getDescription();
        if (desc == null) {
            return null;
        }
        desc = desc.trim();
        if (desc.isEmpty()) {
            return null; // skip blank descriptions
        }

        String done = "X".equals(t.getStatusIcon()) ? "1" : "0";

        if (t instanceof Todo) {
            return "T" + SEP + done + SEP + desc;
        }
        if (t instanceof Deadline d) {
            String by = (d.getBy() == null) ? "" : d.getBy();
            return "D" + SEP + done + SEP + desc + SEP + by;
        }
        if (t instanceof Event e) {
            String from = (e.getFrom() == null) ? "" : e.getFrom();
            String to = (e.getTo() == null) ? "" : e.getTo();
            return "E" + SEP + done + SEP + desc + SEP + from + SEP + to;
        }
        return null;
    }

    private Task parseTsv(String line) {
        try {
            String[] p = line.split(SEP, -1);
            for (int i = 0; i < p.length; i++) {
                p[i] = p[i].trim();
            }
            if (p.length < 3) {
                return null;
            }

            String type = p[0];
            String done = p[1];
            assert type != null && !type.isEmpty() : "Type must not be null/empty";
            assert done != null && (done.equals("0") || done.equals("1")) : "Done flag must be 0 or 1";

            return getT(p, type, done);
        } catch (Exception e) {
            System.out.println("bad TSV line, skipped: " + line);
            return null;
        }
    }

    private static Task getT(String[] p, String type, String done) {
        assert p != null && p.length >= 3 : "TSV array must have at least 3 columns";
        String desc = p[2];
        assert desc != null : "Description must not be null";

        Task t;
        switch (type) {
        case "D" -> {
            String by = (p.length >= 4) ? p[3] : "";
            t = new Deadline(desc, by);
        }
        case "E" -> {
            String from = (p.length >= 4) ? p[3] : "";
            String to = (p.length >= 5) ? p[4] : "";
            t = new Event(desc, from, to);
        }
        default -> t = new Todo(desc);
        }

        if ("1".equals(done)) {
            t.mark();
        } else {
            t.unmark();
        }
        return t;
    }

    // ---- small helpers to improve readability ----
    private static boolean isMetaLine(String line) {
        return line.startsWith("Task Type")
                || line.startsWith("Note to reader:")
                || line.startsWith("Thank you");
    }

    private static void writeHeader(BufferedWriter bw) throws IOException {
        bw.write(HEADER);
        bw.newLine();
    }

    private static void writeFooter(BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.write(FOOTER_NOTE);
        bw.newLine();
        bw.write(FOOTER_THANKS);
        bw.newLine();
    }
}
