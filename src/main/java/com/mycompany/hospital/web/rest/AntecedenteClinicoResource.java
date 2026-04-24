package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.AntecedenteClinicoRepository;
import com.mycompany.hospital.service.AntecedenteClinicoQueryService;
import com.mycompany.hospital.service.AntecedenteClinicoService;
import com.mycompany.hospital.service.criteria.AntecedenteClinicoCriteria;
import com.mycompany.hospital.service.dto.AntecedenteClinicoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.AntecedenteClinico}.
 */
@RestController
@RequestMapping("/api/antecedente-clinicos")
public class AntecedenteClinicoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AntecedenteClinicoResource.class);

    private static final String ENTITY_NAME = "antecedenteClinico";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final AntecedenteClinicoService antecedenteClinicoService;

    private final AntecedenteClinicoRepository antecedenteClinicoRepository;

    private final AntecedenteClinicoQueryService antecedenteClinicoQueryService;

    public AntecedenteClinicoResource(
        AntecedenteClinicoService antecedenteClinicoService,
        AntecedenteClinicoRepository antecedenteClinicoRepository,
        AntecedenteClinicoQueryService antecedenteClinicoQueryService
    ) {
        this.antecedenteClinicoService = antecedenteClinicoService;
        this.antecedenteClinicoRepository = antecedenteClinicoRepository;
        this.antecedenteClinicoQueryService = antecedenteClinicoQueryService;
    }

    /**
     * {@code POST  /antecedente-clinicos} : Create a new antecedenteClinico.
     *
     * @param antecedenteClinicoDTO the antecedenteClinicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new antecedenteClinicoDTO, or with status {@code 400 (Bad Request)} if the antecedenteClinico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AntecedenteClinicoDTO> createAntecedenteClinico(@Valid @RequestBody AntecedenteClinicoDTO antecedenteClinicoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AntecedenteClinico : {}", antecedenteClinicoDTO);
        if (antecedenteClinicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new antecedenteClinico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        antecedenteClinicoDTO = antecedenteClinicoService.save(antecedenteClinicoDTO);
        return ResponseEntity.created(new URI("/api/antecedente-clinicos/" + antecedenteClinicoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, antecedenteClinicoDTO.getId().toString()))
            .body(antecedenteClinicoDTO);
    }

    /**
     * {@code PUT  /antecedente-clinicos/:id} : Updates an existing antecedenteClinico.
     *
     * @param id the id of the antecedenteClinicoDTO to save.
     * @param antecedenteClinicoDTO the antecedenteClinicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated antecedenteClinicoDTO,
     * or with status {@code 400 (Bad Request)} if the antecedenteClinicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the antecedenteClinicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AntecedenteClinicoDTO> updateAntecedenteClinico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AntecedenteClinicoDTO antecedenteClinicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AntecedenteClinico : {}, {}", id, antecedenteClinicoDTO);
        if (antecedenteClinicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, antecedenteClinicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!antecedenteClinicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        antecedenteClinicoDTO = antecedenteClinicoService.update(antecedenteClinicoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, antecedenteClinicoDTO.getId().toString()))
            .body(antecedenteClinicoDTO);
    }

    /**
     * {@code PATCH  /antecedente-clinicos/:id} : Partial updates given fields of an existing antecedenteClinico, field will ignore if it is null
     *
     * @param id the id of the antecedenteClinicoDTO to save.
     * @param antecedenteClinicoDTO the antecedenteClinicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated antecedenteClinicoDTO,
     * or with status {@code 400 (Bad Request)} if the antecedenteClinicoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the antecedenteClinicoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the antecedenteClinicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AntecedenteClinicoDTO> partialUpdateAntecedenteClinico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AntecedenteClinicoDTO antecedenteClinicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AntecedenteClinico partially : {}, {}", id, antecedenteClinicoDTO);
        if (antecedenteClinicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, antecedenteClinicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!antecedenteClinicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AntecedenteClinicoDTO> result = antecedenteClinicoService.partialUpdate(antecedenteClinicoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, antecedenteClinicoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /antecedente-clinicos} : get all the Antecedente Clinicos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Antecedente Clinicos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AntecedenteClinicoDTO>> getAllAntecedenteClinicos(
        AntecedenteClinicoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AntecedenteClinicos by criteria: {}", criteria);

        Page<AntecedenteClinicoDTO> page = antecedenteClinicoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /antecedente-clinicos/count} : count all the antecedenteClinicos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAntecedenteClinicos(AntecedenteClinicoCriteria criteria) {
        LOG.debug("REST request to count AntecedenteClinicos by criteria: {}", criteria);
        return ResponseEntity.ok().body(antecedenteClinicoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /antecedente-clinicos/:id} : get the "id" antecedenteClinico.
     *
     * @param id the id of the antecedenteClinicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the antecedenteClinicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AntecedenteClinicoDTO> getAntecedenteClinico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AntecedenteClinico : {}", id);
        Optional<AntecedenteClinicoDTO> antecedenteClinicoDTO = antecedenteClinicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(antecedenteClinicoDTO);
    }

    /**
     * {@code DELETE  /antecedente-clinicos/:id} : delete the "id" antecedenteClinico.
     *
     * @param id the id of the antecedenteClinicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAntecedenteClinico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AntecedenteClinico : {}", id);
        antecedenteClinicoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
