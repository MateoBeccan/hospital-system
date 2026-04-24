package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.EstadoLaboral;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EstadoLaboral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoLaboralRepository extends JpaRepository<EstadoLaboral, Long>, JpaSpecificationExecutor<EstadoLaboral> {}
