package org.profwell.generic.validation;


public interface ValidationMessage {

    public String getSource();

    public ValidationMessageSeverity getSeverity();

    public String getMessage();

}
