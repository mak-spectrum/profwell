package org.profwell.vacancy.web;

import org.profwell.vacancy.model.HookupStatus;

public class HookupStatusControl {

    private String actionDescription;
    private HookupStatus toStatus;
    private String statusImage;
    private String statusImageAlt;

    public HookupStatusControl(
            String actionDescription,
            HookupStatus toStatus,
            String statusImage,
            String statusImageAlt) {

        this.actionDescription = actionDescription;
        this.toStatus = toStatus;
        this.statusImage = statusImage;
        this.statusImageAlt = statusImageAlt;
    }

    public HookupStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(HookupStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getStatusImage() {
        return statusImage;
    }

    public void setStatusImage(String statusImage) {
        this.statusImage = statusImage;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getStatusImageAlt() {
        return statusImageAlt;
    }

    public void setStatusImageAlt(String statusImageAlt) {
        this.statusImageAlt = statusImageAlt;
    }

}
