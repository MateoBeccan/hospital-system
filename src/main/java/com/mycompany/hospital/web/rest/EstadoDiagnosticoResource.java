package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.EstadoDiagnosticoRepository;
import com.mycompany.hospital.service.EstadoDiagnosticoQueryService;
import com.mycompany.hospital.service.EstadoDiagnosticoService;
import com.mycompany.hospital.service.criteria.EstadoDiagnosticoCriteria;
import com.mycompany.hospital.service.dto.EstadoDiagnosticoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.EstadoDiagnostico}.
 */
@RestController
@RequestMapping("/api/estado-diagnosticos")
public class EstadoDiagnosticoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoDiagnosticoResource.class);

    private static final String ENTITY_NAME = "estadoDiagnostico";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final EstadoDiagnosticoService estadoDiagnosticoService;

    private final EstadoDiagnosticoRepository estadoDiagnosticoRepository;

    private final EstadoDiagnosticoQueryService estadoDiagnosticoQueryService;

    public EstadoDiagnosticoResource(
        EstadoDiagnosticoService estadoDiagnosticoService,
        EstadoDiagnosticoRepository estadoDiagnosticoRepository,
        EstadoDiagnosticoQueryService estadoDiagnosticoQueryService
    ) {
        this.estadoDiagnosticoService = estadoDiagnosticoService;
        this.estadoDiagnosticoRepository = estadoDiagnosticoRepository;
        this.estadoDiagnosticoQueryService = estadoDiagnosticoQueryService;
    }

    /**
     * {@code POST  /estado-diagnosticos} : Create a new estadoDiagnostico.
     *
     * @param estadoDiagnosticoDTO the estadoDiagnosticoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadoDiagnosticoDTO, or with status {@code 400 (Bad Request)} if the estadoDiagnostico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EstadoDiagnosticoDTO> createEstadoDiagnostico(@Valid @RequestBody EstadoDiagnosticoDTO estadoDiagnosticoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EstadoDiagnostico : {}", estadoDiagnosticoDTO);
        if (estadoDiagnosticoDTO.getId() != null) {
            throw new BadRequestAlertException("A new estadoDiagnostico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        estadoDiagnosticoDTO = estadoDiagnosticoService.save(estadoDiagnosticoDTO);
        return ResponseEntity.created(new URI("/api/estado-diagnosticos/" + estadoDiagnosticoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, estadoDiagnosticoDTO.getId().toString()))
            .body(estadoDiagnosticoDTO);
    }

    /**
     * {@code PUT  /estado-diagnosticos/:id} : Updates an existing estadoDiagnostico.
     *
     * @param id the id of the estadoDiagnosticoDTO to save.
     * @param estadoDiagnosticoDTO the estadoDiagnosticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoDiagnosticoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoDiagnosticoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadoDiagnosticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoDiagnosticoDTO> updateEstadoDiagnostico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstadoDiagnosticoDTO estadoDiagnosticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EstadoDiagnostico : {}, {}", id, estadoDiagnosticoDTO);
        if (estadoDiagnosticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoDiagnosticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoDiagnosticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        estadoDiagnosticoDTO = estadoDiagnosticoService.update(estadoDiagnosticoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoDiagnosticoDTO.getId().toString()))
            .body(estadoDiagnosticoDTO);
    }

    /**
     * {@code PATCH  /estado-diagnosticos/:id} : Partial updates given fields of an existing estadoDiagnostico, field will ignore if it is null
     *
     * @param id the id of the estadoDiagnosticoDTO to save.
     * @param estadoDiagnosticoDTO the estadoDiagnosticoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoDiagnosticoDTO,
     * or with status {@code 400 (Bad Request)} if the estadoDiagnosticoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estadoDiagnosticoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estadoDiagnosticoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstadoDiagnosticoDTO> partialUpdateEstadoDiagnostico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstadoDiagnosticoDTO estadoDiagnosticoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EstadoDiagnostico partially : {}, {}", id, estadoDiagnosticoDTO);
        if (estadoDiagnosticoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoDiagnosticoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoDiagnosticoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstadoDiagnosticoDTO> result = estadoDiagnosticoService.partialUpdate(estadoDiagnosticoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoDiagnosticoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estado-diagnosticos} : get all the Estado Diagnosticos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Estado Diagnosticos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EstadoDiagnosticoDTO>> getAllEstadoDiagnosticos(
        EstadoDiagnosticoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EstadoDiagnosticos by criteria: {}", criteria);

        Page<EstadoDiagnosticoDTO> page = estadoDiagnosticoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estado-diagnosticos/count} : count all the estadoDiagnosticos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEstadoDiagnosticos(EstadoDiagnosticoCriteria criteria) {
        LOG.debug("REST request to count EstadoDiagnosticos by criteria: {}", criteria);
        return ResponseEntity.ok().body(estadoDiagnosticoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estado-diagnosticos/:id} : get the "id" estadoDiagnostico.
     *
     * @param id the id of the estadoDiagnosticoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadoDiagnosticoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDiagnosticoDTO> getEstadoDiagnostico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EstadoDiagnostico : {}", id);
        Optional<EstadoDiagnosticoDTO> estadoDiagnosticoDTO = estadoDiagnosticoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadoDiagnosticoDTO);
    }

    /**
     * {@code DELETE  /estado-diagnosticos/:id} : delete the "id" estadoDiagnostico.
     *
     * @param id the id of the estadoDiagnosticoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoDiagnostico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EstadoDiagnostico : {}", id);
        estadoDiagnosticoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
