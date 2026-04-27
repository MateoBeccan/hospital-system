package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.TipoDiagnostico;
import com.mycompany.hospital.repository.TipoDiagnosticoRepository;
import com.mycompany.hospital.service.criteria.TipoDiagnosticoCriteria;
import com.mycompany.hospital.service.dto.TipoDiagnosticoDTO;
import com.mycompany.hospital.service.mapper.TipoDiagnosticoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoDiagnostico} entities in the database.
 * The main input is a {@link TipoDiagnosticoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TipoDiagnosticoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoDiagnosticoQueryService extends QueryService<TipoDiagnostico> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDiagnosticoQueryService.class);

    private final TipoDiagnosticoRepository tipoDiagnosticoRepository;

    private final TipoDiagnosticoMapper tipoDiagnosticoMapper;

    public TipoDiagnosticoQueryService(TipoDiagnosticoRepository tipoDiagnosticoRepository, TipoDiagnosticoMapper tipoDiagnosticoMapper) {
        this.tipoDiagnosticoRepository = tipoDiagnosticoRepository;
        this.tipoDiagnosticoMapper = tipoDiagnosticoMapper;
    }

    /**
     * Return a {@link Page} of {@link TipoDiagnosticoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoDiagnosticoDTO> findByCriteria(TipoDiagnosticoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoDiagnostico> specification = createSpecification(criteria);
        return tipoDiagnosticoRepository.findAll(specification, page).map(tipoDiagnosticoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoDiagnosticoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoDiagnostico> specification = createSpecification(criteria);
        return tipoDiagnosticoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoDiagnosticoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoDiagnostico> createSpecification(TipoDiagnosticoCriteria criteria) {
        Specification<TipoDiagnostico> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), TipoDiagnostico_.id),
                buildStringSpecification(criteria.getCodigo(), TipoDiagnostico_.codigo),
                buildStringSpecification(criteria.getNombre(), TipoDiagnostico_.nombre),
                buildStringSpecification(criteria.getDescripcion(), TipoDiagnostico_.descripcion),
                buildSpecification(criteria.getActivo(), TipoDiagnostico_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), TipoDiagnostico_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), TipoDiagnostico_.fechaBaja)
            );
        }
        return specification;
    }
}
