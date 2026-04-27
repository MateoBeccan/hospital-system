package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TurnoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Turno getTurnoSample1() {
        return new Turno().id(1L).codigo("codigo1").duracionMinutos(1).motivoConsulta("motivoConsulta1");
    }

    public static Turno getTurnoSample2() {
        return new Turno().id(2L).codigo("codigo2").duracionMinutos(2).motivoConsulta("motivoConsulta2");
    }

    public static Turno getTurnoRandomSampleGenerator() {
        return new Turno()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .duracionMinutos(intCount.incrementAndGet())
            .motivoConsulta(UUID.randomUUID().toString());
    }
}
