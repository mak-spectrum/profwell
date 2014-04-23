package org.profwell.vacancy.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.profwell.generic.model.ModelConstants;

@Embeddable
@Access(AccessType.FIELD)
public class VacancyPosition {

    @Column(name="POSITION", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String caption;

    @Column(name="POSITION_DETAILS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String details;

    @Column(name="PROJECT_NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String projectName;

    @Column(name="PROJECT_DETAILS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String projectDetails;

    @OneToMany(
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            targetEntity = RequiredSkill.class)
    private Set<RequiredSkill> requiredSkills = new HashSet<>();

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public Set<RequiredSkill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<RequiredSkill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

}
