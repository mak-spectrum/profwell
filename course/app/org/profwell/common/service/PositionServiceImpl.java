package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.dao.PositionDAO;
import org.profwell.common.model.Position;
import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.model.Workspace;

public class PositionServiceImpl
        extends GenericServiceImpl<PositionDAO, Position>
        implements PositionService {

    @Override
    public List<Position> listPosition(SingleFieldFilter filter) {
        return dao.listPosition(filter);
    }

    @Override
    public void addUniqueDictionaryValue(String positionName,
            Workspace workspace) {

        if (dao.countWithName(positionName, workspace.getId()) == 0) {
            Position position = new Position();
            position.setName(positionName);
            position.setWorkspace(workspace);
            dao.save(position);
        }

    }
}
