package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.EstadoTratamiento;
import com.mycompany.hospital.repository.EstadoTratamientoRepository;
import com.mycompany.hospital.service.criteria.EstadoTratamientoCriteria;
import com.mycompany.hospital.service.dto.EstadoTratamientoDTO;
import com.mycompany.hospital.service.mapper.EstadoTratamientoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EstadoTratamiento} entities in the database.
 * The main input is a {@link EstadoTratamientoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EstadoTratamientoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstadoTratamientoQueryService extends QueryService<EstadoTratamiento> {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoTratamientoQueryService.class);

    private final EstadoTratamientoRepository estadoTratamientoRepository;

    private final EstadoTratamientoMapper estadoTratamientoMapper;

    public EstadoTratamientoQueryService(
        EstadoTratamientoRepository estadoTratamientoRepository,
        EstadoTratamientoMapper estadoTratamientoMapper
    ) {
        this.estadoTratamientoRepository = estadoTratamientoRepository;
        this.estadoTratamientoMapper = estadoTratamientoMapper;
    }

    /**
     * Return a {@link Page} of {@link EstadoTratamientoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoTratamientoDTO> findByCriteria(EstadoTratamientoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EstadoTratamiento> specification = createSpecification(criteria);
        return estadoTratamientoRepository.findAll(specification, page).map(estadoTratamientoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstadoTratamientoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EstadoTratamiento> specification = createSpecification(criteria);
        return estadoTratamientoRepository.count(specification);
    }

    /**
     * Function to convert {@link EstadoTratamientoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EstadoTratamiento> createSpecification(EstadoTratamientoCriteria criteria) {
        Specification<EstadoTratamiento> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), EstadoTratamiento_.id),
                buildStringSpecification(criteria.getCodigo(), EstadoTratamiento_.codigo),
                buildStringSpecification(criteria.getNombre(), EstadoTratamiento_.nombre),
                buildStringSpecification(criteria.getDescripcion(), EstadoTratamiento_.descripcion),
                buildSpecification(criteria.getActivo(), EstadoTratamiento_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), EstadoTratamiento_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), EstadoTratamiento_.fechaBaja)
            );
        }
        return specification;
    }
}
