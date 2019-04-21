package dutyplanner.ui;

import dutyplanner.commons.core.UserType;

/**
 * Represents a pair of UserType and username
 */
public class NricUserPair {
    public final UserType userType;
    public final String userName;

    public NricUserPair(UserType userType, String userName) {
        this.userType = userType;
        this.userName = userName;
    }
}
