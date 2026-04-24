package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AntecedenteClinicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static AntecedenteClinico getAntecedenteClinicoSample1() {
        return new AntecedenteClinico().id(1L).titulo("titulo1").descripcion("descripcion1");
    }

    public static AntecedenteClinico getAntecedenteClinicoSample2() {
        return new AntecedenteClinico().id(2L).titulo("titulo2").descripcion("descripcion2");
    }

    public static AntecedenteClinico getAntecedenteClinicoRandomSampleGenerator() {
        return new AntecedenteClinico()
            .id(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
