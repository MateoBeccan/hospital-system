package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.domain.Especialidad;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.service.dto.EmpleadoDTO;
import com.mycompany.hospital.service.dto.EspecialidadDTO;
import com.mycompany.hospital.service.dto.MedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medico} and its DTO {@link MedicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicoMapper extends EntityMapper<MedicoDTO, Medico> {
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    @Mapping(target = "especialidad", source = "especialidad", qualifiedByName = "especialidadId")
    MedicoDTO toDto(Medico s);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);

    @Named("especialidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EspecialidadDTO toDtoEspecialidadId(Especialidad especialidad);
}
