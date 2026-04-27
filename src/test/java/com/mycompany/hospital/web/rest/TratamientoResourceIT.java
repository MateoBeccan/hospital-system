package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.TratamientoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Diagnostico;
import com.mycompany.hospital.domain.EstadoTratamiento;
import com.mycompany.hospital.domain.Tratamiento;
import com.mycompany.hospital.repository.TratamientoRepository;
import com.mycompany.hospital.service.dto.TratamientoDTO;
import com.mycompany.hospital.service.mapper.TratamientoMapper;
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
 * Integration tests for the {@link TratamientoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TratamientoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_PROXIMA_REVISION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PROXIMA_REVISION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_PROXIMA_REVISION = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/tratamientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private TratamientoMapper tratamientoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTratamientoMockMvc;

    private Tratamiento tratamiento;

    private Tratamiento insertedTratamiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamiento createEntity(EntityManager em) {
        Tratamiento tratamiento = new Tratamiento()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .observaciones(DEFAULT_OBSERVACIONES)
            .fechaProximaRevision(DEFAULT_FECHA_PROXIMA_REVISION)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        Diagnostico diagnostico;
        if (TestUtil.findAll(em, Diagnostico.class).isEmpty()) {
            diagnostico = DiagnosticoResourceIT.createEntity(em);
            em.persist(diagnostico);
            em.flush();
        } else {
            diagnostico = TestUtil.findAll(em, Diagnostico.class).get(0);
        }
        tratamiento.setDiagnostico(diagnostico);
        // Add required entity
        EstadoTratamiento estadoTratamiento;
        if (TestUtil.findAll(em, EstadoTratamiento.class).isEmpty()) {
            estadoTratamiento = EstadoTratamientoResourceIT.createEntity();
            em.persist(estadoTratamiento);
            em.flush();
        } else {
            estadoTratamiento = TestUtil.findAll(em, EstadoTratamiento.class).get(0);
        }
        tratamiento.setEstadoTratamiento(estadoTratamiento);
        return tratamiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamiento createUpdatedEntity(EntityManager em) {
        Tratamiento updatedTratamiento = new Tratamiento()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaProximaRevision(UPDATED_FECHA_PROXIMA_REVISION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        Diagnostico diagnostico;
        if (TestUtil.findAll(em, Diagnostico.class).isEmpty()) {
            diagnostico = DiagnosticoResourceIT.createUpdatedEntity(em);
            em.persist(diagnostico);
            em.flush();
        } else {
            diagnostico = TestUtil.findAll(em, Diagnostico.class).get(0);
        }
        updatedTratamiento.setDiagnostico(diagnostico);
        // Add required entity
        EstadoTratamiento estadoTratamiento;
        if (TestUtil.findAll(em, EstadoTratamiento.class).isEmpty()) {
            estadoTratamiento = EstadoTratamientoResourceIT.createUpdatedEntity();
            em.persist(estadoTratamiento);
            em.flush();
        } else {
            estadoTratamiento = TestUtil.findAll(em, EstadoTratamiento.class).get(0);
        }
        updatedTratamiento.setEstadoTratamiento(estadoTratamiento);
        return updatedTratamiento;
    }

    @BeforeEach
    void initTest() {
        tratamiento = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTratamiento != null) {
            tratamientoRepository.delete(insertedTratamiento);
            insertedTratamiento = null;
        }
    }

    @Test
    @Transactional
    void createTratamiento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);
        var returnedTratamientoDTO = om.readValue(
            restTratamientoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TratamientoDTO.class
        );

        // Validate the Tratamiento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTratamiento = tratamientoMapper.toEntity(returnedTratamientoDTO);
        assertTratamientoUpdatableFieldsEquals(returnedTratamiento, getPersistedTratamiento(returnedTratamiento));

        insertedTratamiento = returnedTratamiento;
    }

    @Test
    @Transactional
    void createTratamientoWithExistingId() throws Exception {
        // Create the Tratamiento with an existing ID
        tratamiento.setId(1L);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setCodigo(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setDescripcion(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setFechaInicio(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setActivo(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setFechaAlta(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTratamientos() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaProximaRevision").value(hasItem(DEFAULT_FECHA_PROXIMA_REVISION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getTratamiento() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get the tratamiento
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL_ID, tratamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tratamiento.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.fechaProximaRevision").value(DEFAULT_FECHA_PROXIMA_REVISION.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getTratamientosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        Long id = tratamiento.getId();

        defaultTratamientoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTratamientoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTratamientoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTratamientosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where codigo equals to
        defaultTratamientoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTratamientosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where codigo in
        defaultTratamientoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTratamientosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where codigo is not null
        defaultTratamientoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where codigo contains
        defaultTratamientoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTratamientosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where codigo does not contain
        defaultTratamientoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllTratamientosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where descripcion equals to
        defaultTratamientoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTratamientosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where descripcion in
        defaultTratamientoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where descripcion is not null
        defaultTratamientoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where descripcion contains
        defaultTratamientoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTratamientosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where descripcion does not contain
        defaultTratamientoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio equals to
        defaultTratamientoFiltering("fechaInicio.equals=" + DEFAULT_FECHA_INICIO, "fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio in
        defaultTratamientoFiltering(
            "fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO,
            "fechaInicio.in=" + UPDATED_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio is not null
        defaultTratamientoFiltering("fechaInicio.specified=true", "fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio is greater than or equal to
        defaultTratamientoFiltering(
            "fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO,
            "fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio is less than or equal to
        defaultTratamientoFiltering(
            "fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO,
            "fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio is less than
        defaultTratamientoFiltering("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO, "fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaInicio is greater than
        defaultTratamientoFiltering("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO, "fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin equals to
        defaultTratamientoFiltering("fechaFin.equals=" + DEFAULT_FECHA_FIN, "fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin in
        defaultTratamientoFiltering("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN, "fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin is not null
        defaultTratamientoFiltering("fechaFin.specified=true", "fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin is greater than or equal to
        defaultTratamientoFiltering("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN, "fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin is less than or equal to
        defaultTratamientoFiltering("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN, "fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin is less than
        defaultTratamientoFiltering("fechaFin.lessThan=" + UPDATED_FECHA_FIN, "fechaFin.lessThan=" + DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaFin is greater than
        defaultTratamientoFiltering("fechaFin.greaterThan=" + SMALLER_FECHA_FIN, "fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision equals to
        defaultTratamientoFiltering(
            "fechaProximaRevision.equals=" + DEFAULT_FECHA_PROXIMA_REVISION,
            "fechaProximaRevision.equals=" + UPDATED_FECHA_PROXIMA_REVISION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision in
        defaultTratamientoFiltering(
            "fechaProximaRevision.in=" + DEFAULT_FECHA_PROXIMA_REVISION + "," + UPDATED_FECHA_PROXIMA_REVISION,
            "fechaProximaRevision.in=" + UPDATED_FECHA_PROXIMA_REVISION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision is not null
        defaultTratamientoFiltering("fechaProximaRevision.specified=true", "fechaProximaRevision.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision is greater than or equal to
        defaultTratamientoFiltering(
            "fechaProximaRevision.greaterThanOrEqual=" + DEFAULT_FECHA_PROXIMA_REVISION,
            "fechaProximaRevision.greaterThanOrEqual=" + UPDATED_FECHA_PROXIMA_REVISION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision is less than or equal to
        defaultTratamientoFiltering(
            "fechaProximaRevision.lessThanOrEqual=" + DEFAULT_FECHA_PROXIMA_REVISION,
            "fechaProximaRevision.lessThanOrEqual=" + SMALLER_FECHA_PROXIMA_REVISION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision is less than
        defaultTratamientoFiltering(
            "fechaProximaRevision.lessThan=" + UPDATED_FECHA_PROXIMA_REVISION,
            "fechaProximaRevision.lessThan=" + DEFAULT_FECHA_PROXIMA_REVISION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaProximaRevisionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaProximaRevision is greater than
        defaultTratamientoFiltering(
            "fechaProximaRevision.greaterThan=" + SMALLER_FECHA_PROXIMA_REVISION,
            "fechaProximaRevision.greaterThan=" + DEFAULT_FECHA_PROXIMA_REVISION
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where activo equals to
        defaultTratamientoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTratamientosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where activo in
        defaultTratamientoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTratamientosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where activo is not null
        defaultTratamientoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta equals to
        defaultTratamientoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta in
        defaultTratamientoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta is not null
        defaultTratamientoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta is greater than or equal to
        defaultTratamientoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta is less than or equal to
        defaultTratamientoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta is less than
        defaultTratamientoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaAlta is greater than
        defaultTratamientoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja equals to
        defaultTratamientoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja in
        defaultTratamientoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja is not null
        defaultTratamientoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja is greater than or equal to
        defaultTratamientoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja is less than or equal to
        defaultTratamientoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja is less than
        defaultTratamientoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTratamientosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList where fechaBaja is greater than
        defaultTratamientoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTratamientosByDiagnosticoIsEqualToSomething() throws Exception {
        Diagnostico diagnostico;
        if (TestUtil.findAll(em, Diagnostico.class).isEmpty()) {
            tratamientoRepository.saveAndFlush(tratamiento);
            diagnostico = DiagnosticoResourceIT.createEntity(em);
        } else {
            diagnostico = TestUtil.findAll(em, Diagnostico.class).get(0);
        }
        em.persist(diagnostico);
        em.flush();
        tratamiento.setDiagnostico(diagnostico);
        tratamientoRepository.saveAndFlush(tratamiento);
        Long diagnosticoId = diagnostico.getId();
        // Get all the tratamientoList where diagnostico equals to diagnosticoId
        defaultTratamientoShouldBeFound("diagnosticoId.equals=" + diagnosticoId);

        // Get all the tratamientoList where diagnostico equals to (diagnosticoId + 1)
        defaultTratamientoShouldNotBeFound("diagnosticoId.equals=" + (diagnosticoId + 1));
    }

    @Test
    @Transactional
    void getAllTratamientosByEstadoTratamientoIsEqualToSomething() throws Exception {
        EstadoTratamiento estadoTratamiento;
        if (TestUtil.findAll(em, EstadoTratamiento.class).isEmpty()) {
            tratamientoRepository.saveAndFlush(tratamiento);
            estadoTratamiento = EstadoTratamientoResourceIT.createEntity();
        } else {
            estadoTratamiento = TestUtil.findAll(em, EstadoTratamiento.class).get(0);
        }
        em.persist(estadoTratamiento);
        em.flush();
        tratamiento.setEstadoTratamiento(estadoTratamiento);
        tratamientoRepository.saveAndFlush(tratamiento);
        Long estadoTratamientoId = estadoTratamiento.getId();
        // Get all the tratamientoList where estadoTratamiento equals to estadoTratamientoId
        defaultTratamientoShouldBeFound("estadoTratamientoId.equals=" + estadoTratamientoId);

        // Get all the tratamientoList where estadoTratamiento equals to (estadoTratamientoId + 1)
        defaultTratamientoShouldNotBeFound("estadoTratamientoId.equals=" + (estadoTratamientoId + 1));
    }

    private void defaultTratamientoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTratamientoShouldBeFound(shouldBeFound);
        defaultTratamientoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTratamientoShouldBeFound(String filter) throws Exception {
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaProximaRevision").value(hasItem(DEFAULT_FECHA_PROXIMA_REVISION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTratamientoShouldNotBeFound(String filter) throws Exception {
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTratamiento() throws Exception {
        // Get the tratamiento
        restTratamientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTratamiento() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tratamiento
        Tratamiento updatedTratamiento = tratamientoRepository.findById(tratamiento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTratamiento are not directly saved in db
        em.detach(updatedTratamiento);
        updatedTratamiento
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaProximaRevision(UPDATED_FECHA_PROXIMA_REVISION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(updatedTratamiento);

        restTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tratamientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTratamientoToMatchAllProperties(updatedTratamiento);
    }

    @Test
    @Transactional
    void putNonExistingTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tratamientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTratamientoWithPatch() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tratamiento using partial update
        Tratamiento partialUpdatedTratamiento = new Tratamiento();
        partialUpdatedTratamiento.setId(tratamiento.getId());

        partialUpdatedTratamiento
            .codigo(UPDATED_CODIGO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO);

        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTratamiento))
            )
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTratamientoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTratamiento, tratamiento),
            getPersistedTratamiento(tratamiento)
        );
    }

    @Test
    @Transactional
    void fullUpdateTratamientoWithPatch() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tratamiento using partial update
        Tratamiento partialUpdatedTratamiento = new Tratamiento();
        partialUpdatedTratamiento.setId(tratamiento.getId());

        partialUpdatedTratamiento
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaProximaRevision(UPDATED_FECHA_PROXIMA_REVISION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTratamiento))
            )
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTratamientoUpdatableFieldsEquals(partialUpdatedTratamiento, getPersistedTratamiento(partialUpdatedTratamiento));
    }

    @Test
    @Transactional
    void patchNonExistingTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tratamientoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTratamiento() throws Exception {
        // Initialize the database
        insertedTratamiento = tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tratamiento
        restTratamientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tratamiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tratamientoRepository.count();
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

    protected Tratamiento getPersistedTratamiento(Tratamiento tratamiento) {
        return tratamientoRepository.findById(tratamiento.getId()).orElseThrow();
    }

    protected void assertPersistedTratamientoToMatchAllProperties(Tratamiento expectedTratamiento) {
        assertTratamientoAllPropertiesEquals(expectedTratamiento, getPersistedTratamiento(expectedTratamiento));
    }

    protected void assertPersistedTratamientoToMatchUpdatableProperties(Tratamiento expectedTratamiento) {
        assertTratamientoAllUpdatablePropertiesEquals(expectedTratamiento, getPersistedTratamiento(expectedTratamiento));
    }
}
