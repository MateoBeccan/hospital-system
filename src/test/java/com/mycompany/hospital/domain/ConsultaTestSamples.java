package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConsultaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Consulta getConsultaSample1() {
        return new Consulta().id(1L).codigo("codigo1").motivoConsulta("motivoConsulta1");
    }

    public static Consulta getConsultaSample2() {
        return new Consulta().id(2L).codigo("codigo2").motivoConsulta("motivoConsulta2");
    }

    public static Consulta getConsultaRandomSampleGenerator() {
        return new Consulta()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .motivoConsulta(UUID.randomUUID().toString());
    }
}
