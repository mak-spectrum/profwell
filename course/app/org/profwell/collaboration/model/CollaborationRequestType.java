package org.profwell.collaboration.model;

public enum CollaborationRequestType {

    COMPANION("Companion"),

    FREELANCER_RECRUITER("Freelancer Recruiter"),
    CLIENT_HIRER("Client of a Freelancer Recruiter"),

    STAFF_RECRUITER("Staff Recruiter"),
    CLIENT_EMPLOYER("Employer of a Staff Recruiter"),

    RECRUITMENT_AGENCY("Recruitment Agency"),
    CLIENT("Client of a Recruitment Agency");

    private String caption;
    private CollaborationRequestType opposite;
    private String description;

    static {
        COMPANION.opposite = COMPANION;
        COMPANION.description =
                "Companion has possibility to share contacts"
                + " to his partner, besides that your partner will be able to set you as a source of a vacancy candidate."
                + " You also will be a Companion for your partner.";



        FREELANCER_RECRUITER.opposite = CLIENT_HIRER;
        FREELANCER_RECRUITER.description =
                "Partner of a Freelancer Recruiter is his Client."
                + " Freelancer Recruiter has possibility to see opened vacancies of his Client and attach candidates to them."
                + " Client gets a copy of a person contact to his contact book, when Freelancer Recruiter attaches a candidate to his vacancy."
                + " However Freelancer Recruiter can't see any candidate, attached to a vacancy by another person."
                + " No one has access to the contact book of his partner."
                + " Client has the same possibilities in regards to Freelancer Recruiter, as a regular Companion has.";

        CLIENT_HIRER.opposite = FREELANCER_RECRUITER;
        CLIENT_HIRER.description = FREELANCER_RECRUITER.description;



        STAFF_RECRUITER.opposite = CLIENT_EMPLOYER;
        STAFF_RECRUITER.description =
                "Partner of a Staff Recruiter is his Employer."
                + " Staff Recruiter has possibility to see opened vacancies of his Employer and attach candidates to them."
                + " Employer gets a copy of a person contact to his contact book, when the Staff Recruiter attaches a candidate to his vacancy."
                + " However Staff Recruiter can't see any candidate, attached to a vacancy by another person."
                + " Additional option is that Staff Recruiter can be appointed, to work,"
                + " as a responsible person on a vacancy, with additional possibility to block the vacancy to be a single executor."
                + " No one has access to the contact book of his partner."
                + " Employer has the same possibilities in regards to the Staff Recruiter, as a regular Companion has.";

        CLIENT_EMPLOYER.opposite = STAFF_RECRUITER;
        CLIENT_EMPLOYER.description = STAFF_RECRUITER.description;



        RECRUITMENT_AGENCY.opposite = CLIENT;
        RECRUITMENT_AGENCY.description =
                "Partner of a Freelancer Recruiter is his Client."
                + " Client is able to share vacancies to his Recruitment Agencies, so that the Recruitment Agency can help him with searching candidates."
                + " Client gets a copy of a person contact to his contact book,"
                + " when the Recruitment Agency or his recruiters attaches a candidate to the shared vacancy."
                + " However Recruitment Agency can't see any candidate, attached to a vacancy by another person."
                + " Finally, no one has access to the contact book of his partner.";

        CLIENT.opposite = RECRUITMENT_AGENCY;
        CLIENT.description = RECRUITMENT_AGENCY.description;
    }

    private CollaborationRequestType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public CollaborationRequestType getOpposite() {
        return opposite;
    }

    public String getDescription() {
        return description;
    }

}
