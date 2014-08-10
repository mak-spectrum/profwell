package org.profwell.dashboard.domain;

import java.util.ArrayList;
import java.util.List;

import org.profwell.notification.model.Notice;


public class DashboardForm {

    private List<DashboardCategoryDTO> vacancyData;

    private List<Notice> messages = new ArrayList<>();


    public List<DashboardCategoryDTO> getVacancyData() {

        /*vacancyData.clear();

        DashboardVacancyDTO dto;
        DashboardHookupDTO hdto;

        dto = new DashboardVacancyDTO();
        dto.setPositionCaption("Data Analyst");
        dto.setProjectName("CyberIQ");
        dto.setCompanyName("AgileEngine");
        dto.setOpeningDate(new Date());
        dto.setPriority(VacancyPriority.URGENT);
        this.vacancyData.add(dto);

        dto = new DashboardVacancyDTO();
        dto.setPositionCaption("Junior Quality Engineer");
        dto.setProjectName("entellitrak");
        dto.setCompanyName("AgileEngine");
        dto.setOpeningDate(new Date());
        dto.setPriority(VacancyPriority.HIGH);
        this.vacancyData.add(dto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("- No company -");
        hdto.setCurrentPosition("- No position -");
        hdto.setPersonFirstName("Anna");
        hdto.setPersonSecondName("Sergeevna");
        hdto.setPersonLastName("Makivskaya");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.CONTACTED);
        dto.getHookups().add(hdto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("Agroprom");
        hdto.setCurrentPosition("Vice president");
        hdto.setPersonFirstName("Igor");
        hdto.setPersonSecondName("Igorevich");
        hdto.setPersonLastName("Igorev");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.CONTACTED);
        dto.getHookups().add(hdto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("Global Logic");
        hdto.setCurrentPosition("Junior QA");
        hdto.setPersonFirstName("Ivaonv");
        hdto.setPersonSecondName("Pertrovich");
        hdto.setPersonLastName("Sidorov");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.CANDIDATE_REFUSED);
        dto.getHookups().add(hdto);

        dto = new DashboardVacancyDTO();
        dto.setPositionCaption("Junior Java Developer");
        dto.setProjectName("entellitrak");
        dto.setCompanyName("AgileEngine");
        dto.setOpeningDate(new Date());
        dto.setPriority(VacancyPriority.LOW);
        this.vacancyData.add(dto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("Profitsoft");
        hdto.setCurrentPosition("Team Leader");
        hdto.setPersonFirstName("Vladimir");
        hdto.setPersonSecondName("Alexandrovich");
        hdto.setPersonLastName("Sugrobov");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.CANDIDATE_REFUSED);
        dto.getHookups().add(hdto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("Team International");
        hdto.setCurrentPosition("Team Leader");
        hdto.setPersonFirstName("Eugene");
        hdto.setPersonSecondName("Vladimirovich");
        hdto.setPersonLastName("Kostin");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.CONTACTED);
        dto.getHookups().add(hdto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("EPAM Systems");
        hdto.setCurrentPosition("Middle Java Developer");
        hdto.setPersonFirstName("Oleg");
        hdto.setPersonSecondName("Anatolievich");
        hdto.setPersonLastName("Semenov");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.GOTTEN_RESUME);
        dto.getHookups().add(hdto);

        hdto = new DashboardHookupDTO();
        hdto.setCurrentCompany("EPAM Systems");
        hdto.setCurrentPosition("Middle Java Developer");
        hdto.setPersonFirstName("Maksim");
        hdto.setPersonSecondName("Sergeevich");
        hdto.setPersonLastName("Zaitsev");
        hdto.setLastUpdateDate(new Date());
        hdto.setStatus(HookupStatus.CONTACTED);
        dto.getHookups().add(hdto);

        dto = new DashboardVacancyDTO();
        dto.setPositionCaption("Middle Java Developer");
        dto.setProjectName("Telenor");
        dto.setCompanyName("Team International");
        dto.setOpeningDate(new Date());
        dto.setPriority(VacancyPriority.BACKGROUND);
        this.vacancyData.add(dto);
        vacancyData.add(dto);*/


        return this.vacancyData;
    }

    public void setVacancyData(List<DashboardCategoryDTO> vacancyData) {
        this.vacancyData = vacancyData;
    }

	public List<Notice> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Notice> messages) {
		this.messages = messages;
	}

}
