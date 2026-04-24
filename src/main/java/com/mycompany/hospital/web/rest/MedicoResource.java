package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.MedicoRepository;
import com.mycompany.hospital.service.MedicoQueryService;
import com.mycompany.hospital.service.MedicoService;
import com.mycompany.hospital.service.criteria.MedicoCriteria;
import com.mycompany.hospital.service.dto.MedicoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.Medico}.
 */
@RestController
@RequestMapping("/api/medicos")
public class MedicoResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicoResource.class);

    private static final String ENTITY_NAME = "medico";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final MedicoService medicoService;

    private final MedicoRepository medicoRepository;

    private final MedicoQueryService medicoQueryService;

    public MedicoResource(MedicoService medicoService, MedicoRepository medicoRepository, MedicoQueryService medicoQueryService) {
        this.medicoService = medicoService;
        this.medicoRepository = medicoRepository;
        this.medicoQueryService = medicoQueryService;
    }

    /**
     * {@code POST  /medicos} : Create a new medico.
     *
     * @param medicoDTO the medicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicoDTO, or with status {@code 400 (Bad Request)} if the medico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicoDTO> createMedico(@Valid @RequestBody MedicoDTO medicoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Medico : {}", medicoDTO);
        if (medicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new medico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicoDTO = medicoService.save(medicoDTO);
        return ResponseEntity.created(new URI("/api/medicos/" + medicoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicoDTO.getId().toString()))
            .body(medicoDTO);
    }

    /**
     * {@code PUT  /medicos/:id} : Updates an existing medico.
     *
     * @param id the id of the medicoDTO to save.
     * @param medicoDTO the medicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicoDTO,
     * or with status {@code 400 (Bad Request)} if the medicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> updateMedico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicoDTO medicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Medico : {}, {}", id, medicoDTO);
        if (medicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicoDTO = medicoService.update(medicoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicoDTO.getId().toString()))
            .body(medicoDTO);
    }

    /**
     * {@code PATCH  /medicos/:id} : Partial updates given fields of an existing medico, field will ignore if it is null
     *
     * @param id the id of the medicoDTO to save.
     * @param medicoDTO the medicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicoDTO,
     * or with status {@code 400 (Bad Request)} if the medicoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicoDTO> partialUpdateMedico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicoDTO medicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Medico partially : {}, {}", id, medicoDTO);
        if (medicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicoDTO> result = medicoService.partialUpdate(medicoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medicos} : get all the Medicos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Medicos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicoDTO>> getAllMedicos(
        MedicoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Medicos by criteria: {}", criteria);

        Page<MedicoDTO> page = medicoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicos/count} : count all the medicos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedicos(MedicoCriteria criteria) {
        LOG.debug("REST request to count Medicos by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medicos/:id} : get the "id" medico.
     *
     * @param id the id of the medicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> getMedico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Medico : {}", id);
        Optional<MedicoDTO> medicoDTO = medicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicoDTO);
    }

    /**
     * {@code DELETE  /medicos/:id} : delete the "id" medico.
     *
     * @param id the id of the medicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Medico : {}", id);
        medicoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
