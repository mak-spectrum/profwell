package org.profwell.security.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.auxiliary.UserFilter;
import org.profwell.security.dao.UserDAO;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;

public class UserServiceImpl extends GenericServiceImpl<UserDAO, User>
        implements UserService {

    @Override
    public Workspace getWorkspace(long workspaceId) {
        return dao.getWorkspace(workspaceId);
    }

    /**
     * Authenticate a User.
     */
    @Override
    public User authenticate(String username, String password) {

        byte[] hash = this.hashPassword(password);

        List<User> users = dao.findAuthenticate(username, new String(hash));

        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    private byte[] hashPassword(String password) {
        byte[] hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException|UnsupportedEncodingException e) {
            throw new RuntimeException("Password hashing exception.", e);
        }
        return hash;
    }

    @Override
    public User findUserByUUID(String uuid) {
        return dao.findUserByUUID(uuid);
    }

    @Override
    public void addNewUser(User user) {

        if (!user.isNew()) {
            throw new IllegalArgumentException("The user is not new one");
        }

        user.setPassword(new String(hashPassword(user.getPassword())));
        user.setUuid(UUID.randomUUID().toString().toUpperCase());

        Workspace wsp = new Workspace();
        wsp.setOwner(user);

        dao.saveWorkspace(wsp);
        dao.save(user);
    }

    @Override
    public void changeUserPassword(Long userId, String newPassword) {

        String passwordHash = new String(hashPassword(newPassword));

        dao.changeUserPassword(userId, passwordHash);
    }

    @Override
    public List<User> getTimeoutedUsers() {
        return dao.getTimeoutedUsers();
    }

    @Override
    public void timeoutUser(String username) {
        dao.setTimeoutForUser(username, new Date());
    }

    @Override
    public boolean chechUserExists(String username) {
        return dao.countUsers(username) > 0;
    }

    @Override
    public void unblockTimeoutedUsers(List<User> users) {
        List<Long> userIds = new ArrayList<>();

        for (User u : users) {
            userIds.add(u.getId());
        }

        dao.unblockTimeoutedUsers(userIds);
    }

    @Override
    public List<User> listUser(UserFilter filter) {
        return this.dao.listUser(filter);
    }
}
