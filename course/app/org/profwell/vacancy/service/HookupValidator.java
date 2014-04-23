package org.profwell.vacancy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.ValidationMessageBuilder;
import org.profwell.generic.validation.utils.ValidationUtility;
import org.profwell.person.service.PersonService;
import org.profwell.security.web.SessionUtility;
import org.profwell.vacancy.domain.HookupForm;

public class HookupValidator {

    public boolean validate(HookupForm form) {
        boolean result = true;

        ValidationContext context = form.getVc();

        result &= validateMainData(form, context);

        result &= validateEngagement(form, context);

        result &= validatePersons(form, context);

        return result;
    }

    private boolean validateMainData(HookupForm form, ValidationContext context) {

        boolean result = true;

        if (!ServiceHolder.getService(VacancyService.class)
                .checkVacancyAccessible(
                        form.getVacancyId(),
                        SessionUtility.getCurrentUserId())) {
            context.add(new ValidationMessageBuilder()
                    .source("message")
                    .message("Erroneous vacancy has been selected, please start from scratch.")
                    .build());
            result &= false;
        }

        if (form.getContactedOn() == null) {
            context.add(new ValidationMessageBuilder()
                    .source("contactedOn")
                    .message("Please specify contact date.")
                    .build());
            result &= false;
        } else if (form.getContactedOn().getTime() > new Date().getTime()) {
            context.add(new ValidationMessageBuilder()
                    .source("contactedOn")
                    .message("Contact date cannot be more, than today.")
                    .build());
            result &= false;
        }

        if (form.getCandidateId() ==
                Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE) {
            context.add(new ValidationMessageBuilder()
                    .source("candidate")
                    .message("Please select a candidate, or register new person, as a candidate.")
                    .build());
            result &= false;
        }

        result &= ValidationUtility.validateMaxLength(
                "currentPosition",
                form.getCandidateCurrentPosition(),
                ModelConstants.STANDARD_TEXT_LIMIT,
                context);

        result &= ValidationUtility.validateMaxLength(
                "currentCompany",
                form.getCandidateCurrentCompany(),
                ModelConstants.STANDARD_TEXT_LIMIT,
                context);

        return result;
    }

    private boolean validateEngagement(HookupForm form,
            ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility.validateDropDown(
                "engagementTypeValue", form.getEngagementTypeValue(), context);

        result &= ValidationUtility.validateDropDown(
                "sourceNote", form.getEngagementSourceNote(), context);

        return result;
    }

    private boolean validatePersons(HookupForm form,
            ValidationContext context) {

        boolean result = true;
        long nullId = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

        List<Long> idsToValidate = new ArrayList<>();
        if (form.getCandidateId() > nullId) {
            idsToValidate.add(form.getCandidateId());
        }
        if (form.getRecommenderId() > nullId) {
            idsToValidate.add(form.getRecommenderId());
        }

        List<Long> checkedIds = ServiceHolder.getService(PersonService.class)
                .checkBelongToWorkspace(idsToValidate,
                        SessionUtility.getCurrentUserId());

        if (form.getCandidateId() > nullId
                && !checkedIds.contains(form.getCandidateId())) {
            context.add(new ValidationMessageBuilder()
                    .source("candidate")
                    .message("Incorrect candidate has been selected. Please select a candidate from your workspace.")
                    .build());
            result &= false;
        }

        if (form.getRecommenderId() > nullId
                && !checkedIds.contains(form.getRecommenderId())) {
            context.add(new ValidationMessageBuilder()
                    .source("recommender")
                    .message("Incorrect recommender has been selected. Please select a recommender from your workspace.")
                    .build());
            result &= false;
        }

        return result;
    }

}
