package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.FactorRh;
import com.mycompany.hospital.service.dto.FactorRhDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FactorRh} and its DTO {@link FactorRhDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactorRhMapper extends EntityMapper<FactorRhDTO, FactorRh> {}
