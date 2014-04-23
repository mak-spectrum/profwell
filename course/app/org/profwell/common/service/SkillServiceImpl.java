package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.dao.SkillDAO;
import org.profwell.common.model.Skill;
import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.model.Workspace;

public class SkillServiceImpl
        extends GenericServiceImpl<SkillDAO, Skill>
        implements SkillService {

    @Override
    public List<Skill> listSkill(SingleFieldFilter filter) {
        return dao.listSkill(filter);
    }

    @Override
    public void addUniqueDictionaryValues(List<String> skillNames,
            Workspace workspace) {

        if (!skillNames.isEmpty()) {
            List<String> existing = dao.getExistingNames(skillNames,
                    workspace.getId());
            for (String newSkill : skillNames) {
                if (!existing.contains(newSkill)) {
                    Skill skill = new Skill();
                    skill.setName(newSkill);
                    skill.setWorkspace(workspace);
                    dao.save(skill);
                }
            }
        }

    }
}
