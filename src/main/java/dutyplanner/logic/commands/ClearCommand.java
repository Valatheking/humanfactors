package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import dutyplanner.commons.core.Messages;
import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.model.PersonnelDatabase;
import dutyplanner.commons.core.UiCommandInteraction;
import dutyplanner.logic.CommandHistory;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Personnel database has been cleared!";

    public final String userName;

    public ClearCommand(String userName) {
        this.userName = userName;
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setPersonnelDatabase(new PersonnelDatabase());
        model.commitPersonnelDatabase();
        if ("Admin".equals(userName)) {
            return new CommandResult(MESSAGE_SUCCESS);
        }
        return new CommandResult(MESSAGE_SUCCESS, UiCommandInteraction.EXIT);
    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) throws CommandException {
        throw new CommandException(Messages.MESSAGE_NO_AUTHORITY);
    }
}
