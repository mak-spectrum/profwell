package org.profwell.generic.exception;

public class ObjectNotFoundException extends RuntimeException {

    /**
     * Generated UID.
     */
    private static final long serialVersionUID = 3118714242548271712L;

    private final String objectName;

    public ObjectNotFoundException(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

}
