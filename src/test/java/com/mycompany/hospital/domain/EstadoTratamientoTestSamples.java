package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EstadoTratamientoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static EstadoTratamiento getEstadoTratamientoSample1() {
        return new EstadoTratamiento().id(1L).codigo("codigo1").nombre("nombre1").descripcion("descripcion1");
    }

    public static EstadoTratamiento getEstadoTratamientoSample2() {
        return new EstadoTratamiento().id(2L).codigo("codigo2").nombre("nombre2").descripcion("descripcion2");
    }

    public static EstadoTratamiento getEstadoTratamientoRandomSampleGenerator() {
        return new EstadoTratamiento()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
