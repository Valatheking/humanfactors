package dutyplanner.model;

import java.nio.file.Path;

import dutyplanner.commons.core.GuiSettings;
import dutyplanner.model.duty.DutySettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    DutySettings getDutySettings();

    Path getPersonnelDatabaseFilePath();


}
