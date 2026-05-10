package nl.miwnn.cohort._9.OHI.Model;

/**
 * @author Alexander Banic
 * INFO OVER PROJECT
 */
public enum Role {
    STUDENT("Student"),
    TEACHER("Docent"),
    ADMIN("Admin");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
