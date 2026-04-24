package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.AntecedenteClinico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AntecedenteClinico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AntecedenteClinicoRepository
    extends JpaRepository<AntecedenteClinico, Long>, JpaSpecificationExecutor<AntecedenteClinico> {}
