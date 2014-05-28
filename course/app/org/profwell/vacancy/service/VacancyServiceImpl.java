package org.profwell.vacancy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.profwell.collaboration.domain.PartnerDTO;
import org.profwell.collaboration.model.ConnectionType;
import org.profwell.collaboration.service.CollaborationService;
import org.profwell.common.service.CityService;
import org.profwell.common.service.CompanyService;
import org.profwell.common.service.PositionService;
import org.profwell.common.service.SkillService;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.model.Workspace;
import org.profwell.security.service.UserService;
import org.profwell.security.web.SessionUtility;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.dao.VacancyDAO;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.model.RequiredSkill;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancySharingConfiguration;
import org.profwell.vacancy.model.VacancySharingRecord;
import org.profwell.vacancy.model.VacancyStatus;

public class VacancyServiceImpl extends GenericServiceImpl<VacancyDAO, Vacancy>
        implements VacancyService {

    @Override
    public void saveVacancyWithDictValues(Vacancy vacancy, Workspace workspace) {

        if (vacancy.isNew()) {
            vacancy.setOpeningDatetime(new Date());
            vacancy.setStatus(VacancyStatus.OPENED);
            vacancy.setWorkspace(workspace);

            if (StringUtils.isNotBlank(vacancy.getPosition().getCaption())) {
                PositionService service = ServiceHolder
                        .getService(PositionService.class);
                service.addUniqueDictionaryValue(
                        vacancy.getPosition().getCaption(),
                        workspace);
            }

            if (StringUtils.isNotBlank(vacancy.getCompany().getName())) {
                CompanyService service = ServiceHolder
                        .getService(CompanyService.class);
                service.addUniqueDictionaryValue(
                        vacancy.getCompany().getName(),
                        vacancy.getCompany().getDetails(),
                        vacancy.getCompany().getSocialBenefits(),
                        workspace);
            }

            List<String> skillNames = new ArrayList<>();
            for (RequiredSkill skill : vacancy.getPosition().getRequiredSkills()) {
                skillNames.add(skill.getName());
            }

            SkillService skillService = ServiceHolder
                    .getService(SkillService.class);
            skillService.addUniqueDictionaryValues(skillNames, workspace);

            if (StringUtils.isNotBlank(vacancy.getCompany().getName())) {
                CompanyService service = ServiceHolder
                        .getService(CompanyService.class);
                service.addUniqueDictionaryValue(
                        vacancy.getCompany().getName(),
                        vacancy.getCompany().getDetails(),
                        vacancy.getCompany().getSocialBenefits(),
                        workspace);
            }

            if (StringUtils.isNotBlank(vacancy.getCity())
                    && vacancy.getCountry() != null) {

                CityService service = ServiceHolder.getService(
                        CityService.class);
                service.addUniqueDictionaryValue(
                        vacancy.getCountry(),
                        vacancy.getCity(),
                        workspace);
            }

            VacancySharingConfiguration configuration = new VacancySharingConfiguration();
            configuration.setVacancy(vacancy);
            vacancy.setSharingConfiguration(configuration);

            this.shareVacancyByDefault(vacancy);
        }

        this.dao.save(vacancy);
    }

    private void shareVacancyByDefault(Vacancy vacancy) {
        List<PartnerDTO> partners = ServiceHolder.getService(CollaborationService.class)
                .getMyPartners(SessionUtility.getCurrentUserId());

        for (PartnerDTO p : partners) {
            if (p.isMy() && p.getType() == ConnectionType.STAFF_RECRUITER) {
                VacancySharingRecord rec = new VacancySharingRecord();
                rec.setWorkspace(vacancy.getWorkspace());
                rec.setPartner(ServiceHolder.getService(UserService.class)
                        .get(p.getPartnerId()));

                vacancy.getSharingConfiguration().getRecords().add(rec);
            }
        }

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

            if (StringUtils.isNotBlank(hookup.getCandidate().getCurrentPosition())) {
                PositionService service = ServiceHolder
                        .getService(PositionService.class);
                service.addUniqueDictionaryValue(
                        hookup.getCandidate().getCurrentPosition(),
                        workspace);
            }

            if (StringUtils.isNotBlank(hookup.getCandidate().getCurrentCompany())) {
                CompanyService service = ServiceHolder
                        .getService(CompanyService.class);
                service.addUniqueDictionaryValue(
                        hookup.getCandidate().getCurrentCompany(),
                        "",
                        "",
                        workspace);
            }

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
