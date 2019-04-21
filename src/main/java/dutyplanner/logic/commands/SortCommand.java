package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.commons.core.UiCommandInteraction;
import dutyplanner.logic.CommandHistory;


/**
 * Sorts all persons in the address book.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Listed all persons sorted by name";

    /**
     * Executes the command
     */
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.sortPersonnelDatabase();
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS, UiCommandInteraction.PEOPLE_LIST);
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
