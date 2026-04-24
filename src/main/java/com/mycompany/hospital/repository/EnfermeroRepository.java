package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.Enfermero;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enfermero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnfermeroRepository extends JpaRepository<Enfermero, Long>, JpaSpecificationExecutor<Enfermero> {}
