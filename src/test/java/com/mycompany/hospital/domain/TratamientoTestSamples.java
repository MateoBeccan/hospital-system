package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TratamientoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Tratamiento getTratamientoSample1() {
        return new Tratamiento().id(1L).codigo("codigo1").descripcion("descripcion1");
    }

    public static Tratamiento getTratamientoSample2() {
        return new Tratamiento().id(2L).codigo("codigo2").descripcion("descripcion2");
    }

    public static Tratamiento getTratamientoRandomSampleGenerator() {
        return new Tratamiento()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
