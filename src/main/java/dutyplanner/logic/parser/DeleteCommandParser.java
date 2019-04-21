package dutyplanner.logic.parser;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.commons.core.index.Index;
import dutyplanner.logic.commands.DeleteCommand;
import dutyplanner.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args, UserType userType, String userName) throws ParseException {
        if (userType == UserType.ADMIN) {
            return adminParse(args, userName);
        } else {
            throw new ParseException(Messages.MESSAGE_NO_AUTHORITY_PARSE);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand for Admin accounts.
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand adminParse(String args, String userName) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteCommand(index, userName);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
