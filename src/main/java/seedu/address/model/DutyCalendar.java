package seedu.address.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import seedu.address.commons.util.CalendarUtil;
import seedu.address.model.duty.DutyMonth;
import seedu.address.model.duty.DutySettings;
import seedu.address.model.duty.DutyStorage;

/**
 * Represents a Calendar that contains duties for the current and the following months
 */
public class DutyCalendar {


    private static final int NUMBER_OF_MONTHS_IN_YEAR = 12;

    private int currentYear;
    private int currentMonthIndex;

    private DutyMonth currentMonth;
    private DutyMonth nextMonth;

    private int currentYear;
    private int currentMonthIndex;

    /**
     * Default constructor with no data contained within.
     */
    public DutyCalendar() {
        this.currentYear = CalendarUtil.getCurrentYear();
        this.currentMonthIndex = CalendarUtil.getCurrentMonth();

        this.currentMonth = new DutyMonth(this.currentYear, this.currentMonthIndex,
                CalendarUtil.dayOfFirstDayOfMonth(this.currentYear, this.currentMonthIndex));

        int yearOfNextMonth = currentMonthIndex == 11 ? currentYear + 1 : currentYear;
        this.nextMonth = new DutyMonth(yearOfNextMonth, this.currentMonthIndex + 1 ,
                CalendarUtil.dayOfFirstDayOfMonth(yearOfNextMonth, this.currentMonthIndex + 1));
    }

    public DutyCalendar(DutyMonth currentMonth, DutyMonth nextMonth) {
        this.currentMonth = currentMonth;
        this.nextMonth = nextMonth;
    }

    public int getCurrentMonthIndex() {
        return this.currentMonthIndex;
    }

    public DutyMonth getCurrentMonth() {
        return currentMonth;
    }

    public DutyMonth getNextMonth() {
        return nextMonth;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setDutyCalendar(DutyCalendar dutyCalendar) {
        if (dutyCalendar.getCurrentMonthIndex() == CalendarUtil.getCurrentMonth()) {
            this.currentMonth = dutyCalendar.getCurrentMonth();
            this.nextMonth = dutyCalendar.getNextMonth();
        } else {
            this.rollover(dutyCalendar);
        }
    }

    /**
     * Replace currentMonth with nextMonth and create a new nextMonth class
     * @param dutyCalendar the dutyCalendar from the storage
     */
    private void rollover(DutyCalendar dutyCalendar) {
        this.currentYear = CalendarUtil.getCurrentYear();
        this.currentMonthIndex = CalendarUtil.getCurrentMonth();
        this.currentMonth = dutyCalendar.getNextMonth();
        int yearOfNextMonth = currentMonthIndex == 11 ? currentYear + 1 : currentYear;
        this.nextMonth = new DutyMonth(yearOfNextMonth, this.currentMonthIndex + 1 ,
                CalendarUtil.dayOfFirstDayOfMonth(yearOfNextMonth, this.currentMonthIndex + 1));
    }
}
