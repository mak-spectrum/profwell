package org.profwell.generic.validation;

public class ValidationMessageBuilder {

    private String source = "";

    private ValidationMessageSeverity severity = ValidationMessageSeverity.ERROR;

    private String message = "";

    public ValidationMessageBuilder source(String source) {
        this.source = source;
        return this;
    }

    public ValidationMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ValidationMessageBuilder error() {
        this.severity = ValidationMessageSeverity.ERROR;
        return this;
    }

    public ValidationMessageBuilder warning() {
        this.severity = ValidationMessageSeverity.WARNING;
        return this;
    }

    public ValidationMessageBuilder info() {
        this.severity = ValidationMessageSeverity.INFO;
        return this;
    }

    public ValidationMessage build() {
        return new DefaultValidationMessage(this.source, this.severity, this.message);
    }

}
