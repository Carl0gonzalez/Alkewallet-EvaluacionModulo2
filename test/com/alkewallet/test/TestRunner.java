package com.alkewallet.test;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestRunner {

    public static void main(String[] args) {
        JUnitCore core = new JUnitCore();

        core.addListener(new RunListener() {

            @Override
            public void testRunStarted(Description description) {
                System.out.println("=== JUnit 4: Inicio de ejecución (" + description.testCount() + " tests) ===");
            }

            @Override
            public void testStarted(Description description) {
                System.out.println("[START] " + nombreTest(description));
            }

            @Override
            public void testFailure(Failure failure) {
                System.out.println("[FAIL ] " + nombreTest(failure.getDescription()));
                if (failure.getMessage() != null && !failure.getMessage().isBlank()) {
                    System.out.println("       " + failure.getMessage());
                }
            }

            @Override
            public void testIgnored(Description description) {
                System.out.println("[SKIP ] " + nombreTest(description));
            }

            @Override
            public void testFinished(Description description) {
                System.out.println("[ OK  ] " + nombreTest(description));
            }

            @Override
            public void testRunFinished(Result result) {
                System.out.println();
                System.out.println("=== Resumen ===");
                System.out.println("Run: " + result.getRunCount()
                        + " | Failed: " + result.getFailureCount()
                        + " | Ignored: " + result.getIgnoreCount()
                        + " | Time: " + result.getRunTime() + " ms");
            }

            private String nombreTest(Description d) {
                String cls = d.getClassName();
                String m = d.getMethodName();
                if (cls == null) cls = "(unknown)";
                if (m == null) m = d.getDisplayName();
                return cls + "#" + m;
            }
        });

        Result result = core.run(
                com.alkewallet.test.CuentaTest.class,
                com.alkewallet.test.TasaCambioTest.class,
                com.alkewallet.test.UsuarioTest.class,
                com.alkewallet.test.GestorUsuariosTest.class
        );

        if (!result.wasSuccessful()) {
            System.exit(1);
        }
    }
}
