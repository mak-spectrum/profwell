package utils;

import org.profwell.vacancy.domain.VacancyViewForm;

public final class VacancyUtils {

    private VacancyUtils() {
        // Nothing here
    }

    public static String getSalaryValue(VacancyViewForm form) {
        String salaryFrom = "";
        String salaryTill = "";
        String salaryCurrency = "";

        if (form.getSalaryFrom() != null && form.getSalaryTill() == null) {
            salaryFrom = "from " + form.getSalaryFromValue();
        } else if (form.getSalaryFrom() == null
                && form.getSalaryTill() != null) {
            salaryTill = " till " + form.getSalaryTillValue();
        } else if (form.getSalaryFrom() != null
                && form.getSalaryTill() != null) {
            salaryFrom = form.getSalaryFromValue()
                    + " - "
                    + form.getSalaryTillValue();
        }

        if (form.getSalaryFrom() != null || form.getSalaryTill() != null) {
            salaryCurrency = " " + form.getSalaryCurrency().getCaption();
        }

        return new StringBuilder()
                .append(salaryFrom)
                .append(salaryTill)
                .append(salaryCurrency)
                .toString();
    }

}
