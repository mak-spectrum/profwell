package org.profwell.vacancy.service;

import java.util.ArrayList;
import java.util.List;

import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.HookupData;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.web.HookupStatusControl;

public class HookupLifecycle {

    public boolean isStatusMoveAllowed(Hookup hookup, HookupStatus nextStatus) {

        // TODO : refactor me, don't use web component as business logic control
        for (HookupStatusControl control : this.getStatusMoves(hookup)) {
            if (control.getToStatus() == nextStatus) {
                return true;
            }
        }

        return false;
    }

    public String getAttachableDocumentTitle(HookupData hookup) {
        if (HookupStatus.GOTTEN_RESUME == hookup.getStatus()) {
            return "Attach Resume";
        } else if (HookupStatus.GOTTEN_TEST_TASK == hookup.getStatus()) {
            return "Attach Test Task Results";
        } else if (HookupStatus.ON_INTERVIEWING == hookup.getStatus()) {
            return "Add Interview Feedback";
        } else if (HookupStatus.PROBATION_IN_PROGRESS == hookup.getStatus()) {
            return "Add Probation Results Feedback";
        }
        return "";
    }

    public List<HookupStatusControl> getStatusMoves(HookupData hookup) {

        List<HookupStatusControl> list = null;

        if (hookup.getStatus() == HookupStatus.CONTACTED) {
            list = fromContacted();
        } else if (hookup.getStatus() == HookupStatus.CANDIDATE_IS_NOT_INTERESTED
                || hookup.getStatus() == HookupStatus.CANDIDATE_DOESNT_RESPOND
                || hookup.getStatus() == HookupStatus.CANDIDATE_ASKED_POSTPONEMENT) {
            list = fromPreliminaryRefusal();
        } else if (hookup.getStatus() == HookupStatus.GOTTEN_RESUME) {
            list = fromResume();
        } else if (hookup.getStatus() == HookupStatus.REJECTED_BY_RESUME) {
            list = fromResumeRejection();
        } else if (hookup.getStatus() == HookupStatus.SENT_TEST_TASK) {
            list = fromTestTask();
        } else if (hookup.getStatus() == HookupStatus.GOTTEN_TEST_TASK) {
            list = fromTestTaskResults();
        } else if (hookup.getStatus() == HookupStatus.REJECTED_BY_TEST_TASK) {
            list = fromTestTaskRejection();
        } else if (hookup.getStatus() == HookupStatus.ON_INTERVIEWING) {
            list = fromInterviewing(hookup);
        } else if (hookup.getStatus() == HookupStatus.REJECTED_BY_INTERVIEW
                || hookup.getStatus() == HookupStatus.CANDIDATE_REFUSED) {
            list = fromInterviewingRejectionRefusal();
        } else if (hookup.getStatus() == HookupStatus.RESERVED) {
            list = fromReserve();
        } else if (hookup.getStatus() == HookupStatus.APPROVED) {
            list = fromApprove();
        } else if (hookup.getStatus() == HookupStatus.PROBATION_IN_PROGRESS) {
            list = fromProbation();
        } else if (hookup.getStatus() == HookupStatus.REJECTED_BY_PROBATION
                || hookup.getStatus() == HookupStatus.CANDIDATE_REFUSED_BY_PROBATION
                || hookup.getStatus() == HookupStatus.CANDIDATE_HAS_BEEN_HIRED) {
            list = fromEnd();
        }

        return list;
    }

