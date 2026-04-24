package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.ObraSocial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ObraSocial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObraSocialRepository extends JpaRepository<ObraSocial, Long>, JpaSpecificationExecutor<ObraSocial> {}
