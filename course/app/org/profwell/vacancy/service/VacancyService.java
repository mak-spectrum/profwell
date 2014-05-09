package org.profwell.vacancy.service;

import java.util.List;

import org.profwell.generic.service.GenericService;
import org.profwell.security.model.Workspace;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancySharingConfiguration;

public interface VacancyService extends GenericService<Vacancy> {

    List<Vacancy> listArchivedVacancies(VacancyFilter filter);

    void saveVacancyWithDictValues(Vacancy object, Workspace workspace);

    boolean checkVacancyAccessible(long vacancyId, long workspaceId);

    Hookup loadHookup(long hookupId, long workspaceId);

    List<HookupDTO> loadHookupsForVacancy(
            long vacancyId, long workspaceId, boolean includeArchived);

    void saveHookupWithDictionaries(Hookup hookup, Workspace workspace);

    void saveHookup(Hookup hookup);

    void deleteHookup(Hookup hookup);

    List<Long> listHookupsOwnersIds(long vacancyId);

    VacancySharingConfiguration getVacancySharingConfiguration(Long id);
}
