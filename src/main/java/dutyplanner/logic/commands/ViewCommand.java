package dutyplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import dutyplanner.commons.core.Messages;
import dutyplanner.logic.commands.exceptions.CommandException;
import dutyplanner.model.Model;
import dutyplanner.model.duty.Duty;
import dutyplanner.model.duty.DutyMonth;
import dutyplanner.model.person.Person;
import dutyplanner.logic.CommandHistory;

/**
 * Allows user to view their duties for the current month
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_SUCCESS = "Viewing %1$s's duties!\n";

    private final String userName;

    public ViewCommand(String userName) {
        this.userName = userName;
    }

    /**
     * Executes the command
     */
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        String messageDuty = MESSAGE_SUCCESS;

        if (!model.hasPerson(userName)) {
            throw new CommandException(Messages.MESSAGE_NO_USER);
        }

        List<Duty> duties = new ArrayList<>();

        DutyMonth currentMonth = model.getDutyCalendar().getCurrentMonth();
        duties.addAll(currentMonth.getScheduledDuties());
        DutyMonth nextMonth = model.getDutyCalendar().getNextMonth();
        duties.addAll(nextMonth.getScheduledDuties());
        int dutyCounter = 0;

        if (!duties.isEmpty()) {
            for (Duty duty : duties) {
                for (Person person : duty.getPersons()) {
                    if (person.getNric().toString().equals(userName)) {
                        dutyCounter++;
                        messageDuty = messageDuty + "Duty " + dutyCounter + ": Month: " + duty.getMonthString()
                                + ", Day: " + duty.getDayIndex() + " with" + duty.getPersonsString(userName) + " \n";
                    }
                }
            }
        } else {
            messageDuty = "%1$s has no duties!";
        }

        return new CommandResult(String.format(messageDuty, userName));
    }

    @Override
    public CommandResult executeAdmin(Model model, CommandHistory history) throws CommandException {
        return execute(model, history);
    }

    @Override
    public CommandResult executeGeneral(Model model, CommandHistory history) throws CommandException {
        return execute(model, history);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewCommand // instanceof handles nulls
                && userName.equals(((ViewCommand) other).userName));
    }
}