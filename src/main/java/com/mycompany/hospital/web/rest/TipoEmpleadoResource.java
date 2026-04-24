package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.TipoEmpleadoRepository;
import com.mycompany.hospital.service.TipoEmpleadoQueryService;
import com.mycompany.hospital.service.TipoEmpleadoService;
import com.mycompany.hospital.service.criteria.TipoEmpleadoCriteria;
import com.mycompany.hospital.service.dto.TipoEmpleadoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.TipoEmpleado}.
 */
@RestController
@RequestMapping("/api/tipo-empleados")
public class TipoEmpleadoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoEmpleadoResource.class);

    private static final String ENTITY_NAME = "tipoEmpleado";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final TipoEmpleadoService tipoEmpleadoService;

    private final TipoEmpleadoRepository tipoEmpleadoRepository;

    private final TipoEmpleadoQueryService tipoEmpleadoQueryService;

    public TipoEmpleadoResource(
        TipoEmpleadoService tipoEmpleadoService,
        TipoEmpleadoRepository tipoEmpleadoRepository,
        TipoEmpleadoQueryService tipoEmpleadoQueryService
    ) {
        this.tipoEmpleadoService = tipoEmpleadoService;
        this.tipoEmpleadoRepository = tipoEmpleadoRepository;
        this.tipoEmpleadoQueryService = tipoEmpleadoQueryService;
    }

    /**
     * {@code POST  /tipo-empleados} : Create a new tipoEmpleado.
     *
     * @param tipoEmpleadoDTO the tipoEmpleadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoEmpleadoDTO, or with status {@code 400 (Bad Request)} if the tipoEmpleado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoEmpleadoDTO> createTipoEmpleado(@Valid @RequestBody TipoEmpleadoDTO tipoEmpleadoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoEmpleado : {}", tipoEmpleadoDTO);
        if (tipoEmpleadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoEmpleado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoEmpleadoDTO = tipoEmpleadoService.save(tipoEmpleadoDTO);
        return ResponseEntity.created(new URI("/api/tipo-empleados/" + tipoEmpleadoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tipoEmpleadoDTO.getId().toString()))
            .body(tipoEmpleadoDTO);
    }

    /**
     * {@code PUT  /tipo-empleados/:id} : Updates an existing tipoEmpleado.
     *
     * @param id the id of the tipoEmpleadoDTO to save.
     * @param tipoEmpleadoDTO the tipoEmpleadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoEmpleadoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoEmpleadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoEmpleadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoEmpleadoDTO> updateTipoEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoEmpleadoDTO tipoEmpleadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoEmpleado : {}, {}", id, tipoEmpleadoDTO);
        if (tipoEmpleadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoEmpleadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoEmpleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoEmpleadoDTO = tipoEmpleadoService.update(tipoEmpleadoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoEmpleadoDTO.getId().toString()))
            .body(tipoEmpleadoDTO);
    }

    /**
     * {@code PATCH  /tipo-empleados/:id} : Partial updates given fields of an existing tipoEmpleado, field will ignore if it is null
     *
     * @param id the id of the tipoEmpleadoDTO to save.
     * @param tipoEmpleadoDTO the tipoEmpleadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoEmpleadoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoEmpleadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoEmpleadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoEmpleadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoEmpleadoDTO> partialUpdateTipoEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoEmpleadoDTO tipoEmpleadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoEmpleado partially : {}, {}", id, tipoEmpleadoDTO);
        if (tipoEmpleadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoEmpleadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoEmpleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoEmpleadoDTO> result = tipoEmpleadoService.partialUpdate(tipoEmpleadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoEmpleadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-empleados} : get all the Tipo Empleados.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tipo Empleados in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoEmpleadoDTO>> getAllTipoEmpleados(
        TipoEmpleadoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TipoEmpleados by criteria: {}", criteria);

        Page<TipoEmpleadoDTO> page = tipoEmpleadoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-empleados/count} : count all the tipoEmpleados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTipoEmpleados(TipoEmpleadoCriteria criteria) {
        LOG.debug("REST request to count TipoEmpleados by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoEmpleadoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-empleados/:id} : get the "id" tipoEmpleado.
     *
     * @param id the id of the tipoEmpleadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoEmpleadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoEmpleadoDTO> getTipoEmpleado(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoEmpleado : {}", id);
        Optional<TipoEmpleadoDTO> tipoEmpleadoDTO = tipoEmpleadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoEmpleadoDTO);
    }

    /**
     * {@code DELETE  /tipo-empleados/:id} : delete the "id" tipoEmpleado.
     *
     * @param id the id of the tipoEmpleadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoEmpleado(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoEmpleado : {}", id);
        tipoEmpleadoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
