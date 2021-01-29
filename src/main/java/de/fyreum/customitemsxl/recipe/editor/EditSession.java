package de.fyreum.customitemsxl.recipe.editor;

public class EditSession {

    private final String id;
    private final String file;
    private final boolean existed;
    private boolean shaped = true;

    public EditSession(String id, String file, boolean existed) {
        this.id = id;
        this.file = file;
        this.existed = existed;
    }

    public void setShaped(boolean shaped) {
        this.shaped = shaped;
    }

    public String getId() {
        return id;
    }

    public String getFile() {
        return file;
    }

    public boolean isExisted() {
        return existed;
    }

    public boolean isShaped() {
        return shaped;
    }
}
