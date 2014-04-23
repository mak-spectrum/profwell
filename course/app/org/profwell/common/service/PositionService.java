package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Position;
import org.profwell.generic.service.GenericService;
import org.profwell.security.model.Workspace;

public interface PositionService extends GenericService<Position> {

    List<Position> listPosition(SingleFieldFilter filter);

    void addUniqueDictionaryValue(String positionName, Workspace workspace);

}
