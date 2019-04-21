package dutyplanner.logic.parser;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.commons.core.index.Index;
import dutyplanner.logic.commands.PointsCommand;
import dutyplanner.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PointsCommnad object
 */
public class PointsCommandParser implements Parser<PointsCommand> {

    @Override
    public PointsCommand parse(String args, UserType userType, String userName) throws ParseException {
        try {
            if (args.equals("")) {
                return new PointsCommand();
            } else {
                Index index = ParserUtil.parseIndex(args);
                return new PointsCommand(index);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PointsCommand.MESSAGE_USAGE), pe);
        }
    }
}
