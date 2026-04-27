package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.EstadoDiagnostico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EstadoDiagnostico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoDiagnosticoRepository extends JpaRepository<EstadoDiagnostico, Long>, JpaSpecificationExecutor<EstadoDiagnostico> {}
