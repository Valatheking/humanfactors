package dutyplanner.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dutyplanner.commons.util.InvalidationListenerManager;
import dutyplanner.model.duty.DutySettings;
import dutyplanner.model.duty.DutyStorage;
import dutyplanner.model.person.Person;
import dutyplanner.model.person.UniquePersonList;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the personnel database level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class PersonnelDatabase implements ReadOnlyPersonnelDatabase {

    private final UniquePersonList persons;
    private final DutyCalendar dutyCalendar;

    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        dutyCalendar = new DutyCalendar();
    }

    public PersonnelDatabase() {}

    /**
     * Creates an PersonnelDatabase using the Persons in the {@code toBeCopied}
     */
    public PersonnelDatabase(ReadOnlyPersonnelDatabase toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
        indicateModified();
    }

    /**
     * Replaces the contents of the duty calendar with {@code dutyCalendar}.
     * {@code dutyCalendar} must not contain duplicate duties.
     */
    public void setDutyCalendar(DutyCalendar dutyCalendar) {
        this.dutyCalendar.setDutyCalendar(dutyCalendar);
        indicateModified();
    }

    /**
     * Replaces the contents of the duty calendar with {@code dutyCalendar} without rollover
     * {@code dutyCalendar} must not contain duplicate duties.
     */
    public void setDutyCalendar(DutyCalendar dutyCalendar, boolean needsRollover) {
        this.dutyCalendar.setDutyCalendar(dutyCalendar, needsRollover);
        indicateModified();

    }

    /**
     * Schedules duty for next month in {@code dutyCalendar}.
     */
    public void scheduleDutyForNextMonth(List<Person> persons,
                                         DutySettings dutySettings, DutyStorage dutyStorage) {
        this.dutyCalendar.scheduleDutyForNextMonth(persons, dutySettings, dutyStorage);
    }

    /**
     * Resets the existing data of this {@code PersonnelDatabase} with {@code newData}.
     */
    public void resetData(ReadOnlyPersonnelDatabase newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setDutyCalendar(newData.getDutyCalendar());
    }
    /**
     * Sorts the persons in the personnal database by name
     */
    public void sort() {

        this.persons.sort();
        indicateModified();
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the personnel database.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the personnel database,
     * using the NRIC value
     */
    public boolean hasPerson(String nric) {
        boolean found = false;
        for (Person person : persons) {
            if (person.getNric().toString().equals(nric)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Adds a person to the personnel database.
     * The person must not already exist in the personnel database.
     */
    public void addPerson(Person p) {
        persons.add(p);
        dutyCalendar.getDutyStorage().addPerson(p);
        indicateModified();
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the personnel database.
     * The person identity of {@code editedPerson}
     * must not be the same as another existing person in the personnel database.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code PersonnelDatabase}.
     * {@code key} must exist in the personnel database.
     */
    public void removePerson(Person key) {
        persons.remove(key);
        indicateModified();
    }


    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the personnel database has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public DutyCalendar getDutyCalendar() {
        return this.dutyCalendar;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonnelDatabase // instanceof handles nulls
                && persons.equals(((PersonnelDatabase) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
