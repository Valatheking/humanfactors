package dutyplanner.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import dutyplanner.model.duty.DutyMonth;
import javafx.collections.ObservableList;
import dutyplanner.commons.exceptions.IllegalValueException;
import dutyplanner.model.DutyCalendar;
import dutyplanner.model.PersonnelDatabase;
import dutyplanner.model.ReadOnlyPersonnelDatabase;
import dutyplanner.model.person.Person;

/**
 * An Immutable PersonnelDatabase that is serializable to JSON format.
 */
@JsonRootName(value = "personneldatabase")
class JsonSerializablePersonnelDatabase {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final JsonAdaptedDutyMonth currentMonth;
    private final JsonAdaptedDutyMonth nextMonth;
    private final JsonAdaptedDutyStorage dutyStorage;

    /**
     * Constructs a {@code JsonSerializablePersonnelDatabase} with the given persons and duty months.
     */
    @JsonCreator
    public JsonSerializablePersonnelDatabase(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                             @JsonProperty("currentMonth") JsonAdaptedDutyMonth currentMonth,
                                             @JsonProperty("nextMonth") JsonAdaptedDutyMonth nextMonth,
                                             @JsonProperty("dutyStorage") JsonAdaptedDutyStorage dutyStorage) {
        this.persons.addAll(persons);
        this.currentMonth = currentMonth;
        this.nextMonth = nextMonth;
        this.dutyStorage = dutyStorage;
    }

    /**
     * Converts a given {@code ReadOnlyPersonnelDatabase} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializablePersonnelDatabase}.
     */
    public JsonSerializablePersonnelDatabase(ReadOnlyPersonnelDatabase source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        this.currentMonth = new JsonAdaptedDutyMonth(source.getDutyCalendar().getCurrentMonth());
        this.nextMonth = new JsonAdaptedDutyMonth(source.getDutyCalendar().getNextMonth());
        this.dutyStorage = new JsonAdaptedDutyStorage(source.getDutyCalendar().getDutyStorage());
    }

    /**
     * Converts this personnel database into the model's {@code PersonnelDatabase} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public PersonnelDatabase toModelType() throws IllegalValueException {
        PersonnelDatabase personnelDatabase = new PersonnelDatabase();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (personnelDatabase.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            personnelDatabase.addPerson(person);
        }
        ObservableList<Person> personList = personnelDatabase.getPersonList();
        
        DutyMonth modelCurrentMonth = currentMonth.toModelType(personList);
        if (modelCurrentMonth.isRollover()) {
            personnelDatabase.setDutyCalendar(new DutyCalendar(modelCurrentMonth,
                    nextMonth.toModelType(personList), dutyStorage.toModelType(personList)));
        } else {
            personnelDatabase.setDutyCalendar(new DutyCalendar(modelCurrentMonth,
                    nextMonth.toModelType(personList), dutyStorage.toModelType(personList)), false);
        }



        if (personnelDatabase.getDutyCalendar().getCurrentMonth().getMonthIndex() != personnelDatabase
            .getDutyCalendar().getNextMonth().getMonthIndex() - 1 && personnelDatabase.getDutyCalendar()
                .getCurrentMonth().getMonthIndex() != 12) {
            throw new IllegalValueException("The month indices of the current month and the next month do not match!");
        } else if (personnelDatabase.getDutyCalendar().getCurrentMonth().getMonthIndex() != 11 && personnelDatabase
                .getDutyCalendar().getCurrentMonth().getYear() != personnelDatabase
                .getDutyCalendar().getNextMonth().getYear()) {
            throw new IllegalValueException("The year indices of the current month and the next month do not match!");
        }

        return personnelDatabase;
    }

}
