package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.domain.Enfermero;
import com.mycompany.hospital.domain.TurnoLaboral;
import com.mycompany.hospital.service.dto.EmpleadoDTO;
import com.mycompany.hospital.service.dto.EnfermeroDTO;
import com.mycompany.hospital.service.dto.TurnoLaboralDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enfermero} and its DTO {@link EnfermeroDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnfermeroMapper extends EntityMapper<EnfermeroDTO, Enfermero> {
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    @Mapping(target = "turnoLaboral", source = "turnoLaboral", qualifiedByName = "turnoLaboralId")
    EnfermeroDTO toDto(Enfermero s);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);

    @Named("turnoLaboralId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TurnoLaboralDTO toDtoTurnoLaboralId(TurnoLaboral turnoLaboral);
}
