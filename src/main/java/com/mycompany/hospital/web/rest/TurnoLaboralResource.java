package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.TurnoLaboralRepository;
import com.mycompany.hospital.service.TurnoLaboralQueryService;
import com.mycompany.hospital.service.TurnoLaboralService;
import com.mycompany.hospital.service.criteria.TurnoLaboralCriteria;
import com.mycompany.hospital.service.dto.TurnoLaboralDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.TurnoLaboral}.
 */
@RestController
@RequestMapping("/api/turno-laborals")
public class TurnoLaboralResource {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoLaboralResource.class);

    private static final String ENTITY_NAME = "turnoLaboral";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final TurnoLaboralService turnoLaboralService;

    private final TurnoLaboralRepository turnoLaboralRepository;

    private final TurnoLaboralQueryService turnoLaboralQueryService;

    public TurnoLaboralResource(
        TurnoLaboralService turnoLaboralService,
        TurnoLaboralRepository turnoLaboralRepository,
        TurnoLaboralQueryService turnoLaboralQueryService
    ) {
        this.turnoLaboralService = turnoLaboralService;
        this.turnoLaboralRepository = turnoLaboralRepository;
        this.turnoLaboralQueryService = turnoLaboralQueryService;
    }

    /**
     * {@code POST  /turno-laborals} : Create a new turnoLaboral.
     *
     * @param turnoLaboralDTO the turnoLaboralDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turnoLaboralDTO, or with status {@code 400 (Bad Request)} if the turnoLaboral has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TurnoLaboralDTO> createTurnoLaboral(@Valid @RequestBody TurnoLaboralDTO turnoLaboralDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TurnoLaboral : {}", turnoLaboralDTO);
        if (turnoLaboralDTO.getId() != null) {
            throw new BadRequestAlertException("A new turnoLaboral cannot already have an ID", ENTITY_NAME, "idexists");
        }
        turnoLaboralDTO = turnoLaboralService.save(turnoLaboralDTO);
        return ResponseEntity.created(new URI("/api/turno-laborals/" + turnoLaboralDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, turnoLaboralDTO.getId().toString()))
            .body(turnoLaboralDTO);
    }

    /**
     * {@code PUT  /turno-laborals/:id} : Updates an existing turnoLaboral.
     *
     * @param id the id of the turnoLaboralDTO to save.
     * @param turnoLaboralDTO the turnoLaboralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnoLaboralDTO,
     * or with status {@code 400 (Bad Request)} if the turnoLaboralDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turnoLaboralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TurnoLaboralDTO> updateTurnoLaboral(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TurnoLaboralDTO turnoLaboralDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TurnoLaboral : {}, {}", id, turnoLaboralDTO);
        if (turnoLaboralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnoLaboralDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnoLaboralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        turnoLaboralDTO = turnoLaboralService.update(turnoLaboralDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, turnoLaboralDTO.getId().toString()))
            .body(turnoLaboralDTO);
    }

    /**
     * {@code PATCH  /turno-laborals/:id} : Partial updates given fields of an existing turnoLaboral, field will ignore if it is null
     *
     * @param id the id of the turnoLaboralDTO to save.
     * @param turnoLaboralDTO the turnoLaboralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnoLaboralDTO,
     * or with status {@code 400 (Bad Request)} if the turnoLaboralDTO is not valid,
     * or with status {@code 404 (Not Found)} if the turnoLaboralDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the turnoLaboralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TurnoLaboralDTO> partialUpdateTurnoLaboral(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TurnoLaboralDTO turnoLaboralDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TurnoLaboral partially : {}, {}", id, turnoLaboralDTO);
        if (turnoLaboralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnoLaboralDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnoLaboralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TurnoLaboralDTO> result = turnoLaboralService.partialUpdate(turnoLaboralDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, turnoLaboralDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /turno-laborals} : get all the Turno Laborals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Turno Laborals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TurnoLaboralDTO>> getAllTurnoLaborals(
        TurnoLaboralCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TurnoLaborals by criteria: {}", criteria);

        Page<TurnoLaboralDTO> page = turnoLaboralQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /turno-laborals/count} : count all the turnoLaborals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTurnoLaborals(TurnoLaboralCriteria criteria) {
        LOG.debug("REST request to count TurnoLaborals by criteria: {}", criteria);
        return ResponseEntity.ok().body(turnoLaboralQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /turno-laborals/:id} : get the "id" turnoLaboral.
     *
     * @param id the id of the turnoLaboralDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turnoLaboralDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurnoLaboralDTO> getTurnoLaboral(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TurnoLaboral : {}", id);
        Optional<TurnoLaboralDTO> turnoLaboralDTO = turnoLaboralService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turnoLaboralDTO);
    }

    /**
     * {@code DELETE  /turno-laborals/:id} : delete the "id" turnoLaboral.
     *
     * @param id the id of the turnoLaboralDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurnoLaboral(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TurnoLaboral : {}", id);
        turnoLaboralService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
