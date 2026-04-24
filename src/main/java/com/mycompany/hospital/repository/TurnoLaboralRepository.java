package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.TurnoLaboral;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TurnoLaboral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurnoLaboralRepository extends JpaRepository<TurnoLaboral, Long>, JpaSpecificationExecutor<TurnoLaboral> {}
