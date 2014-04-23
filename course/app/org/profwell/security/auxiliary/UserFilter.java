package org.profwell.security.auxiliary;

import org.profwell.generic.auxiliary.GenericFilter;
import org.profwell.security.model.Role;
import org.profwell.ui.select.DictionaryConversionUtils;

public class UserFilter extends GenericFilter {

    private String username;

    private String name;

    private Role role;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
