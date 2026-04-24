package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Cargo;
import com.mycompany.hospital.service.dto.CargoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cargo} and its DTO {@link CargoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CargoMapper extends EntityMapper<CargoDTO, Cargo> {}
