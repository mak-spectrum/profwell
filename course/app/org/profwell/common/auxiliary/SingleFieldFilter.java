package org.profwell.common.auxiliary;

import org.profwell.generic.auxiliary.GenericFilter;

public class SingleFieldFilter extends GenericFilter {

    private String value;

    private Long workspaceId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

}
