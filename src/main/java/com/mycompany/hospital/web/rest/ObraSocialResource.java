package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.ObraSocialRepository;
import com.mycompany.hospital.service.ObraSocialQueryService;
import com.mycompany.hospital.service.ObraSocialService;
import com.mycompany.hospital.service.criteria.ObraSocialCriteria;
import com.mycompany.hospital.service.dto.ObraSocialDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.ObraSocial}.
 */
@RestController
@RequestMapping("/api/obra-socials")
public class ObraSocialResource {

    private static final Logger LOG = LoggerFactory.getLogger(ObraSocialResource.class);

    private static final String ENTITY_NAME = "obraSocial";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final ObraSocialService obraSocialService;

    private final ObraSocialRepository obraSocialRepository;

    private final ObraSocialQueryService obraSocialQueryService;

    public ObraSocialResource(
        ObraSocialService obraSocialService,
        ObraSocialRepository obraSocialRepository,
        ObraSocialQueryService obraSocialQueryService
    ) {
        this.obraSocialService = obraSocialService;
        this.obraSocialRepository = obraSocialRepository;
        this.obraSocialQueryService = obraSocialQueryService;
    }

    /**
     * {@code POST  /obra-socials} : Create a new obraSocial.
     *
     * @param obraSocialDTO the obraSocialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new obraSocialDTO, or with status {@code 400 (Bad Request)} if the obraSocial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ObraSocialDTO> createObraSocial(@Valid @RequestBody ObraSocialDTO obraSocialDTO) throws URISyntaxException {
        LOG.debug("REST request to save ObraSocial : {}", obraSocialDTO);
        if (obraSocialDTO.getId() != null) {
            throw new BadRequestAlertException("A new obraSocial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        obraSocialDTO = obraSocialService.save(obraSocialDTO);
        return ResponseEntity.created(new URI("/api/obra-socials/" + obraSocialDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, obraSocialDTO.getId().toString()))
            .body(obraSocialDTO);
    }

    /**
     * {@code PUT  /obra-socials/:id} : Updates an existing obraSocial.
     *
     * @param id the id of the obraSocialDTO to save.
     * @param obraSocialDTO the obraSocialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated obraSocialDTO,
     * or with status {@code 400 (Bad Request)} if the obraSocialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the obraSocialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ObraSocialDTO> updateObraSocial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ObraSocialDTO obraSocialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ObraSocial : {}, {}", id, obraSocialDTO);
        if (obraSocialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obraSocialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!obraSocialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        obraSocialDTO = obraSocialService.update(obraSocialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, obraSocialDTO.getId().toString()))
            .body(obraSocialDTO);
    }

    /**
     * {@code PATCH  /obra-socials/:id} : Partial updates given fields of an existing obraSocial, field will ignore if it is null
     *
     * @param id the id of the obraSocialDTO to save.
     * @param obraSocialDTO the obraSocialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated obraSocialDTO,
     * or with status {@code 400 (Bad Request)} if the obraSocialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the obraSocialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the obraSocialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ObraSocialDTO> partialUpdateObraSocial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ObraSocialDTO obraSocialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ObraSocial partially : {}, {}", id, obraSocialDTO);
        if (obraSocialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obraSocialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!obraSocialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ObraSocialDTO> result = obraSocialService.partialUpdate(obraSocialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, obraSocialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /obra-socials} : get all the Obra Socials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Obra Socials in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ObraSocialDTO>> getAllObraSocials(
        ObraSocialCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ObraSocials by criteria: {}", criteria);

        Page<ObraSocialDTO> page = obraSocialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /obra-socials/count} : count all the obraSocials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countObraSocials(ObraSocialCriteria criteria) {
        LOG.debug("REST request to count ObraSocials by criteria: {}", criteria);
        return ResponseEntity.ok().body(obraSocialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /obra-socials/:id} : get the "id" obraSocial.
     *
     * @param id the id of the obraSocialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the obraSocialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ObraSocialDTO> getObraSocial(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ObraSocial : {}", id);
        Optional<ObraSocialDTO> obraSocialDTO = obraSocialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(obraSocialDTO);
    }

    /**
     * {@code DELETE  /obra-socials/:id} : delete the "id" obraSocial.
     *
     * @param id the id of the obraSocialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObraSocial(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ObraSocial : {}", id);
        obraSocialService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
