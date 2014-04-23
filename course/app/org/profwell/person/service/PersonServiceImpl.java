package org.profwell.person.service;

import java.util.List;

import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.person.auxiliary.PersonFilter;
import org.profwell.person.dao.PersonDAO;
import org.profwell.person.model.Person;

public class PersonServiceImpl extends GenericServiceImpl<PersonDAO, Person>
        implements PersonService {

    @Override
    public Person get(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Person> listPerson(PersonFilter filter) {
        return this.dao.listPerson(filter);
    }

    @Override
    public List<Person> listPersonByName(
            String nameFragment, long workspaceId) {
        return dao.listPersonByName(nameFragment, workspaceId);
    }

    @Override
    public List<Long> checkBelongToWorkspace(List<Long> personIds,
            long workspaceId) {
        return dao.checkBelongToWorkspace(personIds, workspaceId);
    }
}
