package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnfermeroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Enfermero getEnfermeroSample1() {
        return new Enfermero().id(1L).matricula("matricula1");
    }

    public static Enfermero getEnfermeroSample2() {
        return new Enfermero().id(2L).matricula("matricula2");
    }

    public static Enfermero getEnfermeroRandomSampleGenerator() {
        return new Enfermero().id(longCount.incrementAndGet()).matricula(UUID.randomUUID().toString());
    }
}
