package org.profwell.security.web;

import org.profwell.security.model.Role;
import org.profwell.security.model.User;

import play.mvc.Http.Context;
import play.mvc.Http.Session;

public final class SessionUtility {

    private static final String CURRENT_USER_ID = "current.user.id";

    private static final String CURRENT_USER_USERNAME = "current.user.username";

    private static final String CURRENT_USER_FIRSTNAME = "current.user.firstname";

    private static final String CURRENT_USER_LASTNAME = "current.user.lastname";

    private static final String CURRENT_USER_UUID = "current.user.uuid";

    private static final String CURRENT_USER_ROLE = "current.user.role";

    private SessionUtility() {
        // NOTE : empty intentionally
    }

    public static void setCurrentUser(User user) {
        Session session = Context.current().session();
        session.put(CURRENT_USER_ID, String.valueOf(user.getId()));
        session.put(CURRENT_USER_USERNAME, user.getUsername());
        session.put(CURRENT_USER_UUID, user.getUuid());

        if (user.getFirstName() == null) {
            session.put(CURRENT_USER_FIRSTNAME, "");
        } else {
            session.put(CURRENT_USER_FIRSTNAME, user.getFirstName());
        }

        if (user.getLastName() == null) {
            session.put(CURRENT_USER_LASTNAME, "");
        } else {
            session.put(CURRENT_USER_LASTNAME, user.getLastName());
        }

        session.put(CURRENT_USER_ROLE, user.getRole().name());
    }

    public static User getCurrentUser() {
        User user = new User();
        Session session = Context.current().session();
        user.setId(Long.valueOf(session.get(CURRENT_USER_ID)));
        user.setUsername(session.get(CURRENT_USER_USERNAME));
        user.setUuid(session.get(CURRENT_USER_UUID));
        user.setFirstName(session.get(CURRENT_USER_FIRSTNAME));
        user.setLastName(session.get(CURRENT_USER_LASTNAME));
        user.setRole(Role.valueOf(session.get(CURRENT_USER_ROLE)));
        return user;
    }

    public static String getCurrentUsername() {
        Session session = Context.current().session();
        return session.get(CURRENT_USER_USERNAME);
    }

    public static Long getCurrentUserId() {
        Session session = Context.current().session();
        return Long.valueOf(session.get(CURRENT_USER_ID));
    }

    public static boolean isCurrentUserId(Long id) {
        return isCurrentUserId(String.valueOf(id));
    }

    public static boolean isCurrentUserId(String id) {
        Session session = Context.current().session();
        return session.get(CURRENT_USER_ID).equals(id);
    }

    public static Role getCurrentUserRole() {
        Session session = Context.current().session();
        return Role.valueOf(session.get(CURRENT_USER_ROLE));
    }

}
