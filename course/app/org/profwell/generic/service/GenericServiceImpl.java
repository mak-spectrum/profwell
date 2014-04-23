package org.profwell.generic.service;

import java.util.List;

import org.profwell.generic.dao.GenericDAO;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.WorkspaceRestricted;

public class GenericServiceImpl<D extends GenericDAO<T>, T extends Identifiable>
        implements GenericService<T> {

    protected D dao;

    @Override
    public long countAll() {
        return dao.countAll();
    }

    @Override
    public List<T> listAll() {
        return dao.listAll();
    }

    @Override
    public T get(long id) {
        return dao.get(id);
    }

    public <WR extends Identifiable & WorkspaceRestricted> WR getFromWorkspace(
            long id, long workspaceId) {
        return dao.getFromWorkspace(id, workspaceId);
    }

    @Override
    public void save(T object) {
        dao.save(object);
    }

    @Override
    public void delete(T object) {
        dao.delete(object);
    }

    public void setDao(D dao) {
        this.dao = dao;
    }

}
