package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.EstadoTratamientoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.EstadoTratamiento;
import com.mycompany.hospital.repository.EstadoTratamientoRepository;
import com.mycompany.hospital.service.dto.EstadoTratamientoDTO;
import com.mycompany.hospital.service.mapper.EstadoTratamientoMapper;
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
 * Integration tests for the {@link EstadoTratamientoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstadoTratamientoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/estado-tratamientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstadoTratamientoRepository estadoTratamientoRepository;

    @Autowired
    private EstadoTratamientoMapper estadoTratamientoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoTratamientoMockMvc;

    private EstadoTratamiento estadoTratamiento;

    private EstadoTratamiento insertedEstadoTratamiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoTratamiento createEntity() {
        return new EstadoTratamiento()
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
    public static EstadoTratamiento createUpdatedEntity() {
        return new EstadoTratamiento()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        estadoTratamiento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEstadoTratamiento != null) {
            estadoTratamientoRepository.delete(insertedEstadoTratamiento);
            insertedEstadoTratamiento = null;
        }
    }

    @Test
    @Transactional
    void createEstadoTratamiento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);
        var returnedEstadoTratamientoDTO = om.readValue(
            restEstadoTratamientoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EstadoTratamientoDTO.class
        );

        // Validate the EstadoTratamiento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstadoTratamiento = estadoTratamientoMapper.toEntity(returnedEstadoTratamientoDTO);
        assertEstadoTratamientoUpdatableFieldsEquals(returnedEstadoTratamiento, getPersistedEstadoTratamiento(returnedEstadoTratamiento));

        insertedEstadoTratamiento = returnedEstadoTratamiento;
    }

    @Test
    @Transactional
    void createEstadoTratamientoWithExistingId() throws Exception {
        // Create the EstadoTratamiento with an existing ID
        estadoTratamiento.setId(1L);
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTratamiento.setCodigo(null);

        // Create the EstadoTratamiento, which fails.
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        restEstadoTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTratamiento.setNombre(null);

        // Create the EstadoTratamiento, which fails.
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        restEstadoTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTratamiento.setActivo(null);

        // Create the EstadoTratamiento, which fails.
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        restEstadoTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTratamiento.setFechaAlta(null);

        // Create the EstadoTratamiento, which fails.
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        restEstadoTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientos() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList
        restEstadoTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoTratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getEstadoTratamiento() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get the estadoTratamiento
        restEstadoTratamientoMockMvc
            .perform(get(ENTITY_API_URL_ID, estadoTratamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estadoTratamiento.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getEstadoTratamientosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        Long id = estadoTratamiento.getId();

        defaultEstadoTratamientoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEstadoTratamientoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEstadoTratamientoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where codigo equals to
        defaultEstadoTratamientoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where codigo in
        defaultEstadoTratamientoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where codigo is not null
        defaultEstadoTratamientoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where codigo contains
        defaultEstadoTratamientoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where codigo does not contain
        defaultEstadoTratamientoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where nombre equals to
        defaultEstadoTratamientoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where nombre in
        defaultEstadoTratamientoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where nombre is not null
        defaultEstadoTratamientoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where nombre contains
        defaultEstadoTratamientoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where nombre does not contain
        defaultEstadoTratamientoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where descripcion equals to
        defaultEstadoTratamientoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where descripcion in
        defaultEstadoTratamientoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where descripcion is not null
        defaultEstadoTratamientoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where descripcion contains
        defaultEstadoTratamientoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where descripcion does not contain
        defaultEstadoTratamientoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where activo equals to
        defaultEstadoTratamientoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where activo in
        defaultEstadoTratamientoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where activo is not null
        defaultEstadoTratamientoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta equals to
        defaultEstadoTratamientoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta in
        defaultEstadoTratamientoFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta is not null
        defaultEstadoTratamientoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta is greater than or equal to
        defaultEstadoTratamientoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta is less than or equal to
        defaultEstadoTratamientoFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta is less than
        defaultEstadoTratamientoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaAlta is greater than
        defaultEstadoTratamientoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja equals to
        defaultEstadoTratamientoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja in
        defaultEstadoTratamientoFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja is not null
        defaultEstadoTratamientoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja is greater than or equal to
        defaultEstadoTratamientoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja is less than or equal to
        defaultEstadoTratamientoFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja is less than
        defaultEstadoTratamientoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoTratamientosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        // Get all the estadoTratamientoList where fechaBaja is greater than
        defaultEstadoTratamientoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultEstadoTratamientoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEstadoTratamientoShouldBeFound(shouldBeFound);
        defaultEstadoTratamientoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstadoTratamientoShouldBeFound(String filter) throws Exception {
        restEstadoTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoTratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restEstadoTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstadoTratamientoShouldNotBeFound(String filter) throws Exception {
        restEstadoTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstadoTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstadoTratamiento() throws Exception {
        // Get the estadoTratamiento
        restEstadoTratamientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstadoTratamiento() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoTratamiento
        EstadoTratamiento updatedEstadoTratamiento = estadoTratamientoRepository.findById(estadoTratamiento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstadoTratamiento are not directly saved in db
        em.detach(updatedEstadoTratamiento);
        updatedEstadoTratamiento
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(updatedEstadoTratamiento);

        restEstadoTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoTratamientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoTratamientoDTO))
            )
            .andExpect(status().isOk());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstadoTratamientoToMatchAllProperties(updatedEstadoTratamiento);
    }

    @Test
    @Transactional
    void putNonExistingEstadoTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTratamiento.setId(longCount.incrementAndGet());

        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoTratamientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoTratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstadoTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTratamiento.setId(longCount.incrementAndGet());

        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoTratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstadoTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTratamiento.setId(longCount.incrementAndGet());

        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTratamientoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstadoTratamientoWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoTratamiento using partial update
        EstadoTratamiento partialUpdatedEstadoTratamiento = new EstadoTratamiento();
        partialUpdatedEstadoTratamiento.setId(estadoTratamiento.getId());

        partialUpdatedEstadoTratamiento.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).activo(UPDATED_ACTIVO);

        restEstadoTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoTratamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoTratamiento))
            )
            .andExpect(status().isOk());

        // Validate the EstadoTratamiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoTratamientoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEstadoTratamiento, estadoTratamiento),
            getPersistedEstadoTratamiento(estadoTratamiento)
        );
    }

    @Test
    @Transactional
    void fullUpdateEstadoTratamientoWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoTratamiento using partial update
        EstadoTratamiento partialUpdatedEstadoTratamiento = new EstadoTratamiento();
        partialUpdatedEstadoTratamiento.setId(estadoTratamiento.getId());

        partialUpdatedEstadoTratamiento
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restEstadoTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoTratamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoTratamiento))
            )
            .andExpect(status().isOk());

        // Validate the EstadoTratamiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoTratamientoUpdatableFieldsEquals(
            partialUpdatedEstadoTratamiento,
            getPersistedEstadoTratamiento(partialUpdatedEstadoTratamiento)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEstadoTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTratamiento.setId(longCount.incrementAndGet());

        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estadoTratamientoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoTratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstadoTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTratamiento.setId(longCount.incrementAndGet());

        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoTratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstadoTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTratamiento.setId(longCount.incrementAndGet());

        // Create the EstadoTratamiento
        EstadoTratamientoDTO estadoTratamientoDTO = estadoTratamientoMapper.toDto(estadoTratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTratamientoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estadoTratamientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoTratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstadoTratamiento() throws Exception {
        // Initialize the database
        insertedEstadoTratamiento = estadoTratamientoRepository.saveAndFlush(estadoTratamiento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estadoTratamiento
        restEstadoTratamientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, estadoTratamiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estadoTratamientoRepository.count();
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

    protected EstadoTratamiento getPersistedEstadoTratamiento(EstadoTratamiento estadoTratamiento) {
        return estadoTratamientoRepository.findById(estadoTratamiento.getId()).orElseThrow();
    }

    protected void assertPersistedEstadoTratamientoToMatchAllProperties(EstadoTratamiento expectedEstadoTratamiento) {
        assertEstadoTratamientoAllPropertiesEquals(expectedEstadoTratamiento, getPersistedEstadoTratamiento(expectedEstadoTratamiento));
    }

    protected void assertPersistedEstadoTratamientoToMatchUpdatableProperties(EstadoTratamiento expectedEstadoTratamiento) {
        assertEstadoTratamientoAllUpdatablePropertiesEquals(
            expectedEstadoTratamiento,
            getPersistedEstadoTratamiento(expectedEstadoTratamiento)
        );
    }
}
