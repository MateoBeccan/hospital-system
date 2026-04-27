package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.EstadoDiagnostico;
import com.mycompany.hospital.service.dto.EstadoDiagnosticoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EstadoDiagnostico} and its DTO {@link EstadoDiagnosticoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstadoDiagnosticoMapper extends EntityMapper<EstadoDiagnosticoDTO, EstadoDiagnostico> {}
