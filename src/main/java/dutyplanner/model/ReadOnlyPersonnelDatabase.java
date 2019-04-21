package dutyplanner.model;

import java.util.List;

import dutyplanner.model.person.Person;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import dutyplanner.model.request.Request;

/**
 * Unmodifiable view of a personnel database
 */
public interface ReadOnlyPersonnelDatabase extends Observable {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the duty duty.
     * This duty will not contain any duplicate duties.
     */
    DutyCalendar getDutyCalendar();

    /**
     * Returns a list of requests.
     */
    List<Request> getRequestList();
}
