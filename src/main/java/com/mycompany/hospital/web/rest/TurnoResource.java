package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.TurnoRepository;
import com.mycompany.hospital.service.TurnoQueryService;
import com.mycompany.hospital.service.TurnoService;
import com.mycompany.hospital.service.criteria.TurnoCriteria;
import com.mycompany.hospital.service.dto.TurnoDTO;
import com.mycompany.hospital.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.hospital.domain.Turno}.
 */
@RestController
@RequestMapping("/api/turnos")
public class TurnoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoResource.class);

    private static final String ENTITY_NAME = "turno";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final TurnoService turnoService;

    private final TurnoRepository turnoRepository;

    private final TurnoQueryService turnoQueryService;

    public TurnoResource(TurnoService turnoService, TurnoRepository turnoRepository, TurnoQueryService turnoQueryService) {
        this.turnoService = turnoService;
        this.turnoRepository = turnoRepository;
        this.turnoQueryService = turnoQueryService;
    }

    /**
     * {@code POST  /turnos} : Create a new turno.
     *
     * @param turnoDTO the turnoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turnoDTO, or with status {@code 400 (Bad Request)} if the turno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TurnoDTO> createTurno(@Valid @RequestBody TurnoDTO turnoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Turno : {}", turnoDTO);
        if (turnoDTO.getId() != null) {
            throw new BadRequestAlertException("A new turno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        turnoDTO = turnoService.save(turnoDTO);
        return ResponseEntity.created(new URI("/api/turnos/" + turnoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, turnoDTO.getId().toString()))
            .body(turnoDTO);
    }

    /**
     * {@code PUT  /turnos/:id} : Updates an existing turno.
     *
     * @param id the id of the turnoDTO to save.
     * @param turnoDTO the turnoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnoDTO,
     * or with status {@code 400 (Bad Request)} if the turnoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turnoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> updateTurno(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TurnoDTO turnoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Turno : {}, {}", id, turnoDTO);
        if (turnoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        turnoDTO = turnoService.update(turnoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, turnoDTO.getId().toString()))
            .body(turnoDTO);
    }

    /**
     * {@code PATCH  /turnos/:id} : Partial updates given fields of an existing turno, field will ignore if it is null
     *
     * @param id the id of the turnoDTO to save.
     * @param turnoDTO the turnoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnoDTO,
     * or with status {@code 400 (Bad Request)} if the turnoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the turnoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the turnoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TurnoDTO> partialUpdateTurno(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TurnoDTO turnoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Turno partially : {}, {}", id, turnoDTO);
        if (turnoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TurnoDTO> result = turnoService.partialUpdate(turnoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, turnoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /turnos} : get all the Turnos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Turnos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TurnoDTO>> getAllTurnos(
        TurnoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Turnos by criteria: {}", criteria);

        Page<TurnoDTO> page = turnoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /turnos/count} : count all the turnos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTurnos(TurnoCriteria criteria) {
        LOG.debug("REST request to count Turnos by criteria: {}", criteria);
        return ResponseEntity.ok().body(turnoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /turnos/:id} : get the "id" turno.
     *
     * @param id the id of the turnoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turnoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> getTurno(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Turno : {}", id);
        Optional<TurnoDTO> turnoDTO = turnoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turnoDTO);
    }

    /**
     * {@code DELETE  /turnos/:id} : delete the "id" turno.
     *
     * @param id the id of the turnoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurno(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Turno : {}", id);
        turnoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
