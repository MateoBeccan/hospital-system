package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.EstadoTurnoRepository;
import com.mycompany.hospital.service.EstadoTurnoQueryService;
import com.mycompany.hospital.service.EstadoTurnoService;
import com.mycompany.hospital.service.criteria.EstadoTurnoCriteria;
import com.mycompany.hospital.service.dto.EstadoTurnoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.EstadoTurno}.
 */
@RestController
@RequestMapping("/api/estado-turnos")
public class EstadoTurnoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoTurnoResource.class);

    private static final String ENTITY_NAME = "estadoTurno";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final EstadoTurnoService estadoTurnoService;

    private final EstadoTurnoRepository estadoTurnoRepository;

    private final EstadoTurnoQueryService estadoTurnoQueryService;

    public EstadoTurnoResource(
        EstadoTurnoService estadoTurnoService,
        EstadoTurnoRepository estadoTurnoRepository,
        EstadoTurnoQueryService estadoTurnoQueryService
    ) {
        this.estadoTurnoService = estadoTurnoService;
        this.estadoTurnoRepository = estadoTurnoRepository;
        this.estadoTurnoQueryService = estadoTurnoQueryService;
    }

    /**
     * {@code POST  /estado-turnos} : Create a new estadoTurno.
     *
     * @param estadoTurnoDTO the estadoTurnoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadoTurnoDTO, or with status {@code 400 (Bad Request)} if the estadoTurno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EstadoTurnoDTO> createEstadoTurno(@Valid @RequestBody EstadoTurnoDTO estadoTurnoDTO) throws URISyntaxException {
        LOG.debug("REST request to save EstadoTurno : {}", estadoTurnoDTO);
        if (estadoTurnoDTO.getId() != null) {
            throw new BadRequestAlertException("A new estadoTurno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        estadoTurnoDTO = estadoTurnoService.save(estadoTurnoDTO);
        return ResponseEntity.created(new URI("/api/estado-turnos/" + estadoTurnoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, estadoTurnoDTO.getId().toString()))
            .body(estadoTurnoDTO);
    }

    /**
     * {@code PUT  /estado-turnos/:id} : Updates an existing estadoTurno.
     *
     * @param id the id of the estadoTurnoDTO to save.
     * @param estadoTurnoDTO the estadoTurnoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoTurnoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoTurnoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadoTurnoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoTurnoDTO> updateEstadoTurno(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstadoTurnoDTO estadoTurnoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EstadoTurno : {}, {}", id, estadoTurnoDTO);
        if (estadoTurnoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoTurnoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoTurnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        estadoTurnoDTO = estadoTurnoService.update(estadoTurnoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoTurnoDTO.getId().toString()))
            .body(estadoTurnoDTO);
    }

    /**
     * {@code PATCH  /estado-turnos/:id} : Partial updates given fields of an existing estadoTurno, field will ignore if it is null
     *
     * @param id the id of the estadoTurnoDTO to save.
     * @param estadoTurnoDTO the estadoTurnoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoTurnoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoTurnoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estadoTurnoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estadoTurnoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstadoTurnoDTO> partialUpdateEstadoTurno(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstadoTurnoDTO estadoTurnoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EstadoTurno partially : {}, {}", id, estadoTurnoDTO);
        if (estadoTurnoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoTurnoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoTurnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstadoTurnoDTO> result = estadoTurnoService.partialUpdate(estadoTurnoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoTurnoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estado-turnos} : get all the Estado Turnos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Estado Turnos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EstadoTurnoDTO>> getAllEstadoTurnos(
        EstadoTurnoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EstadoTurnos by criteria: {}", criteria);

        Page<EstadoTurnoDTO> page = estadoTurnoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estado-turnos/count} : count all the estadoTurnos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEstadoTurnos(EstadoTurnoCriteria criteria) {
        LOG.debug("REST request to count EstadoTurnos by criteria: {}", criteria);
        return ResponseEntity.ok().body(estadoTurnoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estado-turnos/:id} : get the "id" estadoTurno.
     *
     * @param id the id of the estadoTurnoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadoTurnoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoTurnoDTO> getEstadoTurno(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EstadoTurno : {}", id);
        Optional<EstadoTurnoDTO> estadoTurnoDTO = estadoTurnoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadoTurnoDTO);
    }

    /**
     * {@code DELETE  /estado-turnos/:id} : delete the "id" estadoTurno.
     *
     * @param id the id of the estadoTurnoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoTurno(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EstadoTurno : {}", id);
        estadoTurnoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
