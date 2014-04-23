package org.profwell.collaboration.model;

public enum ConnectionType {

    COMPANION("Companion", "Companion"),

    FREELANCER_RECRUITER("Freelancer Recruiter", "Client"),

    STAFF_RECRUITER("Staff Recruiter", "Employer"),

    RECRUITMENT_AGENCY("Recruitment Agency", "Client");

    private String caption;
    private String oppositeCaption;

    private ConnectionType(String caption, String oppositeCaption) {
        this.caption = caption;
        this.oppositeCaption = oppositeCaption;
    }

    public String getOppositeCaption() {
        return oppositeCaption;
    }

    public String getCaption() {
        return caption;
    }

}
