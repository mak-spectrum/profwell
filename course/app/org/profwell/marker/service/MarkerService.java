package org.profwell.marker.service;

import java.util.List;

import org.profwell.generic.service.GenericService;
import org.profwell.marker.model.ContactMarker;
import org.profwell.marker.model.MarkerType;

public interface MarkerService extends GenericService<ContactMarker> {

    List<ContactMarker> listMarker(MarkerType type, String snippet);

}
