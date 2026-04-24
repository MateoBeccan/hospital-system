package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.TurnoLaboralAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.TurnoLaboral;
import com.mycompany.hospital.repository.TurnoLaboralRepository;
import com.mycompany.hospital.service.dto.TurnoLaboralDTO;
import com.mycompany.hospital.service.mapper.TurnoLaboralMapper;
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
 * Integration tests for the {@link TurnoLaboralResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TurnoLaboralResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_HORA_INICIO = "22:19";
    private static final String UPDATED_HORA_INICIO = "11:01";

    private static final String DEFAULT_HORA_FIN = "01:01";
    private static final String UPDATED_HORA_FIN = "22:26";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/turno-laborals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TurnoLaboralRepository turnoLaboralRepository;

    @Autowired
    private TurnoLaboralMapper turnoLaboralMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurnoLaboralMockMvc;

    private TurnoLaboral turnoLaboral;

    private TurnoLaboral insertedTurnoLaboral;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurnoLaboral createEntity() {
        return new TurnoLaboral()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFin(DEFAULT_HORA_FIN)
            .descripcion(DEFAULT_DESCRIPCION)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurnoLaboral createUpdatedEntity() {
        return new TurnoLaboral()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        turnoLaboral = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTurnoLaboral != null) {
            turnoLaboralRepository.delete(insertedTurnoLaboral);
            insertedTurnoLaboral = null;
        }
    }

    @Test
    @Transactional
    void createTurnoLaboral() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);
        var returnedTurnoLaboralDTO = om.readValue(
            restTurnoLaboralMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TurnoLaboralDTO.class
        );

        // Validate the TurnoLaboral in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTurnoLaboral = turnoLaboralMapper.toEntity(returnedTurnoLaboralDTO);
        assertTurnoLaboralUpdatableFieldsEquals(returnedTurnoLaboral, getPersistedTurnoLaboral(returnedTurnoLaboral));

        insertedTurnoLaboral = returnedTurnoLaboral;
    }

    @Test
    @Transactional
    void createTurnoLaboralWithExistingId() throws Exception {
        // Create the TurnoLaboral with an existing ID
        turnoLaboral.setId(1L);
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoLaboral.setCodigo(null);

        // Create the TurnoLaboral, which fails.
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoLaboral.setNombre(null);

        // Create the TurnoLaboral, which fails.
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoLaboral.setHoraInicio(null);

        // Create the TurnoLaboral, which fails.
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoLaboral.setHoraFin(null);

        // Create the TurnoLaboral, which fails.
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoLaboral.setActivo(null);

        // Create the TurnoLaboral, which fails.
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoLaboral.setFechaAlta(null);

        // Create the TurnoLaboral, which fails.
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        restTurnoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurnoLaborals() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList
        restTurnoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnoLaboral.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO)))
            .andExpect(jsonPath("$.[*].horaFin").value(hasItem(DEFAULT_HORA_FIN)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getTurnoLaboral() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get the turnoLaboral
        restTurnoLaboralMockMvc
            .perform(get(ENTITY_API_URL_ID, turnoLaboral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turnoLaboral.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO))
            .andExpect(jsonPath("$.horaFin").value(DEFAULT_HORA_FIN))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getTurnoLaboralsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        Long id = turnoLaboral.getId();

        defaultTurnoLaboralFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTurnoLaboralFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTurnoLaboralFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where codigo equals to
        defaultTurnoLaboralFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where codigo in
        defaultTurnoLaboralFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where codigo is not null
        defaultTurnoLaboralFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where codigo contains
        defaultTurnoLaboralFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where codigo does not contain
        defaultTurnoLaboralFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where nombre equals to
        defaultTurnoLaboralFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where nombre in
        defaultTurnoLaboralFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where nombre is not null
        defaultTurnoLaboralFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where nombre contains
        defaultTurnoLaboralFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where nombre does not contain
        defaultTurnoLaboralFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaInicio equals to
        defaultTurnoLaboralFiltering("horaInicio.equals=" + DEFAULT_HORA_INICIO, "horaInicio.equals=" + UPDATED_HORA_INICIO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaInicio in
        defaultTurnoLaboralFiltering(
            "horaInicio.in=" + DEFAULT_HORA_INICIO + "," + UPDATED_HORA_INICIO,
            "horaInicio.in=" + UPDATED_HORA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaInicio is not null
        defaultTurnoLaboralFiltering("horaInicio.specified=true", "horaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraInicioContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaInicio contains
        defaultTurnoLaboralFiltering("horaInicio.contains=" + DEFAULT_HORA_INICIO, "horaInicio.contains=" + UPDATED_HORA_INICIO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraInicioNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaInicio does not contain
        defaultTurnoLaboralFiltering(
            "horaInicio.doesNotContain=" + UPDATED_HORA_INICIO,
            "horaInicio.doesNotContain=" + DEFAULT_HORA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaFin equals to
        defaultTurnoLaboralFiltering("horaFin.equals=" + DEFAULT_HORA_FIN, "horaFin.equals=" + UPDATED_HORA_FIN);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaFin in
        defaultTurnoLaboralFiltering("horaFin.in=" + DEFAULT_HORA_FIN + "," + UPDATED_HORA_FIN, "horaFin.in=" + UPDATED_HORA_FIN);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaFin is not null
        defaultTurnoLaboralFiltering("horaFin.specified=true", "horaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraFinContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaFin contains
        defaultTurnoLaboralFiltering("horaFin.contains=" + DEFAULT_HORA_FIN, "horaFin.contains=" + UPDATED_HORA_FIN);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByHoraFinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where horaFin does not contain
        defaultTurnoLaboralFiltering("horaFin.doesNotContain=" + UPDATED_HORA_FIN, "horaFin.doesNotContain=" + DEFAULT_HORA_FIN);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where descripcion equals to
        defaultTurnoLaboralFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where descripcion in
        defaultTurnoLaboralFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where descripcion is not null
        defaultTurnoLaboralFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where descripcion contains
        defaultTurnoLaboralFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where descripcion does not contain
        defaultTurnoLaboralFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where activo equals to
        defaultTurnoLaboralFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where activo in
        defaultTurnoLaboralFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where activo is not null
        defaultTurnoLaboralFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta equals to
        defaultTurnoLaboralFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta in
        defaultTurnoLaboralFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta is not null
        defaultTurnoLaboralFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta is greater than or equal to
        defaultTurnoLaboralFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta is less than or equal to
        defaultTurnoLaboralFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta is less than
        defaultTurnoLaboralFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaAlta is greater than
        defaultTurnoLaboralFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja equals to
        defaultTurnoLaboralFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja in
        defaultTurnoLaboralFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja is not null
        defaultTurnoLaboralFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja is greater than or equal to
        defaultTurnoLaboralFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja is less than or equal to
        defaultTurnoLaboralFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja is less than
        defaultTurnoLaboralFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnoLaboralsByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        // Get all the turnoLaboralList where fechaBaja is greater than
        defaultTurnoLaboralFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultTurnoLaboralFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTurnoLaboralShouldBeFound(shouldBeFound);
        defaultTurnoLaboralShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTurnoLaboralShouldBeFound(String filter) throws Exception {
        restTurnoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnoLaboral.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO)))
            .andExpect(jsonPath("$.[*].horaFin").value(hasItem(DEFAULT_HORA_FIN)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restTurnoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTurnoLaboralShouldNotBeFound(String filter) throws Exception {
        restTurnoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTurnoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTurnoLaboral() throws Exception {
        // Get the turnoLaboral
        restTurnoLaboralMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTurnoLaboral() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turnoLaboral
        TurnoLaboral updatedTurnoLaboral = turnoLaboralRepository.findById(turnoLaboral.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTurnoLaboral are not directly saved in db
        em.detach(updatedTurnoLaboral);
        updatedTurnoLaboral
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(updatedTurnoLaboral);

        restTurnoLaboralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnoLaboralDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoLaboralDTO))
            )
            .andExpect(status().isOk());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTurnoLaboralToMatchAllProperties(updatedTurnoLaboral);
    }

    @Test
    @Transactional
    void putNonExistingTurnoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoLaboral.setId(longCount.incrementAndGet());

        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnoLaboralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnoLaboralDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurnoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoLaboral.setId(longCount.incrementAndGet());

        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoLaboralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurnoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoLaboral.setId(longCount.incrementAndGet());

        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoLaboralMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurnoLaboralWithPatch() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turnoLaboral using partial update
        TurnoLaboral partialUpdatedTurnoLaboral = new TurnoLaboral();
        partialUpdatedTurnoLaboral.setId(turnoLaboral.getId());

        partialUpdatedTurnoLaboral
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restTurnoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurnoLaboral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurnoLaboral))
            )
            .andExpect(status().isOk());

        // Validate the TurnoLaboral in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurnoLaboralUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTurnoLaboral, turnoLaboral),
            getPersistedTurnoLaboral(turnoLaboral)
        );
    }

    @Test
    @Transactional
    void fullUpdateTurnoLaboralWithPatch() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turnoLaboral using partial update
        TurnoLaboral partialUpdatedTurnoLaboral = new TurnoLaboral();
        partialUpdatedTurnoLaboral.setId(turnoLaboral.getId());

        partialUpdatedTurnoLaboral
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTurnoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurnoLaboral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurnoLaboral))
            )
            .andExpect(status().isOk());

        // Validate the TurnoLaboral in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurnoLaboralUpdatableFieldsEquals(partialUpdatedTurnoLaboral, getPersistedTurnoLaboral(partialUpdatedTurnoLaboral));
    }

    @Test
    @Transactional
    void patchNonExistingTurnoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoLaboral.setId(longCount.incrementAndGet());

        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turnoLaboralDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turnoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurnoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoLaboral.setId(longCount.incrementAndGet());

        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turnoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurnoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoLaboral.setId(longCount.incrementAndGet());

        // Create the TurnoLaboral
        TurnoLaboralDTO turnoLaboralDTO = turnoLaboralMapper.toDto(turnoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoLaboralMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(turnoLaboralDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TurnoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurnoLaboral() throws Exception {
        // Initialize the database
        insertedTurnoLaboral = turnoLaboralRepository.saveAndFlush(turnoLaboral);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the turnoLaboral
        restTurnoLaboralMockMvc
            .perform(delete(ENTITY_API_URL_ID, turnoLaboral.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return turnoLaboralRepository.count();
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

    protected TurnoLaboral getPersistedTurnoLaboral(TurnoLaboral turnoLaboral) {
        return turnoLaboralRepository.findById(turnoLaboral.getId()).orElseThrow();
    }

    protected void assertPersistedTurnoLaboralToMatchAllProperties(TurnoLaboral expectedTurnoLaboral) {
        assertTurnoLaboralAllPropertiesEquals(expectedTurnoLaboral, getPersistedTurnoLaboral(expectedTurnoLaboral));
    }

    protected void assertPersistedTurnoLaboralToMatchUpdatableProperties(TurnoLaboral expectedTurnoLaboral) {
        assertTurnoLaboralAllUpdatablePropertiesEquals(expectedTurnoLaboral, getPersistedTurnoLaboral(expectedTurnoLaboral));
    }
}
