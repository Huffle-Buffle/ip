package mimi;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class Save {
    private final File file = Paths.get("data", "MiMi.txt").toFile();

    public Save() {
        try {
            Path path = file.toPath();
            Files.createDirectories(path.getParent());  // no boolean so creates if missing
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.out.println("could not save file :p : " + e.getMessage());
        }
    }

    public ArrayList<Task> load() {
        ArrayList<Task> list = new ArrayList<>();
        BufferedReader buffer = null;
        try {
            if (!file.exists()) return list;
            buffer = new BufferedReader(new FileReader(file));
            String task;
            while ((task = buffer.readLine()) != null) {
                task = task.trim();
                if (task.isEmpty()) continue;

                if (task.startsWith("Task Type")
                        || task.startsWith("Note to reader:")
                        || task.startsWith("Thank you")) {
                    continue;
                }

                Task t = parseLine(task);
                if (t != null) list.add(t);
            }
        } catch (Exception e) {
            System.out.println("load problem: " + e.getMessage());
        } finally {
            try { if (buffer != null) buffer.close(); } catch (Exception ignore) {}
        }
        return list;
    }

    // It is here where we format thr txt file that will be saved which is MiMi.txt
    public void save(ArrayList<Task> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            // header starts here btw
            bw.write("Task Type\tDone/Not Done\tDescription");
            bw.newLine();

            // rows
            for (Task t : list) {
                String line = encodeRow(t);                 // builds the row text
                if (line == null || line.trim().isEmpty())  // skip invalid/blank rows
                    continue;
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
                return parseTSV(line);
            }
        }
        return null;
    }

    private String encodeRow(Task t) {
        if (t == null) return null;

        String desc = t.getDescription();
        if (desc == null) return null;
        desc = desc.trim();
        if (desc.isEmpty()) return null; // skip blank descriptions

        String done = "X".equals(t.getStatusIcon()) ? "1" : "0";

        if (t instanceof Todo) {
            return "T\t" + done + "\t" + desc;
        }
        if (t instanceof Deadline d) {
            String by = (d.by == null) ? "" : d.by;
            return "D\t" + done + "\t" + desc + "\t" + by;
        }
        if (t instanceof Event e) {
            String from = (e.from == null) ? "" : e.from;
            String to = (e.to == null) ? "" : e.to;
            return "E\t" + done + "\t" + desc + "\t" + from + "\t" + to;
        }
        return null;
    }

    private Task parseTSV(String line) {
        try {
            String[] p = line.split("\t", -1);
            for (int i = 0; i < p.length; i++) p[i] = p[i].trim();
            if (p.length < 3) return null;

            String type = p[0];
            String done = p[1];
            return getT(p, type, done);
        } catch (Exception e) {
            System.out.println("bad TSV line, skipped: " + line);
            return null;
        }
    }

    private static Task getT(String[] p, String type, String done) {
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

        if ("1".equals(done)) t.mark(); else t.unmark();
        return t;
    }
}
