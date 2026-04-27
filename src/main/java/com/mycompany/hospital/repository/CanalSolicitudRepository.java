package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.CanalSolicitud;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CanalSolicitud entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CanalSolicitudRepository extends JpaRepository<CanalSolicitud, Long>, JpaSpecificationExecutor<CanalSolicitud> {}
