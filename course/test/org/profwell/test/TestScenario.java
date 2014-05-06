package org.profwell.test;

import play.db.jpa.JPA;
import play.libs.F.Callback0;

public abstract class TestScenario implements Runnable {

    protected abstract void given();

    protected abstract void test();

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
