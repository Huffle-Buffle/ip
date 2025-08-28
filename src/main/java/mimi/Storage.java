package mimi;

/**
 * Thin facade so the project has a {@code Storage} type per the module.
 * Delegates to {@link Save}.
 */
public class Storage extends Save {

    /**
     * Constructs a storage instance. The given {@code filePath}
     * is accepted for API compatibility but not used because
     * {@link Save} uses a fixed relative path.
     * @param filePath ignored path argument
     */
    public Storage(String filePath) {
        super(); // Save() already sets up data/MiMi.txt and creates folders/files
    }
}
