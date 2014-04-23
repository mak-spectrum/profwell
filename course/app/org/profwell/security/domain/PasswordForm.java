package org.profwell.security.domain;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.validation.ValidationContext;


public class PasswordForm {

    private long id;

    private String currentPassword;

    private String password;

    private String confirmPassword;

    private ValidationContext context;

    public boolean isNew() {
        return this.id == Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public String getIdValue() {
        return String.valueOf(this.id);
    }

    public void setIdValue(String id) {
        this.id = Long.parseLong(id);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public ValidationContext getVc() {
        return context;
    }

    public void setVc(ValidationContext context) {
        this.context = context;
    }

}
