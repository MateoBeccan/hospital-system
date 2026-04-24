package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.TipoDocumento;
import com.mycompany.hospital.repository.TipoDocumentoRepository;
import com.mycompany.hospital.service.criteria.TipoDocumentoCriteria;
import com.mycompany.hospital.service.dto.TipoDocumentoDTO;
import com.mycompany.hospital.service.mapper.TipoDocumentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TipoDocumento} entities in the database.
 * The main input is a {@link TipoDocumentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TipoDocumentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoDocumentoQueryService extends QueryService<TipoDocumento> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDocumentoQueryService.class);

    private final TipoDocumentoRepository tipoDocumentoRepository;

    private final TipoDocumentoMapper tipoDocumentoMapper;

    public TipoDocumentoQueryService(TipoDocumentoRepository tipoDocumentoRepository, TipoDocumentoMapper tipoDocumentoMapper) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.tipoDocumentoMapper = tipoDocumentoMapper;
    }

    /**
     * Return a {@link Page} of {@link TipoDocumentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoDocumentoDTO> findByCriteria(TipoDocumentoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoDocumento> specification = createSpecification(criteria);
        return tipoDocumentoRepository.findAll(specification, page).map(tipoDocumentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoDocumentoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoDocumento> specification = createSpecification(criteria);
        return tipoDocumentoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoDocumentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoDocumento> createSpecification(TipoDocumentoCriteria criteria) {
        Specification<TipoDocumento> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), TipoDocumento_.id),
                buildStringSpecification(criteria.getCodigo(), TipoDocumento_.codigo),
                buildStringSpecification(criteria.getNombre(), TipoDocumento_.nombre),
                buildStringSpecification(criteria.getSigla(), TipoDocumento_.sigla),
                buildStringSpecification(criteria.getDescripcion(), TipoDocumento_.descripcion),
                buildSpecification(criteria.getActivo(), TipoDocumento_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), TipoDocumento_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), TipoDocumento_.fechaBaja)
            );
        }
        return specification;
    }
}
