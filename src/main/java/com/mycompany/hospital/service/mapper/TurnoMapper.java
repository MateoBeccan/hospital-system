package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.CanalSolicitud;
import com.mycompany.hospital.domain.Especialidad;
import com.mycompany.hospital.domain.EstadoTurno;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.Turno;
import com.mycompany.hospital.service.dto.CanalSolicitudDTO;
import com.mycompany.hospital.service.dto.EspecialidadDTO;
import com.mycompany.hospital.service.dto.EstadoTurnoDTO;
import com.mycompany.hospital.service.dto.MedicoDTO;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.dto.TurnoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Turno} and its DTO {@link TurnoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TurnoMapper extends EntityMapper<TurnoDTO, Turno> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoId")
    @Mapping(target = "especialidad", source = "especialidad", qualifiedByName = "especialidadId")
    @Mapping(target = "estadoTurno", source = "estadoTurno", qualifiedByName = "estadoTurnoId")
    @Mapping(target = "canalSolicitud", source = "canalSolicitud", qualifiedByName = "canalSolicitudId")
    TurnoDTO toDto(Turno s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("medicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoMedicoId(Medico medico);

    @Named("especialidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EspecialidadDTO toDtoEspecialidadId(Especialidad especialidad);

    @Named("estadoTurnoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstadoTurnoDTO toDtoEstadoTurnoId(EstadoTurno estadoTurno);

    @Named("canalSolicitudId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CanalSolicitudDTO toDtoCanalSolicitudId(CanalSolicitud canalSolicitud);
}
