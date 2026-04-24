package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.MedicoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.domain.Especialidad;
import com.mycompany.hospital.domain.Medico;
import com.mycompany.hospital.repository.MedicoRepository;
import com.mycompany.hospital.service.dto.MedicoDTO;
import com.mycompany.hospital.service.mapper.MedicoMapper;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicoResourceIT {

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_MATRICULACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_MATRICULACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_MATRICULACION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FIRMA_DIGITAL = "AAAAAAAAAA";
    private static final String UPDATED_FIRMA_DIGITAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIENDE_CONSULTORIO = false;
    private static final Boolean UPDATED_ATIENDE_CONSULTORIO = true;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicoMockMvc;

    private Medico medico;

    private Medico insertedMedico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity(EntityManager em) {
        Medico medico = new Medico()
            .matricula(DEFAULT_MATRICULA)
            .fechaMatriculacion(DEFAULT_FECHA_MATRICULACION)
            .firmaDigital(DEFAULT_FIRMA_DIGITAL)
            .atiendeConsultorio(DEFAULT_ATIENDE_CONSULTORIO)
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
        medico.setEmpleado(empleado);
        // Add required entity
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            especialidad = EspecialidadResourceIT.createEntity();
            em.persist(especialidad);
            em.flush();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        medico.setEspecialidad(especialidad);
        return medico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity(EntityManager em) {
        Medico updatedMedico = new Medico()
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .firmaDigital(UPDATED_FIRMA_DIGITAL)
            .atiendeConsultorio(UPDATED_ATIENDE_CONSULTORIO)
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
        updatedMedico.setEmpleado(empleado);
        // Add required entity
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            especialidad = EspecialidadResourceIT.createUpdatedEntity();
            em.persist(especialidad);
            em.flush();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        updatedMedico.setEspecialidad(especialidad);
        return updatedMedico;
    }

    @BeforeEach
    void initTest() {
        medico = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedico != null) {
            medicoRepository.delete(insertedMedico);
            insertedMedico = null;
        }
    }

    @Test
    @Transactional
    void createMedico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);
        var returnedMedicoDTO = om.readValue(
            restMedicoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicoDTO.class
        );

        // Validate the Medico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedico = medicoMapper.toEntity(returnedMedicoDTO);
        assertMedicoUpdatableFieldsEquals(returnedMedico, getPersistedMedico(returnedMedico));

        insertedMedico = returnedMedico;
    }

    @Test
    @Transactional
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setMatricula(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtiendeConsultorioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setAtiendeConsultorio(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setActivo(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setFechaAlta(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].fechaMatriculacion").value(hasItem(DEFAULT_FECHA_MATRICULACION.toString())))
            .andExpect(jsonPath("$.[*].firmaDigital").value(hasItem(DEFAULT_FIRMA_DIGITAL)))
            .andExpect(jsonPath("$.[*].atiendeConsultorio").value(hasItem(DEFAULT_ATIENDE_CONSULTORIO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get the medico
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL_ID, medico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medico.getId().intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.fechaMatriculacion").value(DEFAULT_FECHA_MATRICULACION.toString()))
            .andExpect(jsonPath("$.firmaDigital").value(DEFAULT_FIRMA_DIGITAL))
            .andExpect(jsonPath("$.atiendeConsultorio").value(DEFAULT_ATIENDE_CONSULTORIO))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getMedicosByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        Long id = medico.getId();

        defaultMedicoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicosByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where matricula equals to
        defaultMedicoFiltering("matricula.equals=" + DEFAULT_MATRICULA, "matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllMedicosByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where matricula in
        defaultMedicoFiltering("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA, "matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllMedicosByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where matricula is not null
        defaultMedicoFiltering("matricula.specified=true", "matricula.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where matricula contains
        defaultMedicoFiltering("matricula.contains=" + DEFAULT_MATRICULA, "matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllMedicosByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where matricula does not contain
        defaultMedicoFiltering("matricula.doesNotContain=" + UPDATED_MATRICULA, "matricula.doesNotContain=" + DEFAULT_MATRICULA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion equals to
        defaultMedicoFiltering(
            "fechaMatriculacion.equals=" + DEFAULT_FECHA_MATRICULACION,
            "fechaMatriculacion.equals=" + UPDATED_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion in
        defaultMedicoFiltering(
            "fechaMatriculacion.in=" + DEFAULT_FECHA_MATRICULACION + "," + UPDATED_FECHA_MATRICULACION,
            "fechaMatriculacion.in=" + UPDATED_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion is not null
        defaultMedicoFiltering("fechaMatriculacion.specified=true", "fechaMatriculacion.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion is greater than or equal to
        defaultMedicoFiltering(
            "fechaMatriculacion.greaterThanOrEqual=" + DEFAULT_FECHA_MATRICULACION,
            "fechaMatriculacion.greaterThanOrEqual=" + UPDATED_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion is less than or equal to
        defaultMedicoFiltering(
            "fechaMatriculacion.lessThanOrEqual=" + DEFAULT_FECHA_MATRICULACION,
            "fechaMatriculacion.lessThanOrEqual=" + SMALLER_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion is less than
        defaultMedicoFiltering(
            "fechaMatriculacion.lessThan=" + UPDATED_FECHA_MATRICULACION,
            "fechaMatriculacion.lessThan=" + DEFAULT_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFechaMatriculacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaMatriculacion is greater than
        defaultMedicoFiltering(
            "fechaMatriculacion.greaterThan=" + SMALLER_FECHA_MATRICULACION,
            "fechaMatriculacion.greaterThan=" + DEFAULT_FECHA_MATRICULACION
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFirmaDigitalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where firmaDigital equals to
        defaultMedicoFiltering("firmaDigital.equals=" + DEFAULT_FIRMA_DIGITAL, "firmaDigital.equals=" + UPDATED_FIRMA_DIGITAL);
    }

    @Test
    @Transactional
    void getAllMedicosByFirmaDigitalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where firmaDigital in
        defaultMedicoFiltering(
            "firmaDigital.in=" + DEFAULT_FIRMA_DIGITAL + "," + UPDATED_FIRMA_DIGITAL,
            "firmaDigital.in=" + UPDATED_FIRMA_DIGITAL
        );
    }

    @Test
    @Transactional
    void getAllMedicosByFirmaDigitalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where firmaDigital is not null
        defaultMedicoFiltering("firmaDigital.specified=true", "firmaDigital.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByFirmaDigitalContainsSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where firmaDigital contains
        defaultMedicoFiltering("firmaDigital.contains=" + DEFAULT_FIRMA_DIGITAL, "firmaDigital.contains=" + UPDATED_FIRMA_DIGITAL);
    }

    @Test
    @Transactional
    void getAllMedicosByFirmaDigitalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where firmaDigital does not contain
        defaultMedicoFiltering(
            "firmaDigital.doesNotContain=" + UPDATED_FIRMA_DIGITAL,
            "firmaDigital.doesNotContain=" + DEFAULT_FIRMA_DIGITAL
        );
    }

    @Test
    @Transactional
    void getAllMedicosByAtiendeConsultorioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atiendeConsultorio equals to
        defaultMedicoFiltering(
            "atiendeConsultorio.equals=" + DEFAULT_ATIENDE_CONSULTORIO,
            "atiendeConsultorio.equals=" + UPDATED_ATIENDE_CONSULTORIO
        );
    }

    @Test
    @Transactional
    void getAllMedicosByAtiendeConsultorioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atiendeConsultorio in
        defaultMedicoFiltering(
            "atiendeConsultorio.in=" + DEFAULT_ATIENDE_CONSULTORIO + "," + UPDATED_ATIENDE_CONSULTORIO,
            "atiendeConsultorio.in=" + UPDATED_ATIENDE_CONSULTORIO
        );
    }

    @Test
    @Transactional
    void getAllMedicosByAtiendeConsultorioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where atiendeConsultorio is not null
        defaultMedicoFiltering("atiendeConsultorio.specified=true", "atiendeConsultorio.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where activo equals to
        defaultMedicoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllMedicosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where activo in
        defaultMedicoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllMedicosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where activo is not null
        defaultMedicoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta equals to
        defaultMedicoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta in
        defaultMedicoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta is not null
        defaultMedicoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta is greater than or equal to
        defaultMedicoFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta is less than or equal to
        defaultMedicoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta is less than
        defaultMedicoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaAlta is greater than
        defaultMedicoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja equals to
        defaultMedicoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja in
        defaultMedicoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja is not null
        defaultMedicoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja is greater than or equal to
        defaultMedicoFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja is less than or equal to
        defaultMedicoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja is less than
        defaultMedicoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllMedicosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        // Get all the medicoList where fechaBaja is greater than
        defaultMedicoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllMedicosByEmpleadoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Empleado empleado = medico.getEmpleado();
        medicoRepository.saveAndFlush(medico);
        Long empleadoId = empleado.getId();
        // Get all the medicoList where empleado equals to empleadoId
        defaultMedicoShouldBeFound("empleadoId.equals=" + empleadoId);

        // Get all the medicoList where empleado equals to (empleadoId + 1)
        defaultMedicoShouldNotBeFound("empleadoId.equals=" + (empleadoId + 1));
    }

    @Test
    @Transactional
    void getAllMedicosByEspecialidadIsEqualToSomething() throws Exception {
        Especialidad especialidad;
        if (TestUtil.findAll(em, Especialidad.class).isEmpty()) {
            medicoRepository.saveAndFlush(medico);
            especialidad = EspecialidadResourceIT.createEntity();
        } else {
            especialidad = TestUtil.findAll(em, Especialidad.class).get(0);
        }
        em.persist(especialidad);
        em.flush();
        medico.setEspecialidad(especialidad);
        medicoRepository.saveAndFlush(medico);
        Long especialidadId = especialidad.getId();
        // Get all the medicoList where especialidad equals to especialidadId
        defaultMedicoShouldBeFound("especialidadId.equals=" + especialidadId);

        // Get all the medicoList where especialidad equals to (especialidadId + 1)
        defaultMedicoShouldNotBeFound("especialidadId.equals=" + (especialidadId + 1));
    }

    private void defaultMedicoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicoShouldBeFound(shouldBeFound);
        defaultMedicoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicoShouldBeFound(String filter) throws Exception {
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].fechaMatriculacion").value(hasItem(DEFAULT_FECHA_MATRICULACION.toString())))
            .andExpect(jsonPath("$.[*].firmaDigital").value(hasItem(DEFAULT_FIRMA_DIGITAL)))
            .andExpect(jsonPath("$.[*].atiendeConsultorio").value(hasItem(DEFAULT_ATIENDE_CONSULTORIO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicoShouldNotBeFound(String filter) throws Exception {
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedico() throws Exception {
        // Get the medico
        restMedicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedico are not directly saved in db
        em.detach(updatedMedico);
        updatedMedico
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .firmaDigital(UPDATED_FIRMA_DIGITAL)
            .atiendeConsultorio(UPDATED_ATIENDE_CONSULTORIO)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        MedicoDTO medicoDTO = medicoMapper.toDto(updatedMedico);

        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicoToMatchAllProperties(updatedMedico);
    }

    @Test
    @Transactional
    void putNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .atiendeConsultorio(UPDATED_ATIENDE_CONSULTORIO)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedico, medico), getPersistedMedico(medico));
    }

    @Test
    @Transactional
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .matricula(UPDATED_MATRICULA)
            .fechaMatriculacion(UPDATED_FECHA_MATRICULACION)
            .firmaDigital(UPDATED_FIRMA_DIGITAL)
            .atiendeConsultorio(UPDATED_ATIENDE_CONSULTORIO)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(partialUpdatedMedico, getPersistedMedico(partialUpdatedMedico));
    }

    @Test
    @Transactional
    void patchNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.saveAndFlush(medico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medico
        restMedicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicoRepository.count();
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

    protected Medico getPersistedMedico(Medico medico) {
        return medicoRepository.findById(medico.getId()).orElseThrow();
    }

    protected void assertPersistedMedicoToMatchAllProperties(Medico expectedMedico) {
        assertMedicoAllPropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }

    protected void assertPersistedMedicoToMatchUpdatableProperties(Medico expectedMedico) {
        assertMedicoAllUpdatablePropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }
}
