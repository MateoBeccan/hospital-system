package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ContactoEmergenciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ContactoEmergencia getContactoEmergenciaSample1() {
        return new ContactoEmergencia()
            .id(1L)
            .nombre("nombre1")
            .telefono("telefono1")
            .parentesco("parentesco1")
            .observaciones("observaciones1")
            .prioridad(1);
    }

    public static ContactoEmergencia getContactoEmergenciaSample2() {
        return new ContactoEmergencia()
            .id(2L)
            .nombre("nombre2")
            .telefono("telefono2")
            .parentesco("parentesco2")
            .observaciones("observaciones2")
            .prioridad(2);
    }

    public static ContactoEmergencia getContactoEmergenciaRandomSampleGenerator() {
        return new ContactoEmergencia()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .parentesco(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString())
            .prioridad(intCount.incrementAndGet());
    }
}
