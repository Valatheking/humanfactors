package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import dutyplanner.model.Model;
import dutyplanner.commons.core.UiCommandInteraction;
import dutyplanner.logic.CommandHistory;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    /**
     * Executes the command
     */
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS, UiCommandInteraction.PEOPLE_LIST);
    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) {
        return execute(model, history);
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) {
        return execute(model, history);
    }
}
