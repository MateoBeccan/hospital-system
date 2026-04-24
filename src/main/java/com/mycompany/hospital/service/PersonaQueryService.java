package com.mycompany.hospital.service;

import com.mycompany.hospital.domain.*; // for static metamodels
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.repository.PersonaRepository;
import com.mycompany.hospital.service.criteria.PersonaCriteria;
import com.mycompany.hospital.service.dto.PersonaDTO;
import com.mycompany.hospital.service.mapper.PersonaMapper;
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
 * Service for executing complex queries for {@link Persona} entities in the database.
 * The main input is a {@link PersonaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PersonaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonaQueryService extends QueryService<Persona> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonaQueryService.class);

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    public PersonaQueryService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    /**
     * Return a {@link Page} of {@link PersonaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonaDTO> findByCriteria(PersonaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Persona> specification = createSpecification(criteria);
        return personaRepository.findAll(specification, page).map(personaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Persona> specification = createSpecification(criteria);
        return personaRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Persona> createSpecification(PersonaCriteria criteria) {
        Specification<Persona> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Persona_.id),
                buildStringSpecification(criteria.getNombre(), Persona_.nombre),
                buildStringSpecification(criteria.getApellido(), Persona_.apellido),
                buildStringSpecification(criteria.getNroDocumento(), Persona_.nroDocumento),
                buildRangeSpecification(criteria.getFechaNacimiento(), Persona_.fechaNacimiento),
                buildStringSpecification(criteria.getTelefono(), Persona_.telefono),
                buildStringSpecification(criteria.getEmail(), Persona_.email),
                buildStringSpecification(criteria.getDireccion(), Persona_.direccion),
                buildSpecification(criteria.getActivo(), Persona_.activo),
                buildRangeSpecification(criteria.getFechaAlta(), Persona_.fechaAlta),
                buildRangeSpecification(criteria.getFechaBaja(), Persona_.fechaBaja),
                buildSpecification(criteria.getTipoDocumentoId(), root ->
                    root.join(Persona_.tipoDocumento, JoinType.LEFT).get(TipoDocumento_.id)
                ),
                buildSpecification(criteria.getSexoId(), root -> root.join(Persona_.sexo, JoinType.LEFT).get(Sexo_.id)),
                buildSpecification(criteria.getCiudadId(), root -> root.join(Persona_.ciudad, JoinType.LEFT).get(Ciudad_.id)),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Persona_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getEmpleadoId(), root -> root.join(Persona_.empleado, JoinType.LEFT).get(Empleado_.id))
            );
        }
        return specification;
    }
}
