package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.SignosVitales;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SignosVitales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignosVitalesRepository extends JpaRepository<SignosVitales, Long>, JpaSpecificationExecutor<SignosVitales> {}
