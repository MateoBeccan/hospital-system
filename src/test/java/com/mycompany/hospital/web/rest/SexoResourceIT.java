package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.SexoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Sexo;
import com.mycompany.hospital.repository.SexoRepository;
import com.mycompany.hospital.service.dto.SexoDTO;
import com.mycompany.hospital.service.mapper.SexoMapper;
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
 * Integration tests for the {@link SexoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SexoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/sexos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SexoRepository sexoRepository;

    @Autowired
    private SexoMapper sexoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSexoMockMvc;

    private Sexo sexo;

    private Sexo insertedSexo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sexo createEntity() {
        return new Sexo()
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
    public static Sexo createUpdatedEntity() {
        return new Sexo()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        sexo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSexo != null) {
            sexoRepository.delete(insertedSexo);
            insertedSexo = null;
        }
    }

    @Test
    @Transactional
    void createSexo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);
        var returnedSexoDTO = om.readValue(
            restSexoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SexoDTO.class
        );

        // Validate the Sexo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSexo = sexoMapper.toEntity(returnedSexoDTO);
        assertSexoUpdatableFieldsEquals(returnedSexo, getPersistedSexo(returnedSexo));

        insertedSexo = returnedSexo;
    }

    @Test
    @Transactional
    void createSexoWithExistingId() throws Exception {
        // Create the Sexo with an existing ID
        sexo.setId(1L);
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSexoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sexo.setCodigo(null);

        // Create the Sexo, which fails.
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        restSexoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sexo.setNombre(null);

        // Create the Sexo, which fails.
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        restSexoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sexo.setFechaAlta(null);

        // Create the Sexo, which fails.
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        restSexoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sexo.setActivo(null);

        // Create the Sexo, which fails.
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        restSexoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSexos() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList
        restSexoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sexo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getSexo() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get the sexo
        restSexoMockMvc
            .perform(get(ENTITY_API_URL_ID, sexo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sexo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getSexosByIdFiltering() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        Long id = sexo.getId();

        defaultSexoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSexoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSexoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSexosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where codigo equals to
        defaultSexoFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllSexosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where codigo in
        defaultSexoFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllSexosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where codigo is not null
        defaultSexoFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllSexosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where codigo contains
        defaultSexoFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllSexosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where codigo does not contain
        defaultSexoFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllSexosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where nombre equals to
        defaultSexoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSexosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where nombre in
        defaultSexoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSexosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where nombre is not null
        defaultSexoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllSexosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where nombre contains
        defaultSexoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSexosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where nombre does not contain
        defaultSexoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllSexosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where descripcion equals to
        defaultSexoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllSexosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where descripcion in
        defaultSexoFiltering("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION, "descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllSexosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where descripcion is not null
        defaultSexoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllSexosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where descripcion contains
        defaultSexoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllSexosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where descripcion does not contain
        defaultSexoFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta equals to
        defaultSexoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta in
        defaultSexoFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta is not null
        defaultSexoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta is greater than or equal to
        defaultSexoFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta is less than or equal to
        defaultSexoFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta is less than
        defaultSexoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaAlta is greater than
        defaultSexoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja equals to
        defaultSexoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja in
        defaultSexoFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja is not null
        defaultSexoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja is greater than or equal to
        defaultSexoFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja is less than or equal to
        defaultSexoFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja is less than
        defaultSexoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSexosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where fechaBaja is greater than
        defaultSexoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSexosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where activo equals to
        defaultSexoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllSexosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where activo in
        defaultSexoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllSexosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        // Get all the sexoList where activo is not null
        defaultSexoFiltering("activo.specified=true", "activo.specified=false");
    }

    private void defaultSexoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSexoShouldBeFound(shouldBeFound);
        defaultSexoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSexoShouldBeFound(String filter) throws Exception {
        restSexoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sexo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restSexoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSexoShouldNotBeFound(String filter) throws Exception {
        restSexoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSexoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSexo() throws Exception {
        // Get the sexo
        restSexoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSexo() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sexo
        Sexo updatedSexo = sexoRepository.findById(sexo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSexo are not directly saved in db
        em.detach(updatedSexo);
        updatedSexo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        SexoDTO sexoDTO = sexoMapper.toDto(updatedSexo);

        restSexoMockMvc
            .perform(put(ENTITY_API_URL_ID, sexoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isOk());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSexoToMatchAllProperties(updatedSexo);
    }

    @Test
    @Transactional
    void putNonExistingSexo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sexo.setId(longCount.incrementAndGet());

        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSexoMockMvc
            .perform(put(ENTITY_API_URL_ID, sexoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSexo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sexo.setId(longCount.incrementAndGet());

        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSexoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sexoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSexo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sexo.setId(longCount.incrementAndGet());

        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSexoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSexoWithPatch() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sexo using partial update
        Sexo partialUpdatedSexo = new Sexo();
        partialUpdatedSexo.setId(sexo.getId());

        restSexoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSexo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSexo))
            )
            .andExpect(status().isOk());

        // Validate the Sexo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSexoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSexo, sexo), getPersistedSexo(sexo));
    }

    @Test
    @Transactional
    void fullUpdateSexoWithPatch() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sexo using partial update
        Sexo partialUpdatedSexo = new Sexo();
        partialUpdatedSexo.setId(sexo.getId());

        partialUpdatedSexo
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restSexoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSexo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSexo))
            )
            .andExpect(status().isOk());

        // Validate the Sexo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSexoUpdatableFieldsEquals(partialUpdatedSexo, getPersistedSexo(partialUpdatedSexo));
    }

    @Test
    @Transactional
    void patchNonExistingSexo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sexo.setId(longCount.incrementAndGet());

        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSexoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sexoDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sexoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSexo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sexo.setId(longCount.incrementAndGet());

        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSexoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sexoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSexo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sexo.setId(longCount.incrementAndGet());

        // Create the Sexo
        SexoDTO sexoDTO = sexoMapper.toDto(sexo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSexoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sexoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sexo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSexo() throws Exception {
        // Initialize the database
        insertedSexo = sexoRepository.saveAndFlush(sexo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sexo
        restSexoMockMvc
            .perform(delete(ENTITY_API_URL_ID, sexo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sexoRepository.count();
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

    protected Sexo getPersistedSexo(Sexo sexo) {
        return sexoRepository.findById(sexo.getId()).orElseThrow();
    }

    protected void assertPersistedSexoToMatchAllProperties(Sexo expectedSexo) {
        assertSexoAllPropertiesEquals(expectedSexo, getPersistedSexo(expectedSexo));
    }

    protected void assertPersistedSexoToMatchUpdatableProperties(Sexo expectedSexo) {
        assertSexoAllUpdatablePropertiesEquals(expectedSexo, getPersistedSexo(expectedSexo));
    }
}
