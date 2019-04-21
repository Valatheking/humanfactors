package dutyplanner.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dutyplanner.model.person.Password;
import dutyplanner.commons.core.UserType;
import dutyplanner.commons.exceptions.IllegalValueException;
import dutyplanner.model.person.Company;
import dutyplanner.model.person.Name;
import dutyplanner.model.person.Nric;
import dutyplanner.model.person.Person;
import dutyplanner.model.person.Phone;
import dutyplanner.model.person.Rank;
import dutyplanner.model.person.Section;
import dutyplanner.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String nric;
    private final String company;
    private final String section;
    private final String rank;
    private final String name;
    private final String passwordHashed;
    private final String phone;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();
    private final UserType userType;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator

    public JsonAdaptedPerson(@JsonProperty("nric") String nric, @JsonProperty("company") String company,
                             @JsonProperty("section") String section, @JsonProperty("rank") String rank,
                             @JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
                             @JsonProperty("password") String password, @JsonProperty("userType") UserType userType) {
        this.nric = nric;
        this.company = company;
        this.section = section;
        this.rank = rank;
        this.name = name;
        this.phone = phone;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
        this.passwordHashed = password;
        this.userType = userType;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        nric = source.getNric().value;
        company = source.getCompany().value;
        section = source.getSection().value;
        rank = source.getRank().value;
        name = source.getName().fullName;
        phone = source.getPhone().value;
        passwordHashed = source.getPassword().value;
        userType = source.getUserType();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName()));
        }
        if (!Nric.isValidNric(nric)) {
            throw new IllegalValueException(Nric.MESSAGE_CONSTRAINTS);
        }
        final Nric modelNric = new Nric(nric);

        if (company == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Company.class.getSimpleName()));
        }
        if (!Company.isValidCompany(company)) {
            throw new IllegalValueException(Company.MESSAGE_CONSTRAINTS);
        }
        final Company modelCompany = new Company(company);

        if (section == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Section.class.getSimpleName()));
        }
        if (!Section.isValidSection(section)) {
            throw new IllegalValueException(Section.MESSAGE_CONSTRAINTS);
        }
        final Section modelSection = new Section(section);

        if (rank == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Rank.class.getSimpleName()));
        }
        if (!Rank.isValidRank(rank)) {
            throw new IllegalValueException(Rank.MESSAGE_CONSTRAINTS);
        }
        final Rank modelRank = new Rank(rank);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (passwordHashed == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, Password.class.getSimpleName()));
        }
        final Password modelPassword = Password.hashlessPassword(passwordHashed);

        if (userType == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, UserType.class.getSimpleName()));
        }


        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelNric, modelCompany, modelSection, modelRank, modelName, modelPhone, modelTags,
                modelPassword, userType);
    }

}
