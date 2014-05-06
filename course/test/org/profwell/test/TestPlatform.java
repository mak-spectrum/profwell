package org.profwell.test;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import play.test.FakeApplication;

public class TestPlatform {

    public void runTest(TestScenario scenario) {
        Map<String, String> inMem = inMemoryDatabase();
        Map<String, String> appConf = new HashMap<>();
        for (Entry<String, String> e : inMem.entrySet()) {
            appConf.put(e.getKey(), e.getValue());
        }
        appConf.put("application.schedulers.enable", "false");
        appConf.put("db.default.url", "jdbc:h2:mem:trst-db;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS ccmc");

        final FakeApplication app = fakeApplication(appConf);

        running(app, scenario);
    }
}
