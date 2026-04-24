package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Pais getPaisSample1() {
        return new Pais().id(1L).nombre("nombre1").codigoIso("codigoIso1");
    }

    public static Pais getPaisSample2() {
        return new Pais().id(2L).nombre("nombre2").codigoIso("codigoIso2");
    }

    public static Pais getPaisRandomSampleGenerator() {
        return new Pais().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).codigoIso(UUID.randomUUID().toString());
    }
}
