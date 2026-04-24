package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.TipoEmpleado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoEmpleado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoEmpleadoRepository extends JpaRepository<TipoEmpleado, Long>, JpaSpecificationExecutor<TipoEmpleado> {}
