package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Diagnostico;
import com.mycompany.hospital.repository.DiagnosticoRepository;
import com.mycompany.hospital.service.criteria.DiagnosticoCriteria;
import com.mycompany.hospital.service.dto.DiagnosticoDTO;
import com.mycompany.hospital.service.mapper.DiagnosticoMapper;
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
 * Service for executing complex queries for {@link Diagnostico} entities in the database.
 * The main input is a {@link DiagnosticoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DiagnosticoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticoQueryService extends QueryService<Diagnostico> {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticoQueryService.class);

    private final DiagnosticoRepository diagnosticoRepository;

    private final DiagnosticoMapper diagnosticoMapper;

    public DiagnosticoQueryService(DiagnosticoRepository diagnosticoRepository, DiagnosticoMapper diagnosticoMapper) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.diagnosticoMapper = diagnosticoMapper;
    }

    /**
     * Return a {@link Page} of {@link DiagnosticoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiagnosticoDTO> findByCriteria(DiagnosticoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Diagnostico> specification = createSpecification(criteria);
        return diagnosticoRepository.findAll(specification, page).map(diagnosticoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiagnosticoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Diagnostico> specification = createSpecification(criteria);
        return diagnosticoRepository.count(specification);
    }

    /**
     * Function to convert {@link DiagnosticoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Diagnostico> createSpecification(DiagnosticoCriteria criteria) {
        Specification<Diagnostico> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Diagnostico_.id),
                buildStringSpecification(criteria.getCodigo(), Diagnostico_.codigo),
                buildRangeSpecification(criteria.getFechaDiagnostico(), Diagnostico_.fechaDiagnostico),
                buildStringSpecification(criteria.getDescripcion(), Diagnostico_.descripcion),
                buildSpecification(criteria.getActivo(), Diagnostico_.activo),
                buildRangeSpecification(criteria.getFechaResolucion(), Diagnostico_.fechaResolucion),
                buildSpecification(criteria.getEsPrincipal(), Diagnostico_.esPrincipal),
                buildRangeSpecification(criteria.getFechaAlta(), Diagnostico_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Diagnostico_.fechaBaja),
                buildSpecification(criteria.getConsultaId(), root -> root.join(Diagnostico_.consulta, JoinType.LEFT).get(Consulta_.id)),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Diagnostico_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getMedicoId(), root -> root.join(Diagnostico_.medico, JoinType.LEFT).get(Medico_.id)),
                buildSpecification(criteria.getTipoDiagnosticoId(), root ->
                    root.join(Diagnostico_.tipoDiagnostico, JoinType.LEFT).get(TipoDiagnostico_.id)
                ),
                buildSpecification(criteria.getEstadoDiagnosticoId(), root ->
                    root.join(Diagnostico_.estadoDiagnostico, JoinType.LEFT).get(EstadoDiagnostico_.id)
                )
            );
        }
        return specification;
    }
}
