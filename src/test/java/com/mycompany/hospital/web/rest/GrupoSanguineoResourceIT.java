package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.GrupoSanguineoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.GrupoSanguineo;
import com.mycompany.hospital.repository.GrupoSanguineoRepository;
import com.mycompany.hospital.service.dto.GrupoSanguineoDTO;
import com.mycompany.hospital.service.mapper.GrupoSanguineoMapper;
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
 * Integration tests for the {@link GrupoSanguineoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GrupoSanguineoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/grupo-sanguineos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GrupoSanguineoRepository grupoSanguineoRepository;

    @Autowired
    private GrupoSanguineoMapper grupoSanguineoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGrupoSanguineoMockMvc;

    private GrupoSanguineo grupoSanguineo;

    private GrupoSanguineo insertedGrupoSanguineo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GrupoSanguineo createEntity() {
        return new GrupoSanguineo()
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
    public static GrupoSanguineo createUpdatedEntity() {
        return new GrupoSanguineo()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        grupoSanguineo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGrupoSanguineo != null) {
            grupoSanguineoRepository.delete(insertedGrupoSanguineo);
            insertedGrupoSanguineo = null;
        }
    }

    @Test
    @Transactional
    void createGrupoSanguineo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);
        var returnedGrupoSanguineoDTO = om.readValue(
            restGrupoSanguineoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GrupoSanguineoDTO.class
        );

        // Validate the GrupoSanguineo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGrupoSanguineo = grupoSanguineoMapper.toEntity(returnedGrupoSanguineoDTO);
        assertGrupoSanguineoUpdatableFieldsEquals(returnedGrupoSanguineo, getPersistedGrupoSanguineo(returnedGrupoSanguineo));

        insertedGrupoSanguineo = returnedGrupoSanguineo;
    }

    @Test
    @Transactional
    void createGrupoSanguineoWithExistingId() throws Exception {
        // Create the GrupoSanguineo with an existing ID
        grupoSanguineo.setId(1L);
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGrupoSanguineoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grupoSanguineo.setCodigo(null);

        // Create the GrupoSanguineo, which fails.
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        restGrupoSanguineoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grupoSanguineo.setNombre(null);

        // Create the GrupoSanguineo, which fails.
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        restGrupoSanguineoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grupoSanguineo.setActivo(null);

        // Create the GrupoSanguineo, which fails.
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        restGrupoSanguineoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grupoSanguineo.setFechaAlta(null);

        // Create the GrupoSanguineo, which fails.
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        restGrupoSanguineoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineos() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList
        restGrupoSanguineoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupoSanguineo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getGrupoSanguineo() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get the grupoSanguineo
        restGrupoSanguineoMockMvc
            .perform(get(ENTITY_API_URL_ID, grupoSanguineo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grupoSanguineo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getGrupoSanguineosByIdFiltering() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        Long id = grupoSanguineo.getId();

        defaultGrupoSanguineoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGrupoSanguineoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGrupoSanguineoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where codigo equals to
        defaultGrupoSanguineoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where codigo in
        defaultGrupoSanguineoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where codigo is not null
        defaultGrupoSanguineoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where codigo contains
        defaultGrupoSanguineoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where codigo does not contain
        defaultGrupoSanguineoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where nombre equals to
        defaultGrupoSanguineoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where nombre in
        defaultGrupoSanguineoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where nombre is not null
        defaultGrupoSanguineoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where nombre contains
        defaultGrupoSanguineoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where nombre does not contain
        defaultGrupoSanguineoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where descripcion equals to
        defaultGrupoSanguineoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where descripcion in
        defaultGrupoSanguineoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where descripcion is not null
        defaultGrupoSanguineoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where descripcion contains
        defaultGrupoSanguineoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where descripcion does not contain
        defaultGrupoSanguineoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where activo equals to
        defaultGrupoSanguineoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where activo in
        defaultGrupoSanguineoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where activo is not null
        defaultGrupoSanguineoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta equals to
        defaultGrupoSanguineoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta in
        defaultGrupoSanguineoFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta is not null
        defaultGrupoSanguineoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta is greater than or equal to
        defaultGrupoSanguineoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta is less than or equal to
        defaultGrupoSanguineoFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta is less than
        defaultGrupoSanguineoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaAlta is greater than
        defaultGrupoSanguineoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja equals to
        defaultGrupoSanguineoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja in
        defaultGrupoSanguineoFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja is not null
        defaultGrupoSanguineoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja is greater than or equal to
        defaultGrupoSanguineoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja is less than or equal to
        defaultGrupoSanguineoFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja is less than
        defaultGrupoSanguineoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllGrupoSanguineosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        // Get all the grupoSanguineoList where fechaBaja is greater than
        defaultGrupoSanguineoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultGrupoSanguineoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGrupoSanguineoShouldBeFound(shouldBeFound);
        defaultGrupoSanguineoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGrupoSanguineoShouldBeFound(String filter) throws Exception {
        restGrupoSanguineoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grupoSanguineo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restGrupoSanguineoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGrupoSanguineoShouldNotBeFound(String filter) throws Exception {
        restGrupoSanguineoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGrupoSanguineoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGrupoSanguineo() throws Exception {
        // Get the grupoSanguineo
        restGrupoSanguineoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrupoSanguineo() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupoSanguineo
        GrupoSanguineo updatedGrupoSanguineo = grupoSanguineoRepository.findById(grupoSanguineo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGrupoSanguineo are not directly saved in db
        em.detach(updatedGrupoSanguineo);
        updatedGrupoSanguineo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(updatedGrupoSanguineo);

        restGrupoSanguineoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoSanguineoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(grupoSanguineoDTO))
            )
            .andExpect(status().isOk());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGrupoSanguineoToMatchAllProperties(updatedGrupoSanguineo);
    }

    @Test
    @Transactional
    void putNonExistingGrupoSanguineo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoSanguineo.setId(longCount.incrementAndGet());

        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoSanguineoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grupoSanguineoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(grupoSanguineoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrupoSanguineo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoSanguineo.setId(longCount.incrementAndGet());

        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoSanguineoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(grupoSanguineoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrupoSanguineo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoSanguineo.setId(longCount.incrementAndGet());

        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoSanguineoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGrupoSanguineoWithPatch() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupoSanguineo using partial update
        GrupoSanguineo partialUpdatedGrupoSanguineo = new GrupoSanguineo();
        partialUpdatedGrupoSanguineo.setId(grupoSanguineo.getId());

        partialUpdatedGrupoSanguineo.nombre(UPDATED_NOMBRE).fechaAlta(UPDATED_FECHA_ALTA);

        restGrupoSanguineoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupoSanguineo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrupoSanguineo))
            )
            .andExpect(status().isOk());

        // Validate the GrupoSanguineo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGrupoSanguineoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGrupoSanguineo, grupoSanguineo),
            getPersistedGrupoSanguineo(grupoSanguineo)
        );
    }

    @Test
    @Transactional
    void fullUpdateGrupoSanguineoWithPatch() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grupoSanguineo using partial update
        GrupoSanguineo partialUpdatedGrupoSanguineo = new GrupoSanguineo();
        partialUpdatedGrupoSanguineo.setId(grupoSanguineo.getId());

        partialUpdatedGrupoSanguineo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restGrupoSanguineoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrupoSanguineo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrupoSanguineo))
            )
            .andExpect(status().isOk());

        // Validate the GrupoSanguineo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGrupoSanguineoUpdatableFieldsEquals(partialUpdatedGrupoSanguineo, getPersistedGrupoSanguineo(partialUpdatedGrupoSanguineo));
    }

    @Test
    @Transactional
    void patchNonExistingGrupoSanguineo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoSanguineo.setId(longCount.incrementAndGet());

        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGrupoSanguineoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grupoSanguineoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(grupoSanguineoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrupoSanguineo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoSanguineo.setId(longCount.incrementAndGet());

        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoSanguineoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(grupoSanguineoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrupoSanguineo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grupoSanguineo.setId(longCount.incrementAndGet());

        // Create the GrupoSanguineo
        GrupoSanguineoDTO grupoSanguineoDTO = grupoSanguineoMapper.toDto(grupoSanguineo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGrupoSanguineoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(grupoSanguineoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GrupoSanguineo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrupoSanguineo() throws Exception {
        // Initialize the database
        insertedGrupoSanguineo = grupoSanguineoRepository.saveAndFlush(grupoSanguineo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the grupoSanguineo
        restGrupoSanguineoMockMvc
            .perform(delete(ENTITY_API_URL_ID, grupoSanguineo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return grupoSanguineoRepository.count();
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

    protected GrupoSanguineo getPersistedGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        return grupoSanguineoRepository.findById(grupoSanguineo.getId()).orElseThrow();
    }

    protected void assertPersistedGrupoSanguineoToMatchAllProperties(GrupoSanguineo expectedGrupoSanguineo) {
        assertGrupoSanguineoAllPropertiesEquals(expectedGrupoSanguineo, getPersistedGrupoSanguineo(expectedGrupoSanguineo));
    }

    protected void assertPersistedGrupoSanguineoToMatchUpdatableProperties(GrupoSanguineo expectedGrupoSanguineo) {
        assertGrupoSanguineoAllUpdatablePropertiesEquals(expectedGrupoSanguineo, getPersistedGrupoSanguineo(expectedGrupoSanguineo));
    }
}
