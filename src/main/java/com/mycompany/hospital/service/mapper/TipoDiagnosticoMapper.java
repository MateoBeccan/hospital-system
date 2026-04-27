package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.TipoDiagnostico;
import com.mycompany.hospital.service.dto.TipoDiagnosticoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoDiagnostico} and its DTO {@link TipoDiagnosticoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoDiagnosticoMapper extends EntityMapper<TipoDiagnosticoDTO, TipoDiagnostico> {}
