package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.TurnoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.CanalSolicitud;
import com.mycompany.hospital.domain.Especialidad;
import com.mycompany.hospital.domain.EstadoTurno;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.Turno;
import com.mycompany.hospital.repository.TurnoRepository;
import com.mycompany.hospital.service.dto.TurnoDTO;
import com.mycompany.hospital.service.mapper.TurnoMapper;
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
 * Integration tests for the {@link TurnoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TurnoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURACION_MINUTOS = 5;
    private static final Integer UPDATED_DURACION_MINUTOS = 6;
    private static final Integer SMALLER_DURACION_MINUTOS = 5 - 1;

    private static final String DEFAULT_MOTIVO_CONSULTA = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_CONSULTA = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/turnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private TurnoMapper turnoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurnoMockMvc;

    private Turno turno;

    private Turno insertedTurno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turno createEntity(EntityManager em) {
        Turno turno = new Turno()
            .codigo(DEFAULT_CODIGO)
            .fechaHora(DEFAULT_FECHA_HORA)
            .duracionMinutos(DEFAULT_DURACION_MINUTOS)
            .motivoConsulta(DEFAULT_MOTIVO_CONSULTA)
            .observaciones(DEFAULT_OBSERVACIONES)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .activo(DEFAULT_ACTIVO)
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
        turno.setPaciente(paciente);
        // Add required entity
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            medico = MedicoResourceIT.createEntity(em);
            em.persist(medico);
            em.flush();
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        turno.setMedico(medico);
        // Add required entity
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            especialidad = EspecialidadResourceIT.createEntity();
            em.persist(especialidad);
            em.flush();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        turno.setEspecialidad(especialidad);
        // Add required entity
        EstadoTurno estadoTurno;
        if (TestUtil.findAll(em, EstadoTurno.class).isEmpty()) {
            estadoTurno = EstadoTurnoResourceIT.createEntity();
            em.persist(estadoTurno);
            em.flush();
        } else {
            estadoTurno = TestUtil.findAll(em, EstadoTurno.class).get(0);
        }
        turno.setEstadoTurno(estadoTurno);
        // Add required entity
        CanalSolicitud canalSolicitud;
        if (TestUtil.findAll(em, CanalSolicitud.class).isEmpty()) {
            canalSolicitud = CanalSolicitudResourceIT.createEntity();
            em.persist(canalSolicitud);
            em.flush();
        } else {
            canalSolicitud = TestUtil.findAll(em, CanalSolicitud.class).get(0);
        }
        turno.setCanalSolicitud(canalSolicitud);
        return turno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turno createUpdatedEntity(EntityManager em) {
        Turno updatedTurno = new Turno()
            .codigo(UPDATED_CODIGO)
            .fechaHora(UPDATED_FECHA_HORA)
            .duracionMinutos(UPDATED_DURACION_MINUTOS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .activo(UPDATED_ACTIVO)
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
        updatedTurno.setPaciente(paciente);
        // Add required entity
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            medico = MedicoResourceIT.createUpdatedEntity(em);
            em.persist(medico);
            em.flush();
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        updatedTurno.setMedico(medico);
        // Add required entity
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            especialidad = EspecialidadResourceIT.createUpdatedEntity();
            em.persist(especialidad);
            em.flush();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        updatedTurno.setEspecialidad(especialidad);
        // Add required entity
        EstadoTurno estadoTurno;
        if (TestUtil.findAll(em, EstadoTurno.class).isEmpty()) {
            estadoTurno = EstadoTurnoResourceIT.createUpdatedEntity();
            em.persist(estadoTurno);
            em.flush();
        } else {
            estadoTurno = TestUtil.findAll(em, EstadoTurno.class).get(0);
        }
        updatedTurno.setEstadoTurno(estadoTurno);
        // Add required entity
        CanalSolicitud canalSolicitud;
        if (TestUtil.findAll(em, CanalSolicitud.class).isEmpty()) {
            canalSolicitud = CanalSolicitudResourceIT.createUpdatedEntity();
            em.persist(canalSolicitud);
            em.flush();
        } else {
            canalSolicitud = TestUtil.findAll(em, CanalSolicitud.class).get(0);
        }
        updatedTurno.setCanalSolicitud(canalSolicitud);
        return updatedTurno;
    }

    @BeforeEach
    void initTest() {
        turno = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTurno != null) {
            turnoRepository.delete(insertedTurno);
            insertedTurno = null;
        }
    }

    @Test
    @Transactional
    void createTurno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);
        var returnedTurnoDTO = om.readValue(
            restTurnoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TurnoDTO.class
        );

        // Validate the Turno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTurno = turnoMapper.toEntity(returnedTurnoDTO);
        assertTurnoUpdatableFieldsEquals(returnedTurno, getPersistedTurno(returnedTurno));

        insertedTurno = returnedTurno;
    }

    @Test
    @Transactional
    void createTurnoWithExistingId() throws Exception {
        // Create the Turno with an existing ID
        turno.setId(1L);
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setCodigo(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaHoraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setFechaHora(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDuracionMinutosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setDuracionMinutos(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotivoConsultaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setMotivoConsulta(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaCreacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setFechaCreacion(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setActivo(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turno.setFechaAlta(null);

        // Create the Turno, which fails.
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        restTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurnos() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList
        restTurnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turno.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA.toString())))
            .andExpect(jsonPath("$.[*].duracionMinutos").value(hasItem(DEFAULT_DURACION_MINUTOS)))
            .andExpect(jsonPath("$.[*].motivoConsulta").value(hasItem(DEFAULT_MOTIVO_CONSULTA)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getTurno() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get the turno
        restTurnoMockMvc
            .perform(get(ENTITY_API_URL_ID, turno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turno.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.fechaHora").value(DEFAULT_FECHA_HORA.toString()))
            .andExpect(jsonPath("$.duracionMinutos").value(DEFAULT_DURACION_MINUTOS))
            .andExpect(jsonPath("$.motivoConsulta").value(DEFAULT_MOTIVO_CONSULTA))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getTurnosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        Long id = turno.getId();

        defaultTurnoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTurnoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTurnoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTurnosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where codigo equals to
        defaultTurnoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where codigo in
        defaultTurnoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where codigo is not null
        defaultTurnoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where codigo contains
        defaultTurnoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where codigo does not contain
        defaultTurnoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaHoraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaHora equals to
        defaultTurnoFiltering("fechaHora.equals=" + DEFAULT_FECHA_HORA, "fechaHora.equals=" + UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaHoraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaHora in
        defaultTurnoFiltering("fechaHora.in=" + DEFAULT_FECHA_HORA + "," + UPDATED_FECHA_HORA, "fechaHora.in=" + UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaHoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaHora is not null
        defaultTurnoFiltering("fechaHora.specified=true", "fechaHora.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos equals to
        defaultTurnoFiltering("duracionMinutos.equals=" + DEFAULT_DURACION_MINUTOS, "duracionMinutos.equals=" + UPDATED_DURACION_MINUTOS);
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos in
        defaultTurnoFiltering(
            "duracionMinutos.in=" + DEFAULT_DURACION_MINUTOS + "," + UPDATED_DURACION_MINUTOS,
            "duracionMinutos.in=" + UPDATED_DURACION_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos is not null
        defaultTurnoFiltering("duracionMinutos.specified=true", "duracionMinutos.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos is greater than or equal to
        defaultTurnoFiltering(
            "duracionMinutos.greaterThanOrEqual=" + DEFAULT_DURACION_MINUTOS,
            "duracionMinutos.greaterThanOrEqual=" + (DEFAULT_DURACION_MINUTOS + 1)
        );
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos is less than or equal to
        defaultTurnoFiltering(
            "duracionMinutos.lessThanOrEqual=" + DEFAULT_DURACION_MINUTOS,
            "duracionMinutos.lessThanOrEqual=" + SMALLER_DURACION_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos is less than
        defaultTurnoFiltering(
            "duracionMinutos.lessThan=" + (DEFAULT_DURACION_MINUTOS + 1),
            "duracionMinutos.lessThan=" + DEFAULT_DURACION_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllTurnosByDuracionMinutosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where duracionMinutos is greater than
        defaultTurnoFiltering(
            "duracionMinutos.greaterThan=" + SMALLER_DURACION_MINUTOS,
            "duracionMinutos.greaterThan=" + DEFAULT_DURACION_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllTurnosByMotivoConsultaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where motivoConsulta equals to
        defaultTurnoFiltering("motivoConsulta.equals=" + DEFAULT_MOTIVO_CONSULTA, "motivoConsulta.equals=" + UPDATED_MOTIVO_CONSULTA);
    }

    @Test
    @Transactional
    void getAllTurnosByMotivoConsultaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where motivoConsulta in
        defaultTurnoFiltering(
            "motivoConsulta.in=" + DEFAULT_MOTIVO_CONSULTA + "," + UPDATED_MOTIVO_CONSULTA,
            "motivoConsulta.in=" + UPDATED_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllTurnosByMotivoConsultaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where motivoConsulta is not null
        defaultTurnoFiltering("motivoConsulta.specified=true", "motivoConsulta.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByMotivoConsultaContainsSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where motivoConsulta contains
        defaultTurnoFiltering("motivoConsulta.contains=" + DEFAULT_MOTIVO_CONSULTA, "motivoConsulta.contains=" + UPDATED_MOTIVO_CONSULTA);
    }

    @Test
    @Transactional
    void getAllTurnosByMotivoConsultaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where motivoConsulta does not contain
        defaultTurnoFiltering(
            "motivoConsulta.doesNotContain=" + UPDATED_MOTIVO_CONSULTA,
            "motivoConsulta.doesNotContain=" + DEFAULT_MOTIVO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllTurnosByFechaCreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaCreacion equals to
        defaultTurnoFiltering("fechaCreacion.equals=" + DEFAULT_FECHA_CREACION, "fechaCreacion.equals=" + UPDATED_FECHA_CREACION);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaCreacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaCreacion in
        defaultTurnoFiltering(
            "fechaCreacion.in=" + DEFAULT_FECHA_CREACION + "," + UPDATED_FECHA_CREACION,
            "fechaCreacion.in=" + UPDATED_FECHA_CREACION
        );
    }

    @Test
    @Transactional
    void getAllTurnosByFechaCreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaCreacion is not null
        defaultTurnoFiltering("fechaCreacion.specified=true", "fechaCreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where activo equals to
        defaultTurnoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTurnosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where activo in
        defaultTurnoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTurnosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where activo is not null
        defaultTurnoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta equals to
        defaultTurnoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta in
        defaultTurnoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta is not null
        defaultTurnoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta is greater than or equal to
        defaultTurnoFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta is less than or equal to
        defaultTurnoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta is less than
        defaultTurnoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaAlta is greater than
        defaultTurnoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja equals to
        defaultTurnoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja in
        defaultTurnoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja is not null
        defaultTurnoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja is greater than or equal to
        defaultTurnoFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja is less than or equal to
        defaultTurnoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja is less than
        defaultTurnoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        // Get all the turnoList where fechaBaja is greater than
        defaultTurnoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTurnosByPacienteIsEqualToSomething() throws Exception {
        Paciente paciente;
        if (TestUtil.findAll(em, Paciente.class).isEmpty()) {
            turnoRepository.saveAndFlush(turno);
            paciente = PacienteResourceIT.createEntity(em);
        } else {
            paciente = TestUtil.findAll(em, Paciente.class).get(0);
        }
        em.persist(paciente);
        em.flush();
        turno.setPaciente(paciente);
        turnoRepository.saveAndFlush(turno);
        Long pacienteId = paciente.getId();
        // Get all the turnoList where paciente equals to pacienteId
        defaultTurnoShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the turnoList where paciente equals to (pacienteId + 1)
        defaultTurnoShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    @Test
    @Transactional
    void getAllTurnosByMedicoIsEqualToSomething() throws Exception {
        Medico medico;
        if (TestUtil.findAll(em, Medico.class).isEmpty()) {
            turnoRepository.saveAndFlush(turno);
            medico = MedicoResourceIT.createEntity(em);
        } else {
            medico = TestUtil.findAll(em, Medico.class).get(0);
        }
        em.persist(medico);
        em.flush();
        turno.setMedico(medico);
        turnoRepository.saveAndFlush(turno);
        Long medicoId = medico.getId();
        // Get all the turnoList where medico equals to medicoId
        defaultTurnoShouldBeFound("medicoId.equals=" + medicoId);

        // Get all the turnoList where medico equals to (medicoId + 1)
        defaultTurnoShouldNotBeFound("medicoId.equals=" + (medicoId + 1));
    }

    @Test
    @Transactional
    void getAllTurnosByEspecialidadIsEqualToSomething() throws Exception {
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            turnoRepository.saveAndFlush(turno);
            especialidad = EspecialidadResourceIT.createEntity();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        em.persist(especialidad);
        em.flush();
        turno.setEspecialidad(especialidad);
        turnoRepository.saveAndFlush(turno);
        Long especialidadId = especialidad.getId();
        // Get all the turnoList where especialidad equals to especialidadId
        defaultTurnoShouldBeFound("especialidadId.equals=" + especialidadId);

        // Get all the turnoList where especialidad equals to (especialidadId + 1)
        defaultTurnoShouldNotBeFound("especialidadId.equals=" + (especialidadId + 1));
    }

    @Test
    @Transactional
    void getAllTurnosByEstadoTurnoIsEqualToSomething() throws Exception {
        EstadoTurno estadoTurno;
        if (TestUtil.findAll(em, EstadoTurno.class).isEmpty()) {
            turnoRepository.saveAndFlush(turno);
            estadoTurno = EstadoTurnoResourceIT.createEntity();
        } else {
            estadoTurno = TestUtil.findAll(em, EstadoTurno.class).get(0);
        }
        em.persist(estadoTurno);
        em.flush();
        turno.setEstadoTurno(estadoTurno);
        turnoRepository.saveAndFlush(turno);
        Long estadoTurnoId = estadoTurno.getId();
        // Get all the turnoList where estadoTurno equals to estadoTurnoId
        defaultTurnoShouldBeFound("estadoTurnoId.equals=" + estadoTurnoId);

        // Get all the turnoList where estadoTurno equals to (estadoTurnoId + 1)
        defaultTurnoShouldNotBeFound("estadoTurnoId.equals=" + (estadoTurnoId + 1));
    }

    @Test
    @Transactional
    void getAllTurnosByCanalSolicitudIsEqualToSomething() throws Exception {
        CanalSolicitud canalSolicitud;
        if (TestUtil.findAll(em, CanalSolicitud.class).isEmpty()) {
            turnoRepository.saveAndFlush(turno);
            canalSolicitud = CanalSolicitudResourceIT.createEntity();
        } else {
            canalSolicitud = TestUtil.findAll(em, CanalSolicitud.class).get(0);
        }
        em.persist(canalSolicitud);
        em.flush();
        turno.setCanalSolicitud(canalSolicitud);
        turnoRepository.saveAndFlush(turno);
        Long canalSolicitudId = canalSolicitud.getId();
        // Get all the turnoList where canalSolicitud equals to canalSolicitudId
        defaultTurnoShouldBeFound("canalSolicitudId.equals=" + canalSolicitudId);

        // Get all the turnoList where canalSolicitud equals to (canalSolicitudId + 1)
        defaultTurnoShouldNotBeFound("canalSolicitudId.equals=" + (canalSolicitudId + 1));
    }

    private void defaultTurnoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTurnoShouldBeFound(shouldBeFound);
        defaultTurnoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTurnoShouldBeFound(String filter) throws Exception {
        restTurnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turno.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA.toString())))
            .andExpect(jsonPath("$.[*].duracionMinutos").value(hasItem(DEFAULT_DURACION_MINUTOS)))
            .andExpect(jsonPath("$.[*].motivoConsulta").value(hasItem(DEFAULT_MOTIVO_CONSULTA)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restTurnoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTurnoShouldNotBeFound(String filter) throws Exception {
        restTurnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTurnoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTurno() throws Exception {
        // Get the turno
        restTurnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTurno() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turno
        Turno updatedTurno = turnoRepository.findById(turno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTurno are not directly saved in db
        em.detach(updatedTurno);
        updatedTurno
            .codigo(UPDATED_CODIGO)
            .fechaHora(UPDATED_FECHA_HORA)
            .duracionMinutos(UPDATED_DURACION_MINUTOS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        TurnoDTO turnoDTO = turnoMapper.toDto(updatedTurno);

        restTurnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTurnoToMatchAllProperties(updatedTurno);
    }

    @Test
    @Transactional
    void putNonExistingTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turno.setId(longCount.incrementAndGet());

        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turno.setId(longCount.incrementAndGet());

        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turno.setId(longCount.incrementAndGet());

        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurnoWithPatch() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turno using partial update
        Turno partialUpdatedTurno = new Turno();
        partialUpdatedTurno.setId(turno.getId());

        partialUpdatedTurno
            .fechaHora(UPDATED_FECHA_HORA)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .activo(UPDATED_ACTIVO)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurno))
            )
            .andExpect(status().isOk());

        // Validate the Turno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurnoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTurno, turno), getPersistedTurno(turno));
    }

    @Test
    @Transactional
    void fullUpdateTurnoWithPatch() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turno using partial update
        Turno partialUpdatedTurno = new Turno();
        partialUpdatedTurno.setId(turno.getId());

        partialUpdatedTurno
            .codigo(UPDATED_CODIGO)
            .fechaHora(UPDATED_FECHA_HORA)
            .duracionMinutos(UPDATED_DURACION_MINUTOS)
            .motivoConsulta(UPDATED_MOTIVO_CONSULTA)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurno))
            )
            .andExpect(status().isOk());

        // Validate the Turno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurnoUpdatableFieldsEquals(partialUpdatedTurno, getPersistedTurno(partialUpdatedTurno));
    }

    @Test
    @Transactional
    void patchNonExistingTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turno.setId(longCount.incrementAndGet());

        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turnoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turno.setId(longCount.incrementAndGet());

        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turno.setId(longCount.incrementAndGet());

        // Create the Turno
        TurnoDTO turnoDTO = turnoMapper.toDto(turno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(turnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurno() throws Exception {
        // Initialize the database
        insertedTurno = turnoRepository.saveAndFlush(turno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the turno
        restTurnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, turno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return turnoRepository.count();
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

    protected Turno getPersistedTurno(Turno turno) {
        return turnoRepository.findById(turno.getId()).orElseThrow();
    }

    protected void assertPersistedTurnoToMatchAllProperties(Turno expectedTurno) {
        assertTurnoAllPropertiesEquals(expectedTurno, getPersistedTurno(expectedTurno));
    }

    protected void assertPersistedTurnoToMatchUpdatableProperties(Turno expectedTurno) {
        assertTurnoAllUpdatablePropertiesEquals(expectedTurno, getPersistedTurno(expectedTurno));
    }
}
