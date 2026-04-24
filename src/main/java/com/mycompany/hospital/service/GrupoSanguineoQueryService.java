package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.GrupoSanguineo;
import com.mycompany.hospital.repository.GrupoSanguineoRepository;
import com.mycompany.hospital.service.criteria.GrupoSanguineoCriteria;
import com.mycompany.hospital.service.dto.GrupoSanguineoDTO;
import com.mycompany.hospital.service.mapper.GrupoSanguineoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GrupoSanguineo} entities in the database.
 * The main input is a {@link GrupoSanguineoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GrupoSanguineoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GrupoSanguineoQueryService extends QueryService<GrupoSanguineo> {

    private static final Logger LOG = LoggerFactory.getLogger(GrupoSanguineoQueryService.class);

    private final GrupoSanguineoRepository grupoSanguineoRepository;

    private final GrupoSanguineoMapper grupoSanguineoMapper;

    public GrupoSanguineoQueryService(GrupoSanguineoRepository grupoSanguineoRepository, GrupoSanguineoMapper grupoSanguineoMapper) {
        this.grupoSanguineoRepository = grupoSanguineoRepository;
        this.grupoSanguineoMapper = grupoSanguineoMapper;
    }

    /**
     * Return a {@link Page} of {@link GrupoSanguineoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GrupoSanguineoDTO> findByCriteria(GrupoSanguineoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GrupoSanguineo> specification = createSpecification(criteria);
        return grupoSanguineoRepository.findAll(specification, page).map(grupoSanguineoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GrupoSanguineoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GrupoSanguineo> specification = createSpecification(criteria);
        return grupoSanguineoRepository.count(specification);
    }

    /**
     * Function to convert {@link GrupoSanguineoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GrupoSanguineo> createSpecification(GrupoSanguineoCriteria criteria) {
        Specification<GrupoSanguineo> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), GrupoSanguineo_.id),
                buildStringSpecification(criteria.getCodigo(), GrupoSanguineo_.codigo),
                buildStringSpecification(criteria.getNombre(), GrupoSanguineo_.nombre),
                buildStringSpecification(criteria.getDescripcion(), GrupoSanguineo_.descripcion),
                buildSpecification(criteria.getActivo(), GrupoSanguineo_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), GrupoSanguineo_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), GrupoSanguineo_.fechaBaja)
            );
        }
        return specification;
    }
}
