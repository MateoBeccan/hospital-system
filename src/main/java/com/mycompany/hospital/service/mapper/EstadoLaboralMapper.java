package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.EstadoLaboral;
import com.mycompany.hospital.service.dto.EstadoLaboralDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EstadoLaboral} and its DTO {@link EstadoLaboralDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstadoLaboralMapper extends EntityMapper<EstadoLaboralDTO, EstadoLaboral> {}
