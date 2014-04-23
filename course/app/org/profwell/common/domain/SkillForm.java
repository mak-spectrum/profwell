package org.profwell.common.domain;

import org.profwell.common.model.Skill;
import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;

public class SkillForm extends ValidationForm {

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String name;

    public void transferFrom(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
    }

    public void transferTo(Skill skill) {
        skill.setName(this.name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
