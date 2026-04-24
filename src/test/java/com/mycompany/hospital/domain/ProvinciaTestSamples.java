package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProvinciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Provincia getProvinciaSample1() {
        return new Provincia().id(1L).nombre("nombre1").codigo("codigo1");
    }

    public static Provincia getProvinciaSample2() {
        return new Provincia().id(2L).nombre("nombre2").codigo("codigo2");
    }

    public static Provincia getProvinciaRandomSampleGenerator() {
        return new Provincia().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).codigo(UUID.randomUUID().toString());
    }
}
