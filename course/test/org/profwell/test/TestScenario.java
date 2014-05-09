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
<<<<<<< HEAD
                TestScenario.this.given();
            }
        });
        JPA.withTransaction(new Callback0() {
            @Override
            public void invoke() throws Throwable {
                TestScenario.this.test();
=======
                given();
            }
        });
        JPA.withTransaction(new Callback0() {
            @Override
            public void invoke() throws Throwable {
                test();
>>>>>>> refs/remotes/origin/master
            }
        });
    }

}
