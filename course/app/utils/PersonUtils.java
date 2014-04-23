package utils;

import org.apache.commons.lang3.StringUtils;
import org.profwell.person.domain.PersonInfoHolder;
import org.profwell.person.model.ContactData;
import org.profwell.person.model.Person;

public final class PersonUtils {

    private PersonUtils() {
        // Nothing here
    }

    public static String getFullName(PersonInfoHolder holder) {
        String firstName;
        String secondName;
        String lastName;

        firstName = holder.getPersonFirstName();
        if (firstName.length() > 20) {
            firstName = firstName.substring(0, 20) + "...";
        }

        if (StringUtils.isNotBlank(holder.getPersonSecondName())) {
            secondName = holder.getPersonSecondName().charAt(0) + ". ";
        } else {
            secondName = "";
        }

        lastName = holder.getPersonLastName();
        if (lastName.length() > 20) {
            lastName = lastName.substring(0, 20) + "...";
        }

        return firstName + " " + secondName + lastName;
    }

    public static String getFirstPrimaryContact(Person person) {
        ContactData data = findContact(person, 0);
        if (data != null) {
            return data.getContactType() + " - " + data.getValue();
        } else {
            return " - ";
        }
    }

    public static boolean isSecondPrimaryContactExists(Person person) {
        return findContact(person, 1) != null;
    }

    public static String getSecondPrimaryContact(Person person) {
        ContactData data = findContact(person, 1);
        if (data != null) {
            return data.getContactType() + " - " + data.getValue();
        } else {
            return "";
        }
    }

    private static ContactData findContact(Person person, int primaryIndex) {
        int primaryCount = 0;
        for (int i = 0; i < person.getContactDataRecords().size(); i++) {
            ContactData data = person.getContactDataRecords().get(i);
            if (data.isPrimary()) {
                if (primaryCount == primaryIndex) {
                    return data;
                }
                primaryCount++;
            }
        }

        return null;
    }

}
