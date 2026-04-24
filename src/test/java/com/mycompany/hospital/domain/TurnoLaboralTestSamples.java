package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TurnoLaboralTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static TurnoLaboral getTurnoLaboralSample1() {
        return new TurnoLaboral()
            .id(1L)
            .codigo("codigo1")
            .nombre("nombre1")
            .horaInicio("horaInicio1")
            .horaFin("horaFin1")
            .descripcion("descripcion1");
    }

    public static TurnoLaboral getTurnoLaboralSample2() {
        return new TurnoLaboral()
            .id(2L)
            .codigo("codigo2")
            .nombre("nombre2")
            .horaInicio("horaInicio2")
            .horaFin("horaFin2")
            .descripcion("descripcion2");
    }

    public static TurnoLaboral getTurnoLaboralRandomSampleGenerator() {
        return new TurnoLaboral()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .horaInicio(UUID.randomUUID().toString())
            .horaFin(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
