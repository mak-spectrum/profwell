package org.profwell.vacancy;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.profwell.vacancy.model.Currency;
import org.profwell.vacancy.model.RequiredSkill;
import org.profwell.vacancy.model.SalaryRange;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyCompany;
import org.profwell.vacancy.model.VacancyPosition;
import org.profwell.vacancy.model.VacancyStatus;
import org.profwell.vacancy.service.VacancyService;

import play.db.jpa.JPA;
import play.libs.F.Callback0;
import play.test.FakeApplication;

public class VacancyTest {

    @Test
    public void test() {
        Map<String, String> inMem = inMemoryDatabase();
        Map<String, String> appConf = new HashMap<>();
        for (Entry<String, String> e : inMem.entrySet()) {
            appConf.put(e.getKey(), e.getValue());
        }
        appConf.put("application.schedulers.enable", "false");
        appConf.put("db.default.url", "jdbc:h2:mem:trst-db;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS ccmc");

        final FakeApplication app = fakeApplication(appConf);

        running(app, new TestVacancyCRUD());
    }

}

class TestVacancyCRUD implements Runnable {

    Vacancy vacancy;

    Skill j2ee;
    Skill hibernate;

    City city;

    Workspace wsp;

    private void given() {
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
        user.setPassword("asdf");
        user.setRole(Role.ADMINISTRATOR);

        userService.addNewUser(user);
        wsp = userService.getWorkspace(user.getId());

        city = new City();
        city.setCountry(Country.UA);
        city.setName("Kharkov");
        cityService.save(city);

        j2ee = new Skill();
        j2ee.setName("J2EE");
        technologyService.save(j2ee);

        hibernate = new Skill();
        hibernate.setName("Hibernate");
        technologyService.save(hibernate);




        vacancy = new Vacancy();

        vacancy.setCountry(Country.UA);
        vacancy.setCity(city.getName());

        VacancyCompany company = new VacancyCompany();
        company.setName("TestCompany");
        company.setDetails("MyTest");
        company.setSocialBenefits("Nothing");
        vacancy.setCompany(company);

        VacancyPosition position = new VacancyPosition();
        position.setCaption("Middle Java Developer");
        position.setDetails("smart Junior is enought");
        position.setProjectName("Burda");
        position.setProjectDetails("Do nothing");
        vacancy.setPosition(position);

        vacancy.setOpeningDatetime(new Date());



        RequiredSkill ts1 = new RequiredSkill();
        ts1.setName(j2ee.getName());
        ts1.setVacancy(vacancy);
        ts1.setMandatory(true);
        RequiredSkill ts2 = new RequiredSkill();
        ts2.setName(hibernate.getName());
        ts2.setVacancy(vacancy);

        vacancy.getPosition().getRequiredSkills().add(ts1);
        vacancy.getPosition().getRequiredSkills().add(ts2);

        SalaryRange range = new SalaryRange();
        range.setFrom(1000);
        range.setTill(2000);
        range.setCurrency(Currency.USD);
        vacancy.setSalaryRange(range);

        vacancy.setStatus(VacancyStatus.OPENED);
        vacancy.setWorkspace(wsp);

        vacancyService.save(vacancy);

    }

    @SuppressWarnings("unchecked")
    private void test() {
        List<Vacancy> list = JPA.em()
                .createNativeQuery("SELECT * FROM VACANCY", Vacancy.class)
                .getResultList();

        Vacancy v = list.get(0);
        assertThat(v).isNotEqualTo(vacancy);
        assertThat(v.getId()).isEqualTo(vacancy.getId());
        assertThat(v.getCity()).isEqualTo(city.getName());
        assertThat(v.getClosingDatetime()).isNull();
        assertThat(v.getCompany().getName()).isEqualTo("TestCompany");
        assertThat(v.getCompany().getDetails()).isEqualTo("MyTest");
        assertThat(v.getCompany().getSocialBenefits()).isEqualTo("Nothing");
        assertThat(v.getCountry()).isEqualTo(Country.UA);
        assertThat(v.getOpeningDatetime().getTime()).isEqualTo(vacancy.getOpeningDatetime().getTime());
        assertThat(v.getPosition().getCaption()).isEqualTo("Middle Java Developer");
        assertThat(v.getPosition().getDetails()).isEqualTo("smart Junior is enought");
        assertThat(v.getPosition().getProjectName()).isEqualTo("Burda");
        assertThat(v.getPosition().getProjectDetails()).isEqualTo("Do nothing");
        assertThat(isContainTechnology(v, j2ee)).isTrue();
        assertThat(isContainTechnology(v, hibernate)).isTrue();
        assertThat(v.getSalaryRange().getFrom()).isEqualTo(1000);
        assertThat(v.getSalaryRange().getTill()).isEqualTo(2000);
        assertThat(v.getSalaryRange().getCurrency()).isEqualTo(Currency.USD);
        assertThat(v.getStatus()).isEqualTo(VacancyStatus.OPENED);
        assertThat(v.getWorkspace().getId()).isEqualTo(wsp.getId());
    }

    private boolean isContainTechnology(Vacancy vacancy, Skill skill) {
        for (RequiredSkill rs : vacancy.getPosition().getRequiredSkills()) {
            if (rs.getName().equals(skill.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        JPA.withTransaction(new Callback0() {
            @Override
            public void invoke() throws Throwable {
                given();
            }
        });
        JPA.withTransaction(new Callback0() {
            @Override
            public void invoke() throws Throwable {
                test();
            }
        });
    }

}
