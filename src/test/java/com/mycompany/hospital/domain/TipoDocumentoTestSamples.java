package com.mycompany.hospital.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoDocumentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static TipoDocumento getTipoDocumentoSample1() {
        return new TipoDocumento().id(1L).codigo("codigo1").nombre("nombre1").sigla("sigla1").descripcion("descripcion1");
    }

    public static TipoDocumento getTipoDocumentoSample2() {
        return new TipoDocumento().id(2L).codigo("codigo2").nombre("nombre2").sigla("sigla2").descripcion("descripcion2");
    }

    public static TipoDocumento getTipoDocumentoRandomSampleGenerator() {
        return new TipoDocumento()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .sigla(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
