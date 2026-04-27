package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.CanalSolicitudAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.CanalSolicitud;
import com.mycompany.hospital.repository.CanalSolicitudRepository;
import com.mycompany.hospital.service.dto.CanalSolicitudDTO;
import com.mycompany.hospital.service.mapper.CanalSolicitudMapper;
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
 * Integration tests for the {@link CanalSolicitudResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CanalSolicitudResourceIT {

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

    private static final String ENTITY_API_URL = "/api/canal-solicituds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CanalSolicitudRepository canalSolicitudRepository;

    @Autowired
    private CanalSolicitudMapper canalSolicitudMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCanalSolicitudMockMvc;

    private CanalSolicitud canalSolicitud;

    private CanalSolicitud insertedCanalSolicitud;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanalSolicitud createEntity() {
        return new CanalSolicitud()
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
    public static CanalSolicitud createUpdatedEntity() {
        return new CanalSolicitud()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        canalSolicitud = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCanalSolicitud != null) {
            canalSolicitudRepository.delete(insertedCanalSolicitud);
            insertedCanalSolicitud = null;
        }
    }

    @Test
    @Transactional
    void createCanalSolicitud() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);
        var returnedCanalSolicitudDTO = om.readValue(
            restCanalSolicitudMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CanalSolicitudDTO.class
        );

        // Validate the CanalSolicitud in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCanalSolicitud = canalSolicitudMapper.toEntity(returnedCanalSolicitudDTO);
        assertCanalSolicitudUpdatableFieldsEquals(returnedCanalSolicitud, getPersistedCanalSolicitud(returnedCanalSolicitud));

        insertedCanalSolicitud = returnedCanalSolicitud;
    }

    @Test
    @Transactional
    void createCanalSolicitudWithExistingId() throws Exception {
        // Create the CanalSolicitud with an existing ID
        canalSolicitud.setId(1L);
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCanalSolicitudMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalSolicitud.setCodigo(null);

        // Create the CanalSolicitud, which fails.
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        restCanalSolicitudMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalSolicitud.setNombre(null);

        // Create the CanalSolicitud, which fails.
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        restCanalSolicitudMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalSolicitud.setActivo(null);

        // Create the CanalSolicitud, which fails.
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        restCanalSolicitudMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalSolicitud.setFechaAlta(null);

        // Create the CanalSolicitud, which fails.
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        restCanalSolicitudMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCanalSolicituds() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList
        restCanalSolicitudMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canalSolicitud.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getCanalSolicitud() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get the canalSolicitud
        restCanalSolicitudMockMvc
            .perform(get(ENTITY_API_URL_ID, canalSolicitud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(canalSolicitud.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getCanalSolicitudsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        Long id = canalSolicitud.getId();

        defaultCanalSolicitudFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCanalSolicitudFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCanalSolicitudFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where codigo equals to
        defaultCanalSolicitudFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where codigo in
        defaultCanalSolicitudFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where codigo is not null
        defaultCanalSolicitudFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where codigo contains
        defaultCanalSolicitudFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where codigo does not contain
        defaultCanalSolicitudFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where nombre equals to
        defaultCanalSolicitudFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where nombre in
        defaultCanalSolicitudFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where nombre is not null
        defaultCanalSolicitudFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where nombre contains
        defaultCanalSolicitudFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where nombre does not contain
        defaultCanalSolicitudFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where descripcion equals to
        defaultCanalSolicitudFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where descripcion in
        defaultCanalSolicitudFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where descripcion is not null
        defaultCanalSolicitudFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where descripcion contains
        defaultCanalSolicitudFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where descripcion does not contain
        defaultCanalSolicitudFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where activo equals to
        defaultCanalSolicitudFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where activo in
        defaultCanalSolicitudFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where activo is not null
        defaultCanalSolicitudFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta equals to
        defaultCanalSolicitudFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta in
        defaultCanalSolicitudFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta is not null
        defaultCanalSolicitudFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta is greater than or equal to
        defaultCanalSolicitudFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta is less than or equal to
        defaultCanalSolicitudFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta is less than
        defaultCanalSolicitudFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaAlta is greater than
        defaultCanalSolicitudFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja equals to
        defaultCanalSolicitudFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja in
        defaultCanalSolicitudFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja is not null
        defaultCanalSolicitudFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja is greater than or equal to
        defaultCanalSolicitudFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja is less than or equal to
        defaultCanalSolicitudFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja is less than
        defaultCanalSolicitudFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCanalSolicitudsByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        // Get all the canalSolicitudList where fechaBaja is greater than
        defaultCanalSolicitudFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultCanalSolicitudFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCanalSolicitudShouldBeFound(shouldBeFound);
        defaultCanalSolicitudShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCanalSolicitudShouldBeFound(String filter) throws Exception {
        restCanalSolicitudMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canalSolicitud.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restCanalSolicitudMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCanalSolicitudShouldNotBeFound(String filter) throws Exception {
        restCanalSolicitudMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCanalSolicitudMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCanalSolicitud() throws Exception {
        // Get the canalSolicitud
        restCanalSolicitudMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCanalSolicitud() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canalSolicitud
        CanalSolicitud updatedCanalSolicitud = canalSolicitudRepository.findById(canalSolicitud.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCanalSolicitud are not directly saved in db
        em.detach(updatedCanalSolicitud);
        updatedCanalSolicitud
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(updatedCanalSolicitud);

        restCanalSolicitudMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canalSolicitudDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canalSolicitudDTO))
            )
            .andExpect(status().isOk());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCanalSolicitudToMatchAllProperties(updatedCanalSolicitud);
    }

    @Test
    @Transactional
    void putNonExistingCanalSolicitud() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalSolicitud.setId(longCount.incrementAndGet());

        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanalSolicitudMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canalSolicitudDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canalSolicitudDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCanalSolicitud() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalSolicitud.setId(longCount.incrementAndGet());

        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalSolicitudMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canalSolicitudDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCanalSolicitud() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalSolicitud.setId(longCount.incrementAndGet());

        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalSolicitudMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCanalSolicitudWithPatch() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canalSolicitud using partial update
        CanalSolicitud partialUpdatedCanalSolicitud = new CanalSolicitud();
        partialUpdatedCanalSolicitud.setId(canalSolicitud.getId());

        partialUpdatedCanalSolicitud.descripcion(UPDATED_DESCRIPCION).activo(UPDATED_ACTIVO);

        restCanalSolicitudMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanalSolicitud.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanalSolicitud))
            )
            .andExpect(status().isOk());

        // Validate the CanalSolicitud in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanalSolicitudUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCanalSolicitud, canalSolicitud),
            getPersistedCanalSolicitud(canalSolicitud)
        );
    }

    @Test
    @Transactional
    void fullUpdateCanalSolicitudWithPatch() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canalSolicitud using partial update
        CanalSolicitud partialUpdatedCanalSolicitud = new CanalSolicitud();
        partialUpdatedCanalSolicitud.setId(canalSolicitud.getId());

        partialUpdatedCanalSolicitud
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restCanalSolicitudMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanalSolicitud.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanalSolicitud))
            )
            .andExpect(status().isOk());

        // Validate the CanalSolicitud in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanalSolicitudUpdatableFieldsEquals(partialUpdatedCanalSolicitud, getPersistedCanalSolicitud(partialUpdatedCanalSolicitud));
    }

    @Test
    @Transactional
    void patchNonExistingCanalSolicitud() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalSolicitud.setId(longCount.incrementAndGet());

        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanalSolicitudMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, canalSolicitudDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canalSolicitudDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCanalSolicitud() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalSolicitud.setId(longCount.incrementAndGet());

        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalSolicitudMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canalSolicitudDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCanalSolicitud() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalSolicitud.setId(longCount.incrementAndGet());

        // Create the CanalSolicitud
        CanalSolicitudDTO canalSolicitudDTO = canalSolicitudMapper.toDto(canalSolicitud);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalSolicitudMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(canalSolicitudDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanalSolicitud in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCanalSolicitud() throws Exception {
        // Initialize the database
        insertedCanalSolicitud = canalSolicitudRepository.saveAndFlush(canalSolicitud);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the canalSolicitud
        restCanalSolicitudMockMvc
            .perform(delete(ENTITY_API_URL_ID, canalSolicitud.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return canalSolicitudRepository.count();
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

    protected CanalSolicitud getPersistedCanalSolicitud(CanalSolicitud canalSolicitud) {
        return canalSolicitudRepository.findById(canalSolicitud.getId()).orElseThrow();
    }

    protected void assertPersistedCanalSolicitudToMatchAllProperties(CanalSolicitud expectedCanalSolicitud) {
        assertCanalSolicitudAllPropertiesEquals(expectedCanalSolicitud, getPersistedCanalSolicitud(expectedCanalSolicitud));
    }

    protected void assertPersistedCanalSolicitudToMatchUpdatableProperties(CanalSolicitud expectedCanalSolicitud) {
        assertCanalSolicitudAllUpdatablePropertiesEquals(expectedCanalSolicitud, getPersistedCanalSolicitud(expectedCanalSolicitud));
    }
}
