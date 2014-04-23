package org.profwell.person.dao;

import java.util.List;

import org.profwell.generic.dao.GenericDAO;
import org.profwell.person.auxiliary.PersonFilter;
import org.profwell.person.model.Person;

public interface PersonDAO extends GenericDAO<Person> {

    List<Person> listPerson(PersonFilter filter);

    List<Person> listPersonByName(String nameFragment, long workspaceId);

    List<Long> checkBelongToWorkspace(List<Long> personIds, long workspaceId);

}
