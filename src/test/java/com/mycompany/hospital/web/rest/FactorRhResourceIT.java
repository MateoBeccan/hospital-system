package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.FactorRhAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.FactorRh;
import com.mycompany.hospital.repository.FactorRhRepository;
import com.mycompany.hospital.service.dto.FactorRhDTO;
import com.mycompany.hospital.service.mapper.FactorRhMapper;
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
 * Integration tests for the {@link FactorRhResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactorRhResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/factor-rhs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactorRhRepository factorRhRepository;

    @Autowired
    private FactorRhMapper factorRhMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactorRhMockMvc;

    private FactorRh factorRh;

    private FactorRh insertedFactorRh;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorRh createEntity() {
        return new FactorRh()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
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
    public static FactorRh createUpdatedEntity() {
        return new FactorRh()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        factorRh = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactorRh != null) {
            factorRhRepository.delete(insertedFactorRh);
            insertedFactorRh = null;
        }
    }

    @Test
    @Transactional
    void createFactorRh() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);
        var returnedFactorRhDTO = om.readValue(
            restFactorRhMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactorRhDTO.class
        );

        // Validate the FactorRh in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFactorRh = factorRhMapper.toEntity(returnedFactorRhDTO);
        assertFactorRhUpdatableFieldsEquals(returnedFactorRh, getPersistedFactorRh(returnedFactorRh));

        insertedFactorRh = returnedFactorRh;
    }

    @Test
    @Transactional
    void createFactorRhWithExistingId() throws Exception {
        // Create the FactorRh with an existing ID
        factorRh.setId(1L);
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactorRhMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factorRh.setCodigo(null);

        // Create the FactorRh, which fails.
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        restFactorRhMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factorRh.setNombre(null);

        // Create the FactorRh, which fails.
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        restFactorRhMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factorRh.setActivo(null);

        // Create the FactorRh, which fails.
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        restFactorRhMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factorRh.setFechaAlta(null);

        // Create the FactorRh, which fails.
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        restFactorRhMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactorRhs() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList
        restFactorRhMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorRh.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getFactorRh() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get the factorRh
        restFactorRhMockMvc
            .perform(get(ENTITY_API_URL_ID, factorRh.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factorRh.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getFactorRhsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        Long id = factorRh.getId();

        defaultFactorRhFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFactorRhFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFactorRhFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFactorRhsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where codigo equals to
        defaultFactorRhFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFactorRhsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where codigo in
        defaultFactorRhFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFactorRhsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where codigo is not null
        defaultFactorRhFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorRhsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where codigo contains
        defaultFactorRhFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllFactorRhsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where codigo does not contain
        defaultFactorRhFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllFactorRhsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where nombre equals to
        defaultFactorRhFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFactorRhsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where nombre in
        defaultFactorRhFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFactorRhsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where nombre is not null
        defaultFactorRhFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorRhsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where nombre contains
        defaultFactorRhFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFactorRhsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where nombre does not contain
        defaultFactorRhFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFactorRhsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where descripcion equals to
        defaultFactorRhFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFactorRhsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where descripcion in
        defaultFactorRhFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllFactorRhsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where descripcion is not null
        defaultFactorRhFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorRhsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where descripcion contains
        defaultFactorRhFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFactorRhsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where descripcion does not contain
        defaultFactorRhFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllFactorRhsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where activo equals to
        defaultFactorRhFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllFactorRhsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where activo in
        defaultFactorRhFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllFactorRhsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where activo is not null
        defaultFactorRhFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta equals to
        defaultFactorRhFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta in
        defaultFactorRhFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta is not null
        defaultFactorRhFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta is greater than or equal to
        defaultFactorRhFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta is less than or equal to
        defaultFactorRhFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta is less than
        defaultFactorRhFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaAlta is greater than
        defaultFactorRhFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja equals to
        defaultFactorRhFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja in
        defaultFactorRhFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja is not null
        defaultFactorRhFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja is greater than or equal to
        defaultFactorRhFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja is less than or equal to
        defaultFactorRhFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja is less than
        defaultFactorRhFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllFactorRhsByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        // Get all the factorRhList where fechaBaja is greater than
        defaultFactorRhFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultFactorRhFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFactorRhShouldBeFound(shouldBeFound);
        defaultFactorRhShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactorRhShouldBeFound(String filter) throws Exception {
        restFactorRhMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorRh.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restFactorRhMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactorRhShouldNotBeFound(String filter) throws Exception {
        restFactorRhMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactorRhMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactorRh() throws Exception {
        // Get the factorRh
        restFactorRhMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFactorRh() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factorRh
        FactorRh updatedFactorRh = factorRhRepository.findById(factorRh.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFactorRh are not directly saved in db
        em.detach(updatedFactorRh);
        updatedFactorRh
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(updatedFactorRh);

        restFactorRhMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorRhDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factorRhDTO))
            )
            .andExpect(status().isOk());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactorRhToMatchAllProperties(updatedFactorRh);
    }

    @Test
    @Transactional
    void putNonExistingFactorRh() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factorRh.setId(longCount.incrementAndGet());

        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorRhMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorRhDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factorRhDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactorRh() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factorRh.setId(longCount.incrementAndGet());

        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorRhMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factorRhDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactorRh() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factorRh.setId(longCount.incrementAndGet());

        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorRhMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactorRhWithPatch() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factorRh using partial update
        FactorRh partialUpdatedFactorRh = new FactorRh();
        partialUpdatedFactorRh.setId(factorRh.getId());

        partialUpdatedFactorRh
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restFactorRhMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorRh.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactorRh))
            )
            .andExpect(status().isOk());

        // Validate the FactorRh in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactorRhUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFactorRh, factorRh), getPersistedFactorRh(factorRh));
    }

    @Test
    @Transactional
    void fullUpdateFactorRhWithPatch() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factorRh using partial update
        FactorRh partialUpdatedFactorRh = new FactorRh();
        partialUpdatedFactorRh.setId(factorRh.getId());

        partialUpdatedFactorRh
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restFactorRhMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorRh.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactorRh))
            )
            .andExpect(status().isOk());

        // Validate the FactorRh in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactorRhUpdatableFieldsEquals(partialUpdatedFactorRh, getPersistedFactorRh(partialUpdatedFactorRh));
    }

    @Test
    @Transactional
    void patchNonExistingFactorRh() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factorRh.setId(longCount.incrementAndGet());

        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorRhMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factorRhDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factorRhDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactorRh() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factorRh.setId(longCount.incrementAndGet());

        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorRhMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factorRhDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactorRh() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factorRh.setId(longCount.incrementAndGet());

        // Create the FactorRh
        FactorRhDTO factorRhDTO = factorRhMapper.toDto(factorRh);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorRhMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factorRhDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactorRh in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactorRh() throws Exception {
        // Initialize the database
        insertedFactorRh = factorRhRepository.saveAndFlush(factorRh);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the factorRh
        restFactorRhMockMvc
            .perform(delete(ENTITY_API_URL_ID, factorRh.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return factorRhRepository.count();
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

    protected FactorRh getPersistedFactorRh(FactorRh factorRh) {
        return factorRhRepository.findById(factorRh.getId()).orElseThrow();
    }

    protected void assertPersistedFactorRhToMatchAllProperties(FactorRh expectedFactorRh) {
        assertFactorRhAllPropertiesEquals(expectedFactorRh, getPersistedFactorRh(expectedFactorRh));
    }

    protected void assertPersistedFactorRhToMatchUpdatableProperties(FactorRh expectedFactorRh) {
        assertFactorRhAllUpdatablePropertiesEquals(expectedFactorRh, getPersistedFactorRh(expectedFactorRh));
    }
}
