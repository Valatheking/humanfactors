package dutyplanner.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.logic.parser.exceptions.ParseException;
import dutyplanner.logic.commands.AcceptSwapCommand;
import dutyplanner.logic.commands.AddCommand;
import dutyplanner.logic.commands.ApproveSwapCommand;
import dutyplanner.logic.commands.BlockDateCommand;
import dutyplanner.logic.commands.ClearCommand;
import dutyplanner.logic.commands.Command;
import dutyplanner.logic.commands.ConfirmScheduleCommand;
import dutyplanner.logic.commands.DeleteCommand;
import dutyplanner.logic.commands.DutySettingsCommand;
import dutyplanner.logic.commands.EditCommand;
import dutyplanner.logic.commands.ExitCommand;
import dutyplanner.logic.commands.FindCommand;
import dutyplanner.logic.commands.HelpCommand;
import dutyplanner.logic.commands.HistoryCommand;
import dutyplanner.logic.commands.ListCommand;
import dutyplanner.logic.commands.PenalizeCommand;
import dutyplanner.logic.commands.PointsCommand;
import dutyplanner.logic.commands.RedoCommand;
import dutyplanner.logic.commands.RejectSwapCommand;
import dutyplanner.logic.commands.RemoveBlockCommand;
import dutyplanner.logic.commands.RewardCommand;
import dutyplanner.logic.commands.ScheduleCommand;
import dutyplanner.logic.commands.SelectCommand;
import dutyplanner.logic.commands.SortCommand;
import dutyplanner.logic.commands.SwapCommand;
import dutyplanner.logic.commands.UnconfirmCommand;
import dutyplanner.logic.commands.UndoCommand;
import dutyplanner.logic.commands.ViewBlockCommand;
import dutyplanner.logic.commands.ViewCommand;
import dutyplanner.logic.commands.ViewCurrentCommand;
import dutyplanner.logic.commands.ViewNextCommand;
import dutyplanner.logic.commands.ViewSwapsCommand;

/**
 * Parses user input.
 */
public class PersonnelDatabaseParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput, UserType userType, String userName) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AcceptSwapCommand.COMMAND_WORD:
            return new AcceptSwapCommandParser().parse(arguments, userType, userName);

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments, userType, userName);

        case BlockDateCommand.COMMAND_WORD:
            return new BlockDateCommandParser().parse(arguments, userType, userName);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand(userName);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments, userType, userName);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments, userType, userName);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments, userType, userName);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case ScheduleCommand.COMMAND_WORD:
            return new ScheduleCommand();

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments, userType, userName);

        case SortCommand.COMMAND_WORD:
            return new SortCommand();

        case ConfirmScheduleCommand.COMMAND_WORD:
            return new ConfirmScheduleCommand();

        case SwapCommand.COMMAND_WORD:
            return new SwapCommandParser().parse(arguments, userType, userName);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case ViewCommand.COMMAND_WORD:
            return new ViewCommandParser().parse(arguments, userType, userName);

        case UnconfirmCommand.COMMAND_WORD:
            return new UnconfirmCommand();

        case DutySettingsCommand.COMMAND_WORD:
            return new DutySettingsCommandParser().parse(arguments, userType, userName);

        case PointsCommand.COMMAND_WORD:
            return new PointsCommandParser().parse(arguments, userType, userName);

        case ViewCurrentCommand.COMMAND_WORD:
            return new ViewCurrentCommand();

        case ViewNextCommand.COMMAND_WORD:
            return new ViewNextCommand();

        case ViewSwapsCommand.COMMAND_WORD:
            return new ViewSwapsCommand();

        case ApproveSwapCommand.COMMAND_WORD:
            return new ApproveSwapCommandParser().parse(arguments, userType, userName);

        case RejectSwapCommand.COMMAND_WORD:
            return new RejectSwapCommandParser().parse(arguments, userType, userName);

        case RewardCommand.COMMAND_WORD:
            return new RewardCommandParser().parse(arguments, userType, userName);

        case PenalizeCommand.COMMAND_WORD:
            return new PenalizeCommandParser().parse(arguments, userType, userName);

        case ViewBlockCommand.COMMAND_WORD:
            return new ViewBlockCommand(userName);

        case RemoveBlockCommand.COMMAND_WORD:
            return new RemoveBlockCommand(userName);

        default:
            throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
