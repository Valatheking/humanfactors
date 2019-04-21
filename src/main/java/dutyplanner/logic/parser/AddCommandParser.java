package dutyplanner.logic.parser;

import static dutyplanner.logic.parser.CliSyntax.PREFIX_COMPANY;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_NAME;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_NRIC;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_PHONE;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_RANK;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_SECTION;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import dutyplanner.commons.core.Messages;
import dutyplanner.commons.core.UserType;
import dutyplanner.logic.commands.AddCommand;
import dutyplanner.model.tag.Tag;
import dutyplanner.logic.parser.exceptions.ParseException;
import dutyplanner.model.person.Company;
import dutyplanner.model.person.Name;
import dutyplanner.model.person.Nric;
import dutyplanner.model.person.Person;
import dutyplanner.model.person.Phone;
import dutyplanner.model.person.Rank;
import dutyplanner.model.person.Section;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args, UserType userType, String userName) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NRIC, PREFIX_COMPANY, PREFIX_SECTION,
                        PREFIX_RANK, PREFIX_NAME, PREFIX_PHONE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NRIC, PREFIX_COMPANY, PREFIX_SECTION,
                PREFIX_RANK, PREFIX_NAME, PREFIX_PHONE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }


        Nric nric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        Company company = ParserUtil.parseCompany(argMultimap.getValue(PREFIX_COMPANY).get());
        Section section = ParserUtil.parseSection(argMultimap.getValue(PREFIX_SECTION).get());
        Rank rank = ParserUtil.parseRank(argMultimap.getValue(PREFIX_RANK).get());
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(nric, company, section, rank, name, phone, tagList);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
