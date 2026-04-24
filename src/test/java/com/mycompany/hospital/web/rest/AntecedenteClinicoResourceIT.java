package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.AntecedenteClinicoAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.AntecedenteClinico;
import com.mycompany.hospital.domain.HistoriaClinica;
import com.mycompany.hospital.repository.AntecedenteClinicoRepository;
import com.mycompany.hospital.service.dto.AntecedenteClinicoDTO;
import com.mycompany.hospital.service.mapper.AntecedenteClinicoMapper;
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
 * Integration tests for the {@link AntecedenteClinicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AntecedenteClinicoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_REGISTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_REGISTRO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_REGISTRO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/antecedente-clinicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AntecedenteClinicoRepository antecedenteClinicoRepository;

    @Autowired
    private AntecedenteClinicoMapper antecedenteClinicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAntecedenteClinicoMockMvc;

    private AntecedenteClinico antecedenteClinico;

    private AntecedenteClinico insertedAntecedenteClinico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AntecedenteClinico createEntity(EntityManager em) {
        AntecedenteClinico antecedenteClinico = new AntecedenteClinico()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        HistoriaClinica historiaClinica;
        if (TestUtil.findAll(em, HistoriaClinica.class).isEmpty()) {
            historiaClinica = HistoriaClinicaResourceIT.createEntity(em);
            em.persist(historiaClinica);
            em.flush();
        } else {
            historiaClinica = TestUtil.findAll(em, HistoriaClinica.class).get(0);
        }
        antecedenteClinico.setHistoriaClinica(historiaClinica);
        return antecedenteClinico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AntecedenteClinico createUpdatedEntity(EntityManager em) {
        AntecedenteClinico updatedAntecedenteClinico = new AntecedenteClinico()
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        HistoriaClinica historiaClinica;
        if (TestUtil.findAll(em, HistoriaClinica.class).isEmpty()) {
            historiaClinica = HistoriaClinicaResourceIT.createUpdatedEntity(em);
            em.persist(historiaClinica);
            em.flush();
        } else {
            historiaClinica = TestUtil.findAll(em, HistoriaClinica.class).get(0);
        }
        updatedAntecedenteClinico.setHistoriaClinica(historiaClinica);
        return updatedAntecedenteClinico;
    }

    @BeforeEach
    void initTest() {
        antecedenteClinico = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAntecedenteClinico != null) {
            antecedenteClinicoRepository.delete(insertedAntecedenteClinico);
            insertedAntecedenteClinico = null;
        }
    }

    @Test
    @Transactional
    void createAntecedenteClinico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);
        var returnedAntecedenteClinicoDTO = om.readValue(
            restAntecedenteClinicoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AntecedenteClinicoDTO.class
        );

        // Validate the AntecedenteClinico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAntecedenteClinico = antecedenteClinicoMapper.toEntity(returnedAntecedenteClinicoDTO);
        assertAntecedenteClinicoUpdatableFieldsEquals(
            returnedAntecedenteClinico,
            getPersistedAntecedenteClinico(returnedAntecedenteClinico)
        );

        insertedAntecedenteClinico = returnedAntecedenteClinico;
    }

    @Test
    @Transactional
    void createAntecedenteClinicoWithExistingId() throws Exception {
        // Create the AntecedenteClinico with an existing ID
        antecedenteClinico.setId(1L);
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAntecedenteClinicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antecedenteClinico.setTitulo(null);

        // Create the AntecedenteClinico, which fails.
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        restAntecedenteClinicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antecedenteClinico.setDescripcion(null);

        // Create the AntecedenteClinico, which fails.
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        restAntecedenteClinicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antecedenteClinico.setFechaRegistro(null);

        // Create the AntecedenteClinico, which fails.
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        restAntecedenteClinicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antecedenteClinico.setActivo(null);

        // Create the AntecedenteClinico, which fails.
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        restAntecedenteClinicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        antecedenteClinico.setFechaAlta(null);

        // Create the AntecedenteClinico, which fails.
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        restAntecedenteClinicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicos() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList
        restAntecedenteClinicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antecedenteClinico.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getAntecedenteClinico() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get the antecedenteClinico
        restAntecedenteClinicoMockMvc
            .perform(get(ENTITY_API_URL_ID, antecedenteClinico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(antecedenteClinico.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getAntecedenteClinicosByIdFiltering() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        Long id = antecedenteClinico.getId();

        defaultAntecedenteClinicoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAntecedenteClinicoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAntecedenteClinicoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where titulo equals to
        defaultAntecedenteClinicoFiltering("titulo.equals=" + DEFAULT_TITULO, "titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where titulo in
        defaultAntecedenteClinicoFiltering("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO, "titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where titulo is not null
        defaultAntecedenteClinicoFiltering("titulo.specified=true", "titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByTituloContainsSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where titulo contains
        defaultAntecedenteClinicoFiltering("titulo.contains=" + DEFAULT_TITULO, "titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where titulo does not contain
        defaultAntecedenteClinicoFiltering("titulo.doesNotContain=" + UPDATED_TITULO, "titulo.doesNotContain=" + DEFAULT_TITULO);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where descripcion equals to
        defaultAntecedenteClinicoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where descripcion in
        defaultAntecedenteClinicoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where descripcion is not null
        defaultAntecedenteClinicoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where descripcion contains
        defaultAntecedenteClinicoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where descripcion does not contain
        defaultAntecedenteClinicoFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro equals to
        defaultAntecedenteClinicoFiltering(
            "fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO,
            "fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro in
        defaultAntecedenteClinicoFiltering(
            "fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO,
            "fechaRegistro.in=" + UPDATED_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro is not null
        defaultAntecedenteClinicoFiltering("fechaRegistro.specified=true", "fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro is greater than or equal to
        defaultAntecedenteClinicoFiltering(
            "fechaRegistro.greaterThanOrEqual=" + DEFAULT_FECHA_REGISTRO,
            "fechaRegistro.greaterThanOrEqual=" + UPDATED_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro is less than or equal to
        defaultAntecedenteClinicoFiltering(
            "fechaRegistro.lessThanOrEqual=" + DEFAULT_FECHA_REGISTRO,
            "fechaRegistro.lessThanOrEqual=" + SMALLER_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro is less than
        defaultAntecedenteClinicoFiltering(
            "fechaRegistro.lessThan=" + UPDATED_FECHA_REGISTRO,
            "fechaRegistro.lessThan=" + DEFAULT_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaRegistro is greater than
        defaultAntecedenteClinicoFiltering(
            "fechaRegistro.greaterThan=" + SMALLER_FECHA_REGISTRO,
            "fechaRegistro.greaterThan=" + DEFAULT_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where activo equals to
        defaultAntecedenteClinicoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where activo in
        defaultAntecedenteClinicoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where activo is not null
        defaultAntecedenteClinicoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta equals to
        defaultAntecedenteClinicoFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta in
        defaultAntecedenteClinicoFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta is not null
        defaultAntecedenteClinicoFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta is greater than or equal to
        defaultAntecedenteClinicoFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta is less than or equal to
        defaultAntecedenteClinicoFiltering(
            "fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta is less than
        defaultAntecedenteClinicoFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaAlta is greater than
        defaultAntecedenteClinicoFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja equals to
        defaultAntecedenteClinicoFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja in
        defaultAntecedenteClinicoFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja is not null
        defaultAntecedenteClinicoFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja is greater than or equal to
        defaultAntecedenteClinicoFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja is less than or equal to
        defaultAntecedenteClinicoFiltering(
            "fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja is less than
        defaultAntecedenteClinicoFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        // Get all the antecedenteClinicoList where fechaBaja is greater than
        defaultAntecedenteClinicoFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllAntecedenteClinicosByHistoriaClinicaIsEqualToSomething() throws Exception {
        HistoriaClinica historiaClinica;
        if (TestUtil.findAll(em, HistoriaClinica.class).isEmpty()) {
            antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);
            historiaClinica = HistoriaClinicaResourceIT.createEntity(em);
        } else {
            historiaClinica = TestUtil.findAll(em, HistoriaClinica.class).get(0);
        }
        em.persist(historiaClinica);
        em.flush();
        antecedenteClinico.setHistoriaClinica(historiaClinica);
        antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);
        Long historiaClinicaId = historiaClinica.getId();
        // Get all the antecedenteClinicoList where historiaClinica equals to historiaClinicaId
        defaultAntecedenteClinicoShouldBeFound("historiaClinicaId.equals=" + historiaClinicaId);

        // Get all the antecedenteClinicoList where historiaClinica equals to (historiaClinicaId + 1)
        defaultAntecedenteClinicoShouldNotBeFound("historiaClinicaId.equals=" + (historiaClinicaId + 1));
    }

    private void defaultAntecedenteClinicoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAntecedenteClinicoShouldBeFound(shouldBeFound);
        defaultAntecedenteClinicoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAntecedenteClinicoShouldBeFound(String filter) throws Exception {
        restAntecedenteClinicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(antecedenteClinico.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restAntecedenteClinicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAntecedenteClinicoShouldNotBeFound(String filter) throws Exception {
        restAntecedenteClinicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAntecedenteClinicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAntecedenteClinico() throws Exception {
        // Get the antecedenteClinico
        restAntecedenteClinicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAntecedenteClinico() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antecedenteClinico
        AntecedenteClinico updatedAntecedenteClinico = antecedenteClinicoRepository.findById(antecedenteClinico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAntecedenteClinico are not directly saved in db
        em.detach(updatedAntecedenteClinico);
        updatedAntecedenteClinico
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(updatedAntecedenteClinico);

        restAntecedenteClinicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antecedenteClinicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antecedenteClinicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAntecedenteClinicoToMatchAllProperties(updatedAntecedenteClinico);
    }

    @Test
    @Transactional
    void putNonExistingAntecedenteClinico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antecedenteClinico.setId(longCount.incrementAndGet());

        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntecedenteClinicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, antecedenteClinicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antecedenteClinicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAntecedenteClinico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antecedenteClinico.setId(longCount.incrementAndGet());

        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedenteClinicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(antecedenteClinicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAntecedenteClinico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antecedenteClinico.setId(longCount.incrementAndGet());

        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedenteClinicoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAntecedenteClinicoWithPatch() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antecedenteClinico using partial update
        AntecedenteClinico partialUpdatedAntecedenteClinico = new AntecedenteClinico();
        partialUpdatedAntecedenteClinico.setId(antecedenteClinico.getId());

        partialUpdatedAntecedenteClinico
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restAntecedenteClinicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntecedenteClinico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAntecedenteClinico))
            )
            .andExpect(status().isOk());

        // Validate the AntecedenteClinico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAntecedenteClinicoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAntecedenteClinico, antecedenteClinico),
            getPersistedAntecedenteClinico(antecedenteClinico)
        );
    }

    @Test
    @Transactional
    void fullUpdateAntecedenteClinicoWithPatch() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the antecedenteClinico using partial update
        AntecedenteClinico partialUpdatedAntecedenteClinico = new AntecedenteClinico();
        partialUpdatedAntecedenteClinico.setId(antecedenteClinico.getId());

        partialUpdatedAntecedenteClinico
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restAntecedenteClinicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAntecedenteClinico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAntecedenteClinico))
            )
            .andExpect(status().isOk());

        // Validate the AntecedenteClinico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAntecedenteClinicoUpdatableFieldsEquals(
            partialUpdatedAntecedenteClinico,
            getPersistedAntecedenteClinico(partialUpdatedAntecedenteClinico)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAntecedenteClinico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antecedenteClinico.setId(longCount.incrementAndGet());

        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAntecedenteClinicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, antecedenteClinicoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(antecedenteClinicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAntecedenteClinico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antecedenteClinico.setId(longCount.incrementAndGet());

        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedenteClinicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(antecedenteClinicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAntecedenteClinico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        antecedenteClinico.setId(longCount.incrementAndGet());

        // Create the AntecedenteClinico
        AntecedenteClinicoDTO antecedenteClinicoDTO = antecedenteClinicoMapper.toDto(antecedenteClinico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAntecedenteClinicoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(antecedenteClinicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AntecedenteClinico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAntecedenteClinico() throws Exception {
        // Initialize the database
        insertedAntecedenteClinico = antecedenteClinicoRepository.saveAndFlush(antecedenteClinico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the antecedenteClinico
        restAntecedenteClinicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, antecedenteClinico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return antecedenteClinicoRepository.count();
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

    protected AntecedenteClinico getPersistedAntecedenteClinico(AntecedenteClinico antecedenteClinico) {
        return antecedenteClinicoRepository.findById(antecedenteClinico.getId()).orElseThrow();
    }

    protected void assertPersistedAntecedenteClinicoToMatchAllProperties(AntecedenteClinico expectedAntecedenteClinico) {
        assertAntecedenteClinicoAllPropertiesEquals(expectedAntecedenteClinico, getPersistedAntecedenteClinico(expectedAntecedenteClinico));
    }

    protected void assertPersistedAntecedenteClinicoToMatchUpdatableProperties(AntecedenteClinico expectedAntecedenteClinico) {
        assertAntecedenteClinicoAllUpdatablePropertiesEquals(
            expectedAntecedenteClinico,
            getPersistedAntecedenteClinico(expectedAntecedenteClinico)
        );
    }
}
