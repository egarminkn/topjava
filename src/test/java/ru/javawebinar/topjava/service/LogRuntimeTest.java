package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by eGarmin on 02.10.2016.
 */
public class LogRuntimeTest implements TestRule {
    private static final Logger LOG = LoggerFactory.getLogger(LogRuntimeTest.class);

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Начало выполнения теста");
                long start = System.currentTimeMillis();
                base.evaluate();
                long end = System.currentTimeMillis();
                LOG.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Время выполнения теста: {} мс", end - start);
            }
        };
    }
}
