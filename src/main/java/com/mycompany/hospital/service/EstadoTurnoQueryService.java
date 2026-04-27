package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.EstadoTurno;
import com.mycompany.hospital.repository.EstadoTurnoRepository;
import com.mycompany.hospital.service.criteria.EstadoTurnoCriteria;
import com.mycompany.hospital.service.dto.EstadoTurnoDTO;
import com.mycompany.hospital.service.mapper.EstadoTurnoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EstadoTurno} entities in the database.
 * The main input is a {@link EstadoTurnoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EstadoTurnoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstadoTurnoQueryService extends QueryService<EstadoTurno> {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoTurnoQueryService.class);

    private final EstadoTurnoRepository estadoTurnoRepository;

    private final EstadoTurnoMapper estadoTurnoMapper;

    public EstadoTurnoQueryService(EstadoTurnoRepository estadoTurnoRepository, EstadoTurnoMapper estadoTurnoMapper) {
        this.estadoTurnoRepository = estadoTurnoRepository;
        this.estadoTurnoMapper = estadoTurnoMapper;
    }

    /**
     * Return a {@link Page} of {@link EstadoTurnoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoTurnoDTO> findByCriteria(EstadoTurnoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EstadoTurno> specification = createSpecification(criteria);
        return estadoTurnoRepository.findAll(specification, page).map(estadoTurnoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstadoTurnoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EstadoTurno> specification = createSpecification(criteria);
        return estadoTurnoRepository.count(specification);
    }

    /**
     * Function to convert {@link EstadoTurnoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EstadoTurno> createSpecification(EstadoTurnoCriteria criteria) {
        Specification<EstadoTurno> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), EstadoTurno_.id),
                buildStringSpecification(criteria.getCodigo(), EstadoTurno_.codigo),
                buildStringSpecification(criteria.getNombre(), EstadoTurno_.nombre),
                buildStringSpecification(criteria.getDescripcion(), EstadoTurno_.descripcion),
                buildSpecification(criteria.getActivo(), EstadoTurno_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), EstadoTurno_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), EstadoTurno_.fechaBaja)
            );
        }
        return specification;
    }
}
