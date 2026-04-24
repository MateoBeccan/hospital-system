package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Cargo;
import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.domain.EstadoLaboral;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.domain.TipoEmpleado;
import com.mycompany.hospital.service.dto.CargoDTO;
import com.mycompany.hospital.service.dto.EmpleadoDTO;
import com.mycompany.hospital.service.dto.EstadoLaboralDTO;
import com.mycompany.hospital.service.dto.PersonaDTO;
import com.mycompany.hospital.service.dto.TipoEmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaId")
    @Mapping(target = "tipoEmpleado", source = "tipoEmpleado", qualifiedByName = "tipoEmpleadoId")
    @Mapping(target = "estadoLaboral", source = "estadoLaboral", qualifiedByName = "estadoLaboralId")
    @Mapping(target = "cargo", source = "cargo", qualifiedByName = "cargoId")
    EmpleadoDTO toDto(Empleado s);

    @Named("personaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonaDTO toDtoPersonaId(Persona persona);

    @Named("tipoEmpleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoEmpleadoDTO toDtoTipoEmpleadoId(TipoEmpleado tipoEmpleado);

    @Named("estadoLaboralId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstadoLaboralDTO toDtoEstadoLaboralId(EstadoLaboral estadoLaboral);

    @Named("cargoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CargoDTO toDtoCargoId(Cargo cargo);
}
