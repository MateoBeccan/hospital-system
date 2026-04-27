package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.DiagnosticoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.domain.Diagnostico;
import com.mycompany.hospital.domain.EstadoDiagnostico;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.TipoDiagnostico;
import com.mycompany.hospital.repository.DiagnosticoRepository;
import com.mycompany.hospital.service.dto.DiagnosticoDTO;
import com.mycompany.hospital.service.mapper.DiagnosticoMapper;
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
 * Integration tests for the {@link DiagnosticoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiagnosticoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_DIAGNOSTICO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_DIAGNOSTICO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_DIAGNOSTICO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_RESOLUCION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_RESOLUCION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_RESOLUCION = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ES_PRINCIPAL = false;
    private static final Boolean UPDATED_ES_PRINCIPAL = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/diagnosticos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private DiagnosticoMapper diagnosticoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiagnosticoMockMvc;

    private Diagnostico diagnostico;

    private Diagnostico insertedDiagnostico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostico createEntity(EntityManager em) {
        Diagnostico diagnostico = new Diagnostico()
            .codigo(DEFAULT_CODIGO)
            .fechaDiagnostico(DEFAULT_FECHA_DIAGNOSTICO)
            .descripcion(DEFAULT_DESCRIPCION)
            .observaciones(DEFAULT_OBSERVACIONES)
            .activo(DEFAULT_ACTIVO)
            .fechaResolucion(DEFAULT_FECHA_RESOLUCION)
            .esPrincipal(DEFAULT_ES_PRINCIPAL)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        Consulta consulta;
        if (TestUtil.findAll(em, Consulta.class).isEmpty()) {
            consulta = ConsultaResourceIT.createEntity(em);
            em.persist(consulta);
            em.flush();
        } else {
            consulta = TestUtil.findAll(em, Consulta.class).get(0);
        }
        diagnostico.setConsulta(consulta);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createEntity(em);
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        diagnostico.setPaciente(paciente);
        // Add required entity
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            medico = MedicoResourceIT.createEntity(em);
            em.persist(medico);
            em.flush();
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        diagnostico.setMedico(medico);
        // Add required entity
        TipoDiagnostico tipoDiagnostico;
        if (TestUtil.findAll(em, TipoDiagnostico.class).isEmpty()) {
            tipoDiagnostico = TipoDiagnosticoResourceIT.createEntity();
            em.persist(tipoDiagnostico);
            em.flush();
        } else {
            tipoDiagnostico = TestUtil.findAll(em, TipoDiagnostico.class).get(0);
        }
        diagnostico.setTipoDiagnostico(tipoDiagnostico);
        // Add required entity
        EstadoDiagnostico estadoDiagnostico;
        if (TestUtil.findAll(em, EstadoDiagnostico.class).isEmpty()) {
            estadoDiagnostico = EstadoDiagnosticoResourceIT.createEntity();
            em.persist(estadoDiagnostico);
            em.flush();
        } else {
            estadoDiagnostico = TestUtil.findAll(em, EstadoDiagnostico.class).get(0);
        }
        diagnostico.setEstadoDiagnostico(estadoDiagnostico);
        return diagnostico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostico createUpdatedEntity(EntityManager em) {
        Diagnostico updatedDiagnostico = new Diagnostico()
            .codigo(UPDATED_CODIGO)
            .fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO)
            .descripcion(UPDATED_DESCRIPCION)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .esPrincipal(UPDATED_ES_PRINCIPAL)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        Consulta consulta;
        if (TestUtil.findAll(em, Consulta.class).isEmpty()) {
            consulta = ConsultaResourceIT.createUpdatedEntity(em);
            em.persist(consulta);
            em.flush();
        } else {
            consulta = TestUtil.findAll(em, Consulta.class).get(0);
        }
        updatedDiagnostico.setConsulta(consulta);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createUpdatedEntity(em);
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        updatedDiagnostico.setPaciente(paciente);
        // Add required entity
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            medico = MedicoResourceIT.createUpdatedEntity(em);
            em.persist(medico);
            em.flush();
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        updatedDiagnostico.setMedico(medico);
        // Add required entity
        TipoDiagnostico tipoDiagnostico;
        if (TestUtil.findAll(em, TipoDiagnostico.class).isEmpty()) {
            tipoDiagnostico = TipoDiagnosticoResourceIT.createUpdatedEntity();
            em.persist(tipoDiagnostico);
            em.flush();
        } else {
            tipoDiagnostico = TestUtil.findAll(em, TipoDiagnostico.class).get(0);
        }
        updatedDiagnostico.setTipoDiagnostico(tipoDiagnostico);
        // Add required entity
        EstadoDiagnostico estadoDiagnostico;
        if (TestUtil.findAll(em, EstadoDiagnostico.class).isEmpty()) {
            estadoDiagnostico = EstadoDiagnosticoResourceIT.createUpdatedEntity();
            em.persist(estadoDiagnostico);
            em.flush();
        } else {
            estadoDiagnostico = TestUtil.findAll(em, EstadoDiagnostico.class).get(0);
        }
        updatedDiagnostico.setEstadoDiagnostico(estadoDiagnostico);
        return updatedDiagnostico;
    }

    @BeforeEach
    void initTest() {
        diagnostico = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDiagnostico != null) {
            diagnosticoRepository.delete(insertedDiagnostico);
            insertedDiagnostico = null;
        }
    }

    @Test
    @Transactional
    void createDiagnostico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);
        var returnedDiagnosticoDTO = om.readValue(
            restDiagnosticoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DiagnosticoDTO.class
        );

        // Validate the Diagnostico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDiagnostico = diagnosticoMapper.toEntity(returnedDiagnosticoDTO);
        assertDiagnosticoUpdatableFieldsEquals(returnedDiagnostico, getPersistedDiagnostico(returnedDiagnostico));

        insertedDiagnostico = returnedDiagnostico;
    }

    @Test
    @Transactional
    void createDiagnosticoWithExistingId() throws Exception {
        // Create the Diagnostico with an existing ID
        diagnostico.setId(1L);
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setCodigo(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaDiagnosticoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setFechaDiagnostico(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setDescripcion(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setActivo(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEsPrincipalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setEsPrincipal(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostico.setFechaAlta(null);

        // Create the Diagnostico, which fails.
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        restDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiagnosticos() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaDiagnostico").value(hasItem(DEFAULT_FECHA_DIAGNOSTICO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaResolucion").value(hasItem(DEFAULT_FECHA_RESOLUCION.toString())))
            .andExpect(jsonPath("$.[*].esPrincipal").value(hasItem(DEFAULT_ES_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getDiagnostico() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get the diagnostico
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL_ID, diagnostico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diagnostico.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.fechaDiagnostico").value(DEFAULT_FECHA_DIAGNOSTICO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaResolucion").value(DEFAULT_FECHA_RESOLUCION.toString()))
            .andExpect(jsonPath("$.esPrincipal").value(DEFAULT_ES_PRINCIPAL))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getDiagnosticosByIdFiltering() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        Long id = diagnostico.getId();

        defaultDiagnosticoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDiagnosticoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDiagnosticoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where codigo equals to
        defaultDiagnosticoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where codigo in
        defaultDiagnosticoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where codigo is not null
        defaultDiagnosticoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where codigo contains
        defaultDiagnosticoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where codigo does not contain
        defaultDiagnosticoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico equals to
        defaultDiagnosticoFiltering(
            "fechaDiagnostico.equals=" + DEFAULT_FECHA_DIAGNOSTICO,
            "fechaDiagnostico.equals=" + UPDATED_FECHA_DIAGNOSTICO
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico in
        defaultDiagnosticoFiltering(
            "fechaDiagnostico.in=" + DEFAULT_FECHA_DIAGNOSTICO + "," + UPDATED_FECHA_DIAGNOSTICO,
            "fechaDiagnostico.in=" + UPDATED_FECHA_DIAGNOSTICO
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico is not null
        defaultDiagnosticoFiltering("fechaDiagnostico.specified=true", "fechaDiagnostico.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico is greater than or equal to
        defaultDiagnosticoFiltering(
            "fechaDiagnostico.greaterThanOrEqual=" + DEFAULT_FECHA_DIAGNOSTICO,
            "fechaDiagnostico.greaterThanOrEqual=" + UPDATED_FECHA_DIAGNOSTICO
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico is less than or equal to
        defaultDiagnosticoFiltering(
            "fechaDiagnostico.lessThanOrEqual=" + DEFAULT_FECHA_DIAGNOSTICO,
            "fechaDiagnostico.lessThanOrEqual=" + SMALLER_FECHA_DIAGNOSTICO
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico is less than
        defaultDiagnosticoFiltering(
            "fechaDiagnostico.lessThan=" + UPDATED_FECHA_DIAGNOSTICO,
            "fechaDiagnostico.lessThan=" + DEFAULT_FECHA_DIAGNOSTICO
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaDiagnosticoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaDiagnostico is greater than
        defaultDiagnosticoFiltering(
            "fechaDiagnostico.greaterThan=" + SMALLER_FECHA_DIAGNOSTICO,
            "fechaDiagnostico.greaterThan=" + DEFAULT_FECHA_DIAGNOSTICO
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where descripcion equals to
        defaultDiagnosticoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where descripcion in
        defaultDiagnosticoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where descripcion is not null
        defaultDiagnosticoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where descripcion contains
        defaultDiagnosticoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where descripcion does not contain
        defaultDiagnosticoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where activo equals to
        defaultDiagnosticoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where activo in
        defaultDiagnosticoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where activo is not null
        defaultDiagnosticoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion equals to
        defaultDiagnosticoFiltering(
            "fechaResolucion.equals=" + DEFAULT_FECHA_RESOLUCION,
            "fechaResolucion.equals=" + UPDATED_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion in
        defaultDiagnosticoFiltering(
            "fechaResolucion.in=" + DEFAULT_FECHA_RESOLUCION + "," + UPDATED_FECHA_RESOLUCION,
            "fechaResolucion.in=" + UPDATED_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion is not null
        defaultDiagnosticoFiltering("fechaResolucion.specified=true", "fechaResolucion.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion is greater than or equal to
        defaultDiagnosticoFiltering(
            "fechaResolucion.greaterThanOrEqual=" + DEFAULT_FECHA_RESOLUCION,
            "fechaResolucion.greaterThanOrEqual=" + UPDATED_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion is less than or equal to
        defaultDiagnosticoFiltering(
            "fechaResolucion.lessThanOrEqual=" + DEFAULT_FECHA_RESOLUCION,
            "fechaResolucion.lessThanOrEqual=" + SMALLER_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion is less than
        defaultDiagnosticoFiltering(
            "fechaResolucion.lessThan=" + UPDATED_FECHA_RESOLUCION,
            "fechaResolucion.lessThan=" + DEFAULT_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaResolucionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaResolucion is greater than
        defaultDiagnosticoFiltering(
            "fechaResolucion.greaterThan=" + SMALLER_FECHA_RESOLUCION,
            "fechaResolucion.greaterThan=" + DEFAULT_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByEsPrincipalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where esPrincipal equals to
        defaultDiagnosticoFiltering("esPrincipal.equals=" + DEFAULT_ES_PRINCIPAL, "esPrincipal.equals=" + UPDATED_ES_PRINCIPAL);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByEsPrincipalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where esPrincipal in
        defaultDiagnosticoFiltering(
            "esPrincipal.in=" + DEFAULT_ES_PRINCIPAL + "," + UPDATED_ES_PRINCIPAL,
            "esPrincipal.in=" + UPDATED_ES_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByEsPrincipalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where esPrincipal is not null
        defaultDiagnosticoFiltering("esPrincipal.specified=true", "esPrincipal.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta equals to
        defaultDiagnosticoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta in
        defaultDiagnosticoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta is not null
        defaultDiagnosticoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta is greater than or equal to
        defaultDiagnosticoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta is less than or equal to
        defaultDiagnosticoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta is less than
        defaultDiagnosticoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaAlta is greater than
        defaultDiagnosticoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja equals to
        defaultDiagnosticoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja in
        defaultDiagnosticoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja is not null
        defaultDiagnosticoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja is greater than or equal to
        defaultDiagnosticoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja is less than or equal to
        defaultDiagnosticoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja is less than
        defaultDiagnosticoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        // Get all the diagnosticoList where fechaBaja is greater than
        defaultDiagnosticoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllDiagnosticosByConsultaIsEqualToSomething() throws Exception {
        Consulta consulta;
        if (TestUtil.findAll(em, Consulta.class).isEmpty()) {
            diagnosticoRepository.saveAndFlush(diagnostico);
            consulta = ConsultaResourceIT.createEntity(em);
        } else {
            consulta = TestUtil.findAll(em, Consulta.class).get(0);
        }
        em.persist(consulta);
        em.flush();
        diagnostico.setConsulta(consulta);
        diagnosticoRepository.saveAndFlush(diagnostico);
        Long consultaId = consulta.getId();
        // Get all the diagnosticoList where consulta equals to consultaId
        defaultDiagnosticoShouldBeFound("consultaId.equals=" + consultaId);

        // Get all the diagnosticoList where consulta equals to (consultaId + 1)
        defaultDiagnosticoShouldNotBeFound("consultaId.equals=" + (consultaId + 1));
    }

    @Test
    @Transactional
    void getAllDiagnosticosByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            diagnosticoRepository.saveAndFlush(diagnostico);
            paciente = PacienteResourceIT.createEntity(em);
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        diagnostico.setPaciente(paciente);
        diagnosticoRepository.saveAndFlush(diagnostico);
        Long pacienteId = paciente.getId();
        // Get all the diagnosticoList where paciente equals to pacienteId
        defaultDiagnosticoShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the diagnosticoList where paciente equals to (pacienteId + 1)
        defaultDiagnosticoShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllDiagnosticosByMedicoIsEqualToSomething() throws Exception {
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            diagnosticoRepository.saveAndFlush(diagnostico);
            medico = MedicoResourceIT.createEntity(em);
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        em.persist(medico);
        em.flush();
        diagnostico.setMedico(medico);
        diagnosticoRepository.saveAndFlush(diagnostico);
        Long medicoId = medico.getId();
        // Get all the diagnosticoList where medico equals to medicoId
        defaultDiagnosticoShouldBeFound("medicoId.equals=" + medicoId);

        // Get all the diagnosticoList where medico equals to (medicoId + 1)
        defaultDiagnosticoShouldNotBeFound("medicoId.equals=" + (medicoId + 1));
    }

    @Test
    @Transactional
    void getAllDiagnosticosByTipoDiagnosticoIsEqualToSomething() throws Exception {
        TipoDiagnostico tipoDiagnostico;
        if (TestUtil.findAll(em, TipoDiagnostico.class).isEmpty()) {
            diagnosticoRepository.saveAndFlush(diagnostico);
            tipoDiagnostico = TipoDiagnosticoResourceIT.createEntity();
        } else {
            tipoDiagnostico = TestUtil.findAll(em, TipoDiagnostico.class).get(0);
        }
        em.persist(tipoDiagnostico);
        em.flush();
        diagnostico.setTipoDiagnostico(tipoDiagnostico);
        diagnosticoRepository.saveAndFlush(diagnostico);
        Long tipoDiagnosticoId = tipoDiagnostico.getId();
        // Get all the diagnosticoList where tipoDiagnostico equals to tipoDiagnosticoId
        defaultDiagnosticoShouldBeFound("tipoDiagnosticoId.equals=" + tipoDiagnosticoId);

        // Get all the diagnosticoList where tipoDiagnostico equals to (tipoDiagnosticoId + 1)
        defaultDiagnosticoShouldNotBeFound("tipoDiagnosticoId.equals=" + (tipoDiagnosticoId + 1));
    }

    @Test
    @Transactional
    void getAllDiagnosticosByEstadoDiagnosticoIsEqualToSomething() throws Exception {
        EstadoDiagnostico estadoDiagnostico;
        if (TestUtil.findAll(em, EstadoDiagnostico.class).isEmpty()) {
            diagnosticoRepository.saveAndFlush(diagnostico);
            estadoDiagnostico = EstadoDiagnosticoResourceIT.createEntity();
        } else {
            estadoDiagnostico = TestUtil.findAll(em, EstadoDiagnostico.class).get(0);
        }
        em.persist(estadoDiagnostico);
        em.flush();
        diagnostico.setEstadoDiagnostico(estadoDiagnostico);
        diagnosticoRepository.saveAndFlush(diagnostico);
        Long estadoDiagnosticoId = estadoDiagnostico.getId();
        // Get all the diagnosticoList where estadoDiagnostico equals to estadoDiagnosticoId
        defaultDiagnosticoShouldBeFound("estadoDiagnosticoId.equals=" + estadoDiagnosticoId);

        // Get all the diagnosticoList where estadoDiagnostico equals to (estadoDiagnosticoId + 1)
        defaultDiagnosticoShouldNotBeFound("estadoDiagnosticoId.equals=" + (estadoDiagnosticoId + 1));
    }

    private void defaultDiagnosticoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDiagnosticoShouldBeFound(shouldBeFound);
        defaultDiagnosticoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiagnosticoShouldBeFound(String filter) throws Exception {
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaDiagnostico").value(hasItem(DEFAULT_FECHA_DIAGNOSTICO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaResolucion").value(hasItem(DEFAULT_FECHA_RESOLUCION.toString())))
            .andExpect(jsonPath("$.[*].esPrincipal").value(hasItem(DEFAULT_ES_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiagnosticoShouldNotBeFound(String filter) throws Exception {
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDiagnostico() throws Exception {
        // Get the diagnostico
        restDiagnosticoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiagnostico() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostico
        Diagnostico updatedDiagnostico = diagnosticoRepository.findById(diagnostico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDiagnostico are not directly saved in db
        em.detach(updatedDiagnostico);
        updatedDiagnostico
            .codigo(UPDATED_CODIGO)
            .fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO)
            .descripcion(UPDATED_DESCRIPCION)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .esPrincipal(UPDATED_ES_PRINCIPAL)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(updatedDiagnostico);

        restDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnosticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiagnosticoToMatchAllProperties(updatedDiagnostico);
    }

    @Test
    @Transactional
    void putNonExistingDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnosticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostico using partial update
        Diagnostico partialUpdatedDiagnostico = new Diagnostico();
        partialUpdatedDiagnostico.setId(diagnostico.getId());

        partialUpdatedDiagnostico
            .codigo(UPDATED_CODIGO)
            .fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .esPrincipal(UPDATED_ES_PRINCIPAL)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnostico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiagnosticoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDiagnostico, diagnostico),
            getPersistedDiagnostico(diagnostico)
        );
    }

    @Test
    @Transactional
    void fullUpdateDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostico using partial update
        Diagnostico partialUpdatedDiagnostico = new Diagnostico();
        partialUpdatedDiagnostico.setId(diagnostico.getId());

        partialUpdatedDiagnostico
            .codigo(UPDATED_CODIGO)
            .fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO)
            .descripcion(UPDATED_DESCRIPCION)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .esPrincipal(UPDATED_ES_PRINCIPAL)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnostico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiagnosticoUpdatableFieldsEquals(partialUpdatedDiagnostico, getPersistedDiagnostico(partialUpdatedDiagnostico));
    }

    @Test
    @Transactional
    void patchNonExistingDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diagnosticoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostico.setId(longCount.incrementAndGet());

        // Create the Diagnostico
        DiagnosticoDTO diagnosticoDTO = diagnosticoMapper.toDto(diagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(diagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiagnostico() throws Exception {
        // Initialize the database
        insertedDiagnostico = diagnosticoRepository.saveAndFlush(diagnostico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the diagnostico
        restDiagnosticoMockMvc
            .perform(delete(ENTITY_API_URL_ID, diagnostico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return diagnosticoRepository.count();
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

    protected Diagnostico getPersistedDiagnostico(Diagnostico diagnostico) {
        return diagnosticoRepository.findById(diagnostico.getId()).orElseThrow();
    }

    protected void assertPersistedDiagnosticoToMatchAllProperties(Diagnostico expectedDiagnostico) {
        assertDiagnosticoAllPropertiesEquals(expectedDiagnostico, getPersistedDiagnostico(expectedDiagnostico));
    }

    protected void assertPersistedDiagnosticoToMatchUpdatableProperties(Diagnostico expectedDiagnostico) {
        assertDiagnosticoAllUpdatablePropertiesEquals(expectedDiagnostico, getPersistedDiagnostico(expectedDiagnostico));
    }
}
