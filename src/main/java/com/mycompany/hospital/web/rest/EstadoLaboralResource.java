package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.EstadoLaboralRepository;
import com.mycompany.hospital.service.EstadoLaboralQueryService;
import com.mycompany.hospital.service.EstadoLaboralService;
import com.mycompany.hospital.service.criteria.EstadoLaboralCriteria;
import com.mycompany.hospital.service.dto.EstadoLaboralDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.EstadoLaboral}.
 */
@RestController
@RequestMapping("/api/estado-laborals")
public class EstadoLaboralResource {

    private static final Logger LOG = LoggerFactory.getLogger(EstadoLaboralResource.class);

    private static final String ENTITY_NAME = "estadoLaboral";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final EstadoLaboralService estadoLaboralService;

    private final EstadoLaboralRepository estadoLaboralRepository;

    private final EstadoLaboralQueryService estadoLaboralQueryService;

    public EstadoLaboralResource(
        EstadoLaboralService estadoLaboralService,
        EstadoLaboralRepository estadoLaboralRepository,
        EstadoLaboralQueryService estadoLaboralQueryService
    ) {
        this.estadoLaboralService = estadoLaboralService;
        this.estadoLaboralRepository = estadoLaboralRepository;
        this.estadoLaboralQueryService = estadoLaboralQueryService;
    }

    /**
     * {@code POST  /estado-laborals} : Create a new estadoLaboral.
     *
     * @param estadoLaboralDTO the estadoLaboralDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadoLaboralDTO, or with status {@code 400 (Bad Request)} if the estadoLaboral has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EstadoLaboralDTO> createEstadoLaboral(@Valid @RequestBody EstadoLaboralDTO estadoLaboralDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EstadoLaboral : {}", estadoLaboralDTO);
        if (estadoLaboralDTO.getId() != null) {
            throw new BadRequestAlertException("A new estadoLaboral cannot already have an ID", ENTITY_NAME, "idexists");
        }
        estadoLaboralDTO = estadoLaboralService.save(estadoLaboralDTO);
        return ResponseEntity.created(new URI("/api/estado-laborals/" + estadoLaboralDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, estadoLaboralDTO.getId().toString()))
            .body(estadoLaboralDTO);
    }

    /**
     * {@code PUT  /estado-laborals/:id} : Updates an existing estadoLaboral.
     *
     * @param id the id of the estadoLaboralDTO to save.
     * @param estadoLaboralDTO the estadoLaboralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoLaboralDTO,
     * or with status {@code 400 (Bad Request)} if the estadoLaboralDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadoLaboralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoLaboralDTO> updateEstadoLaboral(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstadoLaboralDTO estadoLaboralDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EstadoLaboral : {}, {}", id, estadoLaboralDTO);
        if (estadoLaboralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoLaboralDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoLaboralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        estadoLaboralDTO = estadoLaboralService.update(estadoLaboralDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoLaboralDTO.getId().toString()))
            .body(estadoLaboralDTO);
    }

    /**
     * {@code PATCH  /estado-laborals/:id} : Partial updates given fields of an existing estadoLaboral, field will ignore if it is null
     *
     * @param id the id of the estadoLaboralDTO to save.
     * @param estadoLaboralDTO the estadoLaboralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoLaboralDTO,
     * or with status {@code 400 (Bad Request)} if the estadoLaboralDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estadoLaboralDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estadoLaboralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstadoLaboralDTO> partialUpdateEstadoLaboral(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstadoLaboralDTO estadoLaboralDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EstadoLaboral partially : {}, {}", id, estadoLaboralDTO);
        if (estadoLaboralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estadoLaboralDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estadoLaboralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstadoLaboralDTO> result = estadoLaboralService.partialUpdate(estadoLaboralDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoLaboralDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estado-laborals} : get all the Estado Laborals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Estado Laborals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EstadoLaboralDTO>> getAllEstadoLaborals(
        EstadoLaboralCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EstadoLaborals by criteria: {}", criteria);

        Page<EstadoLaboralDTO> page = estadoLaboralQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estado-laborals/count} : count all the estadoLaborals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEstadoLaborals(EstadoLaboralCriteria criteria) {
        LOG.debug("REST request to count EstadoLaborals by criteria: {}", criteria);
        return ResponseEntity.ok().body(estadoLaboralQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estado-laborals/:id} : get the "id" estadoLaboral.
     *
     * @param id the id of the estadoLaboralDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadoLaboralDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoLaboralDTO> getEstadoLaboral(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EstadoLaboral : {}", id);
        Optional<EstadoLaboralDTO> estadoLaboralDTO = estadoLaboralService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadoLaboralDTO);
    }

    /**
     * {@code DELETE  /estado-laborals/:id} : delete the "id" estadoLaboral.
     *
     * @param id the id of the estadoLaboralDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoLaboral(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EstadoLaboral : {}", id);
        estadoLaboralService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
