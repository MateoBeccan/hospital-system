package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.FactorRh;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactorRh entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactorRhRepository extends JpaRepository<FactorRh, Long>, JpaSpecificationExecutor<FactorRh> {}
