package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.FactorRhRepository;
import com.mycompany.hospital.service.FactorRhQueryService;
import com.mycompany.hospital.service.FactorRhService;
import com.mycompany.hospital.service.criteria.FactorRhCriteria;
import com.mycompany.hospital.service.dto.FactorRhDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.FactorRh}.
 */
@RestController
@RequestMapping("/api/factor-rhs")
public class FactorRhResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactorRhResource.class);

    private static final String ENTITY_NAME = "factorRh";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final FactorRhService factorRhService;

    private final FactorRhRepository factorRhRepository;

    private final FactorRhQueryService factorRhQueryService;

    public FactorRhResource(
        FactorRhService factorRhService,
        FactorRhRepository factorRhRepository,
        FactorRhQueryService factorRhQueryService
    ) {
        this.factorRhService = factorRhService;
        this.factorRhRepository = factorRhRepository;
        this.factorRhQueryService = factorRhQueryService;
    }

    /**
     * {@code POST  /factor-rhs} : Create a new factorRh.
     *
     * @param factorRhDTO the factorRhDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factorRhDTO, or with status {@code 400 (Bad Request)} if the factorRh has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactorRhDTO> createFactorRh(@Valid @RequestBody FactorRhDTO factorRhDTO) throws URISyntaxException {
        LOG.debug("REST request to save FactorRh : {}", factorRhDTO);
        if (factorRhDTO.getId() != null) {
            throw new BadRequestAlertException("A new factorRh cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factorRhDTO = factorRhService.save(factorRhDTO);
        return ResponseEntity.created(new URI("/api/factor-rhs/" + factorRhDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factorRhDTO.getId().toString()))
            .body(factorRhDTO);
    }

    /**
     * {@code PUT  /factor-rhs/:id} : Updates an existing factorRh.
     *
     * @param id the id of the factorRhDTO to save.
     * @param factorRhDTO the factorRhDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorRhDTO,
     * or with status {@code 400 (Bad Request)} if the factorRhDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factorRhDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactorRhDTO> updateFactorRh(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactorRhDTO factorRhDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactorRh : {}, {}", id, factorRhDTO);
        if (factorRhDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorRhDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorRhRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factorRhDTO = factorRhService.update(factorRhDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factorRhDTO.getId().toString()))
            .body(factorRhDTO);
    }

    /**
     * {@code PATCH  /factor-rhs/:id} : Partial updates given fields of an existing factorRh, field will ignore if it is null
     *
     * @param id the id of the factorRhDTO to save.
     * @param factorRhDTO the factorRhDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorRhDTO,
     * or with status {@code 400 (Bad Request)} if the factorRhDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factorRhDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factorRhDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactorRhDTO> partialUpdateFactorRh(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactorRhDTO factorRhDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactorRh partially : {}, {}", id, factorRhDTO);
        if (factorRhDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorRhDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorRhRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactorRhDTO> result = factorRhService.partialUpdate(factorRhDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factorRhDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factor-rhs} : get all the Factor Rhs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Factor Rhs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FactorRhDTO>> getAllFactorRhs(
        FactorRhCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FactorRhs by criteria: {}", criteria);

        Page<FactorRhDTO> page = factorRhQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factor-rhs/count} : count all the factorRhs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFactorRhs(FactorRhCriteria criteria) {
        LOG.debug("REST request to count FactorRhs by criteria: {}", criteria);
        return ResponseEntity.ok().body(factorRhQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factor-rhs/:id} : get the "id" factorRh.
     *
     * @param id the id of the factorRhDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factorRhDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactorRhDTO> getFactorRh(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FactorRh : {}", id);
        Optional<FactorRhDTO> factorRhDTO = factorRhService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factorRhDTO);
    }

    /**
     * {@code DELETE  /factor-rhs/:id} : delete the "id" factorRh.
     *
     * @param id the id of the factorRhDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactorRh(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FactorRh : {}", id);
        factorRhService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
