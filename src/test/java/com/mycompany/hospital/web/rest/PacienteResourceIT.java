package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.PacienteAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.FactorRh;
import com.mycompany.hospital.domain.GrupoSanguineo;
import com.mycompany.hospital.domain.ObraSocial;
import com.mycompany.hospital.domain.Paciente;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.repository.PacienteRepository;
import com.mycompany.hospital.service.dto.PacienteDTO;
import com.mycompany.hospital.service.mapper.PacienteMapper;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NUMERO_HISTORIA_CLINICA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_HISTORIA_CLINICA = "BBBBBBBBBB";

    private static final String DEFAULT_ALERGIAS_GENERALES = "AAAAAAAAAA";
    private static final String UPDATED_ALERGIAS_GENERALES = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    private Paciente insertedPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .numeroHistoriaClinica(DEFAULT_NUMERO_HISTORIA_CLINICA)
            .alergiasGenerales(DEFAULT_ALERGIAS_GENERALES)
            .observaciones(DEFAULT_OBSERVACIONES)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA)
            .activo(DEFAULT_ACTIVO);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        paciente.setPersona(persona);
        return paciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente updatedPaciente = new Paciente()
            .numeroHistoriaClinica(UPDATED_NUMERO_HISTORIA_CLINICA)
            .alergiasGenerales(UPDATED_ALERGIAS_GENERALES)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createUpdatedEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        updatedPaciente.setPersona(persona);
        return updatedPaciente;
    }

    @BeforeEach
    void initTest() {
        paciente = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPaciente != null) {
            pacienteRepository.delete(insertedPaciente);
            insertedPaciente = null;
        }
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        var returnedPacienteDTO = om.readValue(
            restPacienteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PacienteDTO.class
        );

        // Validate the Paciente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaciente = pacienteMapper.toEntity(returnedPacienteDTO);
        assertPacienteUpdatableFieldsEquals(returnedPaciente, getPersistedPaciente(returnedPaciente));

        insertedPaciente = returnedPaciente;
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroHistoriaClinicaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setNumeroHistoriaClinica(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setFechaAlta(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setActivo(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroHistoriaClinica").value(hasItem(DEFAULT_NUMERO_HISTORIA_CLINICA)))
            .andExpect(jsonPath("$.[*].alergiasGenerales").value(hasItem(DEFAULT_ALERGIAS_GENERALES)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.numeroHistoriaClinica").value(DEFAULT_NUMERO_HISTORIA_CLINICA))
            .andExpect(jsonPath("$.alergiasGenerales").value(DEFAULT_ALERGIAS_GENERALES))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getPacientesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        Long id = paciente.getId();

        defaultPacienteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPacienteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPacienteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPacientesByNumeroHistoriaClinicaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroHistoriaClinica equals to
        defaultPacienteFiltering(
            "numeroHistoriaClinica.equals=" + DEFAULT_NUMERO_HISTORIA_CLINICA,
            "numeroHistoriaClinica.equals=" + UPDATED_NUMERO_HISTORIA_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNumeroHistoriaClinicaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroHistoriaClinica in
        defaultPacienteFiltering(
            "numeroHistoriaClinica.in=" + DEFAULT_NUMERO_HISTORIA_CLINICA + "," + UPDATED_NUMERO_HISTORIA_CLINICA,
            "numeroHistoriaClinica.in=" + UPDATED_NUMERO_HISTORIA_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNumeroHistoriaClinicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroHistoriaClinica is not null
        defaultPacienteFiltering("numeroHistoriaClinica.specified=true", "numeroHistoriaClinica.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByNumeroHistoriaClinicaContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroHistoriaClinica contains
        defaultPacienteFiltering(
            "numeroHistoriaClinica.contains=" + DEFAULT_NUMERO_HISTORIA_CLINICA,
            "numeroHistoriaClinica.contains=" + UPDATED_NUMERO_HISTORIA_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByNumeroHistoriaClinicaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where numeroHistoriaClinica does not contain
        defaultPacienteFiltering(
            "numeroHistoriaClinica.doesNotContain=" + UPDATED_NUMERO_HISTORIA_CLINICA,
            "numeroHistoriaClinica.doesNotContain=" + DEFAULT_NUMERO_HISTORIA_CLINICA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta equals to
        defaultPacienteFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta in
        defaultPacienteFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta is not null
        defaultPacienteFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta is greater than or equal to
        defaultPacienteFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta is less than or equal to
        defaultPacienteFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta is less than
        defaultPacienteFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaAlta is greater than
        defaultPacienteFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja equals to
        defaultPacienteFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja in
        defaultPacienteFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja is not null
        defaultPacienteFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja is greater than or equal to
        defaultPacienteFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja is less than or equal to
        defaultPacienteFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja is less than
        defaultPacienteFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPacientesByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where fechaBaja is greater than
        defaultPacienteFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPacientesByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where activo equals to
        defaultPacienteFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllPacientesByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where activo in
        defaultPacienteFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllPacientesByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where activo is not null
        defaultPacienteFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPersonaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Persona persona = paciente.getPersona();
        pacienteRepository.saveAndFlush(paciente);
        Long personaId = persona.getId();
        // Get all the pacienteList where persona equals to personaId
        defaultPacienteShouldBeFound("personaId.equals=" + personaId);

        // Get all the pacienteList where persona equals to (personaId + 1)
        defaultPacienteShouldNotBeFound("personaId.equals=" + (personaId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByObraSocialIsEqualToSomething() throws Exception {
        ObraSocial obraSocial;
        if (TestUtil.findAll(em, ObraSocial.class).isEmpty()) {
            pacienteRepository.saveAndFlush(paciente);
            obraSocial = ObraSocialResourceIT.createEntity();
        } else {
            obraSocial = TestUtil.findAll(em, ObraSocial.class).get(0);
        }
        em.persist(obraSocial);
        em.flush();
        paciente.setObraSocial(obraSocial);
        pacienteRepository.saveAndFlush(paciente);
        Long obraSocialId = obraSocial.getId();
        // Get all the pacienteList where obraSocial equals to obraSocialId
        defaultPacienteShouldBeFound("obraSocialId.equals=" + obraSocialId);

        // Get all the pacienteList where obraSocial equals to (obraSocialId + 1)
        defaultPacienteShouldNotBeFound("obraSocialId.equals=" + (obraSocialId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByGrupoSanguineoIsEqualToSomething() throws Exception {
        GrupoSanguineo grupoSanguineo;
        if (TestUtil.findAll(em, GrupoSanguineo.class).isEmpty()) {
            pacienteRepository.saveAndFlush(paciente);
            grupoSanguineo = GrupoSanguineoResourceIT.createEntity();
        } else {
            grupoSanguineo = TestUtil.findAll(em, GrupoSanguineo.class).get(0);
        }
        em.persist(grupoSanguineo);
        em.flush();
        paciente.setGrupoSanguineo(grupoSanguineo);
        pacienteRepository.saveAndFlush(paciente);
        Long grupoSanguineoId = grupoSanguineo.getId();
        // Get all the pacienteList where grupoSanguineo equals to grupoSanguineoId
        defaultPacienteShouldBeFound("grupoSanguineoId.equals=" + grupoSanguineoId);

        // Get all the pacienteList where grupoSanguineo equals to (grupoSanguineoId + 1)
        defaultPacienteShouldNotBeFound("grupoSanguineoId.equals=" + (grupoSanguineoId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByFactorRhIsEqualToSomething() throws Exception {
        FactorRh factorRh;
        if (TestUtil.findAll(em, FactorRh.class).isEmpty()) {
            pacienteRepository.saveAndFlush(paciente);
            factorRh = FactorRhResourceIT.createEntity();
        } else {
            factorRh = TestUtil.findAll(em, FactorRh.class).get(0);
        }
        em.persist(factorRh);
        em.flush();
        paciente.setFactorRh(factorRh);
        pacienteRepository.saveAndFlush(paciente);
        Long factorRhId = factorRh.getId();
        // Get all the pacienteList where factorRh equals to factorRhId
        defaultPacienteShouldBeFound("factorRhId.equals=" + factorRhId);

        // Get all the pacienteList where factorRh equals to (factorRhId + 1)
        defaultPacienteShouldNotBeFound("factorRhId.equals=" + (factorRhId + 1));
    }

    private void defaultPacienteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPacienteShouldBeFound(shouldBeFound);
        defaultPacienteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPacienteShouldBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroHistoriaClinica").value(hasItem(DEFAULT_NUMERO_HISTORIA_CLINICA)))
            .andExpect(jsonPath("$.[*].alergiasGenerales").value(hasItem(DEFAULT_ALERGIAS_GENERALES)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPacienteShouldNotBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .numeroHistoriaClinica(UPDATED_NUMERO_HISTORIA_CLINICA)
            .alergiasGenerales(UPDATED_ALERGIAS_GENERALES)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteToMatchAllProperties(updatedPaciente);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .numeroHistoriaClinica(UPDATED_NUMERO_HISTORIA_CLINICA)
            .alergiasGenerales(UPDATED_ALERGIAS_GENERALES)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .activo(UPDATED_ACTIVO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaciente, paciente), getPersistedPaciente(paciente));
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .numeroHistoriaClinica(UPDATED_NUMERO_HISTORIA_CLINICA)
            .alergiasGenerales(UPDATED_ALERGIAS_GENERALES)
            .observaciones(UPDATED_OBSERVACIONES)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(partialUpdatedPaciente, getPersistedPaciente(partialUpdatedPaciente));
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pacienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pacienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteRepository.count();
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

    protected Paciente getPersistedPaciente(Paciente paciente) {
        return pacienteRepository.findById(paciente.getId()).orElseThrow();
    }

    protected void assertPersistedPacienteToMatchAllProperties(Paciente expectedPaciente) {
        assertPacienteAllPropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }

    protected void assertPersistedPacienteToMatchUpdatableProperties(Paciente expectedPaciente) {
        assertPacienteAllUpdatablePropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }
}
