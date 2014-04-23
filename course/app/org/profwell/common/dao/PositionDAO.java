package org.profwell.common.dao;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Position;
import org.profwell.generic.dao.GenericDAO;

public interface PositionDAO extends GenericDAO<Position> {

    List<Position> listPosition(SingleFieldFilter filter);

    long countWithName(String name, long worksoaceId);

}
