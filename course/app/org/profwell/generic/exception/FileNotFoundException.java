package org.profwell.generic.exception;

public class FileNotFoundException extends RuntimeException {

    /**
     * Generated UID.
     */
    private static final long serialVersionUID = 2209805153639362623L;

    private final String message;

    public FileNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
