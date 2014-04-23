package org.profwell.file.service;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.fest.assertions.Assertions;
import org.junit.Test;

import play.test.FakeApplication;

public class FileServiceImplTest {

    @Test
    public void test() {
        Map<String, String> inMem = inMemoryDatabase();
        Map<String, String> appConf = new HashMap<>();
        for (Entry<String, String> e : inMem.entrySet()) {
            appConf.put(e.getKey(), e.getValue());
        }
        appConf.put("application.schedulers.enable", "false");
        appConf.put("application.file.storage", "d:/temp/");
        appConf.put("db.default.url", "jdbc:h2:mem:trst-db;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS ccmc");

        final FakeApplication app = fakeApplication(appConf);

        running(app, new TestMimeTypeClearing());
    }

}

class TestMimeTypeClearing implements Runnable {

    @Override
    public void run() {
        FileServiceImpl s = new FileServiceImpl();

        Assertions.assertThat(s.clearMimeType("application/vnd.oasis.opendocument.text"))
        .isEqualTo("applicationvndoasisopendocumentt");
        Assertions.assertThat(s.clearMimeType("text/javascript"))
        .isEqualTo("textjavascript");
        Assertions.assertThat(s.clearMimeType("multipart/form-data"))
        .isEqualTo("multipartformdata");
        Assertions.assertThat(s.clearMimeType("model/x3d+binary"))
        .isEqualTo("modelx3dbinary");
    }
}