    private List<HookupStatusControl> fromContacted() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Candidate is not interested by the vacancy",
                HookupStatus.CANDIDATE_IS_NOT_INTERESTED,
                "refused",
                "Refused"));

        list.add(new HookupStatusControl(
                "Candidate doesn't respond",
                HookupStatus.CANDIDATE_DOESNT_RESPOND,
                "no-response",
                "No Response"));

        list.add(new HookupStatusControl(
                "Candidate asked to write later",
                HookupStatus.CANDIDATE_ASKED_POSTPONEMENT,
                "postponed",
                "Postponed"));

        list.add(new HookupStatusControl(
                "Gotten the resume",
                HookupStatus.GOTTEN_RESUME,
                "next",
                "Next"));

        return list;
    }

    private List<HookupStatusControl> fromPreliminaryRefusal() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.CONTACTED.getCaption() + ")",
                HookupStatus.CONTACTED,
                "back",
                "Back"));

        return list;
    }

    private List<HookupStatusControl> fromResume() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.CONTACTED.getCaption() + ")",
                HookupStatus.CONTACTED,
                "back",
                "Back"));

        list.add(new HookupStatusControl(
                "Rejected by the resume",
                HookupStatus.REJECTED_BY_RESUME,
                "rejected",
                "Rejected"));

        list.add(new HookupStatusControl(
                "Test task has been sent to the candidate",
                HookupStatus.SENT_TEST_TASK,
                "next",
                "Next"));

        list.add(new HookupStatusControl(
                "Candidate is on interviewing",
                HookupStatus.ON_INTERVIEWING,
                "next-next",
                "Next-Next"));

        return list;
    }

    private List<HookupStatusControl> fromResumeRejection() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.GOTTEN_RESUME.getCaption() + ")",
                HookupStatus.GOTTEN_RESUME,
                "back",
                "Back"));

        return list;
    }

    private List<HookupStatusControl> fromTestTask() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.GOTTEN_RESUME.getCaption() + ")",
                HookupStatus.GOTTEN_RESUME,
                "back",
                "Back"));

        list.add(new HookupStatusControl(
                "Gotten test task results from the candidate",
                HookupStatus.GOTTEN_TEST_TASK,
                "next",
                "Next"));

        return list;
    }

    private List<HookupStatusControl> fromTestTaskResults() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.SENT_TEST_TASK.getCaption() + ")",
                HookupStatus.SENT_TEST_TASK,
                "back",
                "Back"));

        list.add(new HookupStatusControl(
                "Rejected by the test task results",
                HookupStatus.REJECTED_BY_TEST_TASK,
                "rejected",
                "Rejected"));

        list.add(new HookupStatusControl(
                "Candidate is on interviewing",
                HookupStatus.ON_INTERVIEWING,
                "next",
                "Next"));

        return list;
    }

    private List<HookupStatusControl> fromTestTaskRejection() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.GOTTEN_TEST_TASK.getCaption() + ")",
                HookupStatus.GOTTEN_TEST_TASK,
                "back",
                "Back"));

        return list;
    }

    private List<HookupStatusControl> fromInterviewing(HookupData hookup) {
        List<HookupStatusControl> list = new ArrayList<>();

        if (hookup.isPassedTestTaskStatus()) {
            list.add(new HookupStatusControl(
                    "Revert status (" + HookupStatus.GOTTEN_TEST_TASK.getCaption() + ")",
                    HookupStatus.GOTTEN_TEST_TASK,
                    "back",
                    "Back"));
        } else {
            list.add(new HookupStatusControl(
                    "Revert status (" + HookupStatus.GOTTEN_RESUME.getCaption() + ")",
                    HookupStatus.GOTTEN_RESUME,
                    "back",
                    "Back"));
        }

        list.add(new HookupStatusControl(
                "Candidate refused after interviewing",
                HookupStatus.CANDIDATE_REFUSED,
                "refused",
                "Refused"));

        list.add(new HookupStatusControl(
                "Rejected by interviewing results",
                HookupStatus.REJECTED_BY_INTERVIEW,
                "rejected",
                "Rejected"));

        list.add(new HookupStatusControl(
                "Reserved as a possible applicant",
                HookupStatus.RESERVED,
                "reserved",
                "Reserved"));

        list.add(new HookupStatusControl(
                "Approved for the vacancy",
                HookupStatus.APPROVED,
                "approved",
                "Approved"));

        return list;
    }

    private List<HookupStatusControl> fromInterviewingRejectionRefusal() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.ON_INTERVIEWING.getCaption() + ")",
                HookupStatus.ON_INTERVIEWING,
                "back",
                "Back"));

        return list;
    }

    private List<HookupStatusControl> fromReserve() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.ON_INTERVIEWING.getCaption() + ")",
                HookupStatus.ON_INTERVIEWING,
                "back",
                "Back"));

        list.add(new HookupStatusControl(
                "Candidate refused",
                HookupStatus.CANDIDATE_REFUSED,
                "refused",
                "Refused"));

        list.add(new HookupStatusControl(
                "Approved for the vacancy",
                HookupStatus.APPROVED,
                "approved",
                "Approved"));

        return list;
    }

    private List<HookupStatusControl> fromApprove() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.ON_INTERVIEWING.getCaption() + ")",
                HookupStatus.ON_INTERVIEWING,
                "back",
                "Back"));

        list.add(new HookupStatusControl(
                "Candidate refused",
                HookupStatus.CANDIDATE_REFUSED,
                "refused",
                "Refused"));

        list.add(new HookupStatusControl(
                "Reserved as a possible applicant",
                HookupStatus.RESERVED,
                "reserved",
                "Reserved"));

        list.add(new HookupStatusControl(
                "Candidate started probation (trial period)",
                HookupStatus.PROBATION_IN_PROGRESS,
                "start-probation",
                "Probation"));

        return list;
    }

    private List<HookupStatusControl> fromProbation() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.APPROVED.getCaption() + ")",
                HookupStatus.APPROVED,
                "back",
                "Back"));

        list.add(new HookupStatusControl(
                "Candidate refused by probation",
                HookupStatus.CANDIDATE_REFUSED_BY_PROBATION,
                "refused",
                "Refused"));

        list.add(new HookupStatusControl(
                "Rejected by probation",
                HookupStatus.REJECTED_BY_PROBATION,
                "rejected",
                "Rejected"));

        list.add(new HookupStatusControl(
                "Candidate has been hired",
                HookupStatus.CANDIDATE_HAS_BEEN_HIRED,
                "hired",
                "Hired"));

        return list;
    }

    private List<HookupStatusControl> fromEnd() {
        List<HookupStatusControl> list = new ArrayList<>();

        list.add(new HookupStatusControl(
                "Revert status (" + HookupStatus.PROBATION_IN_PROGRESS.getCaption() + ")",
                HookupStatus.PROBATION_IN_PROGRESS,
                "back",
                "Back"));

        return list;
    }

}
