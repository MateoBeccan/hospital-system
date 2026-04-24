package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ObraSocialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ObraSocial getObraSocialSample1() {
        return new ObraSocial().id(1L).codigo("codigo1").nombre("nombre1").telefono("telefono1").email("email1").direccion("direccion1");
    }

    public static ObraSocial getObraSocialSample2() {
        return new ObraSocial().id(2L).codigo("codigo2").nombre("nombre2").telefono("telefono2").email("email2").direccion("direccion2");
    }

    public static ObraSocial getObraSocialRandomSampleGenerator() {
        return new ObraSocial()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString());
    }
}
