package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.EstadoTurnoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.EstadoTurno;
import com.mycompany.hospital.repository.EstadoTurnoRepository;
import com.mycompany.hospital.service.dto.EstadoTurnoDTO;
import com.mycompany.hospital.service.mapper.EstadoTurnoMapper;
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
 * Integration tests for the {@link EstadoTurnoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstadoTurnoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/estado-turnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstadoTurnoRepository estadoTurnoRepository;

    @Autowired
    private EstadoTurnoMapper estadoTurnoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstadoTurnoMockMvc;

    private EstadoTurno estadoTurno;

    private EstadoTurno insertedEstadoTurno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoTurno createEntity() {
        return new EstadoTurno()
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
    public static EstadoTurno createUpdatedEntity() {
        return new EstadoTurno()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        estadoTurno = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEstadoTurno != null) {
            estadoTurnoRepository.delete(insertedEstadoTurno);
            insertedEstadoTurno = null;
        }
    }

    @Test
    @Transactional
    void createEstadoTurno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);
        var returnedEstadoTurnoDTO = om.readValue(
            restEstadoTurnoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EstadoTurnoDTO.class
        );

        // Validate the EstadoTurno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstadoTurno = estadoTurnoMapper.toEntity(returnedEstadoTurnoDTO);
        assertEstadoTurnoUpdatableFieldsEquals(returnedEstadoTurno, getPersistedEstadoTurno(returnedEstadoTurno));

        insertedEstadoTurno = returnedEstadoTurno;
    }

    @Test
    @Transactional
    void createEstadoTurnoWithExistingId() throws Exception {
        // Create the EstadoTurno with an existing ID
        estadoTurno.setId(1L);
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTurno.setCodigo(null);

        // Create the EstadoTurno, which fails.
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        restEstadoTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTurno.setNombre(null);

        // Create the EstadoTurno, which fails.
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        restEstadoTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTurno.setActivo(null);

        // Create the EstadoTurno, which fails.
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        restEstadoTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estadoTurno.setFechaAlta(null);

        // Create the EstadoTurno, which fails.
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        restEstadoTurnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstadoTurnos() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList
        restEstadoTurnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoTurno.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getEstadoTurno() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get the estadoTurno
        restEstadoTurnoMockMvc
            .perform(get(ENTITY_API_URL_ID, estadoTurno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estadoTurno.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getEstadoTurnosByIdFiltering() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        Long id = estadoTurno.getId();

        defaultEstadoTurnoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEstadoTurnoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEstadoTurnoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where codigo equals to
        defaultEstadoTurnoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where codigo in
        defaultEstadoTurnoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where codigo is not null
        defaultEstadoTurnoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where codigo contains
        defaultEstadoTurnoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where codigo does not contain
        defaultEstadoTurnoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where nombre equals to
        defaultEstadoTurnoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where nombre in
        defaultEstadoTurnoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where nombre is not null
        defaultEstadoTurnoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where nombre contains
        defaultEstadoTurnoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where nombre does not contain
        defaultEstadoTurnoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where descripcion equals to
        defaultEstadoTurnoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where descripcion in
        defaultEstadoTurnoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where descripcion is not null
        defaultEstadoTurnoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where descripcion contains
        defaultEstadoTurnoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where descripcion does not contain
        defaultEstadoTurnoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where activo equals to
        defaultEstadoTurnoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where activo in
        defaultEstadoTurnoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where activo is not null
        defaultEstadoTurnoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta equals to
        defaultEstadoTurnoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta in
        defaultEstadoTurnoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta is not null
        defaultEstadoTurnoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta is greater than or equal to
        defaultEstadoTurnoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta is less than or equal to
        defaultEstadoTurnoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta is less than
        defaultEstadoTurnoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaAlta is greater than
        defaultEstadoTurnoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja equals to
        defaultEstadoTurnoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja in
        defaultEstadoTurnoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja is not null
        defaultEstadoTurnoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja is greater than or equal to
        defaultEstadoTurnoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja is less than or equal to
        defaultEstadoTurnoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja is less than
        defaultEstadoTurnoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllEstadoTurnosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        // Get all the estadoTurnoList where fechaBaja is greater than
        defaultEstadoTurnoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultEstadoTurnoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEstadoTurnoShouldBeFound(shouldBeFound);
        defaultEstadoTurnoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstadoTurnoShouldBeFound(String filter) throws Exception {
        restEstadoTurnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoTurno.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restEstadoTurnoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstadoTurnoShouldNotBeFound(String filter) throws Exception {
        restEstadoTurnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstadoTurnoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEstadoTurno() throws Exception {
        // Get the estadoTurno
        restEstadoTurnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstadoTurno() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoTurno
        EstadoTurno updatedEstadoTurno = estadoTurnoRepository.findById(estadoTurno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstadoTurno are not directly saved in db
        em.detach(updatedEstadoTurno);
        updatedEstadoTurno
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(updatedEstadoTurno);

        restEstadoTurnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoTurnoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoTurnoDTO))
            )
            .andExpect(status().isOk());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstadoTurnoToMatchAllProperties(updatedEstadoTurno);
    }

    @Test
    @Transactional
    void putNonExistingEstadoTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTurno.setId(longCount.incrementAndGet());

        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoTurnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estadoTurnoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoTurnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstadoTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTurno.setId(longCount.incrementAndGet());

        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTurnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estadoTurnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstadoTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTurno.setId(longCount.incrementAndGet());

        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTurnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstadoTurnoWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoTurno using partial update
        EstadoTurno partialUpdatedEstadoTurno = new EstadoTurno();
        partialUpdatedEstadoTurno.setId(estadoTurno.getId());

        partialUpdatedEstadoTurno.descripcion(UPDATED_DESCRIPCION).fechaAlta(UPDATED_FECHA_ALTA);

        restEstadoTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoTurno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoTurno))
            )
            .andExpect(status().isOk());

        // Validate the EstadoTurno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoTurnoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEstadoTurno, estadoTurno),
            getPersistedEstadoTurno(estadoTurno)
        );
    }

    @Test
    @Transactional
    void fullUpdateEstadoTurnoWithPatch() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estadoTurno using partial update
        EstadoTurno partialUpdatedEstadoTurno = new EstadoTurno();
        partialUpdatedEstadoTurno.setId(estadoTurno.getId());

        partialUpdatedEstadoTurno
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restEstadoTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstadoTurno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstadoTurno))
            )
            .andExpect(status().isOk());

        // Validate the EstadoTurno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstadoTurnoUpdatableFieldsEquals(partialUpdatedEstadoTurno, getPersistedEstadoTurno(partialUpdatedEstadoTurno));
    }

    @Test
    @Transactional
    void patchNonExistingEstadoTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTurno.setId(longCount.incrementAndGet());

        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estadoTurnoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoTurnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstadoTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTurno.setId(longCount.incrementAndGet());

        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTurnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estadoTurnoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstadoTurno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estadoTurno.setId(longCount.incrementAndGet());

        // Create the EstadoTurno
        EstadoTurnoDTO estadoTurnoDTO = estadoTurnoMapper.toDto(estadoTurno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstadoTurnoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estadoTurnoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EstadoTurno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstadoTurno() throws Exception {
        // Initialize the database
        insertedEstadoTurno = estadoTurnoRepository.saveAndFlush(estadoTurno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estadoTurno
        restEstadoTurnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, estadoTurno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estadoTurnoRepository.count();
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

    protected EstadoTurno getPersistedEstadoTurno(EstadoTurno estadoTurno) {
        return estadoTurnoRepository.findById(estadoTurno.getId()).orElseThrow();
    }

    protected void assertPersistedEstadoTurnoToMatchAllProperties(EstadoTurno expectedEstadoTurno) {
        assertEstadoTurnoAllPropertiesEquals(expectedEstadoTurno, getPersistedEstadoTurno(expectedEstadoTurno));
    }

    protected void assertPersistedEstadoTurnoToMatchUpdatableProperties(EstadoTurno expectedEstadoTurno) {
        assertEstadoTurnoAllUpdatablePropertiesEquals(expectedEstadoTurno, getPersistedEstadoTurno(expectedEstadoTurno));
    }
}
