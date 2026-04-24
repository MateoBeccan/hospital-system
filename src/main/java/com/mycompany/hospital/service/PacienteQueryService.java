package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.repository.PacienteRepository;
import com.mycompany.hospital.service.criteria.PacienteCriteria;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.mapper.PacienteMapper;
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
 * Service for executing complex queries for {@link Paciente} entities in the database.
 * The main input is a {@link PacienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PacienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PacienteQueryService extends QueryService<Paciente> {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteQueryService.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteQueryService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Return a {@link Page} of {@link PacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PacienteDTO> findByCriteria(PacienteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.findAll(specification, page).map(pacienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PacienteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.count(specification);
    }

    /**
     * Function to convert {@link PacienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paciente> createSpecification(PacienteCriteria criteria) {
        Specification<Paciente> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Paciente_.id),
                buildStringSpecification(criteria.getNumeroHistoriaClinica(), Paciente_.numeroHistoriaClinica),
                buildRangeSpecification(criteria.getFechaAlta(), Paciente_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Paciente_.fechaBaja),
                buildSpecification(criteria.getActivo(), Paciente_.activo),
                buildSpecification(criteria.getPersonaId(), root -> root.join(Paciente_.persona, JoinType.LEFT).get(Persona_.id)),
                buildSpecification(criteria.getObraSocialId(), root -> root.join(Paciente_.obraSocial, JoinType.LEFT).get(ObraSocial_.id)),
                buildSpecification(criteria.getGrupoSanguineoId(), root ->
                    root.join(Paciente_.grupoSanguineo, JoinType.LEFT).get(GrupoSanguineo_.id)
                ),
                buildSpecification(criteria.getFactorRhId(), root -> root.join(Paciente_.factorRh, JoinType.LEFT).get(FactorRh_.id)),
                buildSpecification(criteria.getHistoriaClinicaId(), root ->
                    root.join(Paciente_.historiaClinica, JoinType.LEFT).get(HistoriaClinica_.id)
                )
            );
        }
        return specification;
    }
}
