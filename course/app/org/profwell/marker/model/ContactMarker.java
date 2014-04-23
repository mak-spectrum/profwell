package org.profwell.marker.model;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.Marker;


public class ContactMarker implements Identifiable, Marker {

    private MarkerType type;

    private String value;

    private boolean system;

    public ContactMarker() {}

    public static ContactMarker createSystemContact(String value) {
        ContactMarker m = new ContactMarker();

        m.type = MarkerType.CONTACT_TYPE_MARKER;
        m.value = value;
        m.system = true;

        return m;
    }

    @Override
    public String getMarkerValue() {
        return this.value;
    }

    @Override
    public long getId() {
        return DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MarkerType getType() {
        return type;
    }

    public void setType(MarkerType type) {
        this.type = type;
    }

    @Override
    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }
}