package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.TipoDiagnosticoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.TipoDiagnostico;
import com.mycompany.hospital.repository.TipoDiagnosticoRepository;
import com.mycompany.hospital.service.dto.TipoDiagnosticoDTO;
import com.mycompany.hospital.service.mapper.TipoDiagnosticoMapper;
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
 * Integration tests for the {@link TipoDiagnosticoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoDiagnosticoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tipo-diagnosticos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoDiagnosticoRepository tipoDiagnosticoRepository;

    @Autowired
    private TipoDiagnosticoMapper tipoDiagnosticoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoDiagnosticoMockMvc;

    private TipoDiagnostico tipoDiagnostico;

    private TipoDiagnostico insertedTipoDiagnostico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoDiagnostico createEntity() {
        return new TipoDiagnostico()
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
    public static TipoDiagnostico createUpdatedEntity() {
        return new TipoDiagnostico()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        tipoDiagnostico = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoDiagnostico != null) {
            tipoDiagnosticoRepository.delete(insertedTipoDiagnostico);
            insertedTipoDiagnostico = null;
        }
    }

    @Test
    @Transactional
    void createTipoDiagnostico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);
        var returnedTipoDiagnosticoDTO = om.readValue(
            restTipoDiagnosticoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoDiagnosticoDTO.class
        );

        // Validate the TipoDiagnostico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoDiagnostico = tipoDiagnosticoMapper.toEntity(returnedTipoDiagnosticoDTO);
        assertTipoDiagnosticoUpdatableFieldsEquals(returnedTipoDiagnostico, getPersistedTipoDiagnostico(returnedTipoDiagnostico));

        insertedTipoDiagnostico = returnedTipoDiagnostico;
    }

    @Test
    @Transactional
    void createTipoDiagnosticoWithExistingId() throws Exception {
        // Create the TipoDiagnostico with an existing ID
        tipoDiagnostico.setId(1L);
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDiagnostico.setCodigo(null);

        // Create the TipoDiagnostico, which fails.
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        restTipoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDiagnostico.setNombre(null);

        // Create the TipoDiagnostico, which fails.
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        restTipoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDiagnostico.setActivo(null);

        // Create the TipoDiagnostico, which fails.
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        restTipoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDiagnostico.setFechaAlta(null);

        // Create the TipoDiagnostico, which fails.
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        restTipoDiagnosticoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticos() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList
        restTipoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDiagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getTipoDiagnostico() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get the tipoDiagnostico
        restTipoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoDiagnostico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoDiagnostico.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getTipoDiagnosticosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        Long id = tipoDiagnostico.getId();

        defaultTipoDiagnosticoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoDiagnosticoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoDiagnosticoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where codigo equals to
        defaultTipoDiagnosticoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where codigo in
        defaultTipoDiagnosticoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where codigo is not null
        defaultTipoDiagnosticoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where codigo contains
        defaultTipoDiagnosticoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where codigo does not contain
        defaultTipoDiagnosticoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where nombre equals to
        defaultTipoDiagnosticoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where nombre in
        defaultTipoDiagnosticoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where nombre is not null
        defaultTipoDiagnosticoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where nombre contains
        defaultTipoDiagnosticoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where nombre does not contain
        defaultTipoDiagnosticoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where descripcion equals to
        defaultTipoDiagnosticoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where descripcion in
        defaultTipoDiagnosticoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where descripcion is not null
        defaultTipoDiagnosticoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where descripcion contains
        defaultTipoDiagnosticoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where descripcion does not contain
        defaultTipoDiagnosticoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where activo equals to
        defaultTipoDiagnosticoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where activo in
        defaultTipoDiagnosticoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where activo is not null
        defaultTipoDiagnosticoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta equals to
        defaultTipoDiagnosticoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta in
        defaultTipoDiagnosticoFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta is not null
        defaultTipoDiagnosticoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta is greater than or equal to
        defaultTipoDiagnosticoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta is less than or equal to
        defaultTipoDiagnosticoFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta is less than
        defaultTipoDiagnosticoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaAlta is greater than
        defaultTipoDiagnosticoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja equals to
        defaultTipoDiagnosticoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja in
        defaultTipoDiagnosticoFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja is not null
        defaultTipoDiagnosticoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja is greater than or equal to
        defaultTipoDiagnosticoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja is less than or equal to
        defaultTipoDiagnosticoFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja is less than
        defaultTipoDiagnosticoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoDiagnosticosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        // Get all the tipoDiagnosticoList where fechaBaja is greater than
        defaultTipoDiagnosticoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultTipoDiagnosticoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoDiagnosticoShouldBeFound(shouldBeFound);
        defaultTipoDiagnosticoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoDiagnosticoShouldBeFound(String filter) throws Exception {
        restTipoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDiagnostico.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restTipoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoDiagnosticoShouldNotBeFound(String filter) throws Exception {
        restTipoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoDiagnosticoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoDiagnostico() throws Exception {
        // Get the tipoDiagnostico
        restTipoDiagnosticoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoDiagnostico() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDiagnostico
        TipoDiagnostico updatedTipoDiagnostico = tipoDiagnosticoRepository.findById(tipoDiagnostico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoDiagnostico are not directly saved in db
        em.detach(updatedTipoDiagnostico);
        updatedTipoDiagnostico
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(updatedTipoDiagnostico);

        restTipoDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDiagnosticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDiagnosticoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoDiagnosticoToMatchAllProperties(updatedTipoDiagnostico);
    }

    @Test
    @Transactional
    void putNonExistingTipoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDiagnostico.setId(longCount.incrementAndGet());

        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDiagnosticoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDiagnostico.setId(longCount.incrementAndGet());

        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDiagnosticoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDiagnostico.setId(longCount.incrementAndGet());

        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDiagnosticoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDiagnostico using partial update
        TipoDiagnostico partialUpdatedTipoDiagnostico = new TipoDiagnostico();
        partialUpdatedTipoDiagnostico.setId(tipoDiagnostico.getId());

        partialUpdatedTipoDiagnostico
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTipoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoDiagnostico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the TipoDiagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoDiagnosticoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoDiagnostico, tipoDiagnostico),
            getPersistedTipoDiagnostico(tipoDiagnostico)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoDiagnosticoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDiagnostico using partial update
        TipoDiagnostico partialUpdatedTipoDiagnostico = new TipoDiagnostico();
        partialUpdatedTipoDiagnostico.setId(tipoDiagnostico.getId());

        partialUpdatedTipoDiagnostico
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTipoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoDiagnostico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoDiagnostico))
            )
            .andExpect(status().isOk());

        // Validate the TipoDiagnostico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoDiagnosticoUpdatableFieldsEquals(
            partialUpdatedTipoDiagnostico,
            getPersistedTipoDiagnostico(partialUpdatedTipoDiagnostico)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTipoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDiagnostico.setId(longCount.incrementAndGet());

        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoDiagnosticoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDiagnostico.setId(longCount.incrementAndGet());

        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDiagnosticoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDiagnosticoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoDiagnostico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDiagnostico.setId(longCount.incrementAndGet());

        // Create the TipoDiagnostico
        TipoDiagnosticoDTO tipoDiagnosticoDTO = tipoDiagnosticoMapper.toDto(tipoDiagnostico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDiagnosticoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoDiagnosticoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoDiagnostico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoDiagnostico() throws Exception {
        // Initialize the database
        insertedTipoDiagnostico = tipoDiagnosticoRepository.saveAndFlush(tipoDiagnostico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoDiagnostico
        restTipoDiagnosticoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoDiagnostico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoDiagnosticoRepository.count();
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

    protected TipoDiagnostico getPersistedTipoDiagnostico(TipoDiagnostico tipoDiagnostico) {
        return tipoDiagnosticoRepository.findById(tipoDiagnostico.getId()).orElseThrow();
    }

    protected void assertPersistedTipoDiagnosticoToMatchAllProperties(TipoDiagnostico expectedTipoDiagnostico) {
        assertTipoDiagnosticoAllPropertiesEquals(expectedTipoDiagnostico, getPersistedTipoDiagnostico(expectedTipoDiagnostico));
    }

    protected void assertPersistedTipoDiagnosticoToMatchUpdatableProperties(TipoDiagnostico expectedTipoDiagnostico) {
        assertTipoDiagnosticoAllUpdatablePropertiesEquals(expectedTipoDiagnostico, getPersistedTipoDiagnostico(expectedTipoDiagnostico));
    }
}
