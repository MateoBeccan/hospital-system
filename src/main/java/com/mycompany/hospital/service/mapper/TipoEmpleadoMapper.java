package com.mycompany.hospital.service.mapper;

import com.mycompany.hospital.domain.TipoEmpleado;
import com.mycompany.hospital.service.dto.TipoEmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoEmpleado} and its DTO {@link TipoEmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoEmpleadoMapper extends EntityMapper<TipoEmpleadoDTO, TipoEmpleado> {}
