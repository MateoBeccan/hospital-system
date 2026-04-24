package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Empleado getEmpleadoSample1() {
        return new Empleado().id(1L).legajo("legajo1");
    }

    public static Empleado getEmpleadoSample2() {
        return new Empleado().id(2L).legajo("legajo2");
    }

    public static Empleado getEmpleadoRandomSampleGenerator() {
        return new Empleado().id(longCount.incrementAndGet()).legajo(UUID.randomUUID().toString());
    }
}
