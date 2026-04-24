package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Enfermero;
import com.mycompany.hospital.repository.EnfermeroRepository;
import com.mycompany.hospital.service.criteria.EnfermeroCriteria;
import com.mycompany.hospital.service.dto.EnfermeroDTO;
import com.mycompany.hospital.service.mapper.EnfermeroMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Enfermero} entities in the database.
 * The main input is a {@link EnfermeroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EnfermeroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnfermeroQueryService extends QueryService<Enfermero> {

    private static final Logger LOG = LoggerFactory.getLogger(EnfermeroQueryService.class);

    private final EnfermeroRepository enfermeroRepository;

    private final EnfermeroMapper enfermeroMapper;

    public EnfermeroQueryService(EnfermeroRepository enfermeroRepository, EnfermeroMapper enfermeroMapper) {
        this.enfermeroRepository = enfermeroRepository;
        this.enfermeroMapper = enfermeroMapper;
    }

    /**
     * Return a {@link Page} of {@link EnfermeroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnfermeroDTO> findByCriteria(EnfermeroCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Enfermero> specification = createSpecification(criteria);
        return enfermeroRepository.findAll(specification, page).map(enfermeroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnfermeroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Enfermero> specification = createSpecification(criteria);
        return enfermeroRepository.count(specification);
    }

    /**
     * Function to convert {@link EnfermeroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Enfermero> createSpecification(EnfermeroCriteria criteria) {
        Specification<Enfermero> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Enfermero_.id),
                buildStringSpecification(criteria.getMatricula(), Enfermero_.matricula),
                buildRangeSpecification(criteria.getFechaMatriculacion(), Enfermero_.fechaMatriculacion),
                buildSpecification(criteria.getActivo(), Enfermero_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), Enfermero_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Enfermero_.fechaBaja),
                buildSpecification(criteria.getEmpleadoId(), root -> root.join(Enfermero_.empleado, JoinType.LEFT).get(Empleado_.id)),
                buildSpecification(criteria.getTurnoLaboralId(), root ->
                    root.join(Enfermero_.turnoLaboral, JoinType.LEFT).get(TurnoLaboral_.id)
                )
            );
        }
        return specification;
    }
}
