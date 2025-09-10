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
    private final File file = Paths.get("data", "MiMi.txt").toFile();

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
        BufferedReader buffer = null;
        try {
            if (!file.exists()) {
                return list;
            }
            buffer = new BufferedReader(new FileReader(file));
            String task;
            while ((task = buffer.readLine()) != null) {
                task = task.trim();
                if (task.isEmpty()) {
                    continue;
                }

                if (task.startsWith("Task Type")
                        || task.startsWith("Note to reader:")
                        || task.startsWith("Thank you")) {
                    continue;
                }

                Task t = parseLine(task);
                if (t != null) {
                    list.add(t);
                }
            }
        } catch (Exception e) {
            System.out.println("load problem: " + e.getMessage());
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (Exception ignore) {
                //something goes here I guess :p
            }
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
            // header starts here btw
            bw.write("Task Type\tDone/Not Done\tDescription");
            bw.newLine();

            // rows
            for (Task t : list) {
                String line = encodeRow(t);
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                bw.write(line);
                bw.newLine();
            }

            bw.newLine();
            // footer starts here
            bw.write("Note to reader: 1: Done while 0: Not Done");
            bw.newLine();
            bw.write("Thank you for using MiMi");
            bw.newLine();

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
            return "T\t" + done + "\t" + desc;
        }
        if (t instanceof Deadline d) {
            assert d.by != null : "Deadline 'by' cannot be null";
            return "D\t" + done + "\t" + desc + "\t" + d.by;
        }
        if (t instanceof Event e) {
            assert e.from != null && e.to != null : "Event 'from'/'to' cannot be null";
            return "E\t" + done + "\t" + desc + "\t" + e.from + "\t" + e.to;
        }
        return null;
    }

    private Task parseTsv(String line) {
        try {
            String[] p = line.split("\t", -1);
            for (int i = 0; i < p.length; i++) {
                p[i] = p[i].trim();
            }
            if (p.length < 3) {
                return null;
            }

            String type = p[0];
            String done = p[1];
            assert type != null && !type.isEmpty() : "Type cannot be null/empty";
            assert done != null && (done.equals("0") || done.equals("1")) : "Done flag MUST be 0 or 1";
            return getT(p, type, done);
        } catch (Exception e) {
            System.out.println("bad TSV line, skipped: " + line);
            return null;
        }
    }

    private static Task getT(String[] p, String type, String done) {
        assert p != null && p.length >= 3 : "TSV array must have at least 3 columns";
        String desc = p[2];

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
}
