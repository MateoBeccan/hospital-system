package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.GrupoSanguineoRepository;
import com.mycompany.hospital.service.GrupoSanguineoQueryService;
import com.mycompany.hospital.service.GrupoSanguineoService;
import com.mycompany.hospital.service.criteria.GrupoSanguineoCriteria;
import com.mycompany.hospital.service.dto.GrupoSanguineoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.GrupoSanguineo}.
 */
@RestController
@RequestMapping("/api/grupo-sanguineos")
public class GrupoSanguineoResource {

    private static final Logger LOG = LoggerFactory.getLogger(GrupoSanguineoResource.class);

    private static final String ENTITY_NAME = "grupoSanguineo";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final GrupoSanguineoService grupoSanguineoService;

    private final GrupoSanguineoRepository grupoSanguineoRepository;

    private final GrupoSanguineoQueryService grupoSanguineoQueryService;

    public GrupoSanguineoResource(
        GrupoSanguineoService grupoSanguineoService,
        GrupoSanguineoRepository grupoSanguineoRepository,
        GrupoSanguineoQueryService grupoSanguineoQueryService
    ) {
        this.grupoSanguineoService = grupoSanguineoService;
        this.grupoSanguineoRepository = grupoSanguineoRepository;
        this.grupoSanguineoQueryService = grupoSanguineoQueryService;
    }

    /**
     * {@code POST  /grupo-sanguineos} : Create a new grupoSanguineo.
     *
     * @param grupoSanguineoDTO the grupoSanguineoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new grupoSanguineoDTO, or with status {@code 400 (Bad Request)} if the grupoSanguineo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GrupoSanguineoDTO> createGrupoSanguineo(@Valid @RequestBody GrupoSanguineoDTO grupoSanguineoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save GrupoSanguineo : {}", grupoSanguineoDTO);
        if (grupoSanguineoDTO.getId() != null) {
            throw new BadRequestAlertException("A new grupoSanguineo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        grupoSanguineoDTO = grupoSanguineoService.save(grupoSanguineoDTO);
        return ResponseEntity.created(new URI("/api/grupo-sanguineos/" + grupoSanguineoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, grupoSanguineoDTO.getId().toString()))
            .body(grupoSanguineoDTO);
    }

    /**
     * {@code PUT  /grupo-sanguineos/:id} : Updates an existing grupoSanguineo.
     *
     * @param id the id of the grupoSanguineoDTO to save.
     * @param grupoSanguineoDTO the grupoSanguineoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoSanguineoDTO,
     * or with status {@code 400 (Bad Request)} if the grupoSanguineoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the grupoSanguineoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GrupoSanguineoDTO> updateGrupoSanguineo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GrupoSanguineoDTO grupoSanguineoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GrupoSanguineo : {}, {}", id, grupoSanguineoDTO);
        if (grupoSanguineoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoSanguineoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoSanguineoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        grupoSanguineoDTO = grupoSanguineoService.update(grupoSanguineoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, grupoSanguineoDTO.getId().toString()))
            .body(grupoSanguineoDTO);
    }

    /**
     * {@code PATCH  /grupo-sanguineos/:id} : Partial updates given fields of an existing grupoSanguineo, field will ignore if it is null
     *
     * @param id the id of the grupoSanguineoDTO to save.
     * @param grupoSanguineoDTO the grupoSanguineoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated grupoSanguineoDTO,
     * or with status {@code 400 (Bad Request)} if the grupoSanguineoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the grupoSanguineoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the grupoSanguineoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GrupoSanguineoDTO> partialUpdateGrupoSanguineo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GrupoSanguineoDTO grupoSanguineoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GrupoSanguineo partially : {}, {}", id, grupoSanguineoDTO);
        if (grupoSanguineoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, grupoSanguineoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!grupoSanguineoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GrupoSanguineoDTO> result = grupoSanguineoService.partialUpdate(grupoSanguineoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, grupoSanguineoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /grupo-sanguineos} : get all the Grupo Sanguineos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Grupo Sanguineos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GrupoSanguineoDTO>> getAllGrupoSanguineos(
        GrupoSanguineoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GrupoSanguineos by criteria: {}", criteria);

        Page<GrupoSanguineoDTO> page = grupoSanguineoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grupo-sanguineos/count} : count all the grupoSanguineos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGrupoSanguineos(GrupoSanguineoCriteria criteria) {
        LOG.debug("REST request to count GrupoSanguineos by criteria: {}", criteria);
        return ResponseEntity.ok().body(grupoSanguineoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grupo-sanguineos/:id} : get the "id" grupoSanguineo.
     *
     * @param id the id of the grupoSanguineoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the grupoSanguineoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GrupoSanguineoDTO> getGrupoSanguineo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GrupoSanguineo : {}", id);
        Optional<GrupoSanguineoDTO> grupoSanguineoDTO = grupoSanguineoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(grupoSanguineoDTO);
    }

    /**
     * {@code DELETE  /grupo-sanguineos/:id} : delete the "id" grupoSanguineo.
     *
     * @param id the id of the grupoSanguineoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupoSanguineo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GrupoSanguineo : {}", id);
        grupoSanguineoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
