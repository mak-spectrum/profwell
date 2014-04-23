package org.profwell.person.service;

import java.util.List;

import org.profwell.generic.service.GenericService;
import org.profwell.person.auxiliary.PersonFilter;
import org.profwell.person.model.Person;

public interface PersonService extends GenericService<Person> {

    List<Person> listPerson(PersonFilter filter);

    List<Person> listPersonByName(String nameFragment, long workspaceId);

    List<Long> checkBelongToWorkspace(List<Long> personIds, long workspaceId);

}
