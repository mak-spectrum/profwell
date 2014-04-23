package org.profwell.vacancy.dao;

import java.util.List;

import org.profwell.generic.dao.GenericDAO;
import org.profwell.vacancy.auxiliary.VacancyArchiveFilter;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.Vacancy;

public interface VacancyDAO extends GenericDAO<Vacancy> {

    List<Vacancy> listArchivedVacancies(VacancyArchiveFilter filter);

    Hookup loadHookup(long hookupId, long workspaceId);

    void saveHookup(Hookup hookup);

    boolean checkVacancyAccessible(long vacancyId, long workspaceId);

    List<HookupDTO> loadHookupsForVacancy(long vacancyId, long workspaceId,
            boolean includeArchived);

    void deleteHookup(Hookup hookup);
}
