package org.profwell.person.auxiliary;

import org.profwell.generic.auxiliary.GenericFilter;
import org.profwell.person.model.ProfessionGeneralType;
import org.profwell.ui.select.DictionaryConversionUtils;

public class PersonFilter extends GenericFilter {

    private String name;

    private ProfessionGeneralType professionGeneralType;

    private Long workspaceId;

    public String getProfessionValue() {
        if (professionGeneralType == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return String.valueOf(professionGeneralType);
        }
    }

    public void setProfessionValue(String profession) {
        for (ProfessionGeneralType t : ProfessionGeneralType.values()) {
            if (t.getName().equals(profession)) {
                this.professionGeneralType = t;
                return;
            }
        }
        this.professionGeneralType = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public ProfessionGeneralType getProfession() {
        return professionGeneralType;
    }

    public void setProfession(ProfessionGeneralType profession) {
        this.professionGeneralType = profession;
    }

}
