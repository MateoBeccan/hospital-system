package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.AntecedenteClinico;
import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.service.dto.AntecedenteClinicoDTO;
import com.mycompany.hospital.service.dto.HistoriaClinicaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AntecedenteClinico} and its DTO {@link AntecedenteClinicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AntecedenteClinicoMapper extends EntityMapper<AntecedenteClinicoDTO, AntecedenteClinico> {
    @Mapping(target = "historiaClinica", source = "historiaClinica", qualifiedByName = "historiaClinicaId")
    AntecedenteClinicoDTO toDto(AntecedenteClinico s);

    @Named("historiaClinicaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistoriaClinicaDTO toDtoHistoriaClinicaId(HistoriaClinica historiaClinica);
}
