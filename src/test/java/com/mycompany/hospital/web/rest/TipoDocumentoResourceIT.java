package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.TipoDocumentoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.TipoDocumento;
import com.mycompany.hospital.repository.TipoDocumentoRepository;
import com.mycompany.hospital.service.dto.TipoDocumentoDTO;
import com.mycompany.hospital.service.mapper.TipoDocumentoMapper;
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
 * Integration tests for the {@link TipoDocumentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoDocumentoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/tipo-documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private TipoDocumentoMapper tipoDocumentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoDocumentoMockMvc;

    private TipoDocumento tipoDocumento;

    private TipoDocumento insertedTipoDocumento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoDocumento createEntity() {
        return new TipoDocumento()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .sigla(DEFAULT_SIGLA)
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
    public static TipoDocumento createUpdatedEntity() {
        return new TipoDocumento()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .sigla(UPDATED_SIGLA)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        tipoDocumento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoDocumento != null) {
            tipoDocumentoRepository.delete(insertedTipoDocumento);
            insertedTipoDocumento = null;
        }
    }

    @Test
    @Transactional
    void createTipoDocumento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);
        var returnedTipoDocumentoDTO = om.readValue(
            restTipoDocumentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoDocumentoDTO.class
        );

        // Validate the TipoDocumento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoDocumento = tipoDocumentoMapper.toEntity(returnedTipoDocumentoDTO);
        assertTipoDocumentoUpdatableFieldsEquals(returnedTipoDocumento, getPersistedTipoDocumento(returnedTipoDocumento));

        insertedTipoDocumento = returnedTipoDocumento;
    }

    @Test
    @Transactional
    void createTipoDocumentoWithExistingId() throws Exception {
        // Create the TipoDocumento with an existing ID
        tipoDocumento.setId(1L);
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDocumento.setCodigo(null);

        // Create the TipoDocumento, which fails.
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        restTipoDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDocumento.setNombre(null);

        // Create the TipoDocumento, which fails.
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        restTipoDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiglaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDocumento.setSigla(null);

        // Create the TipoDocumento, which fails.
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        restTipoDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDocumento.setActivo(null);

        // Create the TipoDocumento, which fails.
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        restTipoDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoDocumento.setFechaAlta(null);

        // Create the TipoDocumento, which fails.
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        restTipoDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoDocumentos() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList
        restTipoDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDocumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getTipoDocumento() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get the tipoDocumento
        restTipoDocumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoDocumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoDocumento.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getTipoDocumentosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        Long id = tipoDocumento.getId();

        defaultTipoDocumentoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoDocumentoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoDocumentoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where codigo equals to
        defaultTipoDocumentoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where codigo in
        defaultTipoDocumentoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where codigo is not null
        defaultTipoDocumentoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where codigo contains
        defaultTipoDocumentoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where codigo does not contain
        defaultTipoDocumentoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where nombre equals to
        defaultTipoDocumentoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where nombre in
        defaultTipoDocumentoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where nombre is not null
        defaultTipoDocumentoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where nombre contains
        defaultTipoDocumentoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where nombre does not contain
        defaultTipoDocumentoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosBySiglaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where sigla equals to
        defaultTipoDocumentoFiltering("sigla.equals=" + DEFAULT_SIGLA, "sigla.equals=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosBySiglaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where sigla in
        defaultTipoDocumentoFiltering("sigla.in=" + DEFAULT_SIGLA + "," + UPDATED_SIGLA, "sigla.in=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosBySiglaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where sigla is not null
        defaultTipoDocumentoFiltering("sigla.specified=true", "sigla.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosBySiglaContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where sigla contains
        defaultTipoDocumentoFiltering("sigla.contains=" + DEFAULT_SIGLA, "sigla.contains=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosBySiglaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where sigla does not contain
        defaultTipoDocumentoFiltering("sigla.doesNotContain=" + UPDATED_SIGLA, "sigla.doesNotContain=" + DEFAULT_SIGLA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where descripcion equals to
        defaultTipoDocumentoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where descripcion in
        defaultTipoDocumentoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where descripcion is not null
        defaultTipoDocumentoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where descripcion contains
        defaultTipoDocumentoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where descripcion does not contain
        defaultTipoDocumentoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where activo equals to
        defaultTipoDocumentoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where activo in
        defaultTipoDocumentoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where activo is not null
        defaultTipoDocumentoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta equals to
        defaultTipoDocumentoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta in
        defaultTipoDocumentoFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta is not null
        defaultTipoDocumentoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta is greater than or equal to
        defaultTipoDocumentoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta is less than or equal to
        defaultTipoDocumentoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta is less than
        defaultTipoDocumentoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaAlta is greater than
        defaultTipoDocumentoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja equals to
        defaultTipoDocumentoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja in
        defaultTipoDocumentoFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja is not null
        defaultTipoDocumentoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja is greater than or equal to
        defaultTipoDocumentoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja is less than or equal to
        defaultTipoDocumentoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja is less than
        defaultTipoDocumentoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoDocumentosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList where fechaBaja is greater than
        defaultTipoDocumentoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultTipoDocumentoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoDocumentoShouldBeFound(shouldBeFound);
        defaultTipoDocumentoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoDocumentoShouldBeFound(String filter) throws Exception {
        restTipoDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDocumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restTipoDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoDocumentoShouldNotBeFound(String filter) throws Exception {
        restTipoDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoDocumento() throws Exception {
        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoDocumento() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDocumento
        TipoDocumento updatedTipoDocumento = tipoDocumentoRepository.findById(tipoDocumento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoDocumento are not directly saved in db
        em.detach(updatedTipoDocumento);
        updatedTipoDocumento
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .sigla(UPDATED_SIGLA)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(updatedTipoDocumento);

        restTipoDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDocumentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDocumentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoDocumentoToMatchAllProperties(updatedTipoDocumento);
    }

    @Test
    @Transactional
    void putNonExistingTipoDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDocumento.setId(longCount.incrementAndGet());

        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoDocumentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDocumentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDocumento.setId(longCount.incrementAndGet());

        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoDocumentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDocumento.setId(longCount.incrementAndGet());

        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDocumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDocumento using partial update
        TipoDocumento partialUpdatedTipoDocumento = new TipoDocumento();
        partialUpdatedTipoDocumento.setId(tipoDocumento.getId());

        partialUpdatedTipoDocumento.codigo(UPDATED_CODIGO).nombre(UPDATED_NOMBRE).activo(UPDATED_ACTIVO);

        restTipoDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoDocumento))
            )
            .andExpect(status().isOk());

        // Validate the TipoDocumento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoDocumentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoDocumento, tipoDocumento),
            getPersistedTipoDocumento(tipoDocumento)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoDocumento using partial update
        TipoDocumento partialUpdatedTipoDocumento = new TipoDocumento();
        partialUpdatedTipoDocumento.setId(tipoDocumento.getId());

        partialUpdatedTipoDocumento
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .sigla(UPDATED_SIGLA)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restTipoDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoDocumento))
            )
            .andExpect(status().isOk());

        // Validate the TipoDocumento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoDocumentoUpdatableFieldsEquals(partialUpdatedTipoDocumento, getPersistedTipoDocumento(partialUpdatedTipoDocumento));
    }

    @Test
    @Transactional
    void patchNonExistingTipoDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDocumento.setId(longCount.incrementAndGet());

        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoDocumentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDocumentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDocumento.setId(longCount.incrementAndGet());

        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoDocumentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoDocumento.setId(longCount.incrementAndGet());

        // Create the TipoDocumento
        TipoDocumentoDTO tipoDocumentoDTO = tipoDocumentoMapper.toDto(tipoDocumento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoDocumentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoDocumentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoDocumento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoDocumento() throws Exception {
        // Initialize the database
        insertedTipoDocumento = tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoDocumento
        restTipoDocumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoDocumento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoDocumentoRepository.count();
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

    protected TipoDocumento getPersistedTipoDocumento(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.findById(tipoDocumento.getId()).orElseThrow();
    }

    protected void assertPersistedTipoDocumentoToMatchAllProperties(TipoDocumento expectedTipoDocumento) {
        assertTipoDocumentoAllPropertiesEquals(expectedTipoDocumento, getPersistedTipoDocumento(expectedTipoDocumento));
    }

    protected void assertPersistedTipoDocumentoToMatchUpdatableProperties(TipoDocumento expectedTipoDocumento) {
        assertTipoDocumentoAllUpdatablePropertiesEquals(expectedTipoDocumento, getPersistedTipoDocumento(expectedTipoDocumento));
    }
}
