package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.repository.ConsultaRepository;
import com.mycompany.hospital.service.criteria.ConsultaCriteria;
import com.mycompany.hospital.service.dto.ConsultaDTO;
import com.mycompany.hospital.service.mapper.ConsultaMapper;
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
 * Service for executing complex queries for {@link Consulta} entities in the database.
 * The main input is a {@link ConsultaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConsultaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsultaQueryService extends QueryService<Consulta> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultaQueryService.class);

    private final ConsultaRepository consultaRepository;

    private final ConsultaMapper consultaMapper;

    public ConsultaQueryService(ConsultaRepository consultaRepository, ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.consultaMapper = consultaMapper;
    }

    /**
     * Return a {@link Page} of {@link ConsultaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsultaDTO> findByCriteria(ConsultaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Consulta> specification = createSpecification(criteria);
        return consultaRepository.findAll(specification, page).map(consultaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsultaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Consulta> specification = createSpecification(criteria);
        return consultaRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsultaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Consulta> createSpecification(ConsultaCriteria criteria) {
        Specification<Consulta> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Consulta_.id),
                buildStringSpecification(criteria.getCodigo(), Consulta_.codigo),
                buildRangeSpecification(criteria.getFechaHoraInicio(), Consulta_.fechaHoraInicio),
                buildRangeSpecification(criteria.getFechaHoraFin(), Consulta_.fechaHoraFin),
                buildStringSpecification(criteria.getMotivoConsulta(), Consulta_.motivoConsulta),
                buildSpecification(criteria.getActiva(), Consulta_.activa),
                buildRangeSpecification(criteria.getFechaAlta(), Consulta_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Consulta_.fechaBaja),
                buildSpecification(criteria.getTurnoId(), root -> root.join(Consulta_.turno, JoinType.LEFT).get(Turno_.id)),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Consulta_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getMedicoId(), root -> root.join(Consulta_.medico, JoinType.LEFT).get(Medico_.id)),
                buildSpecification(criteria.getHistoriaClinicaId(), root ->
                    root.join(Consulta_.historiaClinica, JoinType.LEFT).get(HistoriaClinica_.id)
                )
            );
        }
        return specification;
    }
}
