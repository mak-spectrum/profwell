package org.profwell.security.service;

import java.util.List;

import org.profwell.generic.service.GenericService;
import org.profwell.security.auxiliary.UserFilter;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;

public interface UserService extends GenericService<User> {

    Workspace getWorkspace(long workspaceId);

    /**
     * Authenticate a User.
     */
    User authenticate(String username, String password);

    User findUserByUUID(String uuid);

    /**
     * Blocks a user for timeout.
     */
    void timeoutUser(String username);

    /**
     * Check, whenever a user with the specified username exists.
     */
    boolean chechUserExists(String username);

    /**
     * Blocks a user.
     */
    List<User> getTimeoutedUsers();

    /**
     * Blocks a user.
     */
    void unblockTimeoutedUsers(List<User> users);

    /**
     * Authenticate a User.
     */
    void addNewUser(User user);

    /**
     * Loads filtered users list.
     *
     * @param filter
     * @return
     */
    List<User> listUser(UserFilter filter);

    void changeUserPassword(Long userId, String newPassword);
}
