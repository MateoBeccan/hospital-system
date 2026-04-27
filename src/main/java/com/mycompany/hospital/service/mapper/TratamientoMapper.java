package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Diagnostico;
import com.mycompany.hospital.domain.EstadoTratamiento;
import com.mycompany.hospital.domain.Tratamiento;
import com.mycompany.hospital.service.dto.DiagnosticoDTO;
import com.mycompany.hospital.service.dto.EstadoTratamientoDTO;
import com.mycompany.hospital.service.dto.TratamientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tratamiento} and its DTO {@link TratamientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TratamientoMapper extends EntityMapper<TratamientoDTO, Tratamiento> {
    @Mapping(target = "diagnostico", source = "diagnostico", qualifiedByName = "diagnosticoId")
    @Mapping(target = "estadoTratamiento", source = "estadoTratamiento", qualifiedByName = "estadoTratamientoId")
    TratamientoDTO toDto(Tratamiento s);

    @Named("diagnosticoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DiagnosticoDTO toDtoDiagnosticoId(Diagnostico diagnostico);

    @Named("estadoTratamientoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstadoTratamientoDTO toDtoEstadoTratamientoId(EstadoTratamiento estadoTratamiento);
}
