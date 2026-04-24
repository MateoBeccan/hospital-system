package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Ciudad;
import com.mycompany.hospital.repository.CiudadRepository;
import com.mycompany.hospital.service.criteria.CiudadCriteria;
import com.mycompany.hospital.service.dto.CiudadDTO;
import com.mycompany.hospital.service.mapper.CiudadMapper;
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
 * Service for executing complex queries for {@link Ciudad} entities in the database.
 * The main input is a {@link CiudadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CiudadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CiudadQueryService extends QueryService<Ciudad> {

    private static final Logger LOG = LoggerFactory.getLogger(CiudadQueryService.class);

    private final CiudadRepository ciudadRepository;

    private final CiudadMapper ciudadMapper;

    public CiudadQueryService(CiudadRepository ciudadRepository, CiudadMapper ciudadMapper) {
        this.ciudadRepository = ciudadRepository;
        this.ciudadMapper = ciudadMapper;
    }

    /**
     * Return a {@link Page} of {@link CiudadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CiudadDTO> findByCriteria(CiudadCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ciudad> specification = createSpecification(criteria);
        return ciudadRepository.findAll(specification, page).map(ciudadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CiudadCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Ciudad> specification = createSpecification(criteria);
        return ciudadRepository.count(specification);
    }

    /**
     * Function to convert {@link CiudadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ciudad> createSpecification(CiudadCriteria criteria) {
        Specification<Ciudad> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Ciudad_.id),
                buildStringSpecification(criteria.getNombre(), Ciudad_.nombre),
                buildStringSpecification(criteria.getCodigo(), Ciudad_.codigo),
                buildStringSpecification(criteria.getCodigoPostal(), Ciudad_.codigoPostal),
                buildRangeSpecification(criteria.getFechaAlta(), Ciudad_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Ciudad_.fechaBaja),
                buildSpecification(criteria.getActivo(), Ciudad_.activo),
                buildSpecification(criteria.getProvinciaId(), root -> root.join(Ciudad_.provincia, JoinType.LEFT).get(Provincia_.id))
            );
        }
        return specification;
    }
}
