package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.CiudadAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Ciudad;
import com.mycompany.hospital.domain.Provincia;
import com.mycompany.hospital.repository.CiudadRepository;
import com.mycompany.hospital.service.dto.CiudadDTO;
import com.mycompany.hospital.service.mapper.CiudadMapper;
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
 * Integration tests for the {@link CiudadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CiudadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_POSTAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/ciudads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private CiudadMapper ciudadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCiudadMockMvc;

    private Ciudad ciudad;

    private Ciudad insertedCiudad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ciudad createEntity(EntityManager em) {
        Ciudad ciudad = new Ciudad()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .codigoPostal(DEFAULT_CODIGO_POSTAL)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA)
            .activo(DEFAULT_ACTIVO);
        // Add required entity
        Provincia provincia;
        if (TestUtil.findAll(em, Provincia.class).isEmpty()) {
            provincia = ProvinciaResourceIT.createEntity(em);
            em.persist(provincia);
            em.flush();
        } else {
            provincia = TestUtil.findAll(em, Provincia.class).get(0);
        }
        ciudad.setProvincia(provincia);
        return ciudad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ciudad createUpdatedEntity(EntityManager em) {
        Ciudad updatedCiudad = new Ciudad()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        // Add required entity
        Provincia provincia;
        if (TestUtil.findAll(em, Provincia.class).isEmpty()) {
            provincia = ProvinciaResourceIT.createUpdatedEntity(em);
            em.persist(provincia);
            em.flush();
        } else {
            provincia = TestUtil.findAll(em, Provincia.class).get(0);
        }
        updatedCiudad.setProvincia(provincia);
        return updatedCiudad;
    }

    @BeforeEach
    void initTest() {
        ciudad = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCiudad != null) {
            ciudadRepository.delete(insertedCiudad);
            insertedCiudad = null;
        }
    }

    @Test
    @Transactional
    void createCiudad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);
        var returnedCiudadDTO = om.readValue(
            restCiudadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CiudadDTO.class
        );

        // Validate the Ciudad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCiudad = ciudadMapper.toEntity(returnedCiudadDTO);
        assertCiudadUpdatableFieldsEquals(returnedCiudad, getPersistedCiudad(returnedCiudad));

        insertedCiudad = returnedCiudad;
    }

    @Test
    @Transactional
    void createCiudadWithExistingId() throws Exception {
        // Create the Ciudad with an existing ID
        ciudad.setId(1L);
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ciudad.setNombre(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ciudad.setFechaAlta(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ciudad.setActivo(null);

        // Create the Ciudad, which fails.
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        restCiudadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCiudads() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ciudad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getCiudad() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get the ciudad
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL_ID, ciudad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ciudad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.codigoPostal").value(DEFAULT_CODIGO_POSTAL))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getCiudadsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        Long id = ciudad.getId();

        defaultCiudadFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCiudadFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCiudadFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCiudadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where nombre equals to
        defaultCiudadFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCiudadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where nombre in
        defaultCiudadFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCiudadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where nombre is not null
        defaultCiudadFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where nombre contains
        defaultCiudadFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCiudadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where nombre does not contain
        defaultCiudadFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigo equals to
        defaultCiudadFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigo in
        defaultCiudadFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigo is not null
        defaultCiudadFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigo contains
        defaultCiudadFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigo does not contain
        defaultCiudadFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoPostalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigoPostal equals to
        defaultCiudadFiltering("codigoPostal.equals=" + DEFAULT_CODIGO_POSTAL, "codigoPostal.equals=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoPostalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigoPostal in
        defaultCiudadFiltering(
            "codigoPostal.in=" + DEFAULT_CODIGO_POSTAL + "," + UPDATED_CODIGO_POSTAL,
            "codigoPostal.in=" + UPDATED_CODIGO_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoPostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigoPostal is not null
        defaultCiudadFiltering("codigoPostal.specified=true", "codigoPostal.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoPostalContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigoPostal contains
        defaultCiudadFiltering("codigoPostal.contains=" + DEFAULT_CODIGO_POSTAL, "codigoPostal.contains=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    void getAllCiudadsByCodigoPostalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where codigoPostal does not contain
        defaultCiudadFiltering(
            "codigoPostal.doesNotContain=" + UPDATED_CODIGO_POSTAL,
            "codigoPostal.doesNotContain=" + DEFAULT_CODIGO_POSTAL
        );
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta equals to
        defaultCiudadFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta in
        defaultCiudadFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta is not null
        defaultCiudadFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta is greater than or equal to
        defaultCiudadFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta is less than or equal to
        defaultCiudadFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta is less than
        defaultCiudadFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaAlta is greater than
        defaultCiudadFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja equals to
        defaultCiudadFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja in
        defaultCiudadFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja is not null
        defaultCiudadFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja is greater than or equal to
        defaultCiudadFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja is less than or equal to
        defaultCiudadFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja is less than
        defaultCiudadFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCiudadsByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where fechaBaja is greater than
        defaultCiudadFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllCiudadsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where activo equals to
        defaultCiudadFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllCiudadsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where activo in
        defaultCiudadFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllCiudadsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudadList where activo is not null
        defaultCiudadFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllCiudadsByProvinciaIsEqualToSomething() throws Exception {
        Provincia provincia;
        if (TestUtil.findAll(em, Provincia.class).isEmpty()) {
            ciudadRepository.saveAndFlush(ciudad);
            provincia = ProvinciaResourceIT.createEntity(em);
        } else {
            provincia = TestUtil.findAll(em, Provincia.class).get(0);
        }
        em.persist(provincia);
        em.flush();
        ciudad.setProvincia(provincia);
        ciudadRepository.saveAndFlush(ciudad);
        Long provinciaId = provincia.getId();
        // Get all the ciudadList where provincia equals to provinciaId
        defaultCiudadShouldBeFound("provinciaId.equals=" + provinciaId);

        // Get all the ciudadList where provincia equals to (provinciaId + 1)
        defaultCiudadShouldNotBeFound("provinciaId.equals=" + (provinciaId + 1));
    }

    private void defaultCiudadFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCiudadShouldBeFound(shouldBeFound);
        defaultCiudadShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCiudadShouldBeFound(String filter) throws Exception {
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ciudad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCiudadShouldNotBeFound(String filter) throws Exception {
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCiudadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCiudad() throws Exception {
        // Get the ciudad
        restCiudadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCiudad() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ciudad
        Ciudad updatedCiudad = ciudadRepository.findById(ciudad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCiudad are not directly saved in db
        em.detach(updatedCiudad);
        updatedCiudad
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        CiudadDTO ciudadDTO = ciudadMapper.toDto(updatedCiudad);

        restCiudadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ciudadDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCiudadToMatchAllProperties(updatedCiudad);
    }

    @Test
    @Transactional
    void putNonExistingCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ciudadDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCiudadWithPatch() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ciudad using partial update
        Ciudad partialUpdatedCiudad = new Ciudad();
        partialUpdatedCiudad.setId(ciudad.getId());

        partialUpdatedCiudad
            .nombre(UPDATED_NOMBRE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCiudad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCiudad))
            )
            .andExpect(status().isOk());

        // Validate the Ciudad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCiudadUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCiudad, ciudad), getPersistedCiudad(ciudad));
    }

    @Test
    @Transactional
    void fullUpdateCiudadWithPatch() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ciudad using partial update
        Ciudad partialUpdatedCiudad = new Ciudad();
        partialUpdatedCiudad.setId(ciudad.getId());

        partialUpdatedCiudad
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCiudad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCiudad))
            )
            .andExpect(status().isOk());

        // Validate the Ciudad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCiudadUpdatableFieldsEquals(partialUpdatedCiudad, getPersistedCiudad(partialUpdatedCiudad));
    }

    @Test
    @Transactional
    void patchNonExistingCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ciudadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ciudadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCiudad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ciudad.setId(longCount.incrementAndGet());

        // Create the Ciudad
        CiudadDTO ciudadDTO = ciudadMapper.toDto(ciudad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCiudadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ciudadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ciudad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCiudad() throws Exception {
        // Initialize the database
        insertedCiudad = ciudadRepository.saveAndFlush(ciudad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ciudad
        restCiudadMockMvc
            .perform(delete(ENTITY_API_URL_ID, ciudad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ciudadRepository.count();
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

    protected Ciudad getPersistedCiudad(Ciudad ciudad) {
        return ciudadRepository.findById(ciudad.getId()).orElseThrow();
    }

    protected void assertPersistedCiudadToMatchAllProperties(Ciudad expectedCiudad) {
        assertCiudadAllPropertiesEquals(expectedCiudad, getPersistedCiudad(expectedCiudad));
    }

    protected void assertPersistedCiudadToMatchUpdatableProperties(Ciudad expectedCiudad) {
        assertCiudadAllUpdatablePropertiesEquals(expectedCiudad, getPersistedCiudad(expectedCiudad));
    }
}
