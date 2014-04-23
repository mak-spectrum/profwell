package org.profwell.person.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;

@Entity
@Table(name = "CONTACT_DATA")
@Access(AccessType.FIELD)
public class ContactData implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Column(name = "CONTACT_TYPE", nullable = true, length = 16)
    private String contactType;

    @Column(name = "VALUE", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String value;

    @Column(name = "CONTACT_INDEX", insertable = false, updatable = false)
    private int index;

    @Column(name = "PRIMARY_CONTACT", nullable = false)
    private boolean primary;

    @Override
    public boolean isNew() {
        return id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

}
