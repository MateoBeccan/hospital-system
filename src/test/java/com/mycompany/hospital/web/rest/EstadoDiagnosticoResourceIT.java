package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.EstadoDiagnosticoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.EstadoDiagnostico;
import com.mycompany.hospital.repository.EstadoDiagnosticoRepository;
import com.mycompany.hospital.service.dto.EstadoDiagnosticoDTO;
import com.mycompany.hospital.service.mapper.EstadoDiagnosticoMapper;
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
 * Integration tests for the {@link EstadoDiagnosticoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstadoDiagnosticoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/estado-diagnosticos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstadoDiagnosticoRepository estadoDiagnosticoRepository;

    @Autowired
    private EstadoDiagnosticoMapper estadoDiagnosticoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoDiagnosticoMockMvc;

    private EstadoDiagnostico estadoDiagnostico;

    private EstadoDiagnostico insertedEstadoDiagnostico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoDiagnostico createEntity() {
        return new EstadoDiagnostico()
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
    public static EstadoDiagnostico createUpdatedEntity() {
        return new EstadoDiagnostico()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        estadoDiagnostico = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEstadoDiagnostico != null) {
            estadoDiagnosticoRepository.delete(insertedEstadoDiagnostico);
            insertedEstadoDiagnostico = null;
        }
    }

    @Test
    @Transactional
    void createEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);
        var returnedEstadoDiagnosticoDTO = om.readValue(
            restEstadoDiagnosticoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EstadoDiagnosticoDTO.class
        );

        // Validate the EstadoDiagnostico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstadoDiagnostico = estadoDiagnosticoMapper.toEntity(returnedEstadoDiagnosticoDTO);
        assertEstadoDiagnosticoUpdatableFieldsEquals(returnedEstadoDiagnostico, getPersistedEstadoDiagnostico(returnedEstadoDiagnostico));

        insertedEstadoDiagnostico = returnedEstadoDiagnostico;
    }

    @Test
    @Transactional
    void createEstadoDiagnosticoWithExistingId() throws Exception {
        // Create the EstadoDiagnostico with an existing ID
        estadoDiagnostico.setId(1L);
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoDiagnostico.setCodigo(null);

        // Create the EstadoDiagnostico, which fails.
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        restEstadoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoDiagnostico.setNombre(null);

        // Create the EstadoDiagnostico, which fails.
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        restEstadoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoDiagnostico.setActivo(null);

        // Create the EstadoDiagnostico, which fails.
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        restEstadoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoDiagnostico.setFechaAlta(null);

        // Create the EstadoDiagnostico, which fails.
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        restEstadoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticos() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList
        restEstadoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoDiagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getEstadoDiagnostico() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get the estadoDiagnostico
        restEstadoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL_ID, estadoDiagnostico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estadoDiagnostico.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getEstadoDiagnosticosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        Long id = estadoDiagnostico.getId();

        defaultEstadoDiagnosticoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEstadoDiagnosticoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEstadoDiagnosticoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where codigo equals to
        defaultEstadoDiagnosticoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where codigo in
        defaultEstadoDiagnosticoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where codigo is not null
        defaultEstadoDiagnosticoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where codigo contains
        defaultEstadoDiagnosticoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where codigo does not contain
        defaultEstadoDiagnosticoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where nombre equals to
        defaultEstadoDiagnosticoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where nombre in
        defaultEstadoDiagnosticoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where nombre is not null
        defaultEstadoDiagnosticoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where nombre contains
        defaultEstadoDiagnosticoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where nombre does not contain
        defaultEstadoDiagnosticoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where descripcion equals to
        defaultEstadoDiagnosticoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where descripcion in
        defaultEstadoDiagnosticoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where descripcion is not null
        defaultEstadoDiagnosticoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where descripcion contains
        defaultEstadoDiagnosticoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where descripcion does not contain
        defaultEstadoDiagnosticoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where activo equals to
        defaultEstadoDiagnosticoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where activo in
        defaultEstadoDiagnosticoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where activo is not null
        defaultEstadoDiagnosticoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta equals to
        defaultEstadoDiagnosticoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta in
        defaultEstadoDiagnosticoFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta is not null
        defaultEstadoDiagnosticoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta is greater than or equal to
        defaultEstadoDiagnosticoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta is less than or equal to
        defaultEstadoDiagnosticoFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta is less than
        defaultEstadoDiagnosticoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaAlta is greater than
        defaultEstadoDiagnosticoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja equals to
        defaultEstadoDiagnosticoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja in
        defaultEstadoDiagnosticoFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja is not null
        defaultEstadoDiagnosticoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja is greater than or equal to
        defaultEstadoDiagnosticoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja is less than or equal to
        defaultEstadoDiagnosticoFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja is less than
        defaultEstadoDiagnosticoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoDiagnosticosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        // Get all the estadoDiagnosticoList where fechaBaja is greater than
        defaultEstadoDiagnosticoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultEstadoDiagnosticoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEstadoDiagnosticoShouldBeFound(shouldBeFound);
        defaultEstadoDiagnosticoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstadoDiagnosticoShouldBeFound(String filter) throws Exception {
        restEstadoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoDiagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restEstadoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstadoDiagnosticoShouldNotBeFound(String filter) throws Exception {
        restEstadoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstadoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstadoDiagnostico() throws Exception {
        // Get the estadoDiagnostico
        restEstadoDiagnosticoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstadoDiagnostico() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoDiagnostico
        EstadoDiagnostico updatedEstadoDiagnostico = estadoDiagnosticoRepository.findById(estadoDiagnostico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstadoDiagnostico are not directly saved in db
        em.detach(updatedEstadoDiagnostico);
        updatedEstadoDiagnostico
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(updatedEstadoDiagnostico);

        restEstadoDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoDiagnosticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoDiagnosticoDTO))
            )
            .andExpect(status().isOk());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstadoDiagnosticoToMatchAllProperties(updatedEstadoDiagnostico);
    }

    @Test
    @Transactional
    void putNonExistingEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoDiagnostico.setId(longCount.incrementAndGet());

        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoDiagnosticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoDiagnostico.setId(longCount.incrementAndGet());

        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoDiagnostico.setId(longCount.incrementAndGet());

        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoDiagnosticoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstadoDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoDiagnostico using partial update
        EstadoDiagnostico partialUpdatedEstadoDiagnostico = new EstadoDiagnostico();
        partialUpdatedEstadoDiagnostico.setId(estadoDiagnostico.getId());

        partialUpdatedEstadoDiagnostico.codigo(UPDATED_CODIGO).nombre(UPDATED_NOMBRE);

        restEstadoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoDiagnostico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the EstadoDiagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoDiagnosticoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEstadoDiagnostico, estadoDiagnostico),
            getPersistedEstadoDiagnostico(estadoDiagnostico)
        );
    }

    @Test
    @Transactional
    void fullUpdateEstadoDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoDiagnostico using partial update
        EstadoDiagnostico partialUpdatedEstadoDiagnostico = new EstadoDiagnostico();
        partialUpdatedEstadoDiagnostico.setId(estadoDiagnostico.getId());

        partialUpdatedEstadoDiagnostico
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restEstadoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoDiagnostico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the EstadoDiagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoDiagnosticoUpdatableFieldsEquals(
            partialUpdatedEstadoDiagnostico,
            getPersistedEstadoDiagnostico(partialUpdatedEstadoDiagnostico)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoDiagnostico.setId(longCount.incrementAndGet());

        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estadoDiagnosticoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoDiagnostico.setId(longCount.incrementAndGet());

        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstadoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoDiagnostico.setId(longCount.incrementAndGet());

        // Create the EstadoDiagnostico
        EstadoDiagnosticoDTO estadoDiagnosticoDTO = estadoDiagnosticoMapper.toDto(estadoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoDiagnosticoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estadoDiagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstadoDiagnostico() throws Exception {
        // Initialize the database
        insertedEstadoDiagnostico = estadoDiagnosticoRepository.saveAndFlush(estadoDiagnostico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estadoDiagnostico
        restEstadoDiagnosticoMockMvc
            .perform(delete(ENTITY_API_URL_ID, estadoDiagnostico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estadoDiagnosticoRepository.count();
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

    protected EstadoDiagnostico getPersistedEstadoDiagnostico(EstadoDiagnostico estadoDiagnostico) {
        return estadoDiagnosticoRepository.findById(estadoDiagnostico.getId()).orElseThrow();
    }

    protected void assertPersistedEstadoDiagnosticoToMatchAllProperties(EstadoDiagnostico expectedEstadoDiagnostico) {
        assertEstadoDiagnosticoAllPropertiesEquals(expectedEstadoDiagnostico, getPersistedEstadoDiagnostico(expectedEstadoDiagnostico));
    }

    protected void assertPersistedEstadoDiagnosticoToMatchUpdatableProperties(EstadoDiagnostico expectedEstadoDiagnostico) {
        assertEstadoDiagnosticoAllUpdatablePropertiesEquals(
            expectedEstadoDiagnostico,
            getPersistedEstadoDiagnostico(expectedEstadoDiagnostico)
        );
    }
}
