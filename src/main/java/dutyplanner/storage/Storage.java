package dutyplanner.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import dutyplanner.commons.exceptions.DataConversionException;
import dutyplanner.model.ReadOnlyPersonnelDatabase;
import dutyplanner.model.ReadOnlyUserPrefs;
import dutyplanner.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends PersonnelDatabaseStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getPersonnelDatabaseFilePath();

    @Override
    Optional<ReadOnlyPersonnelDatabase> readPersonnelDatabase() throws DataConversionException, IOException;

    @Override
    void savePersonnelDatabase(ReadOnlyPersonnelDatabase personnelDatabase) throws IOException;
}
