package org.profwell.vacancy;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.profwell.common.model.City;
import org.profwell.common.model.Country;
import org.profwell.common.model.Skill;
import org.profwell.common.service.CityService;
import org.profwell.common.service.SkillService;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.security.model.Role;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;
import org.profwell.security.service.UserService;
import org.profwell.test.TestPlatform;
import org.profwell.test.TestScenario;
import org.profwell.vacancy.model.Currency;
import org.profwell.vacancy.model.RequiredSkill;
import org.profwell.vacancy.model.SalaryRange;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyCompany;
import org.profwell.vacancy.model.VacancyPosition;
import org.profwell.vacancy.model.VacancyStatus;
import org.profwell.vacancy.service.VacancyService;

import play.db.jpa.JPA;

public class VacancyTest extends TestPlatform {

    @Test
    public void vacancyCRUD() {
        this.runTest(new TestVacancyCRUD());
    }

}

class TestVacancyCRUD extends TestScenario {

    Vacancy vacancy;

    Skill j2ee;
    Skill hibernate;

    City city;

    Workspace wsp;

    @Override
    protected void given() {
        UserService userService = ServiceHolder
                .getService(UserService.class);

        SkillService technologyService = ServiceHolder
                .getService(SkillService.class);

        CityService cityService = ServiceHolder
                .getService(CityService.class);

        VacancyService vacancyService = ServiceHolder
                .getService(VacancyService.class);

        User user = new User();
        user.setUsername("asdf");
        user.setEmail("asd@asd.com");
        user.setPassword("asdf");
        user.setRole(Role.ADMINISTRATOR);

        userService.addNewUser(user);
        this.wsp = userService.getWorkspace(user.getId());

        this.city = new City();
        this.city.setCountry(Country.UA);
        this.city.setName("Kharkov");
        this.city.setWorkspace(this.wsp);
        cityService.save(this.city);

        this.j2ee = new Skill();
        this.j2ee.setName("J2EE");
        this.j2ee.setWorkspace(this.wsp);
        technologyService.save(this.j2ee);

        this.hibernate = new Skill();
        this.hibernate.setName("Hibernate");
        this.hibernate.setWorkspace(this.wsp);
        technologyService.save(this.hibernate);




        this.vacancy = new Vacancy();

        this.vacancy.setCountry(Country.UA);
        this.vacancy.setCity(this.city.getName());

        VacancyCompany company = new VacancyCompany();
        company.setName("TestCompany");
        company.setDetails("MyTest");
        company.setSocialBenefits("Nothing");
        this.vacancy.setCompany(company);

        VacancyPosition position = new VacancyPosition();
        position.setCaption("Middle Java Developer");
        position.setDetails("smart Junior is enought");
        position.setProjectName("Burda");
        position.setProjectDetails("Do nothing");
        this.vacancy.setPosition(position);

        this.vacancy.setOpeningDatetime(new Date());



        RequiredSkill ts1 = new RequiredSkill();
        ts1.setName(this.j2ee.getName());
        ts1.setVacancy(this.vacancy);
        ts1.setMandatory(true);
        RequiredSkill ts2 = new RequiredSkill();
        ts2.setName(this.hibernate.getName());
        ts2.setVacancy(this.vacancy);

        this.vacancy.getPosition().getRequiredSkills().add(ts1);
        this.vacancy.getPosition().getRequiredSkills().add(ts2);

        SalaryRange range = new SalaryRange();
        range.setFrom(1000);
        range.setTill(2000);
        range.setCurrency(Currency.USD);
        this.vacancy.setSalaryRange(range);

        this.vacancy.setStatus(VacancyStatus.OPENED);
        this.vacancy.setWorkspace(this.wsp);

        vacancyService.save(this.vacancy);

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void test() {
        List<Vacancy> list = JPA.em()
                .createNativeQuery("SELECT * FROM VACANCY", Vacancy.class)
                .getResultList();

        Vacancy v = list.get(0);
        assertThat(v).isNotEqualTo(this.vacancy);
        assertThat(v.getId()).isEqualTo(this.vacancy.getId());
        assertThat(v.getCity()).isEqualTo(this.city.getName());
        assertThat(v.getClosingDatetime()).isNull();
        assertThat(v.getCompany().getName()).isEqualTo("TestCompany");
        assertThat(v.getCompany().getDetails()).isEqualTo("MyTest");
        assertThat(v.getCompany().getSocialBenefits()).isEqualTo("Nothing");
        assertThat(v.getCountry()).isEqualTo(Country.UA);
        assertThat(v.getOpeningDatetime().getTime()).isEqualTo(this.vacancy.getOpeningDatetime().getTime());
        assertThat(v.getPosition().getCaption()).isEqualTo("Middle Java Developer");
        assertThat(v.getPosition().getDetails()).isEqualTo("smart Junior is enought");
        assertThat(v.getPosition().getProjectName()).isEqualTo("Burda");
        assertThat(v.getPosition().getProjectDetails()).isEqualTo("Do nothing");
        assertThat(this.isContainTechnology(v, this.j2ee)).isTrue();
        assertThat(this.isContainTechnology(v, this.hibernate)).isTrue();
        assertThat(v.getSalaryRange().getFrom()).isEqualTo(1000);
        assertThat(v.getSalaryRange().getTill()).isEqualTo(2000);
        assertThat(v.getSalaryRange().getCurrency()).isEqualTo(Currency.USD);
        assertThat(v.getStatus()).isEqualTo(VacancyStatus.OPENED);
        assertThat(v.getWorkspace().getId()).isEqualTo(this.wsp.getId());
    }

    private boolean isContainTechnology(Vacancy vacancy, Skill skill) {
        for (RequiredSkill rs : vacancy.getPosition().getRequiredSkills()) {
            if (rs.getName().equals(skill.getName())) {
                return true;
            }
        }
        return false;
    }

}
