package org.profwell.security.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.profwell.generic.model.Identifiable;

@Entity
@Table(name="WORKSPACE")
@Access(AccessType.FIELD)
public class Workspace implements Identifiable {

    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(
                    name = "property",
                    value = "owner"))
    @GeneratedValue(generator = "generator")
    @Column(name="ID", unique = true, nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User owner;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == DEFAULT_UNINITIALIZED_ID_VALUE;
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

}
