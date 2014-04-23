package org.profwell.generic.validation;


public class DefaultValidationMessage implements ValidationMessage {

    private String source;

    private ValidationMessageSeverity severity;

    private String message;

    public DefaultValidationMessage(String source, ValidationMessageSeverity severity, String message) {
        this.source = source;
        this.severity = severity;
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public ValidationMessageSeverity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }
}
