package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.EstadoDiagnostico;
import com.mycompany.hospital.repository.EstadoDiagnosticoRepository;
import com.mycompany.hospital.service.criteria.EstadoDiagnosticoCriteria;
import com.mycompany.hospital.service.dto.EstadoDiagnosticoDTO;
import com.mycompany.hospital.service.mapper.EstadoDiagnosticoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EstadoDiagnostico} entities in the database.
 * The main input is a {@link EstadoDiagnosticoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EstadoDiagnosticoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstadoDiagnosticoQueryService extends QueryService<EstadoDiagnostico> {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoDiagnosticoQueryService.class);

    private final EstadoDiagnosticoRepository estadoDiagnosticoRepository;

    private final EstadoDiagnosticoMapper estadoDiagnosticoMapper;

    public EstadoDiagnosticoQueryService(
        EstadoDiagnosticoRepository estadoDiagnosticoRepository,
        EstadoDiagnosticoMapper estadoDiagnosticoMapper
    ) {
        this.estadoDiagnosticoRepository = estadoDiagnosticoRepository;
        this.estadoDiagnosticoMapper = estadoDiagnosticoMapper;
    }

    /**
     * Return a {@link Page} of {@link EstadoDiagnosticoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoDiagnosticoDTO> findByCriteria(EstadoDiagnosticoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EstadoDiagnostico> specification = createSpecification(criteria);
        return estadoDiagnosticoRepository.findAll(specification, page).map(estadoDiagnosticoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstadoDiagnosticoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EstadoDiagnostico> specification = createSpecification(criteria);
        return estadoDiagnosticoRepository.count(specification);
    }

    /**
     * Function to convert {@link EstadoDiagnosticoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EstadoDiagnostico> createSpecification(EstadoDiagnosticoCriteria criteria) {
        Specification<EstadoDiagnostico> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), EstadoDiagnostico_.id),
                buildStringSpecification(criteria.getCodigo(), EstadoDiagnostico_.codigo),
                buildStringSpecification(criteria.getNombre(), EstadoDiagnostico_.nombre),
                buildStringSpecification(criteria.getDescripcion(), EstadoDiagnostico_.descripcion),
                buildSpecification(criteria.getActivo(), EstadoDiagnostico_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), EstadoDiagnostico_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), EstadoDiagnostico_.fechaBaja)
            );
        }
        return specification;
    }
}
