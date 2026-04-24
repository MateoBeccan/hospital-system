package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EstadoLaboralTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static EstadoLaboral getEstadoLaboralSample1() {
        return new EstadoLaboral().id(1L).codigo("codigo1").nombre("nombre1").descripcion("descripcion1");
    }

    public static EstadoLaboral getEstadoLaboralSample2() {
        return new EstadoLaboral().id(2L).codigo("codigo2").nombre("nombre2").descripcion("descripcion2");
    }

    public static EstadoLaboral getEstadoLaboralRandomSampleGenerator() {
        return new EstadoLaboral()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
