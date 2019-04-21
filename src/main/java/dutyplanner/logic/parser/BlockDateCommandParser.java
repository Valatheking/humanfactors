package dutyplanner.logic.parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.logic.commands.BlockDateCommand;
import dutyplanner.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new BlockDateCommand object
 */
public class BlockDateCommandParser implements Parser<BlockDateCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the BlockDateCommand
     * and returns an BlockDateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public BlockDateCommand parse(String args, UserType userType, String userName) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, BlockDateCommand.MESSAGE_USAGE));
        }
        String[] dates = trimmedArgs.split(" ");
        List<Integer> blockedDatesDuplicates = new ArrayList<>();

        for (String date : dates) {
            Integer dateAdded = Integer.parseInt(date);
            if (dateAdded > 31 || dateAdded < 1) {
                throw new ParseException(Messages.MESSAGE_INVALID_DATE);
            }
            blockedDatesDuplicates.add(dateAdded);
        }
        Set<Integer> set = new HashSet<>(blockedDatesDuplicates);
        List<Integer> blockedDates = new ArrayList<>(set);
        blockedDates.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        return new BlockDateCommand(blockedDates, userName);
    }
    /**
     * Checks if an input date is valid given year,month and date
     */

}
