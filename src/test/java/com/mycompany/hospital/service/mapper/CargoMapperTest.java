package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.CargoAsserts.*;
import static com.mycompany.hospital.domain.CargoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CargoMapperTest {

    private CargoMapper cargoMapper;

    @BeforeEach
    void setUp() {
        cargoMapper = new CargoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCargoSample1();
        var actual = cargoMapper.toEntity(cargoMapper.toDto(expected));
        assertCargoAllPropertiesEquals(expected, actual);
    }
}
