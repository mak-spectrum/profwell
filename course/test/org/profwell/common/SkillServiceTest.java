package org.profwell.common;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.profwell.common.model.Skill;
import org.profwell.common.service.SkillService;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.security.model.Role;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;
import org.profwell.security.service.UserService;

import play.db.jpa.JPA;
import play.libs.F.Callback0;
import play.test.FakeApplication;

public class SkillServiceTest {

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

        running(app, new TestListSkillSave());
    }

}

class TestListSkillSave implements Runnable {

    Workspace wsp;

    private void given() {
        UserService userService = ServiceHolder
                .getService(UserService.class);
        User user = new User();
        user.setUsername("asdf");
        user.setPassword("asdf");
        user.setRole(Role.ADMINISTRATOR);
        userService.addNewUser(user);
        wsp = userService.getWorkspace(user.getId());

        SkillService skillService = ServiceHolder
                .getService(SkillService.class);


        Skill some = new Skill();
        some.setName("Java");
        some.setWorkspace(wsp);
        skillService.save(some);

        JPA.em().flush();


        List<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("Hibernate");
        skills.add("XML");

        skillService.addUniqueDictionaryValues(skills, wsp);
    }

    private void test() {
        SkillService skillService = ServiceHolder
                .getService(SkillService.class);

        List<Skill> result = skillService.listAll();

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(isContainSkill(result, "Java")).isTrue();
        Assertions.assertThat(isContainSkill(result, "Hibernate")).isTrue();
        Assertions.assertThat(isContainSkill(result, "XML")).isTrue();
    }

    private boolean isContainSkill(List<Skill> list, String skill) {
        for (Skill s : list) {
            if (s.getName().equals(skill)) {
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