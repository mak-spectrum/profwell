package org.profwell.marker.dao;

import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.marker.model.ContactMarker;

public class MarkerDAO extends GenericDAOImpl<ContactMarker> {

    @Override
    protected Class<ContactMarker> getEntityClass() {
        return ContactMarker.class;
    }

}
