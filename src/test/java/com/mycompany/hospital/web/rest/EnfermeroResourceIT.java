package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.EnfermeroAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.domain.Enfermero;
import com.mycompany.hospital.domain.TurnoLaboral;
import com.mycompany.hospital.repository.EnfermeroRepository;
import com.mycompany.hospital.service.dto.EnfermeroDTO;
import com.mycompany.hospital.service.mapper.EnfermeroMapper;
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
 * Integration tests for the {@link EnfermeroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnfermeroResourceIT {

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_MATRICULACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_MATRICULACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_MATRICULACION = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/enfermeros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @Autowired
    private EnfermeroMapper enfermeroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnfermeroMockMvc;

    private Enfermero enfermero;

    private Enfermero insertedEnfermero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermero createEntity(EntityManager em) {
        Enfermero enfermero = new Enfermero()
            .matricula(DEFAULT_MATRICULA)
            .fechaMatriculacion(DEFAULT_FECHA_MATRICULACION)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        Empleado empleado;
        if (TestUtil.findAll(em, Empleado.class).isEmpty()) {
            empleado = EmpleadoResourceIT.createEntity(em);
            em.persist(empleado);
            em.flush();
        } else {
            empleado = TestUtil.findAll(em, Empleado.class).get(0);
        }
        enfermero.setEmpleado(empleado);
        return enfermero;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermero createUpdatedEntity(EntityManager em) {
        Enfermero updatedEnfermero = new Enfermero()
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        Empleado empleado;
        if (TestUtil.findAll(em, Empleado.class).isEmpty()) {
            empleado = EmpleadoResourceIT.createUpdatedEntity(em);
            em.persist(empleado);
            em.flush();
        } else {
            empleado = TestUtil.findAll(em, Empleado.class).get(0);
        }
        updatedEnfermero.setEmpleado(empleado);
        return updatedEnfermero;
    }

    @BeforeEach
    void initTest() {
        enfermero = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEnfermero != null) {
            enfermeroRepository.delete(insertedEnfermero);
            insertedEnfermero = null;
        }
    }

    @Test
    @Transactional
    void createEnfermero() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);
        var returnedEnfermeroDTO = om.readValue(
            restEnfermeroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermeroDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnfermeroDTO.class
        );

        // Validate the Enfermero in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnfermero = enfermeroMapper.toEntity(returnedEnfermeroDTO);
        assertEnfermeroUpdatableFieldsEquals(returnedEnfermero, getPersistedEnfermero(returnedEnfermero));

        insertedEnfermero = returnedEnfermero;
    }

    @Test
    @Transactional
    void createEnfermeroWithExistingId() throws Exception {
        // Create the Enfermero with an existing ID
        enfermero.setId(1L);
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnfermeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermeroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enfermero.setMatricula(null);

        // Create the Enfermero, which fails.
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        restEnfermeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermeroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enfermero.setActivo(null);

        // Create the Enfermero, which fails.
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        restEnfermeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermeroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enfermero.setFechaAlta(null);

        // Create the Enfermero, which fails.
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        restEnfermeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermeroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnfermeros() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList
        restEnfermeroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermero.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].fechaMatriculacion").value(hasItem(DEFAULT_FECHA_MATRICULACION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getEnfermero() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get the enfermero
        restEnfermeroMockMvc
            .perform(get(ENTITY_API_URL_ID, enfermero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enfermero.getId().intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.fechaMatriculacion").value(DEFAULT_FECHA_MATRICULACION.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getEnfermerosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        Long id = enfermero.getId();

        defaultEnfermeroFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEnfermeroFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEnfermeroFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnfermerosByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where matricula equals to
        defaultEnfermeroFiltering("matricula.equals=" + DEFAULT_MATRICULA, "matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where matricula in
        defaultEnfermeroFiltering("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA, "matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where matricula is not null
        defaultEnfermeroFiltering("matricula.specified=true", "matricula.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermerosByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where matricula contains
        defaultEnfermeroFiltering("matricula.contains=" + DEFAULT_MATRICULA, "matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where matricula does not contain
        defaultEnfermeroFiltering("matricula.doesNotContain=" + UPDATED_MATRICULA, "matricula.doesNotContain=" + DEFAULT_MATRICULA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion equals to
        defaultEnfermeroFiltering(
            "fechaMatriculacion.equals=" + DEFAULT_FECHA_MATRICULACION,
            "fechaMatriculacion.equals=" + UPDATED_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion in
        defaultEnfermeroFiltering(
            "fechaMatriculacion.in=" + DEFAULT_FECHA_MATRICULACION + "," + UPDATED_FECHA_MATRICULACION,
            "fechaMatriculacion.in=" + UPDATED_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion is not null
        defaultEnfermeroFiltering("fechaMatriculacion.specified=true", "fechaMatriculacion.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion is greater than or equal to
        defaultEnfermeroFiltering(
            "fechaMatriculacion.greaterThanOrEqual=" + DEFAULT_FECHA_MATRICULACION,
            "fechaMatriculacion.greaterThanOrEqual=" + UPDATED_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion is less than or equal to
        defaultEnfermeroFiltering(
            "fechaMatriculacion.lessThanOrEqual=" + DEFAULT_FECHA_MATRICULACION,
            "fechaMatriculacion.lessThanOrEqual=" + SMALLER_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion is less than
        defaultEnfermeroFiltering(
            "fechaMatriculacion.lessThan=" + UPDATED_FECHA_MATRICULACION,
            "fechaMatriculacion.lessThan=" + DEFAULT_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaMatriculacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaMatriculacion is greater than
        defaultEnfermeroFiltering(
            "fechaMatriculacion.greaterThan=" + SMALLER_FECHA_MATRICULACION,
            "fechaMatriculacion.greaterThan=" + DEFAULT_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where activo equals to
        defaultEnfermeroFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEnfermerosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where activo in
        defaultEnfermeroFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEnfermerosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where activo is not null
        defaultEnfermeroFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta equals to
        defaultEnfermeroFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta in
        defaultEnfermeroFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta is not null
        defaultEnfermeroFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta is greater than or equal to
        defaultEnfermeroFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta is less than or equal to
        defaultEnfermeroFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta is less than
        defaultEnfermeroFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaAlta is greater than
        defaultEnfermeroFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja equals to
        defaultEnfermeroFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja in
        defaultEnfermeroFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja is not null
        defaultEnfermeroFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja is greater than or equal to
        defaultEnfermeroFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja is less than or equal to
        defaultEnfermeroFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja is less than
        defaultEnfermeroFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        // Get all the enfermeroList where fechaBaja is greater than
        defaultEnfermeroFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEnfermerosByEmpleadoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Empleado empleado = enfermero.getEmpleado();
        enfermeroRepository.saveAndFlush(enfermero);
        Long empleadoId = empleado.getId();
        // Get all the enfermeroList where empleado equals to empleadoId
        defaultEnfermeroShouldBeFound("empleadoId.equals=" + empleadoId);

        // Get all the enfermeroList where empleado equals to (empleadoId + 1)
        defaultEnfermeroShouldNotBeFound("empleadoId.equals=" + (empleadoId + 1));
    }

    @Test
    @Transactional
    void getAllEnfermerosByTurnoLaboralIsEqualToSomething() throws Exception {
        TurnoLaboral turnoLaboral;
        if (TestUtil.findAll(em, TurnoLaboral.class).isEmpty()) {
            enfermeroRepository.saveAndFlush(enfermero);
            turnoLaboral = TurnoLaboralResourceIT.createEntity();
        } else {
            turnoLaboral = TestUtil.findAll(em, TurnoLaboral.class).get(0);
        }
        em.persist(turnoLaboral);
        em.flush();
        enfermero.setTurnoLaboral(turnoLaboral);
        enfermeroRepository.saveAndFlush(enfermero);
        Long turnoLaboralId = turnoLaboral.getId();
        // Get all the enfermeroList where turnoLaboral equals to turnoLaboralId
        defaultEnfermeroShouldBeFound("turnoLaboralId.equals=" + turnoLaboralId);

        // Get all the enfermeroList where turnoLaboral equals to (turnoLaboralId + 1)
        defaultEnfermeroShouldNotBeFound("turnoLaboralId.equals=" + (turnoLaboralId + 1));
    }

    private void defaultEnfermeroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEnfermeroShouldBeFound(shouldBeFound);
        defaultEnfermeroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnfermeroShouldBeFound(String filter) throws Exception {
        restEnfermeroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermero.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].fechaMatriculacion").value(hasItem(DEFAULT_FECHA_MATRICULACION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restEnfermeroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnfermeroShouldNotBeFound(String filter) throws Exception {
        restEnfermeroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnfermeroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnfermero() throws Exception {
        // Get the enfermero
        restEnfermeroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnfermero() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermero
        Enfermero updatedEnfermero = enfermeroRepository.findById(enfermero.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnfermero are not directly saved in db
        em.detach(updatedEnfermero);
        updatedEnfermero
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(updatedEnfermero);

        restEnfermeroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermeroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermeroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnfermeroToMatchAllProperties(updatedEnfermero);
    }

    @Test
    @Transactional
    void putNonExistingEnfermero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermero.setId(longCount.incrementAndGet());

        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermeroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermeroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermeroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnfermero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermero.setId(longCount.incrementAndGet());

        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermeroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermeroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnfermero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermero.setId(longCount.incrementAndGet());

        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermeroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermeroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnfermeroWithPatch() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermero using partial update
        Enfermero partialUpdatedEnfermero = new Enfermero();
        partialUpdatedEnfermero.setId(enfermero.getId());

        partialUpdatedEnfermero.matricula(UPDATED_MATRICULA).fechaMatriculacion(UPDATED_FECHA_MATRICULACION).fechaBaja(UPDATED_FECHA_BAJA);

        restEnfermeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnfermero))
            )
            .andExpect(status().isOk());

        // Validate the Enfermero in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermeroUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnfermero, enfermero),
            getPersistedEnfermero(enfermero)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnfermeroWithPatch() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermero using partial update
        Enfermero partialUpdatedEnfermero = new Enfermero();
        partialUpdatedEnfermero.setId(enfermero.getId());

        partialUpdatedEnfermero
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restEnfermeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnfermero))
            )
            .andExpect(status().isOk());

        // Validate the Enfermero in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermeroUpdatableFieldsEquals(partialUpdatedEnfermero, getPersistedEnfermero(partialUpdatedEnfermero));
    }

    @Test
    @Transactional
    void patchNonExistingEnfermero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermero.setId(longCount.incrementAndGet());

        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enfermeroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enfermeroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnfermero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermero.setId(longCount.incrementAndGet());

        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enfermeroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnfermero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermero.setId(longCount.incrementAndGet());

        // Create the Enfermero
        EnfermeroDTO enfermeroDTO = enfermeroMapper.toDto(enfermero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermeroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enfermeroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnfermero() throws Exception {
        // Initialize the database
        insertedEnfermero = enfermeroRepository.saveAndFlush(enfermero);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enfermero
        restEnfermeroMockMvc
            .perform(delete(ENTITY_API_URL_ID, enfermero.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enfermeroRepository.count();
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

    protected Enfermero getPersistedEnfermero(Enfermero enfermero) {
        return enfermeroRepository.findById(enfermero.getId()).orElseThrow();
    }

    protected void assertPersistedEnfermeroToMatchAllProperties(Enfermero expectedEnfermero) {
        assertEnfermeroAllPropertiesEquals(expectedEnfermero, getPersistedEnfermero(expectedEnfermero));
    }

    protected void assertPersistedEnfermeroToMatchUpdatableProperties(Enfermero expectedEnfermero) {
        assertEnfermeroAllUpdatablePropertiesEquals(expectedEnfermero, getPersistedEnfermero(expectedEnfermero));
    }
}
