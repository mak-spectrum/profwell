package utils;

import org.apache.commons.lang3.StringUtils;
import org.profwell.security.model.User;

public final class UserUtils {

    private UserUtils() {
        // Nothing here
    }

    public static String getFullName(User user) {
        String firstName;
        String lastName;

        firstName = user.getFirstName();
        if (firstName == null) {
            firstName = "";
        } else if (firstName.length() > 20) {
            firstName = firstName.substring(0, 20) + "...";
        }

        lastName = user.getLastName();
        if (lastName == null) {
            lastName = "";
        } else if (lastName.length() > 20) {
            lastName = lastName.substring(0, 20) + "...";
        }

        return firstName + " " + lastName;
    }

    public static String getFullNameOrUUID(User user) {
        String fullName = getFullName(user);
        if (StringUtils.isBlank(fullName)) {
            return user.getUuid();
        } else {
            return fullName;
        }
    }
}
