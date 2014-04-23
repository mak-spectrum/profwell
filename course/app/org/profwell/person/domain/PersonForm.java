package org.profwell.person.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.validation.ValidationMessage;
import org.profwell.person.model.ContactData;
import org.profwell.person.model.Person;
import org.profwell.person.model.Profession;
import org.profwell.person.model.ProfessionGeneralType;
import org.profwell.ui.select.DictionaryConversionUtils;


public class PersonForm extends ValidationForm {

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String firstName;

    private String secondName;

    private String lastName;

    private String shortNote;

    private ProfessionGeneralType professionGeneralType;

    private String professionDetails;

    private List<ContactForm> contacts = new LinkedList<>();

    public void transferFrom(Person person) {

        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.secondName = person.getSecondName();
        this.lastName = person.getLastName();
        this.shortNote = person.getShortNote();

        this.professionGeneralType = person.getProfession().getGeneralType();
        this.professionDetails = person.getProfession().getDetails();

        this.contacts.clear();
        for (ContactData data : person.getContactDataRecords()) {
            ContactForm contact = new ContactForm();
            contact.transferFrom(data);
            this.contacts.add(contact);
        }

    }

    public void transferTo(Person person) {

        person.setFirstName(this.firstName);
        person.setSecondName(this.secondName);
        person.setLastName(this.lastName);
        person.setShortNote(this.shortNote);

        if (person.getProfession() == null) {
            person.setProfession(new Profession());
        }
        person.getProfession().setGeneralType(this.professionGeneralType);
        person.getProfession().setDetails(this.professionDetails);

        this.clearLostPersonContacts(person);

        List<ContactData> newContactRecords = new ArrayList<>();

        for (ContactForm contactDto : this.contacts) {
            ContactData data = this.findContactById(person, contactDto.getId());
            if (data == null) {
                data = new ContactData();
                contactDto.transferTo(data);
                newContactRecords.add(data);
            } else {
                contactDto.transferTo(data);
            }
        }

        person.getContactDataRecords().addAll(newContactRecords);
    }

    public boolean isGeneralSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return true;
        } else {
            return isBlockHasValidationMessages(
                    "firstName", "secondName", "lastName");
        }
    }

    public boolean isProfessionSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return false;
        } else {
            return isBlockHasValidationMessages(
                    "generalProfessionValue", "details");
        }
    }

    public boolean isContactSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return false;
        } else {
            for (ValidationMessage vm : getVc().getAllMessages()) {
                if (vm.getSource().startsWith("contact:")) {
                    return true;
                }
            }

            return false;
        }
    }

    private ContactData findContactById(Person person, long id) {
        Iterator<ContactData> it = person.getContactDataRecords().iterator();

        ContactData data;
        while (it.hasNext()) {
            data = it.next();
            if (data.getId() == id) {
                return data;
            }
        }

        return null;
    }

    private ContactForm findContactDTOById(long id) {
        Iterator<ContactForm> it = this.contacts.iterator();

        ContactForm dto;
        while (it.hasNext()) {
            dto = it.next();
            if (dto.getId() == id) {
                return dto;
            }
        }

        return null;
    }

    private void clearLostPersonContacts(Person person) {
        Iterator<ContactData> it = person.getContactDataRecords().iterator();

        while (it.hasNext()) {
            if (this.findContactDTOById(it.next().getId()) == null) {
                it.remove();
            }
        }
    }

    public String getGeneralProfessionValue() {
        if (this.professionGeneralType == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return this.professionGeneralType.getName();
        }
    }

    public void setGeneralProfessionValue(String professionGeneralType) {
        for (ProfessionGeneralType t : ProfessionGeneralType.values()) {
            if (t.getName().equals(professionGeneralType)) {
                this.professionGeneralType = t;
                return;
            }
        }
        this.professionGeneralType = null;
    }

    public boolean isNew() {
        return this.id == Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public String getIdValue() {
        return String.valueOf(this.id);
    }

    public void setIdValue(String idvalue) {
        this.id = Long.parseLong(idvalue);
    }

    /* SIMPLE SETTERS/GETTERS */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfessionDetails() {
        return professionDetails;
    }

    public void setProfessionDetails(String professionDetails) {
        this.professionDetails = professionDetails;
    }

    public List<ContactForm> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactForm> contacts) {
        this.contacts = contacts;
    }

    public ProfessionGeneralType getProfessionGeneralType() {
        return professionGeneralType;
    }

    public void setProfessionGeneralType(ProfessionGeneralType professionGeneralType) {
        this.professionGeneralType = professionGeneralType;
    }

    public String getShortNote() {
        return shortNote;
    }

    public void setShortNote(String shortNote) {
        this.shortNote = shortNote;
    }

}
