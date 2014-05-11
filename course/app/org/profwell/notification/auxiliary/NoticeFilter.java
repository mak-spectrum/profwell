package org.profwell.notification.auxiliary;

import org.profwell.generic.auxiliary.GenericFilter;

public class NoticeFilter extends GenericFilter {

    private String text;

    private Long workspaceId;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getWorkspaceId() {
        return this.workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}
