package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.HistoriaClinica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoriaClinica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long>, JpaSpecificationExecutor<HistoriaClinica> {}
