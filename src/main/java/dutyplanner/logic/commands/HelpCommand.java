package dutyplanner.logic.commands;

import dutyplanner.model.Model;
import dutyplanner.commons.core.UiCommandInteraction;
import dutyplanner.logic.CommandHistory;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    public CommandResult execute(Model model, CommandHistory history) {
        return new CommandResult(SHOWING_HELP_MESSAGE, UiCommandInteraction.HELP);
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
