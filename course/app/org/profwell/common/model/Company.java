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

import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.model.WorkspaceRestricted;
import org.profwell.security.model.Workspace;

@Entity
@Table(name="COMPANY")
@Access(AccessType.FIELD)
public class Company implements WorkspaceRestricted {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Column(name="NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String name;

    @Column(name="DETAILS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String details;

    @Column(name="SOCIAL_BENEFITS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String socialBenefits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Workspace workspace;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSocialBenefits() {
        return socialBenefits;
    }

    public void setSocialBenefits(String socialBenefits) {
        this.socialBenefits = socialBenefits;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

}
