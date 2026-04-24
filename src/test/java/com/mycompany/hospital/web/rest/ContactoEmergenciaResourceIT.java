package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.ContactoEmergenciaAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.ContactoEmergencia;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.repository.ContactoEmergenciaRepository;
import com.mycompany.hospital.service.dto.ContactoEmergenciaDTO;
import com.mycompany.hospital.service.mapper.ContactoEmergenciaMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContactoEmergenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactoEmergenciaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTESCO = "AAAAAAAAAA";
    private static final String UPDATED_PARENTESCO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORIDAD = 1;
    private static final Integer UPDATED_PRIORIDAD = 2;
    private static final Integer SMALLER_PRIORIDAD = 1 - 1;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/contacto-emergencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactoEmergenciaRepository contactoEmergenciaRepository;

    @Autowired
    private ContactoEmergenciaMapper contactoEmergenciaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactoEmergenciaMockMvc;

    private ContactoEmergencia contactoEmergencia;

    private ContactoEmergencia insertedContactoEmergencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactoEmergencia createEntity(EntityManager em) {
        ContactoEmergencia contactoEmergencia = new ContactoEmergencia()
            .nombre(DEFAULT_NOMBRE)
            .telefono(DEFAULT_TELEFONO)
            .parentesco(DEFAULT_PARENTESCO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .prioridad(DEFAULT_PRIORIDAD)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        contactoEmergencia.setPersona(persona);
        return contactoEmergencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactoEmergencia createUpdatedEntity(EntityManager em) {
        ContactoEmergencia updatedContactoEmergencia = new ContactoEmergencia()
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .parentesco(UPDATED_PARENTESCO)
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createUpdatedEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        updatedContactoEmergencia.setPersona(persona);
        return updatedContactoEmergencia;
    }

    @BeforeEach
    void initTest() {
        contactoEmergencia = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedContactoEmergencia != null) {
            contactoEmergenciaRepository.delete(insertedContactoEmergencia);
            insertedContactoEmergencia = null;
        }
    }

    @Test
    @Transactional
    void createContactoEmergencia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);
        var returnedContactoEmergenciaDTO = om.readValue(
            restContactoEmergenciaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContactoEmergenciaDTO.class
        );

        // Validate the ContactoEmergencia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContactoEmergencia = contactoEmergenciaMapper.toEntity(returnedContactoEmergenciaDTO);
        assertContactoEmergenciaUpdatableFieldsEquals(
            returnedContactoEmergencia,
            getPersistedContactoEmergencia(returnedContactoEmergencia)
        );

        insertedContactoEmergencia = returnedContactoEmergencia;
    }

    @Test
    @Transactional
    void createContactoEmergenciaWithExistingId() throws Exception {
        // Create the ContactoEmergencia with an existing ID
        contactoEmergencia.setId(1L);
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactoEmergenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoEmergencia.setNombre(null);

        // Create the ContactoEmergencia, which fails.
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        restContactoEmergenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoEmergencia.setTelefono(null);

        // Create the ContactoEmergencia, which fails.
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        restContactoEmergenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrioridadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoEmergencia.setPrioridad(null);

        // Create the ContactoEmergencia, which fails.
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        restContactoEmergenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoEmergencia.setActivo(null);

        // Create the ContactoEmergencia, which fails.
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        restContactoEmergenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoEmergencia.setFechaAlta(null);

        // Create the ContactoEmergencia, which fails.
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        restContactoEmergenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactoEmergencias() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList
        restContactoEmergenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactoEmergencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getContactoEmergencia() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get the contactoEmergencia
        restContactoEmergenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, contactoEmergencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactoEmergencia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.parentesco").value(DEFAULT_PARENTESCO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.prioridad").value(DEFAULT_PRIORIDAD))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getContactoEmergenciasByIdFiltering() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        Long id = contactoEmergencia.getId();

        defaultContactoEmergenciaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContactoEmergenciaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContactoEmergenciaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where nombre equals to
        defaultContactoEmergenciaFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where nombre in
        defaultContactoEmergenciaFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where nombre is not null
        defaultContactoEmergenciaFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where nombre contains
        defaultContactoEmergenciaFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where nombre does not contain
        defaultContactoEmergenciaFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where telefono equals to
        defaultContactoEmergenciaFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where telefono in
        defaultContactoEmergenciaFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where telefono is not null
        defaultContactoEmergenciaFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where telefono contains
        defaultContactoEmergenciaFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where telefono does not contain
        defaultContactoEmergenciaFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByParentescoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where parentesco equals to
        defaultContactoEmergenciaFiltering("parentesco.equals=" + DEFAULT_PARENTESCO, "parentesco.equals=" + UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByParentescoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where parentesco in
        defaultContactoEmergenciaFiltering(
            "parentesco.in=" + DEFAULT_PARENTESCO + "," + UPDATED_PARENTESCO,
            "parentesco.in=" + UPDATED_PARENTESCO
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByParentescoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where parentesco is not null
        defaultContactoEmergenciaFiltering("parentesco.specified=true", "parentesco.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByParentescoContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where parentesco contains
        defaultContactoEmergenciaFiltering("parentesco.contains=" + DEFAULT_PARENTESCO, "parentesco.contains=" + UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByParentescoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where parentesco does not contain
        defaultContactoEmergenciaFiltering(
            "parentesco.doesNotContain=" + UPDATED_PARENTESCO,
            "parentesco.doesNotContain=" + DEFAULT_PARENTESCO
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where observaciones equals to
        defaultContactoEmergenciaFiltering(
            "observaciones.equals=" + DEFAULT_OBSERVACIONES,
            "observaciones.equals=" + UPDATED_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where observaciones in
        defaultContactoEmergenciaFiltering(
            "observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES,
            "observaciones.in=" + UPDATED_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where observaciones is not null
        defaultContactoEmergenciaFiltering("observaciones.specified=true", "observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where observaciones contains
        defaultContactoEmergenciaFiltering(
            "observaciones.contains=" + DEFAULT_OBSERVACIONES,
            "observaciones.contains=" + UPDATED_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where observaciones does not contain
        defaultContactoEmergenciaFiltering(
            "observaciones.doesNotContain=" + UPDATED_OBSERVACIONES,
            "observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad equals to
        defaultContactoEmergenciaFiltering("prioridad.equals=" + DEFAULT_PRIORIDAD, "prioridad.equals=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad in
        defaultContactoEmergenciaFiltering(
            "prioridad.in=" + DEFAULT_PRIORIDAD + "," + UPDATED_PRIORIDAD,
            "prioridad.in=" + UPDATED_PRIORIDAD
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad is not null
        defaultContactoEmergenciaFiltering("prioridad.specified=true", "prioridad.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad is greater than or equal to
        defaultContactoEmergenciaFiltering(
            "prioridad.greaterThanOrEqual=" + DEFAULT_PRIORIDAD,
            "prioridad.greaterThanOrEqual=" + (DEFAULT_PRIORIDAD + 1)
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad is less than or equal to
        defaultContactoEmergenciaFiltering(
            "prioridad.lessThanOrEqual=" + DEFAULT_PRIORIDAD,
            "prioridad.lessThanOrEqual=" + SMALLER_PRIORIDAD
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad is less than
        defaultContactoEmergenciaFiltering("prioridad.lessThan=" + (DEFAULT_PRIORIDAD + 1), "prioridad.lessThan=" + DEFAULT_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPrioridadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where prioridad is greater than
        defaultContactoEmergenciaFiltering("prioridad.greaterThan=" + SMALLER_PRIORIDAD, "prioridad.greaterThan=" + DEFAULT_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where activo equals to
        defaultContactoEmergenciaFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where activo in
        defaultContactoEmergenciaFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where activo is not null
        defaultContactoEmergenciaFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta equals to
        defaultContactoEmergenciaFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta in
        defaultContactoEmergenciaFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta is not null
        defaultContactoEmergenciaFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta is greater than or equal to
        defaultContactoEmergenciaFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta is less than or equal to
        defaultContactoEmergenciaFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta is less than
        defaultContactoEmergenciaFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaAlta is greater than
        defaultContactoEmergenciaFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja equals to
        defaultContactoEmergenciaFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja in
        defaultContactoEmergenciaFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja is not null
        defaultContactoEmergenciaFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja is greater than or equal to
        defaultContactoEmergenciaFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja is less than or equal to
        defaultContactoEmergenciaFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja is less than
        defaultContactoEmergenciaFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        // Get all the contactoEmergenciaList where fechaBaja is greater than
        defaultContactoEmergenciaFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllContactoEmergenciasByPersonaIsEqualToSomething() throws Exception {
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);
            persona = PersonaResourceIT.createEntity(em);
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        em.persist(persona);
        em.flush();
        contactoEmergencia.setPersona(persona);
        contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);
        Long personaId = persona.getId();
        // Get all the contactoEmergenciaList where persona equals to personaId
        defaultContactoEmergenciaShouldBeFound("personaId.equals=" + personaId);

        // Get all the contactoEmergenciaList where persona equals to (personaId + 1)
        defaultContactoEmergenciaShouldNotBeFound("personaId.equals=" + (personaId + 1));
    }

    private void defaultContactoEmergenciaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContactoEmergenciaShouldBeFound(shouldBeFound);
        defaultContactoEmergenciaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactoEmergenciaShouldBeFound(String filter) throws Exception {
        restContactoEmergenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactoEmergencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restContactoEmergenciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactoEmergenciaShouldNotBeFound(String filter) throws Exception {
        restContactoEmergenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactoEmergenciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContactoEmergencia() throws Exception {
        // Get the contactoEmergencia
        restContactoEmergenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactoEmergencia() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactoEmergencia
        ContactoEmergencia updatedContactoEmergencia = contactoEmergenciaRepository.findById(contactoEmergencia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactoEmergencia are not directly saved in db
        em.detach(updatedContactoEmergencia);
        updatedContactoEmergencia
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .parentesco(UPDATED_PARENTESCO)
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(updatedContactoEmergencia);

        restContactoEmergenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactoEmergenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactoEmergenciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactoEmergenciaToMatchAllProperties(updatedContactoEmergencia);
    }

    @Test
    @Transactional
    void putNonExistingContactoEmergencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoEmergencia.setId(longCount.incrementAndGet());

        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactoEmergenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactoEmergenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactoEmergenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactoEmergencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoEmergencia.setId(longCount.incrementAndGet());

        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoEmergenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactoEmergenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactoEmergencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoEmergencia.setId(longCount.incrementAndGet());

        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoEmergenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactoEmergenciaWithPatch() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactoEmergencia using partial update
        ContactoEmergencia partialUpdatedContactoEmergencia = new ContactoEmergencia();
        partialUpdatedContactoEmergencia.setId(contactoEmergencia.getId());

        partialUpdatedContactoEmergencia
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restContactoEmergenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactoEmergencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactoEmergencia))
            )
            .andExpect(status().isOk());

        // Validate the ContactoEmergencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactoEmergenciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContactoEmergencia, contactoEmergencia),
            getPersistedContactoEmergencia(contactoEmergencia)
        );
    }

    @Test
    @Transactional
    void fullUpdateContactoEmergenciaWithPatch() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactoEmergencia using partial update
        ContactoEmergencia partialUpdatedContactoEmergencia = new ContactoEmergencia();
        partialUpdatedContactoEmergencia.setId(contactoEmergencia.getId());

        partialUpdatedContactoEmergencia
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .parentesco(UPDATED_PARENTESCO)
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restContactoEmergenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactoEmergencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactoEmergencia))
            )
            .andExpect(status().isOk());

        // Validate the ContactoEmergencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactoEmergenciaUpdatableFieldsEquals(
            partialUpdatedContactoEmergencia,
            getPersistedContactoEmergencia(partialUpdatedContactoEmergencia)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContactoEmergencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoEmergencia.setId(longCount.incrementAndGet());

        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactoEmergenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactoEmergenciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactoEmergenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactoEmergencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoEmergencia.setId(longCount.incrementAndGet());

        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoEmergenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactoEmergenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactoEmergencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoEmergencia.setId(longCount.incrementAndGet());

        // Create the ContactoEmergencia
        ContactoEmergenciaDTO contactoEmergenciaDTO = contactoEmergenciaMapper.toDto(contactoEmergencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoEmergenciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contactoEmergenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactoEmergencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactoEmergencia() throws Exception {
        // Initialize the database
        insertedContactoEmergencia = contactoEmergenciaRepository.saveAndFlush(contactoEmergencia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contactoEmergencia
        restContactoEmergenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactoEmergencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactoEmergenciaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ContactoEmergencia getPersistedContactoEmergencia(ContactoEmergencia contactoEmergencia) {
        return contactoEmergenciaRepository.findById(contactoEmergencia.getId()).orElseThrow();
    }

    protected void assertPersistedContactoEmergenciaToMatchAllProperties(ContactoEmergencia expectedContactoEmergencia) {
        assertContactoEmergenciaAllPropertiesEquals(expectedContactoEmergencia, getPersistedContactoEmergencia(expectedContactoEmergencia));
    }

    protected void assertPersistedContactoEmergenciaToMatchUpdatableProperties(ContactoEmergencia expectedContactoEmergencia) {
        assertContactoEmergenciaAllUpdatablePropertiesEquals(
            expectedContactoEmergencia,
            getPersistedContactoEmergencia(expectedContactoEmergencia)
        );
    }
}
