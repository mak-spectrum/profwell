package org.profwell.dashboard.domain;

import java.util.List;

public class DashboardCategoryDTO {

    public DashboardCategoryDTO(String caption, String style) {
        this.caption = caption;
        this.style = style;
    }

    private String style;

    private String caption;

    private List<DashboardVacancyDTO> vacancies;

    public boolean isNotEmpty() {
        return !this.vacancies.isEmpty();
    }

    public List<DashboardVacancyDTO> getVacancies() {
        return this.vacancies;
    }

    public void setVacancies(List<DashboardVacancyDTO> vacancies) {
        this.vacancies = vacancies;
    }

    public String getStyle() {
        return this.style;
    }

    public String getCaption() {
        return this.caption;
    }

}
