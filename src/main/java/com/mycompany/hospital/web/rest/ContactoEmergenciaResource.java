package com.mycompany.hospital.web.rest;

import com.mycompany.hospital.repository.ContactoEmergenciaRepository;
import com.mycompany.hospital.service.ContactoEmergenciaQueryService;
import com.mycompany.hospital.service.ContactoEmergenciaService;
import com.mycompany.hospital.service.criteria.ContactoEmergenciaCriteria;
import com.mycompany.hospital.service.dto.ContactoEmergenciaDTO;
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
 * REST controller for managing {@link com.mycompany.hospital.domain.ContactoEmergencia}.
 */
@RestController
@RequestMapping("/api/contacto-emergencias")
public class ContactoEmergenciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContactoEmergenciaResource.class);

    private static final String ENTITY_NAME = "contactoEmergencia";

    @Value("${jhipster.clientApp.name:hospital}")
    private String applicationName;

    private final ContactoEmergenciaService contactoEmergenciaService;

    private final ContactoEmergenciaRepository contactoEmergenciaRepository;

    private final ContactoEmergenciaQueryService contactoEmergenciaQueryService;

    public ContactoEmergenciaResource(
        ContactoEmergenciaService contactoEmergenciaService,
        ContactoEmergenciaRepository contactoEmergenciaRepository,
        ContactoEmergenciaQueryService contactoEmergenciaQueryService
    ) {
        this.contactoEmergenciaService = contactoEmergenciaService;
        this.contactoEmergenciaRepository = contactoEmergenciaRepository;
        this.contactoEmergenciaQueryService = contactoEmergenciaQueryService;
    }

    /**
     * {@code POST  /contacto-emergencias} : Create a new contactoEmergencia.
     *
     * @param contactoEmergenciaDTO the contactoEmergenciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactoEmergenciaDTO, or with status {@code 400 (Bad Request)} if the contactoEmergencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactoEmergenciaDTO> createContactoEmergencia(@Valid @RequestBody ContactoEmergenciaDTO contactoEmergenciaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ContactoEmergencia : {}", contactoEmergenciaDTO);
        if (contactoEmergenciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactoEmergencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactoEmergenciaDTO = contactoEmergenciaService.save(contactoEmergenciaDTO);
        return ResponseEntity.created(new URI("/api/contacto-emergencias/" + contactoEmergenciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, contactoEmergenciaDTO.getId().toString()))
            .body(contactoEmergenciaDTO);
    }

    /**
     * {@code PUT  /contacto-emergencias/:id} : Updates an existing contactoEmergencia.
     *
     * @param id the id of the contactoEmergenciaDTO to save.
     * @param contactoEmergenciaDTO the contactoEmergenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactoEmergenciaDTO,
     * or with status {@code 400 (Bad Request)} if the contactoEmergenciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactoEmergenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactoEmergenciaDTO> updateContactoEmergencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactoEmergenciaDTO contactoEmergenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContactoEmergencia : {}, {}", id, contactoEmergenciaDTO);
        if (contactoEmergenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactoEmergenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactoEmergenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactoEmergenciaDTO = contactoEmergenciaService.update(contactoEmergenciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactoEmergenciaDTO.getId().toString()))
            .body(contactoEmergenciaDTO);
    }

    /**
     * {@code PATCH  /contacto-emergencias/:id} : Partial updates given fields of an existing contactoEmergencia, field will ignore if it is null
     *
     * @param id the id of the contactoEmergenciaDTO to save.
     * @param contactoEmergenciaDTO the contactoEmergenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactoEmergenciaDTO,
     * or with status {@code 400 (Bad Request)} if the contactoEmergenciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactoEmergenciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactoEmergenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactoEmergenciaDTO> partialUpdateContactoEmergencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactoEmergenciaDTO contactoEmergenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContactoEmergencia partially : {}, {}", id, contactoEmergenciaDTO);
        if (contactoEmergenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactoEmergenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactoEmergenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactoEmergenciaDTO> result = contactoEmergenciaService.partialUpdate(contactoEmergenciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactoEmergenciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contacto-emergencias} : get all the Contacto Emergencias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Contacto Emergencias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactoEmergenciaDTO>> getAllContactoEmergencias(
        ContactoEmergenciaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ContactoEmergencias by criteria: {}", criteria);

        Page<ContactoEmergenciaDTO> page = contactoEmergenciaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contacto-emergencias/count} : count all the contactoEmergencias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContactoEmergencias(ContactoEmergenciaCriteria criteria) {
        LOG.debug("REST request to count ContactoEmergencias by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactoEmergenciaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contacto-emergencias/:id} : get the "id" contactoEmergencia.
     *
     * @param id the id of the contactoEmergenciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactoEmergenciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactoEmergenciaDTO> getContactoEmergencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContactoEmergencia : {}", id);
        Optional<ContactoEmergenciaDTO> contactoEmergenciaDTO = contactoEmergenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactoEmergenciaDTO);
    }

    /**
     * {@code DELETE  /contacto-emergencias/:id} : delete the "id" contactoEmergencia.
     *
     * @param id the id of the contactoEmergenciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactoEmergencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContactoEmergencia : {}", id);
        contactoEmergenciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
