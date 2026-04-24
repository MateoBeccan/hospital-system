package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.HistoriaClinicaAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.repository.HistoriaClinicaRepository;
import com.mycompany.hospital.service.dto.HistoriaClinicaDTO;
import com.mycompany.hospital.service.mapper.HistoriaClinicaMapper;
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
 * Integration tests for the {@link HistoriaClinicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriaClinicaResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_APERTURA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_APERTURA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_APERTURA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_ULTIMA_ACTUALIZACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ULTIMA_ACTUALIZACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ULTIMA_ACTUALIZACION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ANTECEDENTES_PERSONALES = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDENTES_PERSONALES = "BBBBBBBBBB";

    private static final String DEFAULT_ANTECEDENTES_FAMILIARES = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDENTES_FAMILIARES = "BBBBBBBBBB";

    private static final String DEFAULT_ENFERMEDADES_PREVIAS = "AAAAAAAAAA";
    private static final String UPDATED_ENFERMEDADES_PREVIAS = "BBBBBBBBBB";

    private static final String DEFAULT_CIRUGIAS_PREVIAS = "AAAAAAAAAA";
    private static final String UPDATED_CIRUGIAS_PREVIAS = "BBBBBBBBBB";

    private static final String DEFAULT_ALERGIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALERGIAS = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICACION_HABITUAL = "AAAAAAAAAA";
    private static final String UPDATED_MEDICACION_HABITUAL = "BBBBBBBBBB";

    private static final String DEFAULT_HABITOS = "AAAAAAAAAA";
    private static final String UPDATED_HABITOS = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES_GENERALES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES_GENERALES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final LocalDate DEFAULT_FECHA_CIERRE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CIERRE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_CIERRE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MOTIVO_CIERRE = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_CIERRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historia-clinicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private HistoriaClinicaMapper historiaClinicaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriaClinicaMockMvc;

    private HistoriaClinica historiaClinica;

    private HistoriaClinica insertedHistoriaClinica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaClinica createEntity(EntityManager em) {
        HistoriaClinica historiaClinica = new HistoriaClinica()
            .numero(DEFAULT_NUMERO)
            .fechaApertura(DEFAULT_FECHA_APERTURA)
            .fechaUltimaActualizacion(DEFAULT_FECHA_ULTIMA_ACTUALIZACION)
            .antecedentesPersonales(DEFAULT_ANTECEDENTES_PERSONALES)
            .antecedentesFamiliares(DEFAULT_ANTECEDENTES_FAMILIARES)
            .enfermedadesPrevias(DEFAULT_ENFERMEDADES_PREVIAS)
            .cirugiasPrevias(DEFAULT_CIRUGIAS_PREVIAS)
            .alergias(DEFAULT_ALERGIAS)
            .medicacionHabitual(DEFAULT_MEDICACION_HABITUAL)
            .habitos(DEFAULT_HABITOS)
            .observacionesGenerales(DEFAULT_OBSERVACIONES_GENERALES)
            .activa(DEFAULT_ACTIVA)
            .fechaCierre(DEFAULT_FECHA_CIERRE)
            .motivoCierre(DEFAULT_MOTIVO_CIERRE);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createEntity(em);
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        historiaClinica.setPaciente(paciente);
        return historiaClinica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaClinica createUpdatedEntity(EntityManager em) {
        HistoriaClinica updatedHistoriaClinica = new HistoriaClinica()
            .numero(UPDATED_NUMERO)
            .fechaApertura(UPDATED_FECHA_APERTURA)
            .fechaUltimaActualizacion(UPDATED_FECHA_ULTIMA_ACTUALIZACION)
            .antecedentesPersonales(UPDATED_ANTECEDENTES_PERSONALES)
            .antecedentesFamiliares(UPDATED_ANTECEDENTES_FAMILIARES)
            .enfermedadesPrevias(UPDATED_ENFERMEDADES_PREVIAS)
            .cirugiasPrevias(UPDATED_CIRUGIAS_PREVIAS)
            .alergias(UPDATED_ALERGIAS)
            .medicacionHabitual(UPDATED_MEDICACION_HABITUAL)
            .habitos(UPDATED_HABITOS)
            .observacionesGenerales(UPDATED_OBSERVACIONES_GENERALES)
            .activa(UPDATED_ACTIVA)
            .fechaCierre(UPDATED_FECHA_CIERRE)
            .motivoCierre(UPDATED_MOTIVO_CIERRE);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createUpdatedEntity(em);
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        updatedHistoriaClinica.setPaciente(paciente);
        return updatedHistoriaClinica;
    }

    @BeforeEach
    void initTest() {
        historiaClinica = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriaClinica != null) {
            historiaClinicaRepository.delete(insertedHistoriaClinica);
            insertedHistoriaClinica = null;
        }
    }

    @Test
    @Transactional
    void createHistoriaClinica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);
        var returnedHistoriaClinicaDTO = om.readValue(
            restHistoriaClinicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriaClinicaDTO.class
        );

        // Validate the HistoriaClinica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriaClinica = historiaClinicaMapper.toEntity(returnedHistoriaClinicaDTO);
        assertHistoriaClinicaUpdatableFieldsEquals(returnedHistoriaClinica, getPersistedHistoriaClinica(returnedHistoriaClinica));

        insertedHistoriaClinica = returnedHistoriaClinica;
    }

    @Test
    @Transactional
    void createHistoriaClinicaWithExistingId() throws Exception {
        // Create the HistoriaClinica with an existing ID
        historiaClinica.setId(1L);
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriaClinicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiaClinica.setNumero(null);

        // Create the HistoriaClinica, which fails.
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        restHistoriaClinicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAperturaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiaClinica.setFechaApertura(null);

        // Create the HistoriaClinica, which fails.
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        restHistoriaClinicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiaClinica.setActiva(null);

        // Create the HistoriaClinica, which fails.
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        restHistoriaClinicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicas() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiaClinica.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaApertura").value(hasItem(DEFAULT_FECHA_APERTURA.toString())))
            .andExpect(jsonPath("$.[*].fechaUltimaActualizacion").value(hasItem(DEFAULT_FECHA_ULTIMA_ACTUALIZACION.toString())))
            .andExpect(jsonPath("$.[*].antecedentesPersonales").value(hasItem(DEFAULT_ANTECEDENTES_PERSONALES)))
            .andExpect(jsonPath("$.[*].antecedentesFamiliares").value(hasItem(DEFAULT_ANTECEDENTES_FAMILIARES)))
            .andExpect(jsonPath("$.[*].enfermedadesPrevias").value(hasItem(DEFAULT_ENFERMEDADES_PREVIAS)))
            .andExpect(jsonPath("$.[*].cirugiasPrevias").value(hasItem(DEFAULT_CIRUGIAS_PREVIAS)))
            .andExpect(jsonPath("$.[*].alergias").value(hasItem(DEFAULT_ALERGIAS)))
            .andExpect(jsonPath("$.[*].medicacionHabitual").value(hasItem(DEFAULT_MEDICACION_HABITUAL)))
            .andExpect(jsonPath("$.[*].habitos").value(hasItem(DEFAULT_HABITOS)))
            .andExpect(jsonPath("$.[*].observacionesGenerales").value(hasItem(DEFAULT_OBSERVACIONES_GENERALES)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].fechaCierre").value(hasItem(DEFAULT_FECHA_CIERRE.toString())))
            .andExpect(jsonPath("$.[*].motivoCierre").value(hasItem(DEFAULT_MOTIVO_CIERRE)));
    }

    @Test
    @Transactional
    void getHistoriaClinica() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get the historiaClinica
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL_ID, historiaClinica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiaClinica.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.fechaApertura").value(DEFAULT_FECHA_APERTURA.toString()))
            .andExpect(jsonPath("$.fechaUltimaActualizacion").value(DEFAULT_FECHA_ULTIMA_ACTUALIZACION.toString()))
            .andExpect(jsonPath("$.antecedentesPersonales").value(DEFAULT_ANTECEDENTES_PERSONALES))
            .andExpect(jsonPath("$.antecedentesFamiliares").value(DEFAULT_ANTECEDENTES_FAMILIARES))
            .andExpect(jsonPath("$.enfermedadesPrevias").value(DEFAULT_ENFERMEDADES_PREVIAS))
            .andExpect(jsonPath("$.cirugiasPrevias").value(DEFAULT_CIRUGIAS_PREVIAS))
            .andExpect(jsonPath("$.alergias").value(DEFAULT_ALERGIAS))
            .andExpect(jsonPath("$.medicacionHabitual").value(DEFAULT_MEDICACION_HABITUAL))
            .andExpect(jsonPath("$.habitos").value(DEFAULT_HABITOS))
            .andExpect(jsonPath("$.observacionesGenerales").value(DEFAULT_OBSERVACIONES_GENERALES))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA))
            .andExpect(jsonPath("$.fechaCierre").value(DEFAULT_FECHA_CIERRE.toString()))
            .andExpect(jsonPath("$.motivoCierre").value(DEFAULT_MOTIVO_CIERRE));
    }

    @Test
    @Transactional
    void getHistoriaClinicasByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        Long id = historiaClinica.getId();

        defaultHistoriaClinicaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoriaClinicaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoriaClinicaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where numero equals to
        defaultHistoriaClinicaFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where numero in
        defaultHistoriaClinicaFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where numero is not null
        defaultHistoriaClinicaFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where numero contains
        defaultHistoriaClinicaFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where numero does not contain
        defaultHistoriaClinicaFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura equals to
        defaultHistoriaClinicaFiltering("fechaApertura.equals=" + DEFAULT_FECHA_APERTURA, "fechaApertura.equals=" + UPDATED_FECHA_APERTURA);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura in
        defaultHistoriaClinicaFiltering(
            "fechaApertura.in=" + DEFAULT_FECHA_APERTURA + "," + UPDATED_FECHA_APERTURA,
            "fechaApertura.in=" + UPDATED_FECHA_APERTURA
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura is not null
        defaultHistoriaClinicaFiltering("fechaApertura.specified=true", "fechaApertura.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura is greater than or equal to
        defaultHistoriaClinicaFiltering(
            "fechaApertura.greaterThanOrEqual=" + DEFAULT_FECHA_APERTURA,
            "fechaApertura.greaterThanOrEqual=" + UPDATED_FECHA_APERTURA
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura is less than or equal to
        defaultHistoriaClinicaFiltering(
            "fechaApertura.lessThanOrEqual=" + DEFAULT_FECHA_APERTURA,
            "fechaApertura.lessThanOrEqual=" + SMALLER_FECHA_APERTURA
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura is less than
        defaultHistoriaClinicaFiltering(
            "fechaApertura.lessThan=" + UPDATED_FECHA_APERTURA,
            "fechaApertura.lessThan=" + DEFAULT_FECHA_APERTURA
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaAperturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaApertura is greater than
        defaultHistoriaClinicaFiltering(
            "fechaApertura.greaterThan=" + SMALLER_FECHA_APERTURA,
            "fechaApertura.greaterThan=" + DEFAULT_FECHA_APERTURA
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion equals to
        defaultHistoriaClinicaFiltering(
            "fechaUltimaActualizacion.equals=" + DEFAULT_FECHA_ULTIMA_ACTUALIZACION,
            "fechaUltimaActualizacion.equals=" + UPDATED_FECHA_ULTIMA_ACTUALIZACION
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion in
        defaultHistoriaClinicaFiltering(
            "fechaUltimaActualizacion.in=" + DEFAULT_FECHA_ULTIMA_ACTUALIZACION + "," + UPDATED_FECHA_ULTIMA_ACTUALIZACION,
            "fechaUltimaActualizacion.in=" + UPDATED_FECHA_ULTIMA_ACTUALIZACION
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion is not null
        defaultHistoriaClinicaFiltering("fechaUltimaActualizacion.specified=true", "fechaUltimaActualizacion.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion is greater than or equal to
        defaultHistoriaClinicaFiltering(
            "fechaUltimaActualizacion.greaterThanOrEqual=" + DEFAULT_FECHA_ULTIMA_ACTUALIZACION,
            "fechaUltimaActualizacion.greaterThanOrEqual=" + UPDATED_FECHA_ULTIMA_ACTUALIZACION
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion is less than or equal to
        defaultHistoriaClinicaFiltering(
            "fechaUltimaActualizacion.lessThanOrEqual=" + DEFAULT_FECHA_ULTIMA_ACTUALIZACION,
            "fechaUltimaActualizacion.lessThanOrEqual=" + SMALLER_FECHA_ULTIMA_ACTUALIZACION
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion is less than
        defaultHistoriaClinicaFiltering(
            "fechaUltimaActualizacion.lessThan=" + UPDATED_FECHA_ULTIMA_ACTUALIZACION,
            "fechaUltimaActualizacion.lessThan=" + DEFAULT_FECHA_ULTIMA_ACTUALIZACION
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaUltimaActualizacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaUltimaActualizacion is greater than
        defaultHistoriaClinicaFiltering(
            "fechaUltimaActualizacion.greaterThan=" + SMALLER_FECHA_ULTIMA_ACTUALIZACION,
            "fechaUltimaActualizacion.greaterThan=" + DEFAULT_FECHA_ULTIMA_ACTUALIZACION
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByActivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where activa equals to
        defaultHistoriaClinicaFiltering("activa.equals=" + DEFAULT_ACTIVA, "activa.equals=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByActivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where activa in
        defaultHistoriaClinicaFiltering("activa.in=" + DEFAULT_ACTIVA + "," + UPDATED_ACTIVA, "activa.in=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByActivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where activa is not null
        defaultHistoriaClinicaFiltering("activa.specified=true", "activa.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre equals to
        defaultHistoriaClinicaFiltering("fechaCierre.equals=" + DEFAULT_FECHA_CIERRE, "fechaCierre.equals=" + UPDATED_FECHA_CIERRE);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre in
        defaultHistoriaClinicaFiltering(
            "fechaCierre.in=" + DEFAULT_FECHA_CIERRE + "," + UPDATED_FECHA_CIERRE,
            "fechaCierre.in=" + UPDATED_FECHA_CIERRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre is not null
        defaultHistoriaClinicaFiltering("fechaCierre.specified=true", "fechaCierre.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre is greater than or equal to
        defaultHistoriaClinicaFiltering(
            "fechaCierre.greaterThanOrEqual=" + DEFAULT_FECHA_CIERRE,
            "fechaCierre.greaterThanOrEqual=" + UPDATED_FECHA_CIERRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre is less than or equal to
        defaultHistoriaClinicaFiltering(
            "fechaCierre.lessThanOrEqual=" + DEFAULT_FECHA_CIERRE,
            "fechaCierre.lessThanOrEqual=" + SMALLER_FECHA_CIERRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre is less than
        defaultHistoriaClinicaFiltering("fechaCierre.lessThan=" + UPDATED_FECHA_CIERRE, "fechaCierre.lessThan=" + DEFAULT_FECHA_CIERRE);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByFechaCierreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where fechaCierre is greater than
        defaultHistoriaClinicaFiltering(
            "fechaCierre.greaterThan=" + SMALLER_FECHA_CIERRE,
            "fechaCierre.greaterThan=" + DEFAULT_FECHA_CIERRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByMotivoCierreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where motivoCierre equals to
        defaultHistoriaClinicaFiltering("motivoCierre.equals=" + DEFAULT_MOTIVO_CIERRE, "motivoCierre.equals=" + UPDATED_MOTIVO_CIERRE);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByMotivoCierreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where motivoCierre in
        defaultHistoriaClinicaFiltering(
            "motivoCierre.in=" + DEFAULT_MOTIVO_CIERRE + "," + UPDATED_MOTIVO_CIERRE,
            "motivoCierre.in=" + UPDATED_MOTIVO_CIERRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByMotivoCierreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where motivoCierre is not null
        defaultHistoriaClinicaFiltering("motivoCierre.specified=true", "motivoCierre.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByMotivoCierreContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where motivoCierre contains
        defaultHistoriaClinicaFiltering("motivoCierre.contains=" + DEFAULT_MOTIVO_CIERRE, "motivoCierre.contains=" + UPDATED_MOTIVO_CIERRE);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByMotivoCierreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList where motivoCierre does not contain
        defaultHistoriaClinicaFiltering(
            "motivoCierre.doesNotContain=" + UPDATED_MOTIVO_CIERRE,
            "motivoCierre.doesNotContain=" + DEFAULT_MOTIVO_CIERRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriaClinicasByPacienteIsEqualToSomething() throws Exception {
        // Get already existing entity
        Paciente paciente = historiaClinica.getPaciente();
        historiaClinicaRepository.saveAndFlush(historiaClinica);
        Long pacienteId = paciente.getId();
        // Get all the historiaClinicaList where paciente equals to pacienteId
        defaultHistoriaClinicaShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the historiaClinicaList where paciente equals to (pacienteId + 1)
        defaultHistoriaClinicaShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    private void defaultHistoriaClinicaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoriaClinicaShouldBeFound(shouldBeFound);
        defaultHistoriaClinicaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriaClinicaShouldBeFound(String filter) throws Exception {
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiaClinica.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaApertura").value(hasItem(DEFAULT_FECHA_APERTURA.toString())))
            .andExpect(jsonPath("$.[*].fechaUltimaActualizacion").value(hasItem(DEFAULT_FECHA_ULTIMA_ACTUALIZACION.toString())))
            .andExpect(jsonPath("$.[*].antecedentesPersonales").value(hasItem(DEFAULT_ANTECEDENTES_PERSONALES)))
            .andExpect(jsonPath("$.[*].antecedentesFamiliares").value(hasItem(DEFAULT_ANTECEDENTES_FAMILIARES)))
            .andExpect(jsonPath("$.[*].enfermedadesPrevias").value(hasItem(DEFAULT_ENFERMEDADES_PREVIAS)))
            .andExpect(jsonPath("$.[*].cirugiasPrevias").value(hasItem(DEFAULT_CIRUGIAS_PREVIAS)))
            .andExpect(jsonPath("$.[*].alergias").value(hasItem(DEFAULT_ALERGIAS)))
            .andExpect(jsonPath("$.[*].medicacionHabitual").value(hasItem(DEFAULT_MEDICACION_HABITUAL)))
            .andExpect(jsonPath("$.[*].habitos").value(hasItem(DEFAULT_HABITOS)))
            .andExpect(jsonPath("$.[*].observacionesGenerales").value(hasItem(DEFAULT_OBSERVACIONES_GENERALES)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].fechaCierre").value(hasItem(DEFAULT_FECHA_CIERRE.toString())))
            .andExpect(jsonPath("$.[*].motivoCierre").value(hasItem(DEFAULT_MOTIVO_CIERRE)));

        // Check, that the count call also returns 1
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriaClinicaShouldNotBeFound(String filter) throws Exception {
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoriaClinica() throws Exception {
        // Get the historiaClinica
        restHistoriaClinicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriaClinica() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiaClinica
        HistoriaClinica updatedHistoriaClinica = historiaClinicaRepository.findById(historiaClinica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriaClinica are not directly saved in db
        em.detach(updatedHistoriaClinica);
        updatedHistoriaClinica
            .numero(UPDATED_NUMERO)
            .fechaApertura(UPDATED_FECHA_APERTURA)
            .fechaUltimaActualizacion(UPDATED_FECHA_ULTIMA_ACTUALIZACION)
            .antecedentesPersonales(UPDATED_ANTECEDENTES_PERSONALES)
            .antecedentesFamiliares(UPDATED_ANTECEDENTES_FAMILIARES)
            .enfermedadesPrevias(UPDATED_ENFERMEDADES_PREVIAS)
            .cirugiasPrevias(UPDATED_CIRUGIAS_PREVIAS)
            .alergias(UPDATED_ALERGIAS)
            .medicacionHabitual(UPDATED_MEDICACION_HABITUAL)
            .habitos(UPDATED_HABITOS)
            .observacionesGenerales(UPDATED_OBSERVACIONES_GENERALES)
            .activa(UPDATED_ACTIVA)
            .fechaCierre(UPDATED_FECHA_CIERRE)
            .motivoCierre(UPDATED_MOTIVO_CIERRE);
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(updatedHistoriaClinica);

        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiaClinicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriaClinicaToMatchAllProperties(updatedHistoriaClinica);
    }

    @Test
    @Transactional
    void putNonExistingHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiaClinicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiaClinicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriaClinicaWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiaClinica using partial update
        HistoriaClinica partialUpdatedHistoriaClinica = new HistoriaClinica();
        partialUpdatedHistoriaClinica.setId(historiaClinica.getId());

        partialUpdatedHistoriaClinica
            .numero(UPDATED_NUMERO)
            .fechaUltimaActualizacion(UPDATED_FECHA_ULTIMA_ACTUALIZACION)
            .antecedentesPersonales(UPDATED_ANTECEDENTES_PERSONALES)
            .antecedentesFamiliares(UPDATED_ANTECEDENTES_FAMILIARES)
            .cirugiasPrevias(UPDATED_CIRUGIAS_PREVIAS)
            .medicacionHabitual(UPDATED_MEDICACION_HABITUAL)
            .habitos(UPDATED_HABITOS)
            .activa(UPDATED_ACTIVA)
            .fechaCierre(UPDATED_FECHA_CIERRE)
            .motivoCierre(UPDATED_MOTIVO_CIERRE);

        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriaClinicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriaClinica, historiaClinica),
            getPersistedHistoriaClinica(historiaClinica)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriaClinicaWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiaClinica using partial update
        HistoriaClinica partialUpdatedHistoriaClinica = new HistoriaClinica();
        partialUpdatedHistoriaClinica.setId(historiaClinica.getId());

        partialUpdatedHistoriaClinica
            .numero(UPDATED_NUMERO)
            .fechaApertura(UPDATED_FECHA_APERTURA)
            .fechaUltimaActualizacion(UPDATED_FECHA_ULTIMA_ACTUALIZACION)
            .antecedentesPersonales(UPDATED_ANTECEDENTES_PERSONALES)
            .antecedentesFamiliares(UPDATED_ANTECEDENTES_FAMILIARES)
            .enfermedadesPrevias(UPDATED_ENFERMEDADES_PREVIAS)
            .cirugiasPrevias(UPDATED_CIRUGIAS_PREVIAS)
            .alergias(UPDATED_ALERGIAS)
            .medicacionHabitual(UPDATED_MEDICACION_HABITUAL)
            .habitos(UPDATED_HABITOS)
            .observacionesGenerales(UPDATED_OBSERVACIONES_GENERALES)
            .activa(UPDATED_ACTIVA)
            .fechaCierre(UPDATED_FECHA_CIERRE)
            .motivoCierre(UPDATED_MOTIVO_CIERRE);

        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriaClinicaUpdatableFieldsEquals(
            partialUpdatedHistoriaClinica,
            getPersistedHistoriaClinica(partialUpdatedHistoriaClinica)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiaClinicaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiaClinicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriaClinica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiaClinica.setId(longCount.incrementAndGet());

        // Create the HistoriaClinica
        HistoriaClinicaDTO historiaClinicaDTO = historiaClinicaMapper.toDto(historiaClinica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historiaClinicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriaClinica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriaClinica() throws Exception {
        // Initialize the database
        insertedHistoriaClinica = historiaClinicaRepository.saveAndFlush(historiaClinica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiaClinica
        restHistoriaClinicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiaClinica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiaClinicaRepository.count();
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

    protected HistoriaClinica getPersistedHistoriaClinica(HistoriaClinica historiaClinica) {
        return historiaClinicaRepository.findById(historiaClinica.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriaClinicaToMatchAllProperties(HistoriaClinica expectedHistoriaClinica) {
        assertHistoriaClinicaAllPropertiesEquals(expectedHistoriaClinica, getPersistedHistoriaClinica(expectedHistoriaClinica));
    }

    protected void assertPersistedHistoriaClinicaToMatchUpdatableProperties(HistoriaClinica expectedHistoriaClinica) {
        assertHistoriaClinicaAllUpdatablePropertiesEquals(expectedHistoriaClinica, getPersistedHistoriaClinica(expectedHistoriaClinica));
    }
}
