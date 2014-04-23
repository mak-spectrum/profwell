package org.profwell.marker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.marker.dao.MarkerDAO;
import org.profwell.marker.model.ContactMarker;
import org.profwell.marker.model.MarkerType;

public class MarkerServiceImpl extends GenericServiceImpl<MarkerDAO, ContactMarker>
        implements MarkerService {

    private static Map<MarkerType, List<ContactMarker>> systemMarkers = new HashMap<>();

    public MarkerServiceImpl() {
        List<ContactMarker> contactMarker = new ArrayList<>();
        contactMarker.add(ContactMarker.createSystemContact("Skype"));
        contactMarker.add(ContactMarker.createSystemContact("Email"));
        contactMarker.add(ContactMarker.createSystemContact("Phone"));

        systemMarkers.put(MarkerType.CONTACT_TYPE_MARKER, contactMarker);

    }

    @Override
    public List<ContactMarker> listMarker(MarkerType type, String snippet) {
        List<ContactMarker> result = new ArrayList<>();
        List<ContactMarker> markers = systemMarkers.get(MarkerType.CONTACT_TYPE_MARKER);

        String token = snippet.trim().toLowerCase();

        for (ContactMarker m : markers) {
            if (m.getValue().toLowerCase().startsWith(token)) {
                result.add(m);
            }
        }

        return result;
    }

}
