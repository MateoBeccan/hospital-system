package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.TipoEmpleado;
import com.mycompany.hospital.repository.TipoEmpleadoRepository;
import com.mycompany.hospital.service.criteria.TipoEmpleadoCriteria;
import com.mycompany.hospital.service.dto.TipoEmpleadoDTO;
import com.mycompany.hospital.service.mapper.TipoEmpleadoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoEmpleado} entities in the database.
 * The main input is a {@link TipoEmpleadoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TipoEmpleadoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoEmpleadoQueryService extends QueryService<TipoEmpleado> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoEmpleadoQueryService.class);

    private final TipoEmpleadoRepository tipoEmpleadoRepository;

    private final TipoEmpleadoMapper tipoEmpleadoMapper;

    public TipoEmpleadoQueryService(TipoEmpleadoRepository tipoEmpleadoRepository, TipoEmpleadoMapper tipoEmpleadoMapper) {
        this.tipoEmpleadoRepository = tipoEmpleadoRepository;
        this.tipoEmpleadoMapper = tipoEmpleadoMapper;
    }

    /**
     * Return a {@link Page} of {@link TipoEmpleadoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoEmpleadoDTO> findByCriteria(TipoEmpleadoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoEmpleado> specification = createSpecification(criteria);
        return tipoEmpleadoRepository.findAll(specification, page).map(tipoEmpleadoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoEmpleadoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoEmpleado> specification = createSpecification(criteria);
        return tipoEmpleadoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoEmpleadoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoEmpleado> createSpecification(TipoEmpleadoCriteria criteria) {
        Specification<TipoEmpleado> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), TipoEmpleado_.id),
                buildStringSpecification(criteria.getCodigo(), TipoEmpleado_.codigo),
                buildStringSpecification(criteria.getNombre(), TipoEmpleado_.nombre),
                buildStringSpecification(criteria.getDescripcion(), TipoEmpleado_.descripcion),
                buildRangeSpecification(criteria.getFechaAlta(), TipoEmpleado_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), TipoEmpleado_.fechaBaja),
                buildSpecification(criteria.getActivo(), TipoEmpleado_.activo)
            );
        }
        return specification;
    }
}
