package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.ContactoEmergenciaAsserts.*;
import static com.mycompany.hospital.domain.ContactoEmergenciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactoEmergenciaMapperTest {

    private ContactoEmergenciaMapper contactoEmergenciaMapper;

    @BeforeEach
    void setUp() {
        contactoEmergenciaMapper = new ContactoEmergenciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContactoEmergenciaSample1();
        var actual = contactoEmergenciaMapper.toEntity(contactoEmergenciaMapper.toDto(expected));
        assertContactoEmergenciaAllPropertiesEquals(expected, actual);
    }
}
