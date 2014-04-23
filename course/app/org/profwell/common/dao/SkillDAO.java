package org.profwell.common.dao;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Skill;
import org.profwell.generic.dao.GenericDAO;

public interface SkillDAO extends GenericDAO<Skill> {

    List<Skill> listSkill(SingleFieldFilter filter);

    List<String> getExistingNames(List<String> skillNames, long workspaceId);
}
