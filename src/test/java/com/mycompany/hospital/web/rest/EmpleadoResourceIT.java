package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.EmpleadoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Cargo;
import com.mycompany.hospital.domain.Empleado;
import com.mycompany.hospital.domain.EstadoLaboral;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.domain.TipoEmpleado;
import com.mycompany.hospital.repository.EmpleadoRepository;
import com.mycompany.hospital.service.dto.EmpleadoDTO;
import com.mycompany.hospital.service.mapper.EmpleadoMapper;
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
 * Integration tests for the {@link EmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoResourceIT {

    private static final String DEFAULT_LEGAJO = "AAAAAAAAAA";
    private static final String UPDATED_LEGAJO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INGRESO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    private Empleado insertedEmpleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createEntity(EntityManager em) {
        Empleado empleado = new Empleado()
            .legajo(DEFAULT_LEGAJO)
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
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
        empleado.setPersona(persona);
        // Add required entity
        TipoEmpleado tipoEmpleado;
        if (TestUtil.findAll(em, TipoEmpleado.class).isEmpty()) {
            tipoEmpleado = TipoEmpleadoResourceIT.createEntity();
            em.persist(tipoEmpleado);
            em.flush();
        } else {
            tipoEmpleado = TestUtil.findAll(em, TipoEmpleado.class).get(0);
        }
        empleado.setTipoEmpleado(tipoEmpleado);
        // Add required entity
        EstadoLaboral estadoLaboral;
        if (TestUtil.findAll(em, EstadoLaboral.class).isEmpty()) {
            estadoLaboral = EstadoLaboralResourceIT.createEntity();
            em.persist(estadoLaboral);
            em.flush();
        } else {
            estadoLaboral = TestUtil.findAll(em, EstadoLaboral.class).get(0);
        }
        empleado.setEstadoLaboral(estadoLaboral);
        // Add required entity
        Cargo cargo;
        if (TestUtil.findAll(em, Cargo.class).isEmpty()) {
            cargo = CargoResourceIT.createEntity();
            em.persist(cargo);
            em.flush();
        } else {
            cargo = TestUtil.findAll(em, Cargo.class).get(0);
        }
        empleado.setCargo(cargo);
        return empleado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createUpdatedEntity(EntityManager em) {
        Empleado updatedEmpleado = new Empleado()
            .legajo(UPDATED_LEGAJO)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
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
        updatedEmpleado.setPersona(persona);
        // Add required entity
        TipoEmpleado tipoEmpleado;
        if (TestUtil.findAll(em, TipoEmpleado.class).isEmpty()) {
            tipoEmpleado = TipoEmpleadoResourceIT.createUpdatedEntity();
            em.persist(tipoEmpleado);
            em.flush();
        } else {
            tipoEmpleado = TestUtil.findAll(em, TipoEmpleado.class).get(0);
        }
        updatedEmpleado.setTipoEmpleado(tipoEmpleado);
        // Add required entity
        EstadoLaboral estadoLaboral;
        if (TestUtil.findAll(em, EstadoLaboral.class).isEmpty()) {
            estadoLaboral = EstadoLaboralResourceIT.createUpdatedEntity();
            em.persist(estadoLaboral);
            em.flush();
        } else {
            estadoLaboral = TestUtil.findAll(em, EstadoLaboral.class).get(0);
        }
        updatedEmpleado.setEstadoLaboral(estadoLaboral);
        // Add required entity
        Cargo cargo;
        if (TestUtil.findAll(em, Cargo.class).isEmpty()) {
            cargo = CargoResourceIT.createUpdatedEntity();
            em.persist(cargo);
            em.flush();
        } else {
            cargo = TestUtil.findAll(em, Cargo.class).get(0);
        }
        updatedEmpleado.setCargo(cargo);
        return updatedEmpleado;
    }

    @BeforeEach
    void initTest() {
        empleado = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEmpleado != null) {
            empleadoRepository.delete(insertedEmpleado);
            insertedEmpleado = null;
        }
    }

    @Test
    @Transactional
    void createEmpleado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);
        var returnedEmpleadoDTO = om.readValue(
            restEmpleadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpleadoDTO.class
        );

        // Validate the Empleado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmpleado = empleadoMapper.toEntity(returnedEmpleadoDTO);
        assertEmpleadoUpdatableFieldsEquals(returnedEmpleado, getPersistedEmpleado(returnedEmpleado));

        insertedEmpleado = returnedEmpleado;
    }

    @Test
    @Transactional
    void createEmpleadoWithExistingId() throws Exception {
        // Create the Empleado with an existing ID
        empleado.setId(1L);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLegajoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setLegajo(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIngresoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setFechaIngreso(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setActivo(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmpleados() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].legajo").value(hasItem(DEFAULT_LEGAJO)))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.legajo").value(DEFAULT_LEGAJO))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getEmpleadosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        Long id = empleado.getId();

        defaultEmpleadoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmpleadoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmpleadoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmpleadosByLegajoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where legajo equals to
        defaultEmpleadoFiltering("legajo.equals=" + DEFAULT_LEGAJO, "legajo.equals=" + UPDATED_LEGAJO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByLegajoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where legajo in
        defaultEmpleadoFiltering("legajo.in=" + DEFAULT_LEGAJO + "," + UPDATED_LEGAJO, "legajo.in=" + UPDATED_LEGAJO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByLegajoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where legajo is not null
        defaultEmpleadoFiltering("legajo.specified=true", "legajo.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByLegajoContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where legajo contains
        defaultEmpleadoFiltering("legajo.contains=" + DEFAULT_LEGAJO, "legajo.contains=" + UPDATED_LEGAJO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByLegajoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where legajo does not contain
        defaultEmpleadoFiltering("legajo.doesNotContain=" + UPDATED_LEGAJO, "legajo.doesNotContain=" + DEFAULT_LEGAJO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso equals to
        defaultEmpleadoFiltering("fechaIngreso.equals=" + DEFAULT_FECHA_INGRESO, "fechaIngreso.equals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso in
        defaultEmpleadoFiltering(
            "fechaIngreso.in=" + DEFAULT_FECHA_INGRESO + "," + UPDATED_FECHA_INGRESO,
            "fechaIngreso.in=" + UPDATED_FECHA_INGRESO
        );
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso is not null
        defaultEmpleadoFiltering("fechaIngreso.specified=true", "fechaIngreso.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso is greater than or equal to
        defaultEmpleadoFiltering(
            "fechaIngreso.greaterThanOrEqual=" + DEFAULT_FECHA_INGRESO,
            "fechaIngreso.greaterThanOrEqual=" + UPDATED_FECHA_INGRESO
        );
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso is less than or equal to
        defaultEmpleadoFiltering(
            "fechaIngreso.lessThanOrEqual=" + DEFAULT_FECHA_INGRESO,
            "fechaIngreso.lessThanOrEqual=" + SMALLER_FECHA_INGRESO
        );
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso is less than
        defaultEmpleadoFiltering("fechaIngreso.lessThan=" + UPDATED_FECHA_INGRESO, "fechaIngreso.lessThan=" + DEFAULT_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaIngresoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaIngreso is greater than
        defaultEmpleadoFiltering("fechaIngreso.greaterThan=" + SMALLER_FECHA_INGRESO, "fechaIngreso.greaterThan=" + DEFAULT_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja equals to
        defaultEmpleadoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja in
        defaultEmpleadoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja is not null
        defaultEmpleadoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja is greater than or equal to
        defaultEmpleadoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja is less than or equal to
        defaultEmpleadoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja is less than
        defaultEmpleadoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where fechaBaja is greater than
        defaultEmpleadoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEmpleadosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where activo equals to
        defaultEmpleadoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where activo in
        defaultEmpleadoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEmpleadosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList where activo is not null
        defaultEmpleadoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpleadosByPersonaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Persona persona = empleado.getPersona();
        empleadoRepository.saveAndFlush(empleado);
        Long personaId = persona.getId();
        // Get all the empleadoList where persona equals to personaId
        defaultEmpleadoShouldBeFound("personaId.equals=" + personaId);

        // Get all the empleadoList where persona equals to (personaId + 1)
        defaultEmpleadoShouldNotBeFound("personaId.equals=" + (personaId + 1));
    }

    @Test
    @Transactional
    void getAllEmpleadosByTipoEmpleadoIsEqualToSomething() throws Exception {
        TipoEmpleado tipoEmpleado;
        if (TestUtil.findAll(em, TipoEmpleado.class).isEmpty()) {
            empleadoRepository.saveAndFlush(empleado);
            tipoEmpleado = TipoEmpleadoResourceIT.createEntity();
        } else {
            tipoEmpleado = TestUtil.findAll(em, TipoEmpleado.class).get(0);
        }
        em.persist(tipoEmpleado);
        em.flush();
        empleado.setTipoEmpleado(tipoEmpleado);
        empleadoRepository.saveAndFlush(empleado);
        Long tipoEmpleadoId = tipoEmpleado.getId();
        // Get all the empleadoList where tipoEmpleado equals to tipoEmpleadoId
        defaultEmpleadoShouldBeFound("tipoEmpleadoId.equals=" + tipoEmpleadoId);

        // Get all the empleadoList where tipoEmpleado equals to (tipoEmpleadoId + 1)
        defaultEmpleadoShouldNotBeFound("tipoEmpleadoId.equals=" + (tipoEmpleadoId + 1));
    }

    @Test
    @Transactional
    void getAllEmpleadosByEstadoLaboralIsEqualToSomething() throws Exception {
        EstadoLaboral estadoLaboral;
        if (TestUtil.findAll(em, EstadoLaboral.class).isEmpty()) {
            empleadoRepository.saveAndFlush(empleado);
            estadoLaboral = EstadoLaboralResourceIT.createEntity();
        } else {
            estadoLaboral = TestUtil.findAll(em, EstadoLaboral.class).get(0);
        }
        em.persist(estadoLaboral);
        em.flush();
        empleado.setEstadoLaboral(estadoLaboral);
        empleadoRepository.saveAndFlush(empleado);
        Long estadoLaboralId = estadoLaboral.getId();
        // Get all the empleadoList where estadoLaboral equals to estadoLaboralId
        defaultEmpleadoShouldBeFound("estadoLaboralId.equals=" + estadoLaboralId);

        // Get all the empleadoList where estadoLaboral equals to (estadoLaboralId + 1)
        defaultEmpleadoShouldNotBeFound("estadoLaboralId.equals=" + (estadoLaboralId + 1));
    }

    @Test
    @Transactional
    void getAllEmpleadosByCargoIsEqualToSomething() throws Exception {
        Cargo cargo;
        if (TestUtil.findAll(em, Cargo.class).isEmpty()) {
            empleadoRepository.saveAndFlush(empleado);
            cargo = CargoResourceIT.createEntity();
        } else {
            cargo = TestUtil.findAll(em, Cargo.class).get(0);
        }
        em.persist(cargo);
        em.flush();
        empleado.setCargo(cargo);
        empleadoRepository.saveAndFlush(empleado);
        Long cargoId = cargo.getId();
        // Get all the empleadoList where cargo equals to cargoId
        defaultEmpleadoShouldBeFound("cargoId.equals=" + cargoId);

        // Get all the empleadoList where cargo equals to (cargoId + 1)
        defaultEmpleadoShouldNotBeFound("cargoId.equals=" + (cargoId + 1));
    }

    private void defaultEmpleadoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmpleadoShouldBeFound(shouldBeFound);
        defaultEmpleadoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmpleadoShouldBeFound(String filter) throws Exception {
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].legajo").value(hasItem(DEFAULT_LEGAJO)))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmpleadoShouldNotBeFound(String filter) throws Exception {
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado
        Empleado updatedEmpleado = empleadoRepository.findById(empleado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmpleado are not directly saved in db
        em.detach(updatedEmpleado);
        updatedEmpleado.legajo(UPDATED_LEGAJO).fechaIngreso(UPDATED_FECHA_INGRESO).fechaBaja(UPDATED_FECHA_BAJA).activo(UPDATED_ACTIVO);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(updatedEmpleado);

        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadoToMatchAllProperties(updatedEmpleado);
    }

    @Test
    @Transactional
    void putNonExistingEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado.fechaIngreso(UPDATED_FECHA_INGRESO).fechaBaja(UPDATED_FECHA_BAJA);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmpleado, empleado), getPersistedEmpleado(empleado));
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado
            .legajo(UPDATED_LEGAJO)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoUpdatableFieldsEquals(partialUpdatedEmpleado, getPersistedEmpleado(partialUpdatedEmpleado));
    }

    @Test
    @Transactional
    void patchNonExistingEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleado
        restEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadoRepository.count();
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

    protected Empleado getPersistedEmpleado(Empleado empleado) {
        return empleadoRepository.findById(empleado.getId()).orElseThrow();
    }

    protected void assertPersistedEmpleadoToMatchAllProperties(Empleado expectedEmpleado) {
        assertEmpleadoAllPropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
    }

    protected void assertPersistedEmpleadoToMatchUpdatableProperties(Empleado expectedEmpleado) {
        assertEmpleadoAllUpdatablePropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
    }
}
