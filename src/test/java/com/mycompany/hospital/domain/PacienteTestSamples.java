package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente().id(1L).numeroHistoriaClinica("numeroHistoriaClinica1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente().id(2L).numeroHistoriaClinica("numeroHistoriaClinica2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente().id(longCount.incrementAndGet()).numeroHistoriaClinica(UUID.randomUUID().toString());
    }
}
