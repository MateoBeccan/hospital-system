package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.ProvinciaAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Pais;
import com.mycompany.hospital.domain.Provincia;
import com.mycompany.hospital.repository.ProvinciaRepository;
import com.mycompany.hospital.service.dto.ProvinciaDTO;
import com.mycompany.hospital.service.mapper.ProvinciaMapper;
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
 * Integration tests for the {@link ProvinciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinciaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ALTA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_BAJA = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/provincias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private ProvinciaMapper provinciaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinciaMockMvc;

    private Provincia provincia;

    private Provincia insertedProvincia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provincia createEntity(EntityManager em) {
        Provincia provincia = new Provincia()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA)
            .activo(DEFAULT_ACTIVO);
        // Add required entity
        Pais pais;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            pais = PaisResourceIT.createEntity();
            em.persist(pais);
            em.flush();
        } else {
            pais = TestUtil.findAll(em, Pais.class).get(0);
        }
        provincia.setPais(pais);
        return provincia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provincia createUpdatedEntity(EntityManager em) {
        Provincia updatedProvincia = new Provincia()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        // Add required entity
        Pais pais;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            pais = PaisResourceIT.createUpdatedEntity();
            em.persist(pais);
            em.flush();
        } else {
            pais = TestUtil.findAll(em, Pais.class).get(0);
        }
        updatedProvincia.setPais(pais);
        return updatedProvincia;
    }

    @BeforeEach
    void initTest() {
        provincia = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProvincia != null) {
            provinciaRepository.delete(insertedProvincia);
            insertedProvincia = null;
        }
    }

    @Test
    @Transactional
    void createProvincia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);
        var returnedProvinciaDTO = om.readValue(
            restProvinciaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProvinciaDTO.class
        );

        // Validate the Provincia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProvincia = provinciaMapper.toEntity(returnedProvinciaDTO);
        assertProvinciaUpdatableFieldsEquals(returnedProvincia, getPersistedProvincia(returnedProvincia));

        insertedProvincia = returnedProvincia;
    }

    @Test
    @Transactional
    void createProvinciaWithExistingId() throws Exception {
        // Create the Provincia with an existing ID
        provincia.setId(1L);
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        provincia.setNombre(null);

        // Create the Provincia, which fails.
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        provincia.setFechaAlta(null);

        // Create the Provincia, which fails.
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        provincia.setActivo(null);

        // Create the Provincia, which fails.
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        restProvinciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProvincias() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provincia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getProvincia() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get the provincia
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL_ID, provincia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(provincia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getProvinciasByIdFiltering() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        Long id = provincia.getId();

        defaultProvinciaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProvinciaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProvinciaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvinciasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where nombre equals to
        defaultProvinciaFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProvinciasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where nombre in
        defaultProvinciaFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProvinciasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where nombre is not null
        defaultProvinciaFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where nombre contains
        defaultProvinciaFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProvinciasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where nombre does not contain
        defaultProvinciaFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProvinciasByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where codigo equals to
        defaultProvinciaFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllProvinciasByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where codigo in
        defaultProvinciaFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllProvinciasByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where codigo is not null
        defaultProvinciaFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where codigo contains
        defaultProvinciaFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllProvinciasByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where codigo does not contain
        defaultProvinciaFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta equals to
        defaultProvinciaFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta in
        defaultProvinciaFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta is not null
        defaultProvinciaFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta is greater than or equal to
        defaultProvinciaFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta is less than or equal to
        defaultProvinciaFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta is less than
        defaultProvinciaFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaAlta is greater than
        defaultProvinciaFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja equals to
        defaultProvinciaFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja in
        defaultProvinciaFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja is not null
        defaultProvinciaFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja is greater than or equal to
        defaultProvinciaFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja is less than or equal to
        defaultProvinciaFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja is less than
        defaultProvinciaFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllProvinciasByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where fechaBaja is greater than
        defaultProvinciaFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllProvinciasByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where activo equals to
        defaultProvinciaFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllProvinciasByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where activo in
        defaultProvinciaFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllProvinciasByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        // Get all the provinciaList where activo is not null
        defaultProvinciaFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllProvinciasByPaisIsEqualToSomething() throws Exception {
        Pais pais;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            provinciaRepository.saveAndFlush(provincia);
            pais = PaisResourceIT.createEntity();
        } else {
            pais = TestUtil.findAll(em, Pais.class).get(0);
        }
        em.persist(pais);
        em.flush();
        provincia.setPais(pais);
        provinciaRepository.saveAndFlush(provincia);
        Long paisId = pais.getId();
        // Get all the provinciaList where pais equals to paisId
        defaultProvinciaShouldBeFound("paisId.equals=" + paisId);

        // Get all the provinciaList where pais equals to (paisId + 1)
        defaultProvinciaShouldNotBeFound("paisId.equals=" + (paisId + 1));
    }

    private void defaultProvinciaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProvinciaShouldBeFound(shouldBeFound);
        defaultProvinciaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinciaShouldBeFound(String filter) throws Exception {
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provincia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinciaShouldNotBeFound(String filter) throws Exception {
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvincia() throws Exception {
        // Get the provincia
        restProvinciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProvincia() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provincia
        Provincia updatedProvincia = provinciaRepository.findById(provincia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProvincia are not directly saved in db
        em.detach(updatedProvincia);
        updatedProvincia
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(updatedProvincia);

        restProvinciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProvinciaToMatchAllProperties(updatedProvincia);
    }

    @Test
    @Transactional
    void putNonExistingProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinciaWithPatch() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provincia using partial update
        Provincia partialUpdatedProvincia = new Provincia();
        partialUpdatedProvincia.setId(provincia.getId());

        partialUpdatedProvincia.fechaAlta(UPDATED_FECHA_ALTA).fechaBaja(UPDATED_FECHA_BAJA).activo(UPDATED_ACTIVO);

        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvincia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvincia))
            )
            .andExpect(status().isOk());

        // Validate the Provincia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProvinciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProvincia, provincia),
            getPersistedProvincia(provincia)
        );
    }

    @Test
    @Transactional
    void fullUpdateProvinciaWithPatch() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the provincia using partial update
        Provincia partialUpdatedProvincia = new Provincia();
        partialUpdatedProvincia.setId(provincia.getId());

        partialUpdatedProvincia
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA)
            .activo(UPDATED_ACTIVO);

        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvincia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvincia))
            )
            .andExpect(status().isOk());

        // Validate the Provincia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProvinciaUpdatableFieldsEquals(partialUpdatedProvincia, getPersistedProvincia(partialUpdatedProvincia));
    }

    @Test
    @Transactional
    void patchNonExistingProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provinciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provinciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvincia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        provincia.setId(longCount.incrementAndGet());

        // Create the Provincia
        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(provinciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provincia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvincia() throws Exception {
        // Initialize the database
        insertedProvincia = provinciaRepository.saveAndFlush(provincia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the provincia
        restProvinciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, provincia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return provinciaRepository.count();
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

    protected Provincia getPersistedProvincia(Provincia provincia) {
        return provinciaRepository.findById(provincia.getId()).orElseThrow();
    }

    protected void assertPersistedProvinciaToMatchAllProperties(Provincia expectedProvincia) {
        assertProvinciaAllPropertiesEquals(expectedProvincia, getPersistedProvincia(expectedProvincia));
    }

    protected void assertPersistedProvinciaToMatchUpdatableProperties(Provincia expectedProvincia) {
        assertProvinciaAllUpdatablePropertiesEquals(expectedProvincia, getPersistedProvincia(expectedProvincia));
    }
}
