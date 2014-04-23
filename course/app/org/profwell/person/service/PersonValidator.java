package org.profwell.person.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.ValidationMessageBuilder;
import org.profwell.generic.validation.utils.ValidationUtility;
import org.profwell.person.domain.ContactForm;
import org.profwell.person.domain.PersonForm;

public class PersonValidator {

    public boolean validate(PersonForm personForm) {
        boolean result = true;

        ValidationContext context = personForm.getVc();

        result &= validateGeneralData(personForm, context);

        result &= validateSpeciality(personForm, context);

        result &= validateContacts(personForm.getContacts(), context);

        return result;
    }

    private boolean validateGeneralData(PersonForm person,
            ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility.validateRequiredText(
                "firstName", person.getFirstName(), context);

        result &= ValidationUtility.validateMaxLength(
                "secondName",
                person.getSecondName(),
                ModelConstants.STANDARD_TEXT_LIMIT,
                context);

        result &= ValidationUtility.validateRequiredText(
                "lastName", person.getLastName(), context);

        result &= ValidationUtility.validateMaxLength(
                "shortNote",
                person.getShortNote(),
                ModelConstants.STANDARD_TEXT_LIMIT,
                context);

        return result;
    }

    private boolean validateSpeciality(PersonForm person,
            ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility.validateDropDown(
                "generalProfessionValue",
                person.getGeneralProfessionValue(),
                context);

        result &= ValidationUtility
                .validateMaxLength("details", person.getProfessionDetails(),
                        ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT, context);

        return result;
    }

    private boolean validateContacts(List<ContactForm> contacts, ValidationContext context) {

        boolean result = true;

        int primaryCount = 0;

        ContactForm contactData = null;
        for (int i = 0; i < contacts.size(); i++) {
            contactData = contacts.get(i);

            result &= validateSingleContact("contact:" + i, contactData, context);

            if (contactData.isPrimary()) {
                primaryCount++;
            }
        }

        if (primaryCount > 2) {
            context.add(new ValidationMessageBuilder()
            .source("contact")
            .message("Only 2 contacts can be marked, as primary.")
            .build());
            result = false;
        }

        return result;
    }

    private boolean validateSingleContact(String source, ContactForm contact,
            ValidationContext context) {

        boolean result = true;

        if (StringUtils.isBlank(contact.getContactType())) {
            context.add(new ValidationMessageBuilder()
                    .source(source)
                    .message("Please set Contact Type.")
                    .build());
            result = false;
        } else if (contact.getContactType().trim().length()
                > ModelConstants.SHORT_TEXT_LIMIT) {
            context.add(new ValidationMessageBuilder()
                    .source(source)
                    .message("Contact Type can't be greater than "
                            + ModelConstants.SHORT_TEXT_LIMIT
                            + " symbols in length.")
                    .build());
            result = false;
        }

        if (StringUtils.isBlank(contact.getValue())) {
            context.add(new ValidationMessageBuilder()
                    .source(source)
                    .message("Please set Contact Value.")
                    .build());
            result = false;
        } else if (contact.getValue().trim().length() > ModelConstants.STANDARD_TEXT_LIMIT) {
            context.add(new ValidationMessageBuilder()
                    .source(source)
                    .message("Contact Value can't be greater than "
                            + ModelConstants.STANDARD_TEXT_LIMIT
                            + " symbols in length.")
                    .build());
            result = false;
        }

        return result;
    }

}
