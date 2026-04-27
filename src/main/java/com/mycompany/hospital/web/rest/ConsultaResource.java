package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.ConsultaRepository;
import com.mycompany.hospital.service.ConsultaQueryService;
import com.mycompany.hospital.service.ConsultaService;
import com.mycompany.hospital.service.criteria.ConsultaCriteria;
import com.mycompany.hospital.service.dto.ConsultaDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.Consulta}.
 */
@RestController
@RequestMapping("/api/consultas")
public class ConsultaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultaResource.class);

    private static final String ENTITY_NAME = "consulta";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final ConsultaService consultaService;

    private final ConsultaRepository consultaRepository;

    private final ConsultaQueryService consultaQueryService;

    public ConsultaResource(
        ConsultaService consultaService,
        ConsultaRepository consultaRepository,
        ConsultaQueryService consultaQueryService
    ) {
        this.consultaService = consultaService;
        this.consultaRepository = consultaRepository;
        this.consultaQueryService = consultaQueryService;
    }

    /**
     * {@code POST  /consultas} : Create a new consulta.
     *
     * @param consultaDTO the consultaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultaDTO, or with status {@code 400 (Bad Request)} if the consulta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConsultaDTO> createConsulta(@Valid @RequestBody ConsultaDTO consultaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Consulta : {}", consultaDTO);
        if (consultaDTO.getId() != null) {
            throw new BadRequestAlertException("A new consulta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        consultaDTO = consultaService.save(consultaDTO);
        return ResponseEntity.created(new URI("/api/consultas/" + consultaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, consultaDTO.getId().toString()))
            .body(consultaDTO);
    }

    /**
     * {@code PUT  /consultas/:id} : Updates an existing consulta.
     *
     * @param id the id of the consultaDTO to save.
     * @param consultaDTO the consultaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultaDTO,
     * or with status {@code 400 (Bad Request)} if the consultaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> updateConsulta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConsultaDTO consultaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Consulta : {}, {}", id, consultaDTO);
        if (consultaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        consultaDTO = consultaService.update(consultaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consultaDTO.getId().toString()))
            .body(consultaDTO);
    }

    /**
     * {@code PATCH  /consultas/:id} : Partial updates given fields of an existing consulta, field will ignore if it is null
     *
     * @param id the id of the consultaDTO to save.
     * @param consultaDTO the consultaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultaDTO,
     * or with status {@code 400 (Bad Request)} if the consultaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consultaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsultaDTO> partialUpdateConsulta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConsultaDTO consultaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Consulta partially : {}, {}", id, consultaDTO);
        if (consultaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsultaDTO> result = consultaService.partialUpdate(consultaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consultaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consultas} : get all the Consultas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Consultas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConsultaDTO>> getAllConsultas(
        ConsultaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Consultas by criteria: {}", criteria);

        Page<ConsultaDTO> page = consultaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consultas/count} : count all the consultas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countConsultas(ConsultaCriteria criteria) {
        LOG.debug("REST request to count Consultas by criteria: {}", criteria);
        return ResponseEntity.ok().body(consultaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consultas/:id} : get the "id" consulta.
     *
     * @param id the id of the consultaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> getConsulta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Consulta : {}", id);
        Optional<ConsultaDTO> consultaDTO = consultaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultaDTO);
    }

    /**
     * {@code DELETE  /consultas/:id} : delete the "id" consulta.
     *
     * @param id the id of the consultaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Consulta : {}", id);
        consultaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
