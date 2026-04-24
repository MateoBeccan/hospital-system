package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.ObraSocialAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.ObraSocial;
import com.mycompany.hospital.repository.ObraSocialRepository;
import com.mycompany.hospital.service.dto.ObraSocialDTO;
import com.mycompany.hospital.service.mapper.ObraSocialMapper;
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
 * Integration tests for the {@link ObraSocialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObraSocialResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "408@PQ.1O]4";
    private static final String UPDATED_EMAIL = "M@&Wcx8.YJ1";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/obra-socials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ObraSocialRepository obraSocialRepository;

    @Autowired
    private ObraSocialMapper obraSocialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObraSocialMockMvc;

    private ObraSocial obraSocial;

    private ObraSocial insertedObraSocial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ObraSocial createEntity() {
        return new ObraSocial()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .direccion(DEFAULT_DIRECCION)
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
    public static ObraSocial createUpdatedEntity() {
        return new ObraSocial()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
    }

    @BeforeEach
    void initTest() {
        obraSocial = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedObraSocial != null) {
            obraSocialRepository.delete(insertedObraSocial);
            insertedObraSocial = null;
        }
    }

    @Test
    @Transactional
    void createObraSocial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);
        var returnedObraSocialDTO = om.readValue(
            restObraSocialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ObraSocialDTO.class
        );

        // Validate the ObraSocial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedObraSocial = obraSocialMapper.toEntity(returnedObraSocialDTO);
        assertObraSocialUpdatableFieldsEquals(returnedObraSocial, getPersistedObraSocial(returnedObraSocial));

        insertedObraSocial = returnedObraSocial;
    }

    @Test
    @Transactional
    void createObraSocialWithExistingId() throws Exception {
        // Create the ObraSocial with an existing ID
        obraSocial.setId(1L);
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObraSocialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        obraSocial.setCodigo(null);

        // Create the ObraSocial, which fails.
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        restObraSocialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        obraSocial.setNombre(null);

        // Create the ObraSocial, which fails.
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        restObraSocialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        obraSocial.setActivo(null);

        // Create the ObraSocial, which fails.
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        restObraSocialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        obraSocial.setFechaAlta(null);

        // Create the ObraSocial, which fails.
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        restObraSocialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllObraSocials() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList
        restObraSocialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(obraSocial.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getObraSocial() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get the obraSocial
        restObraSocialMockMvc
            .perform(get(ENTITY_API_URL_ID, obraSocial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(obraSocial.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getObraSocialsByIdFiltering() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        Long id = obraSocial.getId();

        defaultObraSocialFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultObraSocialFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultObraSocialFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllObraSocialsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where codigo equals to
        defaultObraSocialFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where codigo in
        defaultObraSocialFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where codigo is not null
        defaultObraSocialFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where codigo contains
        defaultObraSocialFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where codigo does not contain
        defaultObraSocialFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where nombre equals to
        defaultObraSocialFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllObraSocialsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where nombre in
        defaultObraSocialFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllObraSocialsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where nombre is not null
        defaultObraSocialFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where nombre contains
        defaultObraSocialFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllObraSocialsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where nombre does not contain
        defaultObraSocialFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllObraSocialsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where telefono equals to
        defaultObraSocialFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where telefono in
        defaultObraSocialFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where telefono is not null
        defaultObraSocialFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where telefono contains
        defaultObraSocialFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where telefono does not contain
        defaultObraSocialFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where email equals to
        defaultObraSocialFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllObraSocialsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where email in
        defaultObraSocialFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllObraSocialsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where email is not null
        defaultObraSocialFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where email contains
        defaultObraSocialFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllObraSocialsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where email does not contain
        defaultObraSocialFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllObraSocialsByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where direccion equals to
        defaultObraSocialFiltering("direccion.equals=" + DEFAULT_DIRECCION, "direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllObraSocialsByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where direccion in
        defaultObraSocialFiltering("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION, "direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllObraSocialsByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where direccion is not null
        defaultObraSocialFiltering("direccion.specified=true", "direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where direccion contains
        defaultObraSocialFiltering("direccion.contains=" + DEFAULT_DIRECCION, "direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllObraSocialsByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where direccion does not contain
        defaultObraSocialFiltering("direccion.doesNotContain=" + UPDATED_DIRECCION, "direccion.doesNotContain=" + DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void getAllObraSocialsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where activo equals to
        defaultObraSocialFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where activo in
        defaultObraSocialFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllObraSocialsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where activo is not null
        defaultObraSocialFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta equals to
        defaultObraSocialFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta in
        defaultObraSocialFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta is not null
        defaultObraSocialFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta is greater than or equal to
        defaultObraSocialFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta is less than or equal to
        defaultObraSocialFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta is less than
        defaultObraSocialFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaAlta is greater than
        defaultObraSocialFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja equals to
        defaultObraSocialFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja in
        defaultObraSocialFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja is not null
        defaultObraSocialFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja is greater than or equal to
        defaultObraSocialFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja is less than or equal to
        defaultObraSocialFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja is less than
        defaultObraSocialFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllObraSocialsByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        // Get all the obraSocialList where fechaBaja is greater than
        defaultObraSocialFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    private void defaultObraSocialFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultObraSocialShouldBeFound(shouldBeFound);
        defaultObraSocialShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultObraSocialShouldBeFound(String filter) throws Exception {
        restObraSocialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(obraSocial.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restObraSocialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultObraSocialShouldNotBeFound(String filter) throws Exception {
        restObraSocialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restObraSocialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingObraSocial() throws Exception {
        // Get the obraSocial
        restObraSocialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingObraSocial() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the obraSocial
        ObraSocial updatedObraSocial = obraSocialRepository.findById(obraSocial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedObraSocial are not directly saved in db
        em.detach(updatedObraSocial);
        updatedObraSocial
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(updatedObraSocial);

        restObraSocialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, obraSocialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(obraSocialDTO))
            )
            .andExpect(status().isOk());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedObraSocialToMatchAllProperties(updatedObraSocial);
    }

    @Test
    @Transactional
    void putNonExistingObraSocial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obraSocial.setId(longCount.incrementAndGet());

        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObraSocialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, obraSocialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(obraSocialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObraSocial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obraSocial.setId(longCount.incrementAndGet());

        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraSocialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(obraSocialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObraSocial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obraSocial.setId(longCount.incrementAndGet());

        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraSocialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObraSocialWithPatch() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the obraSocial using partial update
        ObraSocial partialUpdatedObraSocial = new ObraSocial();
        partialUpdatedObraSocial.setId(obraSocial.getId());

        partialUpdatedObraSocial
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .direccion(UPDATED_DIRECCION)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restObraSocialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObraSocial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedObraSocial))
            )
            .andExpect(status().isOk());

        // Validate the ObraSocial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObraSocialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedObraSocial, obraSocial),
            getPersistedObraSocial(obraSocial)
        );
    }

    @Test
    @Transactional
    void fullUpdateObraSocialWithPatch() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the obraSocial using partial update
        ObraSocial partialUpdatedObraSocial = new ObraSocial();
        partialUpdatedObraSocial.setId(obraSocial.getId());

        partialUpdatedObraSocial
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restObraSocialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObraSocial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedObraSocial))
            )
            .andExpect(status().isOk());

        // Validate the ObraSocial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObraSocialUpdatableFieldsEquals(partialUpdatedObraSocial, getPersistedObraSocial(partialUpdatedObraSocial));
    }

    @Test
    @Transactional
    void patchNonExistingObraSocial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obraSocial.setId(longCount.incrementAndGet());

        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObraSocialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, obraSocialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(obraSocialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObraSocial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obraSocial.setId(longCount.incrementAndGet());

        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraSocialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(obraSocialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObraSocial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obraSocial.setId(longCount.incrementAndGet());

        // Create the ObraSocial
        ObraSocialDTO obraSocialDTO = obraSocialMapper.toDto(obraSocial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraSocialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(obraSocialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ObraSocial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObraSocial() throws Exception {
        // Initialize the database
        insertedObraSocial = obraSocialRepository.saveAndFlush(obraSocial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the obraSocial
        restObraSocialMockMvc
            .perform(delete(ENTITY_API_URL_ID, obraSocial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return obraSocialRepository.count();
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

    protected ObraSocial getPersistedObraSocial(ObraSocial obraSocial) {
        return obraSocialRepository.findById(obraSocial.getId()).orElseThrow();
    }

    protected void assertPersistedObraSocialToMatchAllProperties(ObraSocial expectedObraSocial) {
        assertObraSocialAllPropertiesEquals(expectedObraSocial, getPersistedObraSocial(expectedObraSocial));
    }

    protected void assertPersistedObraSocialToMatchUpdatableProperties(ObraSocial expectedObraSocial) {
        assertObraSocialAllUpdatablePropertiesEquals(expectedObraSocial, getPersistedObraSocial(expectedObraSocial));
    }
}
