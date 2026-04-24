package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriaClinicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static HistoriaClinica getHistoriaClinicaSample1() {
        return new HistoriaClinica().id(1L).numero("numero1").motivoCierre("motivoCierre1");
    }

    public static HistoriaClinica getHistoriaClinicaSample2() {
        return new HistoriaClinica().id(2L).numero("numero2").motivoCierre("motivoCierre2");
    }

    public static HistoriaClinica getHistoriaClinicaRandomSampleGenerator() {
        return new HistoriaClinica()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .motivoCierre(UUID.randomUUID().toString());
    }
}
