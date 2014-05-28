package org.profwell.vacancy.model;

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
@Table(name = "HOOKUP_CONTACT_DATA")
@Access(AccessType.FIELD)
public class HookupPersonContactData implements Identifiable {

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
        return this.id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getContactType() {
        return this.contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

}
