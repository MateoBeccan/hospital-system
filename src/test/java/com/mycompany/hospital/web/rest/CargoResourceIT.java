package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.CargoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Cargo;
import com.mycompany.hospital.repository.CargoRepository;
import com.mycompany.hospital.service.dto.CargoDTO;
import com.mycompany.hospital.service.mapper.CargoMapper;
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
 * Integration tests for the {@link CargoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CargoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cargos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CargoMapper cargoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoMockMvc;

    private Cargo cargo;

    private Cargo insertedCargo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cargo createEntity() {
        return new Cargo()
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
    public static Cargo createUpdatedEntity() {
        return new Cargo()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        cargo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCargo != null) {
            cargoRepository.delete(insertedCargo);
            insertedCargo = null;
        }
    }

    @Test
    @Transactional
    void createCargo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);
        var returnedCargoDTO = om.readValue(
            restCargoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CargoDTO.class
        );

        // Validate the Cargo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCargo = cargoMapper.toEntity(returnedCargoDTO);
        assertCargoUpdatableFieldsEquals(returnedCargo, getPersistedCargo(returnedCargo));

        insertedCargo = returnedCargo;
    }

    @Test
    @Transactional
    void createCargoWithExistingId() throws Exception {
        // Create the Cargo with an existing ID
        cargo.setId(1L);
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargo.setCodigo(null);

        // Create the Cargo, which fails.
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargo.setNombre(null);

        // Create the Cargo, which fails.
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargo.setActivo(null);

        // Create the Cargo, which fails.
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cargo.setFechaAlta(null);

        // Create the Cargo, which fails.
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCargos() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList
        restCargoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getCargo() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get the cargo
        restCargoMockMvc
            .perform(get(ENTITY_API_URL_ID, cargo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getCargosByIdFiltering() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        Long id = cargo.getId();

        defaultCargoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCargoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCargoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCargosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where codigo equals to
        defaultCargoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCargosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where codigo in
        defaultCargoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCargosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where codigo is not null
        defaultCargoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllCargosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where codigo contains
        defaultCargoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCargosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where codigo does not contain
        defaultCargoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllCargosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where nombre equals to
        defaultCargoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCargosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where nombre in
        defaultCargoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCargosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where nombre is not null
        defaultCargoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCargosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where nombre contains
        defaultCargoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCargosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where nombre does not contain
        defaultCargoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCargosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where descripcion equals to
        defaultCargoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCargosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where descripcion in
        defaultCargoFiltering("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION, "descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCargosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where descripcion is not null
        defaultCargoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCargosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where descripcion contains
        defaultCargoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCargosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where descripcion does not contain
        defaultCargoFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCargosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where activo equals to
        defaultCargoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllCargosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where activo in
        defaultCargoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllCargosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where activo is not null
        defaultCargoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta equals to
        defaultCargoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta in
        defaultCargoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta is not null
        defaultCargoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta is greater than or equal to
        defaultCargoFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta is less than or equal to
        defaultCargoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta is less than
        defaultCargoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaAlta is greater than
        defaultCargoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja equals to
        defaultCargoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja in
        defaultCargoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja is not null
        defaultCargoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja is greater than or equal to
        defaultCargoFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja is less than or equal to
        defaultCargoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja is less than
        defaultCargoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCargosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList where fechaBaja is greater than
        defaultCargoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultCargoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCargoShouldBeFound(shouldBeFound);
        defaultCargoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCargoShouldBeFound(String filter) throws Exception {
        restCargoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restCargoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCargoShouldNotBeFound(String filter) throws Exception {
        restCargoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCargoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCargo() throws Exception {
        // Get the cargo
        restCargoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargo() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargo
        Cargo updatedCargo = cargoRepository.findById(cargo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCargo are not directly saved in db
        em.detach(updatedCargo);
        updatedCargo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        CargoDTO cargoDTO = cargoMapper.toDto(updatedCargo);

        restCargoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCargoToMatchAllProperties(updatedCargo);
    }

    @Test
    @Transactional
    void putNonExistingCargo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargo.setId(longCount.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargo.setId(longCount.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargo.setId(longCount.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCargoWithPatch() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargo using partial update
        Cargo partialUpdatedCargo = new Cargo();
        partialUpdatedCargo.setId(cargo.getId());

        partialUpdatedCargo.codigo(UPDATED_CODIGO).nombre(UPDATED_NOMBRE).activo(UPDATED_ACTIVO).fechaBaja(UPDATED_FECHA_BAJA);

        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargo))
            )
            .andExpect(status().isOk());

        // Validate the Cargo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCargo, cargo), getPersistedCargo(cargo));
    }

    @Test
    @Transactional
    void fullUpdateCargoWithPatch() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cargo using partial update
        Cargo partialUpdatedCargo = new Cargo();
        partialUpdatedCargo.setId(cargo.getId());

        partialUpdatedCargo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCargo))
            )
            .andExpect(status().isOk());

        // Validate the Cargo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCargoUpdatableFieldsEquals(partialUpdatedCargo, getPersistedCargo(partialUpdatedCargo));
    }

    @Test
    @Transactional
    void patchNonExistingCargo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargo.setId(longCount.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargo.setId(longCount.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cargo.setId(longCount.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cargoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cargo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCargo() throws Exception {
        // Initialize the database
        insertedCargo = cargoRepository.saveAndFlush(cargo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cargo
        restCargoMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cargoRepository.count();
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

    protected Cargo getPersistedCargo(Cargo cargo) {
        return cargoRepository.findById(cargo.getId()).orElseThrow();
    }

    protected void assertPersistedCargoToMatchAllProperties(Cargo expectedCargo) {
        assertCargoAllPropertiesEquals(expectedCargo, getPersistedCargo(expectedCargo));
    }

    protected void assertPersistedCargoToMatchUpdatableProperties(Cargo expectedCargo) {
        assertCargoAllUpdatablePropertiesEquals(expectedCargo, getPersistedCargo(expectedCargo));
    }
}
