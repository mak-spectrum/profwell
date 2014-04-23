package org.profwell.security.domain;

import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;
import org.profwell.security.model.Role;
import org.profwell.security.model.User;
import org.profwell.ui.select.DictionaryConversionUtils;

public class UserForm extends ValidationForm {

    private long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String uuid;

    private Role role;

    private boolean noRole;

    private boolean timeouted;

    private String timeoutStampTo;

    private PasswordForm passwordForm = new PasswordForm();

    public void transferFrom(User user) {
        this.id = user.getId();

        this.username = user.getUsername();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.uuid = user.getUuid();

        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public void transferToMin(User user) {
        user.setId(this.id);

        user.setEmail(this.email);

        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
    }

    public void transferTo(User user) {
        this.transferToMin(user);
        user.setRole(this.role);
    }

    public void transferToOnCreation(User user) {
        this.transferTo(user);

        user.setUsername(username);
        user.setPassword(passwordForm.getPassword());
    }

    public boolean isUserInfoSectionExpanded() {
        if (getVc().isEmpty()) {
            return true;
        } else {
            return isBlockHasValidationMessages(
                    "username", "roleValue", "password", "confirmPassword");
        }
    }

    public boolean isPersonInfoSectionExpanded() {
        if (getVc().isEmpty()) {
            return false;
        } else {
            return isBlockHasValidationMessages(
                    "firstName", "lastName");
        }
    }

    public boolean isNew() {
        return this.id == Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public String getIdValue() {
        return String.valueOf(this.id);
    }

    public void setIdValue(String id) {
        this.id = Long.parseLong(id);
    }

    public void setPassword(String password) {
        this.passwordForm.setPassword(password);
    }

    public void setConfirmPassword(String passwordConfirmation) {
        this.passwordForm.setConfirmPassword(passwordConfirmation);
    }

    public String getRoleValue() {
        if (role == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return String.valueOf(role);
        }
    }

    public void setRoleValue(String roleValue) {
        for (Role t : Role.values()) {
            if (t.getName().equals(roleValue)) {
                this.role = t;
                return;
            }
        }
        this.role = null;
    }

    public void setTimeoutStampTo(String timeoutStampTo) {
        this.timeoutStampTo = timeoutStampTo;
        this.timeouted = true;
    }

    /* SIMPLE SETTERS/GETTERS */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public PasswordForm getPasswordForm() {
        return passwordForm;
    }

    public void setPasswordForm(PasswordForm passwordForm) {
        this.passwordForm = passwordForm;
    }

    public String getTimeoutStampTo() {
        return timeoutStampTo;
    }

    public boolean isTimeouted() {
        return timeouted;
    }

    public boolean isNoRole() {
        return noRole;
    }

    public void setNoRole(boolean noRole) {
        this.noRole = noRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
