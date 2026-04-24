package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.TipoEmpleadoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.TipoEmpleado;
import com.mycompany.hospital.repository.TipoEmpleadoRepository;
import com.mycompany.hospital.service.dto.TipoEmpleadoDTO;
import com.mycompany.hospital.service.mapper.TipoEmpleadoMapper;
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
 * Integration tests for the {@link TipoEmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoEmpleadoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tipo-empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoEmpleadoRepository tipoEmpleadoRepository;

    @Autowired
    private TipoEmpleadoMapper tipoEmpleadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoEmpleadoMockMvc;

    private TipoEmpleado tipoEmpleado;

    private TipoEmpleado insertedTipoEmpleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoEmpleado createEntity() {
        return new TipoEmpleado()
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
    public static TipoEmpleado createUpdatedEntity() {
        return new TipoEmpleado()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        tipoEmpleado = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoEmpleado != null) {
            tipoEmpleadoRepository.delete(insertedTipoEmpleado);
            insertedTipoEmpleado = null;
        }
    }

    @Test
    @Transactional
    void createTipoEmpleado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);
        var returnedTipoEmpleadoDTO = om.readValue(
            restTipoEmpleadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoEmpleadoDTO.class
        );

        // Validate the TipoEmpleado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoEmpleado = tipoEmpleadoMapper.toEntity(returnedTipoEmpleadoDTO);
        assertTipoEmpleadoUpdatableFieldsEquals(returnedTipoEmpleado, getPersistedTipoEmpleado(returnedTipoEmpleado));

        insertedTipoEmpleado = returnedTipoEmpleado;
    }

    @Test
    @Transactional
    void createTipoEmpleadoWithExistingId() throws Exception {
        // Create the TipoEmpleado with an existing ID
        tipoEmpleado.setId(1L);
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoEmpleado.setCodigo(null);

        // Create the TipoEmpleado, which fails.
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        restTipoEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoEmpleado.setNombre(null);

        // Create the TipoEmpleado, which fails.
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        restTipoEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoEmpleado.setFechaAlta(null);

        // Create the TipoEmpleado, which fails.
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        restTipoEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoEmpleado.setActivo(null);

        // Create the TipoEmpleado, which fails.
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        restTipoEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoEmpleados() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList
        restTipoEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoEmpleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getTipoEmpleado() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get the tipoEmpleado
        restTipoEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoEmpleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoEmpleado.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getTipoEmpleadosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        Long id = tipoEmpleado.getId();

        defaultTipoEmpleadoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTipoEmpleadoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTipoEmpleadoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where codigo equals to
        defaultTipoEmpleadoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where codigo in
        defaultTipoEmpleadoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where codigo is not null
        defaultTipoEmpleadoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where codigo contains
        defaultTipoEmpleadoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where codigo does not contain
        defaultTipoEmpleadoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where nombre equals to
        defaultTipoEmpleadoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where nombre in
        defaultTipoEmpleadoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where nombre is not null
        defaultTipoEmpleadoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where nombre contains
        defaultTipoEmpleadoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where nombre does not contain
        defaultTipoEmpleadoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where descripcion equals to
        defaultTipoEmpleadoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where descripcion in
        defaultTipoEmpleadoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where descripcion is not null
        defaultTipoEmpleadoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where descripcion contains
        defaultTipoEmpleadoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where descripcion does not contain
        defaultTipoEmpleadoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta equals to
        defaultTipoEmpleadoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta in
        defaultTipoEmpleadoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta is not null
        defaultTipoEmpleadoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta is greater than or equal to
        defaultTipoEmpleadoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta is less than or equal to
        defaultTipoEmpleadoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta is less than
        defaultTipoEmpleadoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaAlta is greater than
        defaultTipoEmpleadoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja equals to
        defaultTipoEmpleadoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja in
        defaultTipoEmpleadoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja is not null
        defaultTipoEmpleadoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja is greater than or equal to
        defaultTipoEmpleadoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja is less than or equal to
        defaultTipoEmpleadoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja is less than
        defaultTipoEmpleadoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where fechaBaja is greater than
        defaultTipoEmpleadoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where activo equals to
        defaultTipoEmpleadoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where activo in
        defaultTipoEmpleadoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTipoEmpleadosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        // Get all the tipoEmpleadoList where activo is not null
        defaultTipoEmpleadoFiltering("activo.specified=true", "activo.specified=false");
    }

    private void defaultTipoEmpleadoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTipoEmpleadoShouldBeFound(shouldBeFound);
        defaultTipoEmpleadoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoEmpleadoShouldBeFound(String filter) throws Exception {
        restTipoEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoEmpleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restTipoEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoEmpleadoShouldNotBeFound(String filter) throws Exception {
        restTipoEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTipoEmpleado() throws Exception {
        // Get the tipoEmpleado
        restTipoEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoEmpleado() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoEmpleado
        TipoEmpleado updatedTipoEmpleado = tipoEmpleadoRepository.findById(tipoEmpleado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoEmpleado are not directly saved in db
        em.detach(updatedTipoEmpleado);
        updatedTipoEmpleado
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(updatedTipoEmpleado);

        restTipoEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoEmpleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoEmpleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoEmpleadoToMatchAllProperties(updatedTipoEmpleado);
    }

    @Test
    @Transactional
    void putNonExistingTipoEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEmpleado.setId(longCount.incrementAndGet());

        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoEmpleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEmpleado.setId(longCount.incrementAndGet());

        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEmpleado.setId(longCount.incrementAndGet());

        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEmpleadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoEmpleado using partial update
        TipoEmpleado partialUpdatedTipoEmpleado = new TipoEmpleado();
        partialUpdatedTipoEmpleado.setId(tipoEmpleado.getId());

        partialUpdatedTipoEmpleado.codigo(UPDATED_CODIGO).nombre(UPDATED_NOMBRE).fechaBaja(UPDATED_FECHA_BAJA).activo(UPDATED_ACTIVO);

        restTipoEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the TipoEmpleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoEmpleadoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoEmpleado, tipoEmpleado),
            getPersistedTipoEmpleado(tipoEmpleado)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoEmpleado using partial update
        TipoEmpleado partialUpdatedTipoEmpleado = new TipoEmpleado();
        partialUpdatedTipoEmpleado.setId(tipoEmpleado.getId());

        partialUpdatedTipoEmpleado
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restTipoEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the TipoEmpleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoEmpleadoUpdatableFieldsEquals(partialUpdatedTipoEmpleado, getPersistedTipoEmpleado(partialUpdatedTipoEmpleado));
    }

    @Test
    @Transactional
    void patchNonExistingTipoEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEmpleado.setId(longCount.incrementAndGet());

        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoEmpleadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEmpleado.setId(longCount.incrementAndGet());

        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoEmpleado.setId(longCount.incrementAndGet());

        // Create the TipoEmpleado
        TipoEmpleadoDTO tipoEmpleadoDTO = tipoEmpleadoMapper.toDto(tipoEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoEmpleadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoEmpleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoEmpleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoEmpleado() throws Exception {
        // Initialize the database
        insertedTipoEmpleado = tipoEmpleadoRepository.saveAndFlush(tipoEmpleado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoEmpleado
        restTipoEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoEmpleado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoEmpleadoRepository.count();
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

    protected TipoEmpleado getPersistedTipoEmpleado(TipoEmpleado tipoEmpleado) {
        return tipoEmpleadoRepository.findById(tipoEmpleado.getId()).orElseThrow();
    }

    protected void assertPersistedTipoEmpleadoToMatchAllProperties(TipoEmpleado expectedTipoEmpleado) {
        assertTipoEmpleadoAllPropertiesEquals(expectedTipoEmpleado, getPersistedTipoEmpleado(expectedTipoEmpleado));
    }

    protected void assertPersistedTipoEmpleadoToMatchUpdatableProperties(TipoEmpleado expectedTipoEmpleado) {
        assertTipoEmpleadoAllUpdatablePropertiesEquals(expectedTipoEmpleado, getPersistedTipoEmpleado(expectedTipoEmpleado));
    }
}
