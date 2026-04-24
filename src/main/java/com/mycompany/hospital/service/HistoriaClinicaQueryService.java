package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.repository.HistoriaClinicaRepository;
import com.mycompany.hospital.service.criteria.HistoriaClinicaCriteria;
import com.mycompany.hospital.service.dto.HistoriaClinicaDTO;
import com.mycompany.hospital.service.mapper.HistoriaClinicaMapper;
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
 * Service for executing complex queries for {@link HistoriaClinica} entities in the database.
 * The main input is a {@link HistoriaClinicaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HistoriaClinicaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriaClinicaQueryService extends QueryService<HistoriaClinica> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriaClinicaQueryService.class);

    private final HistoriaClinicaRepository historiaClinicaRepository;

    private final HistoriaClinicaMapper historiaClinicaMapper;

    public HistoriaClinicaQueryService(HistoriaClinicaRepository historiaClinicaRepository, HistoriaClinicaMapper historiaClinicaMapper) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.historiaClinicaMapper = historiaClinicaMapper;
    }

    /**
     * Return a {@link Page} of {@link HistoriaClinicaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriaClinicaDTO> findByCriteria(HistoriaClinicaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriaClinica> specification = createSpecification(criteria);
        return historiaClinicaRepository.findAll(specification, page).map(historiaClinicaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriaClinicaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HistoriaClinica> specification = createSpecification(criteria);
        return historiaClinicaRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriaClinicaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriaClinica> createSpecification(HistoriaClinicaCriteria criteria) {
        Specification<HistoriaClinica> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), HistoriaClinica_.id),
                buildStringSpecification(criteria.getNumero(), HistoriaClinica_.numero),
                buildRangeSpecification(criteria.getFechaApertura(), HistoriaClinica_.fechaApertura),
                buildRangeSpecification(criteria.getFechaUltimaActualizacion(), HistoriaClinica_.fechaUltimaActualizacion),
                buildSpecification(criteria.getActiva(), HistoriaClinica_.activa),
                buildRangeSpecification(criteria.getFechaCierre(), HistoriaClinica_.fechaCierre),
                buildStringSpecification(criteria.getMotivoCierre(), HistoriaClinica_.motivoCierre),
                buildSpecification(criteria.getPacienteId(), root -> root.join(HistoriaClinica_.paciente, JoinType.LEFT).get(Paciente_.id))
            );
        }
        return specification;
    }
}
