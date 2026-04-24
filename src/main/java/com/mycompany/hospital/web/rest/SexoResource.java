package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.SexoRepository;
import com.mycompany.hospital.service.SexoQueryService;
import com.mycompany.hospital.service.SexoService;
import com.mycompany.hospital.service.criteria.SexoCriteria;
import com.mycompany.hospital.service.dto.SexoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.Sexo}.
 */
@RestController
@RequestMapping("/api/sexos")
public class SexoResource {

    private static final Logger LOG = LoggerFactory.getLogger(SexoResource.class);

    private static final String ENTITY_NAME = "sexo";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final SexoService sexoService;

    private final SexoRepository sexoRepository;

    private final SexoQueryService sexoQueryService;

    public SexoResource(SexoService sexoService, SexoRepository sexoRepository, SexoQueryService sexoQueryService) {
        this.sexoService = sexoService;
        this.sexoRepository = sexoRepository;
        this.sexoQueryService = sexoQueryService;
    }

    /**
     * {@code POST  /sexos} : Create a new sexo.
     *
     * @param sexoDTO the sexoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sexoDTO, or with status {@code 400 (Bad Request)} if the sexo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SexoDTO> createSexo(@Valid @RequestBody SexoDTO sexoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Sexo : {}", sexoDTO);
        if (sexoDTO.getId() != null) {
            throw new BadRequestAlertException("A new sexo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sexoDTO = sexoService.save(sexoDTO);
        return ResponseEntity.created(new URI("/api/sexos/" + sexoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sexoDTO.getId().toString()))
            .body(sexoDTO);
    }

    /**
     * {@code PUT  /sexos/:id} : Updates an existing sexo.
     *
     * @param id the id of the sexoDTO to save.
     * @param sexoDTO the sexoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sexoDTO,
     * or with status {@code 400 (Bad Request)} if the sexoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sexoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SexoDTO> updateSexo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SexoDTO sexoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Sexo : {}, {}", id, sexoDTO);
        if (sexoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sexoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sexoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sexoDTO = sexoService.update(sexoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sexoDTO.getId().toString()))
            .body(sexoDTO);
    }

    /**
     * {@code PATCH  /sexos/:id} : Partial updates given fields of an existing sexo, field will ignore if it is null
     *
     * @param id the id of the sexoDTO to save.
     * @param sexoDTO the sexoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sexoDTO,
     * or with status {@code 400 (Bad Request)} if the sexoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sexoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sexoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SexoDTO> partialUpdateSexo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SexoDTO sexoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Sexo partially : {}, {}", id, sexoDTO);
        if (sexoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sexoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sexoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SexoDTO> result = sexoService.partialUpdate(sexoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sexoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sexos} : get all the Sexos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Sexos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SexoDTO>> getAllSexos(
        SexoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Sexos by criteria: {}", criteria);

        Page<SexoDTO> page = sexoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sexos/count} : count all the sexos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSexos(SexoCriteria criteria) {
        LOG.debug("REST request to count Sexos by criteria: {}", criteria);
        return ResponseEntity.ok().body(sexoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sexos/:id} : get the "id" sexo.
     *
     * @param id the id of the sexoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sexoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SexoDTO> getSexo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Sexo : {}", id);
        Optional<SexoDTO> sexoDTO = sexoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sexoDTO);
    }

    /**
     * {@code DELETE  /sexos/:id} : delete the "id" sexo.
     *
     * @param id the id of the sexoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSexo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Sexo : {}", id);
        sexoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
