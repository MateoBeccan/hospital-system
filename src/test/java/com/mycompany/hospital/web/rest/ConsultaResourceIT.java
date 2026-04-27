package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.ConsultaAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.Turno;
import com.mycompany.hospital.repository.ConsultaRepository;
import com.mycompany.hospital.service.dto.ConsultaDTO;
import com.mycompany.hospital.service.mapper.ConsultaMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ConsultaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultaResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_HORA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_HORA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SINTOMAS = "AAAAAAAAAA";
    private static final String UPDATED_SINTOMAS = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO_CONSULTA = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_CONSULTA = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMEN_FISICO = "AAAAAAAAAA";
    private static final String UPDATED_EXAMEN_FISICO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String DEFAULT_INDICACIONES = "AAAAAAAAAA";
    private static final String UPDATED_INDICACIONES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/consultas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ConsultaMapper consultaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultaMockMvc;

    private Consulta consulta;

    private Consulta insertedConsulta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consulta createEntity(EntityManager em) {
        Consulta consulta = new Consulta()
            .codigo(DEFAULT_CODIGO)
            .fechaHoraInicio(DEFAULT_FECHA_HORA_INICIO)
            .fechaHoraFin(DEFAULT_FECHA_HORA_FIN)
            .sintomas(DEFAULT_SINTOMAS)
            .motivoConsulta(DEFAULT_MOTIVO_CONSULTA)
            .examenFisico(DEFAULT_EXAMEN_FISICO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .indicaciones(DEFAULT_INDICACIONES)
            .activa(DEFAULT_ACTIVA)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createEntity(em);
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        consulta.setPaciente(paciente);
        // Add required entity
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            medico = MedicoResourceIT.createEntity(em);
            em.persist(medico);
            em.flush();
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        consulta.setMedico(medico);
        // Add required entity
        HistoriaClinica historiaClinica;
        if (TestUtil.findAll(em, HistoriaClinica.class).isEmpty()) {
            historiaClinica = HistoriaClinicaResourceIT.createEntity(em);
            em.persist(historiaClinica);
            em.flush();
        } else {
            historiaClinica = TestUtil.findAll(em, HistoriaClinica.class).get(0);
        }
        consulta.setHistoriaClinica(historiaClinica);
        return consulta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consulta createUpdatedEntity(EntityManager em) {
        Consulta updatedConsulta = new Consulta()
            .codigo(UPDATED_CODIGO)
            .fechaHoraInicio(UPDATED_FECHA_HORA_INICIO)
            .fechaHoraFin(UPDATED_FECHA_HORA_FIN)
            .sintomas(UPDATED_SINTOMAS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .examenFisico(UPDATED_EXAMEN_FISICO)
            .observaciones(UPDATED_OBSERVACIONES)
            .indicaciones(UPDATED_INDICACIONES)
            .activa(UPDATED_ACTIVA)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            paciente = PacienteResourceIT.createUpdatedEntity(em);
            em.persist(paciente);
            em.flush();
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        updatedConsulta.setPaciente(paciente);
        // Add required entity
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            medico = MedicoResourceIT.createUpdatedEntity(em);
            em.persist(medico);
            em.flush();
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        updatedConsulta.setMedico(medico);
        // Add required entity
        HistoriaClinica historiaClinica;
        if (TestUtil.findAll(em, HistoriaClinica.class).isEmpty()) {
            historiaClinica = HistoriaClinicaResourceIT.createUpdatedEntity(em);
            em.persist(historiaClinica);
            em.flush();
        } else {
            historiaClinica = TestUtil.findAll(em, HistoriaClinica.class).get(0);
        }
        updatedConsulta.setHistoriaClinica(historiaClinica);
        return updatedConsulta;
    }

    @BeforeEach
    void initTest() {
        consulta = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedConsulta != null) {
            consultaRepository.delete(insertedConsulta);
            insertedConsulta = null;
        }
    }

    @Test
    @Transactional
    void createConsulta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);
        var returnedConsultaDTO = om.readValue(
            restConsultaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsultaDTO.class
        );

        // Validate the Consulta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConsulta = consultaMapper.toEntity(returnedConsultaDTO);
        assertConsultaUpdatableFieldsEquals(returnedConsulta, getPersistedConsulta(returnedConsulta));

        insertedConsulta = returnedConsulta;
    }

    @Test
    @Transactional
    void createConsultaWithExistingId() throws Exception {
        // Create the Consulta with an existing ID
        consulta.setId(1L);
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consulta.setCodigo(null);

        // Create the Consulta, which fails.
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaHoraInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consulta.setFechaHoraInicio(null);

        // Create the Consulta, which fails.
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotivoConsultaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consulta.setMotivoConsulta(null);

        // Create the Consulta, which fails.
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consulta.setActiva(null);

        // Create the Consulta, which fails.
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consulta.setFechaAlta(null);

        // Create the Consulta, which fails.
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        restConsultaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsultas() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consulta.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaHoraInicio").value(hasItem(DEFAULT_FECHA_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaHoraFin").value(hasItem(DEFAULT_FECHA_HORA_FIN.toString())))
            .andExpect(jsonPath("$.[*].sintomas").value(hasItem(DEFAULT_SINTOMAS)))
            .andExpect(jsonPath("$.[*].motivoConsulta").value(hasItem(DEFAULT_MOTIVO_CONSULTA)))
            .andExpect(jsonPath("$.[*].examenFisico").value(hasItem(DEFAULT_EXAMEN_FISICO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].indicaciones").value(hasItem(DEFAULT_INDICACIONES)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getConsulta() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get the consulta
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL_ID, consulta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consulta.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.fechaHoraInicio").value(DEFAULT_FECHA_HORA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaHoraFin").value(DEFAULT_FECHA_HORA_FIN.toString()))
            .andExpect(jsonPath("$.sintomas").value(DEFAULT_SINTOMAS))
            .andExpect(jsonPath("$.motivoConsulta").value(DEFAULT_MOTIVO_CONSULTA))
            .andExpect(jsonPath("$.examenFisico").value(DEFAULT_EXAMEN_FISICO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.indicaciones").value(DEFAULT_INDICACIONES))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getConsultasByIdFiltering() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        Long id = consulta.getId();

        defaultConsultaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConsultaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConsultaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsultasByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where codigo equals to
        defaultConsultaFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllConsultasByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where codigo in
        defaultConsultaFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllConsultasByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where codigo is not null
        defaultConsultaFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where codigo contains
        defaultConsultaFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllConsultasByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where codigo does not contain
        defaultConsultaFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaHoraInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaHoraInicio equals to
        defaultConsultaFiltering(
            "fechaHoraInicio.equals=" + DEFAULT_FECHA_HORA_INICIO,
            "fechaHoraInicio.equals=" + UPDATED_FECHA_HORA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllConsultasByFechaHoraInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaHoraInicio in
        defaultConsultaFiltering(
            "fechaHoraInicio.in=" + DEFAULT_FECHA_HORA_INICIO + "," + UPDATED_FECHA_HORA_INICIO,
            "fechaHoraInicio.in=" + UPDATED_FECHA_HORA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllConsultasByFechaHoraInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaHoraInicio is not null
        defaultConsultaFiltering("fechaHoraInicio.specified=true", "fechaHoraInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByFechaHoraFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaHoraFin equals to
        defaultConsultaFiltering("fechaHoraFin.equals=" + DEFAULT_FECHA_HORA_FIN, "fechaHoraFin.equals=" + UPDATED_FECHA_HORA_FIN);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaHoraFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaHoraFin in
        defaultConsultaFiltering(
            "fechaHoraFin.in=" + DEFAULT_FECHA_HORA_FIN + "," + UPDATED_FECHA_HORA_FIN,
            "fechaHoraFin.in=" + UPDATED_FECHA_HORA_FIN
        );
    }

    @Test
    @Transactional
    void getAllConsultasByFechaHoraFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaHoraFin is not null
        defaultConsultaFiltering("fechaHoraFin.specified=true", "fechaHoraFin.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByMotivoConsultaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where motivoConsulta equals to
        defaultConsultaFiltering("motivoConsulta.equals=" + DEFAULT_MOTIVO_CONSULTA, "motivoConsulta.equals=" + UPDATED_MOTIVO_CONSULTA);
    }

    @Test
    @Transactional
    void getAllConsultasByMotivoConsultaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where motivoConsulta in
        defaultConsultaFiltering(
            "motivoConsulta.in=" + DEFAULT_MOTIVO_CONSULTA + "," + UPDATED_MOTIVO_CONSULTA,
            "motivoConsulta.in=" + UPDATED_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultasByMotivoConsultaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where motivoConsulta is not null
        defaultConsultaFiltering("motivoConsulta.specified=true", "motivoConsulta.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByMotivoConsultaContainsSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where motivoConsulta contains
        defaultConsultaFiltering(
            "motivoConsulta.contains=" + DEFAULT_MOTIVO_CONSULTA,
            "motivoConsulta.contains=" + UPDATED_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultasByMotivoConsultaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where motivoConsulta does not contain
        defaultConsultaFiltering(
            "motivoConsulta.doesNotContain=" + UPDATED_MOTIVO_CONSULTA,
            "motivoConsulta.doesNotContain=" + DEFAULT_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultasByActivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where activa equals to
        defaultConsultaFiltering("activa.equals=" + DEFAULT_ACTIVA, "activa.equals=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllConsultasByActivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where activa in
        defaultConsultaFiltering("activa.in=" + DEFAULT_ACTIVA + "," + UPDATED_ACTIVA, "activa.in=" + UPDATED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllConsultasByActivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where activa is not null
        defaultConsultaFiltering("activa.specified=true", "activa.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta equals to
        defaultConsultaFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta in
        defaultConsultaFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta is not null
        defaultConsultaFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta is greater than or equal to
        defaultConsultaFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta is less than or equal to
        defaultConsultaFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta is less than
        defaultConsultaFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaAlta is greater than
        defaultConsultaFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja equals to
        defaultConsultaFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja in
        defaultConsultaFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja is not null
        defaultConsultaFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja is greater than or equal to
        defaultConsultaFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja is less than or equal to
        defaultConsultaFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja is less than
        defaultConsultaFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllConsultasByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        // Get all the consultaList where fechaBaja is greater than
        defaultConsultaFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllConsultasByTurnoIsEqualToSomething() throws Exception {
        Turno turno;
        if (TestUtil.findAll(em, Turno.class).isEmpty()) {
            consultaRepository.saveAndFlush(consulta);
            turno = TurnoResourceIT.createEntity(em);
        } else {
            turno = TestUtil.findAll(em, Turno.class).get(0);
        }
        em.persist(turno);
        em.flush();
        consulta.setTurno(turno);
        consultaRepository.saveAndFlush(consulta);
        Long turnoId = turno.getId();
        // Get all the consultaList where turno equals to turnoId
        defaultConsultaShouldBeFound("turnoId.equals=" + turnoId);

        // Get all the consultaList where turno equals to (turnoId + 1)
        defaultConsultaShouldNotBeFound("turnoId.equals=" + (turnoId + 1));
    }

    @Test
    @Transactional
    void getAllConsultasByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            consultaRepository.saveAndFlush(consulta);
            paciente = PacienteResourceIT.createEntity(em);
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        consulta.setPaciente(paciente);
        consultaRepository.saveAndFlush(consulta);
        Long pacienteId = paciente.getId();
        // Get all the consultaList where paciente equals to pacienteId
        defaultConsultaShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the consultaList where paciente equals to (pacienteId + 1)
        defaultConsultaShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllConsultasByMedicoIsEqualToSomething() throws Exception {
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            consultaRepository.saveAndFlush(consulta);
            medico = MedicoResourceIT.createEntity(em);
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        em.persist(medico);
        em.flush();
        consulta.setMedico(medico);
        consultaRepository.saveAndFlush(consulta);
        Long medicoId = medico.getId();
        // Get all the consultaList where medico equals to medicoId
        defaultConsultaShouldBeFound("medicoId.equals=" + medicoId);

        // Get all the consultaList where medico equals to (medicoId + 1)
        defaultConsultaShouldNotBeFound("medicoId.equals=" + (medicoId + 1));
    }

    @Test
    @Transactional
    void getAllConsultasByHistoriaClinicaIsEqualToSomething() throws Exception {
        HistoriaClinica historiaClinica;
        if (TestUtil.findAll(em, HistoriaClinica.class).isEmpty()) {
            consultaRepository.saveAndFlush(consulta);
            historiaClinica = HistoriaClinicaResourceIT.createEntity(em);
        } else {
            historiaClinica = TestUtil.findAll(em, HistoriaClinica.class).get(0);
        }
        em.persist(historiaClinica);
        em.flush();
        consulta.setHistoriaClinica(historiaClinica);
        consultaRepository.saveAndFlush(consulta);
        Long historiaClinicaId = historiaClinica.getId();
        // Get all the consultaList where historiaClinica equals to historiaClinicaId
        defaultConsultaShouldBeFound("historiaClinicaId.equals=" + historiaClinicaId);

        // Get all the consultaList where historiaClinica equals to (historiaClinicaId + 1)
        defaultConsultaShouldNotBeFound("historiaClinicaId.equals=" + (historiaClinicaId + 1));
    }

    private void defaultConsultaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConsultaShouldBeFound(shouldBeFound);
        defaultConsultaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsultaShouldBeFound(String filter) throws Exception {
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consulta.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaHoraInicio").value(hasItem(DEFAULT_FECHA_HORA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaHoraFin").value(hasItem(DEFAULT_FECHA_HORA_FIN.toString())))
            .andExpect(jsonPath("$.[*].sintomas").value(hasItem(DEFAULT_SINTOMAS)))
            .andExpect(jsonPath("$.[*].motivoConsulta").value(hasItem(DEFAULT_MOTIVO_CONSULTA)))
            .andExpect(jsonPath("$.[*].examenFisico").value(hasItem(DEFAULT_EXAMEN_FISICO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].indicaciones").value(hasItem(DEFAULT_INDICACIONES)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsultaShouldNotBeFound(String filter) throws Exception {
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsultaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsulta() throws Exception {
        // Get the consulta
        restConsultaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsulta() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consulta
        Consulta updatedConsulta = consultaRepository.findById(consulta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsulta are not directly saved in db
        em.detach(updatedConsulta);
        updatedConsulta
            .codigo(UPDATED_CODIGO)
            .fechaHoraInicio(UPDATED_FECHA_HORA_INICIO)
            .fechaHoraFin(UPDATED_FECHA_HORA_FIN)
            .sintomas(UPDATED_SINTOMAS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .examenFisico(UPDATED_EXAMEN_FISICO)
            .observaciones(UPDATED_OBSERVACIONES)
            .indicaciones(UPDATED_INDICACIONES)
            .activa(UPDATED_ACTIVA)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        ConsultaDTO consultaDTO = consultaMapper.toDto(updatedConsulta);

        restConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsultaToMatchAllProperties(updatedConsulta);
    }

    @Test
    @Transactional
    void putNonExistingConsulta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consulta.setId(longCount.incrementAndGet());

        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsulta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consulta.setId(longCount.incrementAndGet());

        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsulta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consulta.setId(longCount.incrementAndGet());

        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultaWithPatch() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consulta using partial update
        Consulta partialUpdatedConsulta = new Consulta();
        partialUpdatedConsulta.setId(consulta.getId());

        partialUpdatedConsulta
            .codigo(UPDATED_CODIGO)
            .fechaHoraFin(UPDATED_FECHA_HORA_FIN)
            .sintomas(UPDATED_SINTOMAS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .examenFisico(UPDATED_EXAMEN_FISICO)
            .observaciones(UPDATED_OBSERVACIONES)
            .indicaciones(UPDATED_INDICACIONES);

        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsulta))
            )
            .andExpect(status().isOk());

        // Validate the Consulta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsultaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedConsulta, consulta), getPersistedConsulta(consulta));
    }

    @Test
    @Transactional
    void fullUpdateConsultaWithPatch() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consulta using partial update
        Consulta partialUpdatedConsulta = new Consulta();
        partialUpdatedConsulta.setId(consulta.getId());

        partialUpdatedConsulta
            .codigo(UPDATED_CODIGO)
            .fechaHoraInicio(UPDATED_FECHA_HORA_INICIO)
            .fechaHoraFin(UPDATED_FECHA_HORA_FIN)
            .sintomas(UPDATED_SINTOMAS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .examenFisico(UPDATED_EXAMEN_FISICO)
            .observaciones(UPDATED_OBSERVACIONES)
            .indicaciones(UPDATED_INDICACIONES)
            .activa(UPDATED_ACTIVA)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsulta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsulta))
            )
            .andExpect(status().isOk());

        // Validate the Consulta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsultaUpdatableFieldsEquals(partialUpdatedConsulta, getPersistedConsulta(partialUpdatedConsulta));
    }

    @Test
    @Transactional
    void patchNonExistingConsulta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consulta.setId(longCount.incrementAndGet());

        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consultaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsulta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consulta.setId(longCount.incrementAndGet());

        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consultaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsulta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consulta.setId(longCount.incrementAndGet());

        // Create the Consulta
        ConsultaDTO consultaDTO = consultaMapper.toDto(consulta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consultaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consulta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsulta() throws Exception {
        // Initialize the database
        insertedConsulta = consultaRepository.saveAndFlush(consulta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consulta
        restConsultaMockMvc
            .perform(delete(ENTITY_API_URL_ID, consulta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consultaRepository.count();
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

    protected Consulta getPersistedConsulta(Consulta consulta) {
        return consultaRepository.findById(consulta.getId()).orElseThrow();
    }

    protected void assertPersistedConsultaToMatchAllProperties(Consulta expectedConsulta) {
        assertConsultaAllPropertiesEquals(expectedConsulta, getPersistedConsulta(expectedConsulta));
    }

    protected void assertPersistedConsultaToMatchUpdatableProperties(Consulta expectedConsulta) {
        assertConsultaAllUpdatablePropertiesEquals(expectedConsulta, getPersistedConsulta(expectedConsulta));
    }
}
