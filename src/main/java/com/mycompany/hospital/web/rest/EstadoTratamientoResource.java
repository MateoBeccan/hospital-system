package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.EstadoTratamientoRepository;
import com.mycompany.hospital.service.EstadoTratamientoQueryService;
import com.mycompany.hospital.service.EstadoTratamientoService;
import com.mycompany.hospital.service.criteria.EstadoTratamientoCriteria;
import com.mycompany.hospital.service.dto.EstadoTratamientoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.EstadoTratamiento}.
 */
@RestController
@RequestMapping("/api/estado-tratamientos")
public class EstadoTratamientoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoTratamientoResource.class);

    private static final String ENTITY_NAME = "estadoTratamiento";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final EstadoTratamientoService estadoTratamientoService;

    private final EstadoTratamientoRepository estadoTratamientoRepository;

    private final EstadoTratamientoQueryService estadoTratamientoQueryService;

    public EstadoTratamientoResource(
        EstadoTratamientoService estadoTratamientoService,
        EstadoTratamientoRepository estadoTratamientoRepository,
        EstadoTratamientoQueryService estadoTratamientoQueryService
    ) {
        this.estadoTratamientoService = estadoTratamientoService;
        this.estadoTratamientoRepository = estadoTratamientoRepository;
        this.estadoTratamientoQueryService = estadoTratamientoQueryService;
    }

    /**
     * {@code POST  /estado-tratamientos} : Create a new estadoTratamiento.
     *
     * @param estadoTratamientoDTO the estadoTratamientoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadoTratamientoDTO, or with status {@code 400 (Bad Request)} if the estadoTratamiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EstadoTratamientoDTO> createEstadoTratamiento(@Valid @RequestBody EstadoTratamientoDTO estadoTratamientoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EstadoTratamiento : {}", estadoTratamientoDTO);
        if (estadoTratamientoDTO.getId() != null) {
            throw new BadRequestAlertException("A new estadoTratamiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        estadoTratamientoDTO = estadoTratamientoService.save(estadoTratamientoDTO);
        return ResponseEntity.created(new URI("/api/estado-tratamientos/" + estadoTratamientoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, estadoTratamientoDTO.getId().toString()))
            .body(estadoTratamientoDTO);
    }

    /**
     * {@code PUT  /estado-tratamientos/:id} : Updates an existing estadoTratamiento.
     *
     * @param id the id of the estadoTratamientoDTO to save.
     * @param estadoTratamientoDTO the estadoTratamientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoTratamientoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoTratamientoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadoTratamientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoTratamientoDTO> updateEstadoTratamiento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstadoTratamientoDTO estadoTratamientoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EstadoTratamiento : {}, {}", id, estadoTratamientoDTO);
        if (estadoTratamientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoTratamientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoTratamientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        estadoTratamientoDTO = estadoTratamientoService.update(estadoTratamientoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoTratamientoDTO.getId().toString()))
            .body(estadoTratamientoDTO);
    }

    /**
     * {@code PATCH  /estado-tratamientos/:id} : Partial updates given fields of an existing estadoTratamiento, field will ignore if it is null
     *
     * @param id the id of the estadoTratamientoDTO to save.
     * @param estadoTratamientoDTO the estadoTratamientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoTratamientoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoTratamientoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estadoTratamientoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estadoTratamientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstadoTratamientoDTO> partialUpdateEstadoTratamiento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstadoTratamientoDTO estadoTratamientoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EstadoTratamiento partially : {}, {}", id, estadoTratamientoDTO);
        if (estadoTratamientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoTratamientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoTratamientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstadoTratamientoDTO> result = estadoTratamientoService.partialUpdate(estadoTratamientoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoTratamientoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estado-tratamientos} : get all the Estado Tratamientos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Estado Tratamientos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EstadoTratamientoDTO>> getAllEstadoTratamientos(
        EstadoTratamientoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EstadoTratamientos by criteria: {}", criteria);

        Page<EstadoTratamientoDTO> page = estadoTratamientoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estado-tratamientos/count} : count all the estadoTratamientos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEstadoTratamientos(EstadoTratamientoCriteria criteria) {
        LOG.debug("REST request to count EstadoTratamientos by criteria: {}", criteria);
        return ResponseEntity.ok().body(estadoTratamientoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estado-tratamientos/:id} : get the "id" estadoTratamiento.
     *
     * @param id the id of the estadoTratamientoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadoTratamientoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoTratamientoDTO> getEstadoTratamiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EstadoTratamiento : {}", id);
        Optional<EstadoTratamientoDTO> estadoTratamientoDTO = estadoTratamientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadoTratamientoDTO);
    }

    /**
     * {@code DELETE  /estado-tratamientos/:id} : delete the "id" estadoTratamiento.
     *
     * @param id the id of the estadoTratamientoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoTratamiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EstadoTratamiento : {}", id);
        estadoTratamientoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
