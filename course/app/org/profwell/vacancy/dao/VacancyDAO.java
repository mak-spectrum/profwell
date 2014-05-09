package org.profwell.vacancy.dao;

import java.util.List;

import org.profwell.generic.dao.GenericDAO;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancySharingConfiguration;

public interface VacancyDAO extends GenericDAO<Vacancy> {

    List<Vacancy> listVacancies(VacancyFilter filter);

    Hookup loadHookup(long hookupId, long workspaceId);

    void saveHookup(Hookup hookup);

    boolean checkVacancyAccessible(long vacancyId, long workspaceId);

    List<HookupDTO> loadHookupsForVacancy(long vacancyId, long workspaceId,
            boolean includeArchived);

    void deleteHookup(Hookup hookup);

    List<Long> listHookupsOwnersIds(long vacancyId);

    VacancySharingConfiguration getVacancySharingConfiguration(Long id);

}
