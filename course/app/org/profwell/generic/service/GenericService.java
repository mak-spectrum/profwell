package org.profwell.generic.service;

import java.util.List;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.WorkspaceRestricted;

public interface GenericService<T extends Identifiable> {

    long countAll();

    List<T> listAll();

    T get(long id);

    <WR extends Identifiable & WorkspaceRestricted> WR getFromWorkspace(long id,
            long workspaceId);

    void save(T object);

    void delete(T object);

}
