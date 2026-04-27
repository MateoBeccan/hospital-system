package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.EstadoTurno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EstadoTurno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoTurnoRepository extends JpaRepository<EstadoTurno, Long>, JpaSpecificationExecutor<EstadoTurno> {}
