package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.ContactoEmergencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactoEmergencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactoEmergenciaRepository
    extends JpaRepository<ContactoEmergencia, Long>, JpaSpecificationExecutor<ContactoEmergencia> {}
