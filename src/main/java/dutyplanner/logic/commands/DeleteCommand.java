package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.index.Index;
import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.model.duty.DutyMonth;
import dutyplanner.model.duty.DutyStorage;
import dutyplanner.model.person.Person;
import dutyplanner.commons.core.UiCommandInteraction;
import dutyplanner.logic.CommandHistory;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s\n"
            + "Please run \"schedule\" again.";

    private final Index targetIndex;
    private final String userName;

    public DeleteCommand(Index targetIndex, String userName) {
        this.targetIndex = targetIndex;
        this.userName = userName;
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        String userNameDeleted = personToDelete.getNric().value;
        model.deletePerson(personToDelete);

        DutyMonth dutyMonth = model.getDutyCalendar().getNextMonth();
        DutyStorage dutyStorage = model.getDutyStorage();
        dutyMonth.clearAllDuties();
        dutyStorage.removePerson(personToDelete);
        dutyMonth.unconfirm();
        dutyStorage.undo();

        model.commitPersonnelDatabase();
        if (userNameDeleted.equals(userName)) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete),
                    UiCommandInteraction.EXIT);
        }
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) throws CommandException {
        throw new CommandException(Messages.MESSAGE_NO_AUTHORITY);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
