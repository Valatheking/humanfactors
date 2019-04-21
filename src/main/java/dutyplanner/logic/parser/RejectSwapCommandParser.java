package dutyplanner.logic.parser;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.commons.core.index.Index;
import dutyplanner.logic.commands.RejectSwapCommand;
import dutyplanner.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RejectSwapCommand object
 */
public class RejectSwapCommandParser implements Parser<RejectSwapCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RejectSwapCommand
     * and returns an RejectSwapCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectSwapCommand parse(String args, UserType userType, String userName) throws ParseException {
        if (userType == UserType.GENERAL) {
            throw new ParseException(Messages.MESSAGE_NO_AUTHORITY_PARSE);
        } else {
            try {
                Index index = ParserUtil.parseIndex(args);
                return new RejectSwapCommand(index);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, RejectSwapCommand.MESSAGE_USAGE), pe);
            }
        }
    }

}
