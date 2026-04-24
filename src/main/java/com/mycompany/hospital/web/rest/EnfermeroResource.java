package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.EnfermeroRepository;
import com.mycompany.hospital.service.EnfermeroQueryService;
import com.mycompany.hospital.service.EnfermeroService;
import com.mycompany.hospital.service.criteria.EnfermeroCriteria;
import com.mycompany.hospital.service.dto.EnfermeroDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.Enfermero}.
 */
@RestController
@RequestMapping("/api/enfermeros")
public class EnfermeroResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnfermeroResource.class);

    private static final String ENTITY_NAME = "enfermero";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final EnfermeroService enfermeroService;

    private final EnfermeroRepository enfermeroRepository;

    private final EnfermeroQueryService enfermeroQueryService;

    public EnfermeroResource(
        EnfermeroService enfermeroService,
        EnfermeroRepository enfermeroRepository,
        EnfermeroQueryService enfermeroQueryService
    ) {
        this.enfermeroService = enfermeroService;
        this.enfermeroRepository = enfermeroRepository;
        this.enfermeroQueryService = enfermeroQueryService;
    }

    /**
     * {@code POST  /enfermeros} : Create a new enfermero.
     *
     * @param enfermeroDTO the enfermeroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enfermeroDTO, or with status {@code 400 (Bad Request)} if the enfermero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnfermeroDTO> createEnfermero(@Valid @RequestBody EnfermeroDTO enfermeroDTO) throws URISyntaxException {
        LOG.debug("REST request to save Enfermero : {}", enfermeroDTO);
        if (enfermeroDTO.getId() != null) {
            throw new BadRequestAlertException("A new enfermero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        enfermeroDTO = enfermeroService.save(enfermeroDTO);
        return ResponseEntity.created(new URI("/api/enfermeros/" + enfermeroDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, enfermeroDTO.getId().toString()))
            .body(enfermeroDTO);
    }

    /**
     * {@code PUT  /enfermeros/:id} : Updates an existing enfermero.
     *
     * @param id the id of the enfermeroDTO to save.
     * @param enfermeroDTO the enfermeroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermeroDTO,
     * or with status {@code 400 (Bad Request)} if the enfermeroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enfermeroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnfermeroDTO> updateEnfermero(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnfermeroDTO enfermeroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Enfermero : {}, {}", id, enfermeroDTO);
        if (enfermeroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermeroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfermeroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        enfermeroDTO = enfermeroService.update(enfermeroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, enfermeroDTO.getId().toString()))
            .body(enfermeroDTO);
    }

    /**
     * {@code PATCH  /enfermeros/:id} : Partial updates given fields of an existing enfermero, field will ignore if it is null
     *
     * @param id the id of the enfermeroDTO to save.
     * @param enfermeroDTO the enfermeroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermeroDTO,
     * or with status {@code 400 (Bad Request)} if the enfermeroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enfermeroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enfermeroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnfermeroDTO> partialUpdateEnfermero(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnfermeroDTO enfermeroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Enfermero partially : {}, {}", id, enfermeroDTO);
        if (enfermeroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermeroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfermeroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnfermeroDTO> result = enfermeroService.partialUpdate(enfermeroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, enfermeroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enfermeros} : get all the Enfermeros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Enfermeros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EnfermeroDTO>> getAllEnfermeros(
        EnfermeroCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Enfermeros by criteria: {}", criteria);

        Page<EnfermeroDTO> page = enfermeroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enfermeros/count} : count all the enfermeros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEnfermeros(EnfermeroCriteria criteria) {
        LOG.debug("REST request to count Enfermeros by criteria: {}", criteria);
        return ResponseEntity.ok().body(enfermeroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /enfermeros/:id} : get the "id" enfermero.
     *
     * @param id the id of the enfermeroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enfermeroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnfermeroDTO> getEnfermero(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Enfermero : {}", id);
        Optional<EnfermeroDTO> enfermeroDTO = enfermeroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enfermeroDTO);
    }

    /**
     * {@code DELETE  /enfermeros/:id} : delete the "id" enfermero.
     *
     * @param id the id of the enfermeroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnfermero(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Enfermero : {}", id);
        enfermeroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
