package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.FactorRh;
import com.mycompany.hospital.repository.FactorRhRepository;
import com.mycompany.hospital.service.criteria.FactorRhCriteria;
import com.mycompany.hospital.service.dto.FactorRhDTO;
import com.mycompany.hospital.service.mapper.FactorRhMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FactorRh} entities in the database.
 * The main input is a {@link FactorRhCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FactorRhDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactorRhQueryService extends QueryService<FactorRh> {

    private static final Logger LOG = LoggerFactory.getLogger(FactorRhQueryService.class);

    private final FactorRhRepository factorRhRepository;

    private final FactorRhMapper factorRhMapper;

    public FactorRhQueryService(FactorRhRepository factorRhRepository, FactorRhMapper factorRhMapper) {
        this.factorRhRepository = factorRhRepository;
        this.factorRhMapper = factorRhMapper;
    }

    /**
     * Return a {@link Page} of {@link FactorRhDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactorRhDTO> findByCriteria(FactorRhCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FactorRh> specification = createSpecification(criteria);
        return factorRhRepository.findAll(specification, page).map(factorRhMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactorRhCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FactorRh> specification = createSpecification(criteria);
        return factorRhRepository.count(specification);
    }

    /**
     * Function to convert {@link FactorRhCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FactorRh> createSpecification(FactorRhCriteria criteria) {
        Specification<FactorRh> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), FactorRh_.id),
                buildStringSpecification(criteria.getCodigo(), FactorRh_.codigo),
                buildStringSpecification(criteria.getNombre(), FactorRh_.nombre),
                buildStringSpecification(criteria.getDescripcion(), FactorRh_.descripcion),
                buildSpecification(criteria.getActivo(), FactorRh_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), FactorRh_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), FactorRh_.fechaBaja)
            );
        }
        return specification;
    }
}
