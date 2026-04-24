package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.TurnoLaboral;
import com.mycompany.hospital.repository.TurnoLaboralRepository;
import com.mycompany.hospital.service.criteria.TurnoLaboralCriteria;
import com.mycompany.hospital.service.dto.TurnoLaboralDTO;
import com.mycompany.hospital.service.mapper.TurnoLaboralMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TurnoLaboral} entities in the database.
 * The main input is a {@link TurnoLaboralCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TurnoLaboralDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TurnoLaboralQueryService extends QueryService<TurnoLaboral> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoLaboralQueryService.class);

    private final TurnoLaboralRepository turnoLaboralRepository;

    private final TurnoLaboralMapper turnoLaboralMapper;

    public TurnoLaboralQueryService(TurnoLaboralRepository turnoLaboralRepository, TurnoLaboralMapper turnoLaboralMapper) {
        this.turnoLaboralRepository = turnoLaboralRepository;
        this.turnoLaboralMapper = turnoLaboralMapper;
    }

    /**
     * Return a {@link Page} of {@link TurnoLaboralDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TurnoLaboralDTO> findByCriteria(TurnoLaboralCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TurnoLaboral> specification = createSpecification(criteria);
        return turnoLaboralRepository.findAll(specification, page).map(turnoLaboralMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TurnoLaboralCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TurnoLaboral> specification = createSpecification(criteria);
        return turnoLaboralRepository.count(specification);
    }

    /**
     * Function to convert {@link TurnoLaboralCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TurnoLaboral> createSpecification(TurnoLaboralCriteria criteria) {
        Specification<TurnoLaboral> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), TurnoLaboral_.id),
                buildStringSpecification(criteria.getCodigo(), TurnoLaboral_.codigo),
                buildStringSpecification(criteria.getNombre(), TurnoLaboral_.nombre),
                buildStringSpecification(criteria.getHoraInicio(), TurnoLaboral_.horaInicio),
                buildStringSpecification(criteria.getHoraFin(), TurnoLaboral_.horaFin),
                buildStringSpecification(criteria.getDescripcion(), TurnoLaboral_.descripcion),
                buildSpecification(criteria.getActivo(), TurnoLaboral_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), TurnoLaboral_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), TurnoLaboral_.fechaBaja)
            );
        }
        return specification;
    }
}
