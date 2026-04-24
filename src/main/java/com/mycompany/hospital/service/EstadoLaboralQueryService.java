package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.EstadoLaboral;
import com.mycompany.hospital.repository.EstadoLaboralRepository;
import com.mycompany.hospital.service.criteria.EstadoLaboralCriteria;
import com.mycompany.hospital.service.dto.EstadoLaboralDTO;
import com.mycompany.hospital.service.mapper.EstadoLaboralMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EstadoLaboral} entities in the database.
 * The main input is a {@link EstadoLaboralCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EstadoLaboralDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstadoLaboralQueryService extends QueryService<EstadoLaboral> {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoLaboralQueryService.class);

    private final EstadoLaboralRepository estadoLaboralRepository;

    private final EstadoLaboralMapper estadoLaboralMapper;

    public EstadoLaboralQueryService(EstadoLaboralRepository estadoLaboralRepository, EstadoLaboralMapper estadoLaboralMapper) {
        this.estadoLaboralRepository = estadoLaboralRepository;
        this.estadoLaboralMapper = estadoLaboralMapper;
    }

    /**
     * Return a {@link Page} of {@link EstadoLaboralDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoLaboralDTO> findByCriteria(EstadoLaboralCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EstadoLaboral> specification = createSpecification(criteria);
        return estadoLaboralRepository.findAll(specification, page).map(estadoLaboralMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstadoLaboralCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EstadoLaboral> specification = createSpecification(criteria);
        return estadoLaboralRepository.count(specification);
    }

    /**
     * Function to convert {@link EstadoLaboralCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EstadoLaboral> createSpecification(EstadoLaboralCriteria criteria) {
        Specification<EstadoLaboral> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), EstadoLaboral_.id),
                buildStringSpecification(criteria.getCodigo(), EstadoLaboral_.codigo),
                buildStringSpecification(criteria.getNombre(), EstadoLaboral_.nombre),
                buildStringSpecification(criteria.getDescripcion(), EstadoLaboral_.descripcion),
                buildRangeSpecification(criteria.getFechaAlta(), EstadoLaboral_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), EstadoLaboral_.fechaBaja),
                buildSpecification(criteria.getActivo(), EstadoLaboral_.activo)
            );
        }
        return specification;
    }
}
