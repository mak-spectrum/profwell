package org.profwell.vacancy.model;

import org.profwell.generic.model.Dictionary;

public enum HookupStatus implements Dictionary {

    CONTACTED("Contacted", false, false),

    CANDIDATE_ASKED_POSTPONEMENT("Candidate Asked Postponement", true, false),
    CANDIDATE_DOESNT_RESPOND("Candidate Doesn't Respond", true, false),
    CANDIDATE_IS_NOT_INTERESTED("Candidate is not Interested", true, false),
    GOTTEN_RESUME("Gotten resume", false, true),

    SENT_TEST_TASK("Sent Test Task", false, false),
    REJECTED_BY_RESUME("Rejected by Resume", true, false),

    GOTTEN_TEST_TASK("Gotten Test Task Results", false, true),

    ON_INTERVIEWING("On Interviewing", false, true),
    REJECTED_BY_TEST_TASK("Rejected by Test Task Results", true, false),

    RESERVED("Reserved", true, false),
    APPROVED("Approved", false, false),
    CANDIDATE_REFUSED("Candidate Refused", true, false),
    REJECTED_BY_INTERVIEW("Rejected by Interview", true, false),

    PROBATION_IN_PROGRESS("Probation is in Progress", false, true),

    CANDIDATE_REFUSED_BY_PROBATION("Candidate Refused", true, false),
    REJECTED_BY_PROBATION("Rejected by probation results", true, false),
    CANDIDATE_HAS_BEEN_HIRED("Candidate has been Hired", true, false);

    private String caption;

    private boolean canBeArchived;

    private boolean documentAttachable;

    private HookupStatus(String caption, boolean canBeArchived,
            boolean documentAttachable) {
        this.caption = caption;
        this.canBeArchived = canBeArchived;
        this.documentAttachable = documentAttachable;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    public static Dictionary[] getValues() {
        return values();
    }

    public boolean isCanBeArchived() {
        return canBeArchived;
    }

    public boolean isDocumentAttachable() {
        return documentAttachable;
    }

}
