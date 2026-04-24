package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.PaisAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Pais;
import com.mycompany.hospital.repository.PaisRepository;
import com.mycompany.hospital.service.dto.PaisDTO;
import com.mycompany.hospital.service.mapper.PaisMapper;
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
 * Integration tests for the {@link PaisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaisResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_ISO = "AAA";
    private static final String UPDATED_CODIGO_ISO = "BBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/pais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private PaisMapper paisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaisMockMvc;

    private Pais pais;

    private Pais insertedPais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createEntity() {
        return new Pais()
            .nombre(DEFAULT_NOMBRE)
            .codigoIso(DEFAULT_CODIGO_ISO)
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
    public static Pais createUpdatedEntity() {
        return new Pais()
            .nombre(UPDATED_NOMBRE)
            .codigoIso(UPDATED_CODIGO_ISO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        pais = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPais != null) {
            paisRepository.delete(insertedPais);
            insertedPais = null;
        }
    }

    @Test
    @Transactional
    void createPais() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);
        var returnedPaisDTO = om.readValue(
            restPaisMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaisDTO.class
        );

        // Validate the Pais in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPais = paisMapper.toEntity(returnedPaisDTO);
        assertPaisUpdatableFieldsEquals(returnedPais, getPersistedPais(returnedPais));

        insertedPais = returnedPais;
    }

    @Test
    @Transactional
    void createPaisWithExistingId() throws Exception {
        // Create the Pais with an existing ID
        pais.setId(1L);
        PaisDTO paisDTO = paisMapper.toDto(pais);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setNombre(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setCodigoIso(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setFechaAlta(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setActivo(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaises() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigoIso").value(hasItem(DEFAULT_CODIGO_ISO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get the pais
        restPaisMockMvc
            .perform(get(ENTITY_API_URL_ID, pais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pais.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigoIso").value(DEFAULT_CODIGO_ISO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getPaisesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        Long id = pais.getId();

        defaultPaisFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaisFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaisFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaisesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where nombre equals to
        defaultPaisFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPaisesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where nombre in
        defaultPaisFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPaisesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where nombre is not null
        defaultPaisFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPaisesByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where nombre contains
        defaultPaisFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPaisesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where nombre does not contain
        defaultPaisFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPaisesByCodigoIsoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where codigoIso equals to
        defaultPaisFiltering("codigoIso.equals=" + DEFAULT_CODIGO_ISO, "codigoIso.equals=" + UPDATED_CODIGO_ISO);
    }

    @Test
    @Transactional
    void getAllPaisesByCodigoIsoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where codigoIso in
        defaultPaisFiltering("codigoIso.in=" + DEFAULT_CODIGO_ISO + "," + UPDATED_CODIGO_ISO, "codigoIso.in=" + UPDATED_CODIGO_ISO);
    }

    @Test
    @Transactional
    void getAllPaisesByCodigoIsoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where codigoIso is not null
        defaultPaisFiltering("codigoIso.specified=true", "codigoIso.specified=false");
    }

    @Test
    @Transactional
    void getAllPaisesByCodigoIsoContainsSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where codigoIso contains
        defaultPaisFiltering("codigoIso.contains=" + DEFAULT_CODIGO_ISO, "codigoIso.contains=" + UPDATED_CODIGO_ISO);
    }

    @Test
    @Transactional
    void getAllPaisesByCodigoIsoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where codigoIso does not contain
        defaultPaisFiltering("codigoIso.doesNotContain=" + UPDATED_CODIGO_ISO, "codigoIso.doesNotContain=" + DEFAULT_CODIGO_ISO);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta equals to
        defaultPaisFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta in
        defaultPaisFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta is not null
        defaultPaisFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta is greater than or equal to
        defaultPaisFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta is less than or equal to
        defaultPaisFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta is less than
        defaultPaisFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaAlta is greater than
        defaultPaisFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja equals to
        defaultPaisFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja in
        defaultPaisFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja is not null
        defaultPaisFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja is greater than or equal to
        defaultPaisFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja is less than or equal to
        defaultPaisFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja is less than
        defaultPaisFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPaisesByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where fechaBaja is greater than
        defaultPaisFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPaisesByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where activo equals to
        defaultPaisFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllPaisesByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where activo in
        defaultPaisFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllPaisesByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList where activo is not null
        defaultPaisFiltering("activo.specified=true", "activo.specified=false");
    }

    private void defaultPaisFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaisShouldBeFound(shouldBeFound);
        defaultPaisShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaisShouldBeFound(String filter) throws Exception {
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigoIso").value(hasItem(DEFAULT_CODIGO_ISO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaisShouldNotBeFound(String filter) throws Exception {
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPais() throws Exception {
        // Get the pais
        restPaisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais
        Pais updatedPais = paisRepository.findById(pais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPais are not directly saved in db
        em.detach(updatedPais);
        updatedPais
            .nombre(UPDATED_NOMBRE)
            .codigoIso(UPDATED_CODIGO_ISO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        PaisDTO paisDTO = paisMapper.toDto(updatedPais);

        restPaisMockMvc
            .perform(put(ENTITY_API_URL_ID, paisDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isOk());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaisToMatchAllProperties(updatedPais);
    }

    @Test
    @Transactional
    void putNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL_ID, paisDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.nombre(UPDATED_NOMBRE).codigoIso(UPDATED_CODIGO_ISO).fechaAlta(UPDATED_FECHA_ALTA).activo(UPDATED_ACTIVO);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPais, pais), getPersistedPais(pais));
    }

    @Test
    @Transactional
    void fullUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais
            .nombre(UPDATED_NOMBRE)
            .codigoIso(UPDATED_CODIGO_ISO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(partialUpdatedPais, getPersistedPais(partialUpdatedPais));
    }

    @Test
    @Transactional
    void patchNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paisDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pais
        restPaisMockMvc
            .perform(delete(ENTITY_API_URL_ID, pais.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paisRepository.count();
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

    protected Pais getPersistedPais(Pais pais) {
        return paisRepository.findById(pais.getId()).orElseThrow();
    }

    protected void assertPersistedPaisToMatchAllProperties(Pais expectedPais) {
        assertPaisAllPropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }

    protected void assertPersistedPaisToMatchUpdatableProperties(Pais expectedPais) {
        assertPaisAllUpdatablePropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }
}
