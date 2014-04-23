package org.profwell.common.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.profwell.generic.model.Marker;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.model.WorkspaceRestricted;
import org.profwell.security.model.Workspace;

@Entity
@Table(name="SKILL")
@Access(AccessType.FIELD)
public class Skill implements WorkspaceRestricted, Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Column(name="NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Workspace workspace;

    @Override
    public String getMarkerValue() {
        return this.name;
    }

    @Override
    public boolean isSystem() {
        // TODO Auto-generated method stub
        return false;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

}
