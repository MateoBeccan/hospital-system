package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.EstadoTratamiento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EstadoTratamiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoTratamientoRepository extends JpaRepository<EstadoTratamiento, Long>, JpaSpecificationExecutor<EstadoTratamiento> {}
