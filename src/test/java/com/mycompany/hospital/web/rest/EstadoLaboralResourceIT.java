package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.EstadoLaboralAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.EstadoLaboral;
import com.mycompany.hospital.repository.EstadoLaboralRepository;
import com.mycompany.hospital.service.dto.EstadoLaboralDTO;
import com.mycompany.hospital.service.mapper.EstadoLaboralMapper;
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
 * Integration tests for the {@link EstadoLaboralResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstadoLaboralResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/estado-laborals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstadoLaboralRepository estadoLaboralRepository;

    @Autowired
    private EstadoLaboralMapper estadoLaboralMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoLaboralMockMvc;

    private EstadoLaboral estadoLaboral;

    private EstadoLaboral insertedEstadoLaboral;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoLaboral createEntity() {
        return new EstadoLaboral()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoLaboral createUpdatedEntity() {
        return new EstadoLaboral()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        estadoLaboral = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEstadoLaboral != null) {
            estadoLaboralRepository.delete(insertedEstadoLaboral);
            insertedEstadoLaboral = null;
        }
    }

    @Test
    @Transactional
    void createEstadoLaboral() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);
        var returnedEstadoLaboralDTO = om.readValue(
            restEstadoLaboralMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EstadoLaboralDTO.class
        );

        // Validate the EstadoLaboral in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstadoLaboral = estadoLaboralMapper.toEntity(returnedEstadoLaboralDTO);
        assertEstadoLaboralUpdatableFieldsEquals(returnedEstadoLaboral, getPersistedEstadoLaboral(returnedEstadoLaboral));

        insertedEstadoLaboral = returnedEstadoLaboral;
    }

    @Test
    @Transactional
    void createEstadoLaboralWithExistingId() throws Exception {
        // Create the EstadoLaboral with an existing ID
        estadoLaboral.setId(1L);
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoLaboral.setCodigo(null);

        // Create the EstadoLaboral, which fails.
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        restEstadoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoLaboral.setNombre(null);

        // Create the EstadoLaboral, which fails.
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        restEstadoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoLaboral.setFechaAlta(null);

        // Create the EstadoLaboral, which fails.
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        restEstadoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoLaboral.setActivo(null);

        // Create the EstadoLaboral, which fails.
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        restEstadoLaboralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstadoLaborals() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList
        restEstadoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoLaboral.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getEstadoLaboral() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get the estadoLaboral
        restEstadoLaboralMockMvc
            .perform(get(ENTITY_API_URL_ID, estadoLaboral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estadoLaboral.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getEstadoLaboralsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        Long id = estadoLaboral.getId();

        defaultEstadoLaboralFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEstadoLaboralFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEstadoLaboralFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where codigo equals to
        defaultEstadoLaboralFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where codigo in
        defaultEstadoLaboralFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where codigo is not null
        defaultEstadoLaboralFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where codigo contains
        defaultEstadoLaboralFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where codigo does not contain
        defaultEstadoLaboralFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where nombre equals to
        defaultEstadoLaboralFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where nombre in
        defaultEstadoLaboralFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where nombre is not null
        defaultEstadoLaboralFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where nombre contains
        defaultEstadoLaboralFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where nombre does not contain
        defaultEstadoLaboralFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where descripcion equals to
        defaultEstadoLaboralFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where descripcion in
        defaultEstadoLaboralFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where descripcion is not null
        defaultEstadoLaboralFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where descripcion contains
        defaultEstadoLaboralFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where descripcion does not contain
        defaultEstadoLaboralFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta equals to
        defaultEstadoLaboralFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta in
        defaultEstadoLaboralFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta is not null
        defaultEstadoLaboralFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta is greater than or equal to
        defaultEstadoLaboralFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta is less than or equal to
        defaultEstadoLaboralFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta is less than
        defaultEstadoLaboralFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaAlta is greater than
        defaultEstadoLaboralFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja equals to
        defaultEstadoLaboralFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja in
        defaultEstadoLaboralFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja is not null
        defaultEstadoLaboralFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja is greater than or equal to
        defaultEstadoLaboralFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja is less than or equal to
        defaultEstadoLaboralFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja is less than
        defaultEstadoLaboralFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where fechaBaja is greater than
        defaultEstadoLaboralFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where activo equals to
        defaultEstadoLaboralFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where activo in
        defaultEstadoLaboralFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoLaboralsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        // Get all the estadoLaboralList where activo is not null
        defaultEstadoLaboralFiltering("activo.specified=true", "activo.specified=false");
    }

    private void defaultEstadoLaboralFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEstadoLaboralShouldBeFound(shouldBeFound);
        defaultEstadoLaboralShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstadoLaboralShouldBeFound(String filter) throws Exception {
        restEstadoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoLaboral.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restEstadoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstadoLaboralShouldNotBeFound(String filter) throws Exception {
        restEstadoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstadoLaboralMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstadoLaboral() throws Exception {
        // Get the estadoLaboral
        restEstadoLaboralMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstadoLaboral() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoLaboral
        EstadoLaboral updatedEstadoLaboral = estadoLaboralRepository.findById(estadoLaboral.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstadoLaboral are not directly saved in db
        em.detach(updatedEstadoLaboral);
        updatedEstadoLaboral
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(updatedEstadoLaboral);

        restEstadoLaboralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoLaboralDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoLaboralDTO))
            )
            .andExpect(status().isOk());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstadoLaboralToMatchAllProperties(updatedEstadoLaboral);
    }

    @Test
    @Transactional
    void putNonExistingEstadoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoLaboral.setId(longCount.incrementAndGet());

        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoLaboralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoLaboralDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstadoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoLaboral.setId(longCount.incrementAndGet());

        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoLaboralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstadoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoLaboral.setId(longCount.incrementAndGet());

        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoLaboralMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstadoLaboralWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoLaboral using partial update
        EstadoLaboral partialUpdatedEstadoLaboral = new EstadoLaboral();
        partialUpdatedEstadoLaboral.setId(estadoLaboral.getId());

        partialUpdatedEstadoLaboral.codigo(UPDATED_CODIGO).descripcion(UPDATED_DESCRIPCION);

        restEstadoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoLaboral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoLaboral))
            )
            .andExpect(status().isOk());

        // Validate the EstadoLaboral in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoLaboralUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEstadoLaboral, estadoLaboral),
            getPersistedEstadoLaboral(estadoLaboral)
        );
    }

    @Test
    @Transactional
    void fullUpdateEstadoLaboralWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoLaboral using partial update
        EstadoLaboral partialUpdatedEstadoLaboral = new EstadoLaboral();
        partialUpdatedEstadoLaboral.setId(estadoLaboral.getId());

        partialUpdatedEstadoLaboral
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restEstadoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoLaboral.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoLaboral))
            )
            .andExpect(status().isOk());

        // Validate the EstadoLaboral in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoLaboralUpdatableFieldsEquals(partialUpdatedEstadoLaboral, getPersistedEstadoLaboral(partialUpdatedEstadoLaboral));
    }

    @Test
    @Transactional
    void patchNonExistingEstadoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoLaboral.setId(longCount.incrementAndGet());

        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estadoLaboralDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstadoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoLaboral.setId(longCount.incrementAndGet());

        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoLaboralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoLaboralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstadoLaboral() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoLaboral.setId(longCount.incrementAndGet());

        // Create the EstadoLaboral
        EstadoLaboralDTO estadoLaboralDTO = estadoLaboralMapper.toDto(estadoLaboral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoLaboralMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estadoLaboralDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoLaboral in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstadoLaboral() throws Exception {
        // Initialize the database
        insertedEstadoLaboral = estadoLaboralRepository.saveAndFlush(estadoLaboral);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estadoLaboral
        restEstadoLaboralMockMvc
            .perform(delete(ENTITY_API_URL_ID, estadoLaboral.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estadoLaboralRepository.count();
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

    protected EstadoLaboral getPersistedEstadoLaboral(EstadoLaboral estadoLaboral) {
        return estadoLaboralRepository.findById(estadoLaboral.getId()).orElseThrow();
    }

    protected void assertPersistedEstadoLaboralToMatchAllProperties(EstadoLaboral expectedEstadoLaboral) {
        assertEstadoLaboralAllPropertiesEquals(expectedEstadoLaboral, getPersistedEstadoLaboral(expectedEstadoLaboral));
    }

    protected void assertPersistedEstadoLaboralToMatchUpdatableProperties(EstadoLaboral expectedEstadoLaboral) {
        assertEstadoLaboralAllUpdatablePropertiesEquals(expectedEstadoLaboral, getPersistedEstadoLaboral(expectedEstadoLaboral));
    }
}
