package seedu.address.logic;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.UserType;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyPersonnelDatabase;
import seedu.address.model.duty.DutyMonth;
import seedu.address.model.duty.DutySettings;
import seedu.address.model.person.Person;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText, UserType user, String userName) throws CommandException, ParseException;

    /**
     * Returns the PersonnelDatabase.
     *
     * @see seedu.address.model.Model#getPersonnelDatabase()
     */
    ReadOnlyPersonnelDatabase getPersonnelDatabase();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /** returns an unmodifiable view of duty for dates */
    //ObservableList<Person> getDutyForDates();

    /**
     * Returns an unmodifiable view of the list of commands entered by the user.
     * The list is ordered from the least recent command to the most recent command.
     */
    ObservableList<String> getHistory();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getPersonnelDatabaseFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' GUI settings.
     */
    DutySettings getDutySettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setDutySettings(DutySettings dutySettings);

    /**
     * Selected person in the filtered person list.
     * null if no person is selected.
     *
     * @see seedu.address.model.Model#selectedPersonProperty()
     */
    ReadOnlyProperty<Person> selectedPersonProperty();

    /**
     * Sets the selected person in the filtered person list.
     *
     * @see seedu.address.model.Model#setSelectedPerson(Person)
     */
    void setSelectedPerson(Person person);

    /**
     * Returns UserType of User if valid username and password, null otherwise.
     */
    UserType findAccount(String userName, String password);

    /**
     * Returns dutyMonth for current month
     */
    DutyMonth getCurrentDutyMonth();

    /**
     * Returns DutyMonth for next month
     */
    DutyMonth getNextDutyMonth();
}
