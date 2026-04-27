package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.domain.Diagnostico;
import com.mycompany.hospital.domain.EstadoDiagnostico;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.TipoDiagnostico;
import com.mycompany.hospital.service.dto.ConsultaDTO;
import com.mycompany.hospital.service.dto.DiagnosticoDTO;
import com.mycompany.hospital.service.dto.EstadoDiagnosticoDTO;
import com.mycompany.hospital.service.dto.MedicoDTO;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.dto.TipoDiagnosticoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Diagnostico} and its DTO {@link DiagnosticoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiagnosticoMapper extends EntityMapper<DiagnosticoDTO, Diagnostico> {
    @Mapping(target = "consulta", source = "consulta", qualifiedByName = "consultaId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "medico", source = "medico", qualifiedByName = "medicoId")
    @Mapping(target = "tipoDiagnostico", source = "tipoDiagnostico", qualifiedByName = "tipoDiagnosticoId")
    @Mapping(target = "estadoDiagnostico", source = "estadoDiagnostico", qualifiedByName = "estadoDiagnosticoId")
    DiagnosticoDTO toDto(Diagnostico s);

    @Named("consultaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConsultaDTO toDtoConsultaId(Consulta consulta);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("medicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicoDTO toDtoMedicoId(Medico medico);

    @Named("tipoDiagnosticoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoDiagnosticoDTO toDtoTipoDiagnosticoId(TipoDiagnostico tipoDiagnostico);

    @Named("estadoDiagnosticoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstadoDiagnosticoDTO toDtoEstadoDiagnosticoId(EstadoDiagnostico estadoDiagnostico);
}
