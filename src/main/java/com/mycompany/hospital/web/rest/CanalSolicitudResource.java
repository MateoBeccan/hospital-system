package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.CanalSolicitudRepository;
import com.mycompany.hospital.service.CanalSolicitudQueryService;
import com.mycompany.hospital.service.CanalSolicitudService;
import com.mycompany.hospital.service.criteria.CanalSolicitudCriteria;
import com.mycompany.hospital.service.dto.CanalSolicitudDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.CanalSolicitud}.
 */
@RestController
@RequestMapping("/api/canal-solicituds")
public class CanalSolicitudResource {

    private static final Logger LOG = LoggerFactory.getLogger(CanalSolicitudResource.class);

    private static final String ENTITY_NAME = "canalSolicitud";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final CanalSolicitudService canalSolicitudService;

    private final CanalSolicitudRepository canalSolicitudRepository;

    private final CanalSolicitudQueryService canalSolicitudQueryService;

    public CanalSolicitudResource(
        CanalSolicitudService canalSolicitudService,
        CanalSolicitudRepository canalSolicitudRepository,
        CanalSolicitudQueryService canalSolicitudQueryService
    ) {
        this.canalSolicitudService = canalSolicitudService;
        this.canalSolicitudRepository = canalSolicitudRepository;
        this.canalSolicitudQueryService = canalSolicitudQueryService;
    }

    /**
     * {@code POST  /canal-solicituds} : Create a new canalSolicitud.
     *
     * @param canalSolicitudDTO the canalSolicitudDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new canalSolicitudDTO, or with status {@code 400 (Bad Request)} if the canalSolicitud has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CanalSolicitudDTO> createCanalSolicitud(@Valid @RequestBody CanalSolicitudDTO canalSolicitudDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CanalSolicitud : {}", canalSolicitudDTO);
        if (canalSolicitudDTO.getId() != null) {
            throw new BadRequestAlertException("A new canalSolicitud cannot already have an ID", ENTITY_NAME, "idexists");
        }
        canalSolicitudDTO = canalSolicitudService.save(canalSolicitudDTO);
        return ResponseEntity.created(new URI("/api/canal-solicituds/" + canalSolicitudDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, canalSolicitudDTO.getId().toString()))
            .body(canalSolicitudDTO);
    }

    /**
     * {@code PUT  /canal-solicituds/:id} : Updates an existing canalSolicitud.
     *
     * @param id the id of the canalSolicitudDTO to save.
     * @param canalSolicitudDTO the canalSolicitudDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canalSolicitudDTO,
     * or with status {@code 400 (Bad Request)} if the canalSolicitudDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the canalSolicitudDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CanalSolicitudDTO> updateCanalSolicitud(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CanalSolicitudDTO canalSolicitudDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CanalSolicitud : {}, {}", id, canalSolicitudDTO);
        if (canalSolicitudDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canalSolicitudDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canalSolicitudRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        canalSolicitudDTO = canalSolicitudService.update(canalSolicitudDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canalSolicitudDTO.getId().toString()))
            .body(canalSolicitudDTO);
    }

    /**
     * {@code PATCH  /canal-solicituds/:id} : Partial updates given fields of an existing canalSolicitud, field will ignore if it is null
     *
     * @param id the id of the canalSolicitudDTO to save.
     * @param canalSolicitudDTO the canalSolicitudDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canalSolicitudDTO,
     * or with status {@code 400 (Bad Request)} if the canalSolicitudDTO is not valid,
     * or with status {@code 404 (Not Found)} if the canalSolicitudDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the canalSolicitudDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CanalSolicitudDTO> partialUpdateCanalSolicitud(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CanalSolicitudDTO canalSolicitudDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CanalSolicitud partially : {}, {}", id, canalSolicitudDTO);
        if (canalSolicitudDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canalSolicitudDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canalSolicitudRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CanalSolicitudDTO> result = canalSolicitudService.partialUpdate(canalSolicitudDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canalSolicitudDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /canal-solicituds} : get all the Canal Solicituds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Canal Solicituds in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CanalSolicitudDTO>> getAllCanalSolicituds(
        CanalSolicitudCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CanalSolicituds by criteria: {}", criteria);

        Page<CanalSolicitudDTO> page = canalSolicitudQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /canal-solicituds/count} : count all the canalSolicituds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCanalSolicituds(CanalSolicitudCriteria criteria) {
        LOG.debug("REST request to count CanalSolicituds by criteria: {}", criteria);
        return ResponseEntity.ok().body(canalSolicitudQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /canal-solicituds/:id} : get the "id" canalSolicitud.
     *
     * @param id the id of the canalSolicitudDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the canalSolicitudDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CanalSolicitudDTO> getCanalSolicitud(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CanalSolicitud : {}", id);
        Optional<CanalSolicitudDTO> canalSolicitudDTO = canalSolicitudService.findOne(id);
        return ResponseUtil.wrapOrNotFound(canalSolicitudDTO);
    }

    /**
     * {@code DELETE  /canal-solicituds/:id} : delete the "id" canalSolicitud.
     *
     * @param id the id of the canalSolicitudDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCanalSolicitud(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CanalSolicitud : {}", id);
        canalSolicitudService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
