package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.GrupoSanguineo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GrupoSanguineo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrupoSanguineoRepository extends JpaRepository<GrupoSanguineo, Long>, JpaSpecificationExecutor<GrupoSanguineo> {}
