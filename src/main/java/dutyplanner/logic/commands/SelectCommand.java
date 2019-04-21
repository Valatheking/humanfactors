package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.index.Index;
import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.model.person.Person;
import dutyplanner.logic.CommandHistory;

/**
 * Selects a person identified using it's displayed index from the address book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Person: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }
    /**
     * Executes the command
     */
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Person> filteredPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= filteredPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        model.setSelectedPerson(filteredPersonList.get(targetIndex.getZeroBased()));
        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) throws CommandException {
        return execute(model, history);
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) throws CommandException {
        return execute(model, history);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
