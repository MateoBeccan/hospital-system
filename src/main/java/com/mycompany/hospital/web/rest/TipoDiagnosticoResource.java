package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.TipoDiagnosticoRepository;
import com.mycompany.hospital.service.TipoDiagnosticoQueryService;
import com.mycompany.hospital.service.TipoDiagnosticoService;
import com.mycompany.hospital.service.criteria.TipoDiagnosticoCriteria;
import com.mycompany.hospital.service.dto.TipoDiagnosticoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.TipoDiagnostico}.
 */
@RestController
@RequestMapping("/api/tipo-diagnosticos")
public class TipoDiagnosticoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoDiagnosticoResource.class);

    private static final String ENTITY_NAME = "tipoDiagnostico";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final TipoDiagnosticoService tipoDiagnosticoService;

    private final TipoDiagnosticoRepository tipoDiagnosticoRepository;

    private final TipoDiagnosticoQueryService tipoDiagnosticoQueryService;

    public TipoDiagnosticoResource(
        TipoDiagnosticoService tipoDiagnosticoService,
        TipoDiagnosticoRepository tipoDiagnosticoRepository,
        TipoDiagnosticoQueryService tipoDiagnosticoQueryService
    ) {
        this.tipoDiagnosticoService = tipoDiagnosticoService;
        this.tipoDiagnosticoRepository = tipoDiagnosticoRepository;
        this.tipoDiagnosticoQueryService = tipoDiagnosticoQueryService;
    }

    /**
     * {@code POST  /tipo-diagnosticos} : Create a new tipoDiagnostico.
     *
     * @param tipoDiagnosticoDTO the tipoDiagnosticoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoDiagnosticoDTO, or with status {@code 400 (Bad Request)} if the tipoDiagnostico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoDiagnosticoDTO> createTipoDiagnostico(@Valid @RequestBody TipoDiagnosticoDTO tipoDiagnosticoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoDiagnostico : {}", tipoDiagnosticoDTO);
        if (tipoDiagnosticoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoDiagnostico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoDiagnosticoDTO = tipoDiagnosticoService.save(tipoDiagnosticoDTO);
        return ResponseEntity.created(new URI("/api/tipo-diagnosticos/" + tipoDiagnosticoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tipoDiagnosticoDTO.getId().toString()))
            .body(tipoDiagnosticoDTO);
    }

    /**
     * {@code PUT  /tipo-diagnosticos/:id} : Updates an existing tipoDiagnostico.
     *
     * @param id the id of the tipoDiagnosticoDTO to save.
     * @param tipoDiagnosticoDTO the tipoDiagnosticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoDiagnosticoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoDiagnosticoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoDiagnosticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoDiagnosticoDTO> updateTipoDiagnostico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoDiagnosticoDTO tipoDiagnosticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoDiagnostico : {}, {}", id, tipoDiagnosticoDTO);
        if (tipoDiagnosticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoDiagnosticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoDiagnosticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoDiagnosticoDTO = tipoDiagnosticoService.update(tipoDiagnosticoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoDiagnosticoDTO.getId().toString()))
            .body(tipoDiagnosticoDTO);
    }

    /**
     * {@code PATCH  /tipo-diagnosticos/:id} : Partial updates given fields of an existing tipoDiagnostico, field will ignore if it is null
     *
     * @param id the id of the tipoDiagnosticoDTO to save.
     * @param tipoDiagnosticoDTO the tipoDiagnosticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoDiagnosticoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoDiagnosticoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoDiagnosticoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoDiagnosticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoDiagnosticoDTO> partialUpdateTipoDiagnostico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoDiagnosticoDTO tipoDiagnosticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoDiagnostico partially : {}, {}", id, tipoDiagnosticoDTO);
        if (tipoDiagnosticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoDiagnosticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoDiagnosticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoDiagnosticoDTO> result = tipoDiagnosticoService.partialUpdate(tipoDiagnosticoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoDiagnosticoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-diagnosticos} : get all the Tipo Diagnosticos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tipo Diagnosticos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoDiagnosticoDTO>> getAllTipoDiagnosticos(
        TipoDiagnosticoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TipoDiagnosticos by criteria: {}", criteria);

        Page<TipoDiagnosticoDTO> page = tipoDiagnosticoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-diagnosticos/count} : count all the tipoDiagnosticos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoDiagnosticos(TipoDiagnosticoCriteria criteria) {
        LOG.debug("REST request to count TipoDiagnosticos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoDiagnosticoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-diagnosticos/:id} : get the "id" tipoDiagnostico.
     *
     * @param id the id of the tipoDiagnosticoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoDiagnosticoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoDiagnosticoDTO> getTipoDiagnostico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoDiagnostico : {}", id);
        Optional<TipoDiagnosticoDTO> tipoDiagnosticoDTO = tipoDiagnosticoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoDiagnosticoDTO);
    }

    /**
     * {@code DELETE  /tipo-diagnosticos/:id} : delete the "id" tipoDiagnostico.
     *
     * @param id the id of the tipoDiagnosticoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoDiagnostico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoDiagnostico : {}", id);
        tipoDiagnosticoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
