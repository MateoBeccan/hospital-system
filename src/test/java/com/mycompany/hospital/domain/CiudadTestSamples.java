package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CiudadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Ciudad getCiudadSample1() {
        return new Ciudad().id(1L).nombre("nombre1").codigo("codigo1").codigoPostal("codigoPostal1");
    }

    public static Ciudad getCiudadSample2() {
        return new Ciudad().id(2L).nombre("nombre2").codigo("codigo2").codigoPostal("codigoPostal2");
    }

    public static Ciudad getCiudadRandomSampleGenerator() {
        return new Ciudad()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .codigo(UUID.randomUUID().toString())
            .codigoPostal(UUID.randomUUID().toString());
    }
}
