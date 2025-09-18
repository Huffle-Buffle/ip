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
 * Handles persistence for MiMi tasks.
 * This class saves and loads {@link Task} objects to/from a tab-separated (TSV-like)
 * text file at {@code data/MiMi.txt}. It automatically creates the data folder/file
 * if they do not already exist.
 */
public class Save {
    // constants added to avoid magic strings as per week 5's increment
    /** Directory where data is stored. */
    private static final String data_dir = "data";
    /** Name of the save file. */
    private static final String filename = "MiMi.txt";
    /** Tab character used as field separator. */
    private static final String seperator = "\t";
    /** Header row written at the top of the save file. */
    private static final String header = "Task Type\tDone/Not Done\tDescription\tFrom/By\tTo";
    /** Footer note indicating meaning of 0 and 1 values. */
    private static final String footernote = "Note to reader: 1: Done while 0: Not Done";
    /** Footer thank-you message. */
    private static final String thankyou = "Thank you for using MiMi";

    /** The file object representing the save file. */
    private final File file = Paths.get(data_dir, filename).toFile();

    /**
     * Creates a {@code Save} object and ensures that the data directory and save file exist.
     * If they do not exist, they will be created automatically.
     */
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
     * Loads all saved tasks from the save file on disk.
     * Skips empty lines and metadata lines (header/footer). Each valid line
     * is parsed into a {@link Task} object.
     *
     * @return An {@link ArrayList} of {@link Task} objects.
     *         Returns an empty list if the file does not exist or cannot be read.
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
                if (trimmed.isEmpty() || endingLineCheck(trimmed)) {
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
     * Saves all given tasks to the save file on disk.
     * This method overwrites the existing file, writes a header, each task as a TSV row,
     * and then writes a footer.
     *
     * @param list The list of {@link Task} objects to save.
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

    // ==========================
    // Private helper methods
    // ==========================
    // Helpers for Save.java, mostly using these for formatting u know
    private Task parseLine(String line) {
        if (!line.isEmpty()) {
            char c = line.charAt(0);
            if ((c == 'T' || c == 'D' || c == 'E' || c == 'W') && line.indexOf('\t') > 0) {
                return parseTsv(line);
            }
        }
        return null;
    }

    /**
     * Encodes a {@link Task} into a tab-separated line for saving.
     *
     * @param t The task to encode.
     * @return A TSV-formatted line, or {@code null} if invalid.
     */
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
            return "T" + seperator + done + seperator + desc;
        }
        if (t instanceof Deadline d) {
            String by = (d.getBy() == null) ? "" : d.getBy();
            return "D" + seperator + done + seperator + desc + seperator + by;
        }
        if (t instanceof Event e) {
            String from = (e.getFrom() == null) ? "" : e.getFrom();
            String to = (e.getTo() == null) ? "" : e.getTo();
            return "E" + seperator + done + seperator + desc + seperator + from + seperator + to;
        }
        if (t instanceof DoWithinPeriodTasks w) {
            String from = (w.getFrom() == null) ? "" : w.getFrom();
            String to = (w.getTo() == null) ? "" : w.getTo();
            return "W" + seperator + done + seperator + desc + seperator + from + seperator + to;
        }
        return null;
    }

    /**
     * Parses a TSV-formatted line into a {@link Task} object.
     *
     * @param line A TSV-formatted line from the save file.
     * @return A {@link Task}, or {@code null} if parsing fails.
     */
    private Task parseTsv(String line) {
        try {
            String[] p = line.split(seperator, -1);
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

    /**
     * Creates a {@link Task} object from parsed TSV values.
     *
     * @param p    The parsed TSV fields.
     * @param type The task type (T, D, or E).
     * @param done The done flag ("0" or "1").
     * @return A {@link Task} object.
     */
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
        case "W" -> {
            String from = (p.length >= 4) ? p[3] : "";
            String to = (p.length >= 5) ? p[4] : "";
            t = new DoWithinPeriodTasks(desc, from, to);
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

    /**
     * Checks if a line is a metadata line (header or footer).
     *
     * @param line The line to check.
     * @return {@code true} if the line is a header/footer, {@code false} otherwise.
     */
    private static boolean endingLineCheck(String line) {
        return line.startsWith("Task Type")
                || line.startsWith("Note to reader:")
                || line.startsWith("Thank you");
    }

    /**
     * Writes the header row to the save file.
     *
     * @param bw The writer to use.
     * @throws IOException If an I/O error occurs.
     */
    private static void writeHeader(BufferedWriter bw) throws IOException {
        bw.write(header);
        bw.newLine();
    }

    /**
     * Writes the footer notes to the save file.
     *
     * @param bw The writer to use.
     * @throws IOException If an I/O error occurs.
     */
    private static void writeFooter(BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.write(footernote);
        bw.newLine();
        bw.write(thankyou);
        bw.newLine();
    }
}
