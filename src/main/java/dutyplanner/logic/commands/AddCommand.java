package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_COMPANY;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_NAME;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_NRIC;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_PHONE;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_RANK;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_SECTION;
import static dutyplanner.logic.parser.CliSyntax.PREFIX_TAG;

import dutyplanner.commons.core.Messages;
import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.model.person.Person;
import dutyplanner.logic.CommandHistory;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the duty planner. "
            + "Parameters: "
            + PREFIX_NRIC + "NRIC "
            + PREFIX_COMPANY + "COMPANY "
            + PREFIX_SECTION + "SECTION "
            + PREFIX_RANK + "RANK "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NRIC + "S9012345L "
            + PREFIX_COMPANY + "Echo "
            + PREFIX_SECTION + "1 "
            + PREFIX_RANK + "CPL "
            + PREFIX_NAME + "Brandon Foo "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_TAG + "injury";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s\n";
    public static final String MESSAGE_RUN_SCHEDULE_AGAIN = "Schedule unconfirmed! Please run <schedule> again.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);

        boolean isConfirmed = model.getDutyCalendar().getNextMonth().isConfirmed();

        model.getDutyCalendar().getNextMonth().unconfirm();
        model.getDutyCalendar().getDutyStorage().undo();

        model.commitPersonnelDatabase();
        return (isConfirmed ? new CommandResult(String.format(MESSAGE_SUCCESS + MESSAGE_RUN_SCHEDULE_AGAIN, toAdd))
                : new CommandResult(String.format(MESSAGE_SUCCESS, toAdd)));
    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) throws CommandException {
        throw new CommandException(Messages.MESSAGE_NO_AUTHORITY);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
