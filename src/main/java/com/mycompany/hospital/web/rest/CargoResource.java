package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.CargoRepository;
import com.mycompany.hospital.service.CargoQueryService;
import com.mycompany.hospital.service.CargoService;
import com.mycompany.hospital.service.criteria.CargoCriteria;
import com.mycompany.hospital.service.dto.CargoDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.Cargo}.
 */
@RestController
@RequestMapping("/api/cargos")
public class CargoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CargoResource.class);

    private static final String ENTITY_NAME = "cargo";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final CargoService cargoService;

    private final CargoRepository cargoRepository;

    private final CargoQueryService cargoQueryService;

    public CargoResource(CargoService cargoService, CargoRepository cargoRepository, CargoQueryService cargoQueryService) {
        this.cargoService = cargoService;
        this.cargoRepository = cargoRepository;
        this.cargoQueryService = cargoQueryService;
    }

    /**
     * {@code POST  /cargos} : Create a new cargo.
     *
     * @param cargoDTO the cargoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoDTO, or with status {@code 400 (Bad Request)} if the cargo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CargoDTO> createCargo(@Valid @RequestBody CargoDTO cargoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cargo : {}", cargoDTO);
        if (cargoDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cargoDTO = cargoService.save(cargoDTO);
        return ResponseEntity.created(new URI("/api/cargos/" + cargoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cargoDTO.getId().toString()))
            .body(cargoDTO);
    }

    /**
     * {@code PUT  /cargos/:id} : Updates an existing cargo.
     *
     * @param id the id of the cargoDTO to save.
     * @param cargoDTO the cargoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoDTO,
     * or with status {@code 400 (Bad Request)} if the cargoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CargoDTO> updateCargo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CargoDTO cargoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cargo : {}, {}", id, cargoDTO);
        if (cargoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cargoDTO = cargoService.update(cargoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cargoDTO.getId().toString()))
            .body(cargoDTO);
    }

    /**
     * {@code PATCH  /cargos/:id} : Partial updates given fields of an existing cargo, field will ignore if it is null
     *
     * @param id the id of the cargoDTO to save.
     * @param cargoDTO the cargoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoDTO,
     * or with status {@code 400 (Bad Request)} if the cargoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cargoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoDTO> partialUpdateCargo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CargoDTO cargoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cargo partially : {}, {}", id, cargoDTO);
        if (cargoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoDTO> result = cargoService.partialUpdate(cargoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cargoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cargos} : get all the Cargos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cargos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CargoDTO>> getAllCargos(
        CargoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cargos by criteria: {}", criteria);

        Page<CargoDTO> page = cargoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargos/count} : count all the cargos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCargos(CargoCriteria criteria) {
        LOG.debug("REST request to count Cargos by criteria: {}", criteria);
        return ResponseEntity.ok().body(cargoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cargos/:id} : get the "id" cargo.
     *
     * @param id the id of the cargoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CargoDTO> getCargo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cargo : {}", id);
        Optional<CargoDTO> cargoDTO = cargoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoDTO);
    }

    /**
     * {@code DELETE  /cargos/:id} : delete the "id" cargo.
     *
     * @param id the id of the cargoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cargo : {}", id);
        cargoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
