package org.profwell.generic.domain;

import org.profwell.generic.validation.ValidationContext;

public class ValidationForm {

    private ValidationContext vc = new ValidationContext();

    protected boolean isBlockHasValidationMessages(String ... fieldNames) {

        for (String fName : fieldNames) {
            if (vc.existForSource(fName)) {
                return true;
            }
        }

        return false;
    }

    public ValidationContext getVc() {
        return vc;
    }

    public void setVc(ValidationContext vc) {
        this.vc = vc;
    }

}
