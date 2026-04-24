package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.service.dto.HistoriaClinicaDTO;
import com.mycompany.hospital.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoriaClinica} and its DTO {@link HistoriaClinicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriaClinicaMapper extends EntityMapper<HistoriaClinicaDTO, HistoriaClinica> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    HistoriaClinicaDTO toDto(HistoriaClinica s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);
}
