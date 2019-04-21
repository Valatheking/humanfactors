package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import dutyplanner.model.Model;
import dutyplanner.logic.CommandHistory;
import dutyplanner.logic.commands.exceptions.CommandException;

/**
 * Reverts the {@code model}'s address book to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";
    /**
     * Executes the command
     */
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoPersonnelDatabase()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoPersonnelDatabase();
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
