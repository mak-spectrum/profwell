package org.profwell.vacancy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.profwell.common.service.CityService;
import org.profwell.common.service.CompanyService;
import org.profwell.common.service.PositionService;
import org.profwell.common.service.SkillService;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.model.Workspace;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.dao.VacancyDAO;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.model.RequiredSkill;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancySharingConfiguration;
import org.profwell.vacancy.model.VacancyStatus;

public class VacancyServiceImpl extends GenericServiceImpl<VacancyDAO, Vacancy>
        implements VacancyService {

    @Override
    public void saveVacancyWithDictValues(Vacancy object, Workspace workspace) {

        if (object.isNew()) {
            object.setOpeningDatetime(new Date());
            object.setStatus(VacancyStatus.OPENED);
            object.setWorkspace(workspace);
        }

        if (StringUtils.isNotBlank(object.getPosition().getCaption())) {
            PositionService service = ServiceHolder
                    .getService(PositionService.class);
            service.addUniqueDictionaryValue(
                    object.getPosition().getCaption(),
                    workspace);
        }

        if (StringUtils.isNotBlank(object.getCompany().getName())) {
            CompanyService service = ServiceHolder
                    .getService(CompanyService.class);
            service.addUniqueDictionaryValue(
                    object.getCompany().getName(),
                    object.getCompany().getDetails(),
                    object.getCompany().getSocialBenefits(),
                    workspace);
        }

        List<String> skillNames = new ArrayList<>();
        for (RequiredSkill skill : object.getPosition().getRequiredSkills()) {
            skillNames.add(skill.getName());
        }

        SkillService skillService = ServiceHolder
                .getService(SkillService.class);
        skillService.addUniqueDictionaryValues(skillNames, workspace);

        if (StringUtils.isNotBlank(object.getCompany().getName())) {
            CompanyService service = ServiceHolder
                    .getService(CompanyService.class);
            service.addUniqueDictionaryValue(
                    object.getCompany().getName(),
                    object.getCompany().getDetails(),
                    object.getCompany().getSocialBenefits(),
                    workspace);
        }

        if (StringUtils.isNotBlank(object.getCity())
                && object.getCountry() != null) {

            CityService service = ServiceHolder.getService(CityService.class);
            service.addUniqueDictionaryValue(
                    object.getCountry(),
                    object.getCity(),
                    workspace);
        }

        this.dao.save(object);
    }

    @Override
    public Hookup loadHookup(long hookupId, long workspaceId) {
        return this.dao.loadHookup(hookupId, workspaceId);
    }

    @Override
    public List<HookupDTO> loadHookupsForVacancy(
            long vacancyId, long workspaceId, boolean includeArchived) {

        return this.dao.loadHookupsForVacancy(vacancyId, workspaceId,
                includeArchived);
    }

    @Override
    public void saveHookupWithDictionaries(Hookup hookup, Workspace workspace) {

        if (hookup.isNew()) {
            hookup.setStatus(HookupStatus.CONTACTED);
            hookup.setWorkspace(workspace);
        }


        if (StringUtils.isNotBlank(hookup.getCandidateCurrentPosition())) {
            PositionService service = ServiceHolder
                    .getService(PositionService.class);
            service.addUniqueDictionaryValue(
                    hookup.getCandidateCurrentPosition(),
                    workspace);
        }

        if (StringUtils.isNotBlank(hookup.getCandidateCurrentCompany())) {
            CompanyService service = ServiceHolder
                    .getService(CompanyService.class);
            service.addUniqueDictionaryValue(
                    hookup.getCandidateCurrentCompany(),
                    "",
                    "",
                    workspace);
        }

        this.saveHookup(hookup);
    }

    @Override
    public void saveHookup(Hookup hookup) {
        hookup.setLastActivityOn(new Date());
        if (hookup.getStatus() == HookupStatus.GOTTEN_RESUME) {
            hookup.setPassedTestTaskStatus(false);
        }
        if (hookup.getStatus() == HookupStatus.SENT_TEST_TASK) {
            hookup.setPassedTestTaskStatus(true);
        }
        this.dao.saveHookup(hookup);
    }

    @Override
    public List<Vacancy> listArchivedVacancies(VacancyFilter filter) {
        return this.dao.listVacancies(filter);
    }

    @Override
    public boolean checkVacancyAccessible(long vacancyId, long workspaceId) {
        return this.dao.checkVacancyAccessible(vacancyId, workspaceId);
    }

    @Override
    public void deleteHookup(Hookup hookup) {
        this.dao.deleteHookup(hookup);
    }

    @Override
    public List<Long> listHookupsOwnersIds(long vacancyId) {
        return this.dao.listHookupsOwnersIds(vacancyId);
    }

    @Override
    public VacancySharingConfiguration getVacancySharingConfiguration(Long id) {
        return this.dao.getVacancySharingConfiguration(id);
    }

}
