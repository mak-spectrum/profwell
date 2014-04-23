package org.profwell.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.validation.utils.ValidationUtility;
import org.profwell.security.domain.LoginForm;
import org.profwell.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;
import play.db.jpa.JPA;
import play.libs.Akka;
import play.libs.F.Callback0;
import scala.concurrent.duration.Duration;

public class Authenticator {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Authenticator.class);

    private static final int MAX_NUMBER_OF_UNSUCCESSFUL_ATTEMPTS = 5;

    private static final long AN_HOUR = 60*60*1;

    private static final long QARTER_AN_HOUR = 15*60*1000;

    private WeakHashMap<String, LinkedList<Date>> unsuccesfullLoginAttempts =
            new WeakHashMap<>();

    public Authenticator() {
        if (Play.application().configuration()
                .getBoolean("application.schedulers.enable")) {

            Akka.system().scheduler().schedule(
                    Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
                    Duration.create(5, TimeUnit.MINUTES),     //Frequency 30 minutes
                    new Runnable() {

                        @Override
                        public void run() {
                            Authenticator.this.unblockTimeoutedUsers();
                        }
                    },
                    Akka.system().dispatcher()
            );
        }
    }

    private void unblockTimeoutedUsers() {
        try {
            JPA.withTransaction(new Callback0() {

                @Override
                public void invoke() throws Throwable {
                    List<User> timeoutedUsers = ServiceHolder
                            .getService(UserService.class)
                            .getTimeoutedUsers();

                    List<User> usersToUnblock = new ArrayList<>();

                    long current = System.currentTimeMillis();
                    for (User user : timeoutedUsers) {
                        if (current - user.getTimeoutStamp()
                                .getTime() > AN_HOUR) {
                            usersToUnblock.add(user);
                        }
                    }

                    if (!usersToUnblock.isEmpty()) {
                        ServiceHolder.getService(UserService.class)
                                .unblockTimeoutedUsers(usersToUnblock);
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error("Users unblocking exception.", e);
        }
    }

    public User authenticate(LoginForm login) {
        User user = null;

        if (this.validateInputData(login)) {
            user = this.authenicateInternally(login);
        }

        return user;
    }

    private boolean validateInputData(LoginForm login) {
        boolean result = true;

        result &= ValidationUtility.validateRequiredText(
                "username", login.getUsername(), login.getVc());
        result &= ValidationUtility.validateRequiredText(
                "password", login.getPassword(), login.getVc());

        return result;
    }

    private User authenicateInternally(LoginForm login) {
        User user = ServiceHolder.getService(UserService.class).authenticate(
                login.getUsername(), login.getPassword());

        if (user == null) {
            this.registerUnsuccessfulLoginAttempt(login.getUsername());

            if (this.checkBlockingRequired(login.getUsername())) {
                timeoutUser(login);
                login.getVc().add("login",
                        "There were at least 5 unsuccessful attempts to login during the last 15 minutes."
                                + " User account will be blocked for an hour.");
            } else {
                login.getVc().add("login", "Invalid user or password");
            }

        } else if (user.getTimeoutStamp() != null) {
            login.getVc().add("login",
                    "There were at least 5 unsuccessful attempts to login during the last 15 minutes."
                            + " User account has been blocked for an hour.");
            user = null;

        } else {
            this.unsuccesfullLoginAttempts.remove(login.getUsername());
        }

        return user;
    }

    private void timeoutUser(final LoginForm login) {
        JPA.withTransaction(new Callback0() {

            @Override
            public void invoke() throws Throwable {
                ServiceHolder.getService(UserService.class)
                        .timeoutUser(login.getUsername());
            }

        });
    }

    private synchronized void registerUnsuccessfulLoginAttempt(String username) {
        LinkedList<Date> attempts = this.unsuccesfullLoginAttempts.get(username);

        if (attempts == null) {
            attempts = new LinkedList<>();
            this.unsuccesfullLoginAttempts.put(username, attempts);
        }

        attempts.add(new Date());
    }

    private synchronized boolean checkBlockingRequired(String username) {
        boolean blockUser = false;

        LinkedList<Date> attempts =
                this.unsuccesfullLoginAttempts.get(username);

        while (attempts != null
                && attempts.size() >= MAX_NUMBER_OF_UNSUCCESSFUL_ATTEMPTS) {

            Date mostOldAttempt = attempts.getFirst();

            if (System.currentTimeMillis() - mostOldAttempt.getTime()
                    < QARTER_AN_HOUR) {
                blockUser = true;
                break;
            } else {
                attempts.pollFirst();
            }
        }

        return blockUser;
    }

}
