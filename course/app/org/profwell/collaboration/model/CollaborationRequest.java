package org.profwell.collaboration.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.profwell.generic.model.Identifiable;
import org.profwell.security.model.User;

@Entity
@Table(name="COLLABORATION_REQUEST")
@Access(AccessType.FIELD)
public class CollaborationRequest implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", unique = true, nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID",
            referencedColumnName = "ID",
            nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTNER_ID",
            referencedColumnName = "ID",
            nullable = false)
    private User partner;

    @Enumerated(EnumType.STRING)
    @Column(name="TYPE", nullable = false, length = 20)
    private CollaborationRequestType type;

    @Override
    public boolean isNew() {
        return this.id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public CollaborationRequestType getType() {
        return type;
    }

    public void setType(CollaborationRequestType type) {
        this.type = type;
    }

}
