package org.profwell.person.domain;

import org.profwell.generic.model.Identifiable;
import org.profwell.person.model.ContactData;

public class ContactForm {

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String contactType;

    private String value;

    private int index;

    private boolean primary;

    public void transferFrom(ContactData contact) {
        this.id = contact.getId();
        this.contactType = contact.getContactType();
        this.value = contact.getValue();
        this.index = contact.getIndex();
        this.primary = contact.isPrimary();
    }

    public void transferTo(ContactData contact) {
        contact.setId(this.id);
        contact.setContactType(this.contactType);
        contact.setValue(this.value);
        contact.setIndex(this.index);
        contact.setPrimary(this.primary);
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

    public String getIndexValue() {
        return String.valueOf(this.index);
    }

    public void setIndexValue(String indexValue) {
        this.index = Integer.parseInt(indexValue);
    }

    /* SIMPLE SETTERS/GETTERS */

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

}
