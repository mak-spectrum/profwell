package utils;

import org.profwell.generic.exception.ObjectNotFoundException;
import org.profwell.generic.model.Identifiable;
import org.profwell.security.model.Role;
import org.profwell.security.web.SessionUtility;

public final class SecurityControl {

    private SecurityControl() {}

    public static boolean admin() {
        return Role.ADMINISTRATOR == SessionUtility.getCurrentUserRole();
    }

    public static void checkObjectExists(Identifiable identifiable, String name) {
        if (identifiable == null) {
            throw new ObjectNotFoundException(name);
        }
    }
}
