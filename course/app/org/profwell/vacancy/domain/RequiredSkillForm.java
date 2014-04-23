package org.profwell.vacancy.domain;

import org.profwell.vacancy.model.RequiredSkill;

public class RequiredSkillForm implements Comparable<RequiredSkillForm> {

    private String name;

    private int index;

    private boolean mandatory;

    public void transferFrom(RequiredSkill skill) {
        this.name = skill.getName();
        this.index = skill.getIndex();
        this.mandatory = skill.isMandatory();
    }

    public void transferTo(RequiredSkill skill) {
        skill.setName(this.name);
        skill.setIndex(this.index);
        skill.setMandatory(this.mandatory);
    }

    @Override
    public int compareTo(RequiredSkillForm o) {
        return Integer.valueOf(this.index)
                .compareTo(Integer.valueOf(o.index));
    }

    /* SIMPLE SETTERS/GETTERS */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

}
