package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Skill;
import org.profwell.generic.service.GenericService;
import org.profwell.security.model.Workspace;

public interface SkillService extends GenericService<Skill> {

    List<Skill> listSkill(SingleFieldFilter filter);

    void addUniqueDictionaryValues(List<String> skillNames,
            Workspace workspace);
}
