package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.logic.CommandHistory;

/**
 * Reverts the {@code model}'s address book to its previously undone state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo success!";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";
    /**
     * Executes the command
     */
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoPersonnelDatabase()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoPersonnelDatabase();
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) throws CommandException {
        return execute(model, history);
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) throws CommandException {
        return execute(model, history);
    }
}
