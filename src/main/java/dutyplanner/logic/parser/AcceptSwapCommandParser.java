package dutyplanner.logic.parser;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.commons.core.index.Index;
import dutyplanner.logic.commands.AcceptSwapCommand;
import dutyplanner.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AcceptSwapCommand object
 */
public class AcceptSwapCommandParser implements Parser<AcceptSwapCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AcceptSwapCommand
     * and returns an AcceptSwapCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AcceptSwapCommand parse(String args, UserType userType, String userName) throws ParseException {
        if (!userName.equals(UserType.DEFAULT_ADMIN_USERNAME)) {
            return parseForPersonnelUser(args, userName);
        } else {
            throw new ParseException(Messages.MESSAGE_NO_AUTHORITY_PARSE);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AcceptSwapCommand for accounts associated
     * with a personnel and returns an AcceptSwapCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AcceptSwapCommand parseForPersonnelUser(String args, String userName) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new AcceptSwapCommand(userName, index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AcceptSwapCommand.MESSAGE_USAGE), pe);
        }
    }
}
