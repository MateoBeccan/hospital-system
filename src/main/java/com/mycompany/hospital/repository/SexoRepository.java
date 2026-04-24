package com.mycompany.hospital.repository;

import com.mycompany.hospital.domain.Sexo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sexo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SexoRepository extends JpaRepository<Sexo, Long>, JpaSpecificationExecutor<Sexo> {}
