package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.Turno;
import com.mycompany.hospital.service.dto.ConsultaDTO;
import com.mycompany.hospital.service.dto.HistoriaClinicaDTO;
import com.mycompany.hospital.service.dto.MedicoDTO;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.dto.TurnoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consulta} and its DTO {@link ConsultaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsultaMapper extends EntityMapper<ConsultaDTO, Consulta> {
    @Mapping(target = "turno", source = "turno", qualifiedByName = "turnoId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoId")
    @Mapping(target = "historiaClinica", source = "historiaClinica", qualifiedByName = "historiaClinicaId")
    ConsultaDTO toDto(Consulta s);

    @Named("turnoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TurnoDTO toDtoTurnoId(Turno turno);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("medicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoMedicoId(Medico medico);

    @Named("historiaClinicaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistoriaClinicaDTO toDtoHistoriaClinicaId(HistoriaClinica historiaClinica);
}
