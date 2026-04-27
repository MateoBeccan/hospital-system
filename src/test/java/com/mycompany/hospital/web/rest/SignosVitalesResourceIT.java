package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.SignosVitalesAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.hospital.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Consulta;
import com.mycompany.hospital.domain.SignosVitales;
import com.mycompany.hospital.repository.SignosVitalesRepository;
import com.mycompany.hospital.service.dto.SignosVitalesDTO;
import com.mycompany.hospital.service.mapper.SignosVitalesMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SignosVitalesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SignosVitalesResourceIT {

    private static final Instant DEFAULT_FECHA_HORA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_HORA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PESO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PESO = new BigDecimal(1);
    private static final BigDecimal SMALLER_PESO = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TALLA = new BigDecimal(0);
    private static final BigDecimal UPDATED_TALLA = new BigDecimal(1);
    private static final BigDecimal SMALLER_TALLA = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TEMPERATURA = new BigDecimal(0);
    private static final BigDecimal UPDATED_TEMPERATURA = new BigDecimal(1);
    private static final BigDecimal SMALLER_TEMPERATURA = new BigDecimal(0 - 1);

    private static final String DEFAULT_PRESION_ARTERIAL = "AAAAAAAAAA";
    private static final String UPDATED_PRESION_ARTERIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_FRECUENCIA_CARDIACA = 0;
    private static final Integer UPDATED_FRECUENCIA_CARDIACA = 1;
    private static final Integer SMALLER_FRECUENCIA_CARDIACA = 0 - 1;

    private static final Integer DEFAULT_FRECUENCIA_RESPIRATORIA = 0;
    private static final Integer UPDATED_FRECUENCIA_RESPIRATORIA = 1;
    private static final Integer SMALLER_FRECUENCIA_RESPIRATORIA = 0 - 1;

    private static final Integer DEFAULT_SATURACION_OXIGENO = 0;
    private static final Integer UPDATED_SATURACION_OXIGENO = 1;
    private static final Integer SMALLER_SATURACION_OXIGENO = 0 - 1;

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

    private static final String ENTITY_API_URL = "/api/signos-vitales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SignosVitalesRepository signosVitalesRepository;

    @Autowired
    private SignosVitalesMapper signosVitalesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignosVitalesMockMvc;

    private SignosVitales signosVitales;

    private SignosVitales insertedSignosVitales;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignosVitales createEntity(EntityManager em) {
        SignosVitales signosVitales = new SignosVitales()
            .fechaHoraRegistro(DEFAULT_FECHA_HORA_REGISTRO)
            .peso(DEFAULT_PESO)
            .talla(DEFAULT_TALLA)
            .temperatura(DEFAULT_TEMPERATURA)
            .presionArterial(DEFAULT_PRESION_ARTERIAL)
            .frecuenciaCardiaca(DEFAULT_FRECUENCIA_CARDIACA)
            .frecuenciaRespiratoria(DEFAULT_FRECUENCIA_RESPIRATORIA)
            .saturacionOxigeno(DEFAULT_SATURACION_OXIGENO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        Consulta consulta;
        if (TestUtil.findAll(em, Consulta.class).isEmpty()) {
            consulta = ConsultaResourceIT.createEntity(em);
            em.persist(consulta);
            em.flush();
        } else {
            consulta = TestUtil.findAll(em, Consulta.class).get(0);
        }
        signosVitales.setConsulta(consulta);
        return signosVitales;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignosVitales createUpdatedEntity(EntityManager em) {
        SignosVitales updatedSignosVitales = new SignosVitales()
            .fechaHoraRegistro(UPDATED_FECHA_HORA_REGISTRO)
            .peso(UPDATED_PESO)
            .talla(UPDATED_TALLA)
            .temperatura(UPDATED_TEMPERATURA)
            .presionArterial(UPDATED_PRESION_ARTERIAL)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        Consulta consulta;
        if (TestUtil.findAll(em, Consulta.class).isEmpty()) {
            consulta = ConsultaResourceIT.createUpdatedEntity(em);
            em.persist(consulta);
            em.flush();
        } else {
            consulta = TestUtil.findAll(em, Consulta.class).get(0);
        }
        updatedSignosVitales.setConsulta(consulta);
        return updatedSignosVitales;
    }

    @BeforeEach
    void initTest() {
        signosVitales = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSignosVitales != null) {
            signosVitalesRepository.delete(insertedSignosVitales);
            insertedSignosVitales = null;
        }
    }

    @Test
    @Transactional
    void createSignosVitales() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);
        var returnedSignosVitalesDTO = om.readValue(
            restSignosVitalesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signosVitalesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SignosVitalesDTO.class
        );

        // Validate the SignosVitales in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSignosVitales = signosVitalesMapper.toEntity(returnedSignosVitalesDTO);
        assertSignosVitalesUpdatableFieldsEquals(returnedSignosVitales, getPersistedSignosVitales(returnedSignosVitales));

        insertedSignosVitales = returnedSignosVitales;
    }

    @Test
    @Transactional
    void createSignosVitalesWithExistingId() throws Exception {
        // Create the SignosVitales with an existing ID
        signosVitales.setId(1L);
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignosVitalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signosVitalesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaHoraRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signosVitales.setFechaHoraRegistro(null);

        // Create the SignosVitales, which fails.
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        restSignosVitalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signosVitalesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signosVitales.setActivo(null);

        // Create the SignosVitales, which fails.
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        restSignosVitalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signosVitalesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signosVitales.setFechaAlta(null);

        // Create the SignosVitales, which fails.
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        restSignosVitalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signosVitalesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSignosVitaleses() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signosVitales.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaHoraRegistro").value(hasItem(DEFAULT_FECHA_HORA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(sameNumber(DEFAULT_PESO))))
            .andExpect(jsonPath("$.[*].talla").value(hasItem(sameNumber(DEFAULT_TALLA))))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(sameNumber(DEFAULT_TEMPERATURA))))
            .andExpect(jsonPath("$.[*].presionArterial").value(hasItem(DEFAULT_PRESION_ARTERIAL)))
            .andExpect(jsonPath("$.[*].frecuenciaCardiaca").value(hasItem(DEFAULT_FRECUENCIA_CARDIACA)))
            .andExpect(jsonPath("$.[*].frecuenciaRespiratoria").value(hasItem(DEFAULT_FRECUENCIA_RESPIRATORIA)))
            .andExpect(jsonPath("$.[*].saturacionOxigeno").value(hasItem(DEFAULT_SATURACION_OXIGENO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getSignosVitales() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get the signosVitales
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL_ID, signosVitales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signosVitales.getId().intValue()))
            .andExpect(jsonPath("$.fechaHoraRegistro").value(DEFAULT_FECHA_HORA_REGISTRO.toString()))
            .andExpect(jsonPath("$.peso").value(sameNumber(DEFAULT_PESO)))
            .andExpect(jsonPath("$.talla").value(sameNumber(DEFAULT_TALLA)))
            .andExpect(jsonPath("$.temperatura").value(sameNumber(DEFAULT_TEMPERATURA)))
            .andExpect(jsonPath("$.presionArterial").value(DEFAULT_PRESION_ARTERIAL))
            .andExpect(jsonPath("$.frecuenciaCardiaca").value(DEFAULT_FRECUENCIA_CARDIACA))
            .andExpect(jsonPath("$.frecuenciaRespiratoria").value(DEFAULT_FRECUENCIA_RESPIRATORIA))
            .andExpect(jsonPath("$.saturacionOxigeno").value(DEFAULT_SATURACION_OXIGENO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getSignosVitalesesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        Long id = signosVitales.getId();

        defaultSignosVitalesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSignosVitalesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSignosVitalesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaHoraRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaHoraRegistro equals to
        defaultSignosVitalesFiltering(
            "fechaHoraRegistro.equals=" + DEFAULT_FECHA_HORA_REGISTRO,
            "fechaHoraRegistro.equals=" + UPDATED_FECHA_HORA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaHoraRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaHoraRegistro in
        defaultSignosVitalesFiltering(
            "fechaHoraRegistro.in=" + DEFAULT_FECHA_HORA_REGISTRO + "," + UPDATED_FECHA_HORA_REGISTRO,
            "fechaHoraRegistro.in=" + UPDATED_FECHA_HORA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaHoraRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaHoraRegistro is not null
        defaultSignosVitalesFiltering("fechaHoraRegistro.specified=true", "fechaHoraRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso equals to
        defaultSignosVitalesFiltering("peso.equals=" + DEFAULT_PESO, "peso.equals=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso in
        defaultSignosVitalesFiltering("peso.in=" + DEFAULT_PESO + "," + UPDATED_PESO, "peso.in=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso is not null
        defaultSignosVitalesFiltering("peso.specified=true", "peso.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso is greater than or equal to
        defaultSignosVitalesFiltering("peso.greaterThanOrEqual=" + DEFAULT_PESO, "peso.greaterThanOrEqual=" + UPDATED_PESO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso is less than or equal to
        defaultSignosVitalesFiltering("peso.lessThanOrEqual=" + DEFAULT_PESO, "peso.lessThanOrEqual=" + SMALLER_PESO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso is less than
        defaultSignosVitalesFiltering("peso.lessThan=" + UPDATED_PESO, "peso.lessThan=" + DEFAULT_PESO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPesoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where peso is greater than
        defaultSignosVitalesFiltering("peso.greaterThan=" + SMALLER_PESO, "peso.greaterThan=" + DEFAULT_PESO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla equals to
        defaultSignosVitalesFiltering("talla.equals=" + DEFAULT_TALLA, "talla.equals=" + UPDATED_TALLA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla in
        defaultSignosVitalesFiltering("talla.in=" + DEFAULT_TALLA + "," + UPDATED_TALLA, "talla.in=" + UPDATED_TALLA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla is not null
        defaultSignosVitalesFiltering("talla.specified=true", "talla.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla is greater than or equal to
        defaultSignosVitalesFiltering("talla.greaterThanOrEqual=" + DEFAULT_TALLA, "talla.greaterThanOrEqual=" + UPDATED_TALLA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla is less than or equal to
        defaultSignosVitalesFiltering("talla.lessThanOrEqual=" + DEFAULT_TALLA, "talla.lessThanOrEqual=" + SMALLER_TALLA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla is less than
        defaultSignosVitalesFiltering("talla.lessThan=" + UPDATED_TALLA, "talla.lessThan=" + DEFAULT_TALLA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTallaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where talla is greater than
        defaultSignosVitalesFiltering("talla.greaterThan=" + SMALLER_TALLA, "talla.greaterThan=" + DEFAULT_TALLA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura equals to
        defaultSignosVitalesFiltering("temperatura.equals=" + DEFAULT_TEMPERATURA, "temperatura.equals=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura in
        defaultSignosVitalesFiltering(
            "temperatura.in=" + DEFAULT_TEMPERATURA + "," + UPDATED_TEMPERATURA,
            "temperatura.in=" + UPDATED_TEMPERATURA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura is not null
        defaultSignosVitalesFiltering("temperatura.specified=true", "temperatura.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura is greater than or equal to
        defaultSignosVitalesFiltering(
            "temperatura.greaterThanOrEqual=" + DEFAULT_TEMPERATURA,
            "temperatura.greaterThanOrEqual=" + UPDATED_TEMPERATURA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura is less than or equal to
        defaultSignosVitalesFiltering(
            "temperatura.lessThanOrEqual=" + DEFAULT_TEMPERATURA,
            "temperatura.lessThanOrEqual=" + SMALLER_TEMPERATURA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura is less than
        defaultSignosVitalesFiltering("temperatura.lessThan=" + UPDATED_TEMPERATURA, "temperatura.lessThan=" + DEFAULT_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByTemperaturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where temperatura is greater than
        defaultSignosVitalesFiltering("temperatura.greaterThan=" + SMALLER_TEMPERATURA, "temperatura.greaterThan=" + DEFAULT_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPresionArterialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where presionArterial equals to
        defaultSignosVitalesFiltering(
            "presionArterial.equals=" + DEFAULT_PRESION_ARTERIAL,
            "presionArterial.equals=" + UPDATED_PRESION_ARTERIAL
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPresionArterialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where presionArterial in
        defaultSignosVitalesFiltering(
            "presionArterial.in=" + DEFAULT_PRESION_ARTERIAL + "," + UPDATED_PRESION_ARTERIAL,
            "presionArterial.in=" + UPDATED_PRESION_ARTERIAL
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPresionArterialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where presionArterial is not null
        defaultSignosVitalesFiltering("presionArterial.specified=true", "presionArterial.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPresionArterialContainsSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where presionArterial contains
        defaultSignosVitalesFiltering(
            "presionArterial.contains=" + DEFAULT_PRESION_ARTERIAL,
            "presionArterial.contains=" + UPDATED_PRESION_ARTERIAL
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByPresionArterialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where presionArterial does not contain
        defaultSignosVitalesFiltering(
            "presionArterial.doesNotContain=" + UPDATED_PRESION_ARTERIAL,
            "presionArterial.doesNotContain=" + DEFAULT_PRESION_ARTERIAL
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca equals to
        defaultSignosVitalesFiltering(
            "frecuenciaCardiaca.equals=" + DEFAULT_FRECUENCIA_CARDIACA,
            "frecuenciaCardiaca.equals=" + UPDATED_FRECUENCIA_CARDIACA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca in
        defaultSignosVitalesFiltering(
            "frecuenciaCardiaca.in=" + DEFAULT_FRECUENCIA_CARDIACA + "," + UPDATED_FRECUENCIA_CARDIACA,
            "frecuenciaCardiaca.in=" + UPDATED_FRECUENCIA_CARDIACA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca is not null
        defaultSignosVitalesFiltering("frecuenciaCardiaca.specified=true", "frecuenciaCardiaca.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca is greater than or equal to
        defaultSignosVitalesFiltering(
            "frecuenciaCardiaca.greaterThanOrEqual=" + DEFAULT_FRECUENCIA_CARDIACA,
            "frecuenciaCardiaca.greaterThanOrEqual=" + UPDATED_FRECUENCIA_CARDIACA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca is less than or equal to
        defaultSignosVitalesFiltering(
            "frecuenciaCardiaca.lessThanOrEqual=" + DEFAULT_FRECUENCIA_CARDIACA,
            "frecuenciaCardiaca.lessThanOrEqual=" + SMALLER_FRECUENCIA_CARDIACA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca is less than
        defaultSignosVitalesFiltering(
            "frecuenciaCardiaca.lessThan=" + UPDATED_FRECUENCIA_CARDIACA,
            "frecuenciaCardiaca.lessThan=" + DEFAULT_FRECUENCIA_CARDIACA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaCardiacaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaCardiaca is greater than
        defaultSignosVitalesFiltering(
            "frecuenciaCardiaca.greaterThan=" + SMALLER_FRECUENCIA_CARDIACA,
            "frecuenciaCardiaca.greaterThan=" + DEFAULT_FRECUENCIA_CARDIACA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria equals to
        defaultSignosVitalesFiltering(
            "frecuenciaRespiratoria.equals=" + DEFAULT_FRECUENCIA_RESPIRATORIA,
            "frecuenciaRespiratoria.equals=" + UPDATED_FRECUENCIA_RESPIRATORIA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria in
        defaultSignosVitalesFiltering(
            "frecuenciaRespiratoria.in=" + DEFAULT_FRECUENCIA_RESPIRATORIA + "," + UPDATED_FRECUENCIA_RESPIRATORIA,
            "frecuenciaRespiratoria.in=" + UPDATED_FRECUENCIA_RESPIRATORIA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria is not null
        defaultSignosVitalesFiltering("frecuenciaRespiratoria.specified=true", "frecuenciaRespiratoria.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria is greater than or equal to
        defaultSignosVitalesFiltering(
            "frecuenciaRespiratoria.greaterThanOrEqual=" + DEFAULT_FRECUENCIA_RESPIRATORIA,
            "frecuenciaRespiratoria.greaterThanOrEqual=" + UPDATED_FRECUENCIA_RESPIRATORIA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria is less than or equal to
        defaultSignosVitalesFiltering(
            "frecuenciaRespiratoria.lessThanOrEqual=" + DEFAULT_FRECUENCIA_RESPIRATORIA,
            "frecuenciaRespiratoria.lessThanOrEqual=" + SMALLER_FRECUENCIA_RESPIRATORIA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria is less than
        defaultSignosVitalesFiltering(
            "frecuenciaRespiratoria.lessThan=" + UPDATED_FRECUENCIA_RESPIRATORIA,
            "frecuenciaRespiratoria.lessThan=" + DEFAULT_FRECUENCIA_RESPIRATORIA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFrecuenciaRespiratoriaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where frecuenciaRespiratoria is greater than
        defaultSignosVitalesFiltering(
            "frecuenciaRespiratoria.greaterThan=" + SMALLER_FRECUENCIA_RESPIRATORIA,
            "frecuenciaRespiratoria.greaterThan=" + DEFAULT_FRECUENCIA_RESPIRATORIA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno equals to
        defaultSignosVitalesFiltering(
            "saturacionOxigeno.equals=" + DEFAULT_SATURACION_OXIGENO,
            "saturacionOxigeno.equals=" + UPDATED_SATURACION_OXIGENO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno in
        defaultSignosVitalesFiltering(
            "saturacionOxigeno.in=" + DEFAULT_SATURACION_OXIGENO + "," + UPDATED_SATURACION_OXIGENO,
            "saturacionOxigeno.in=" + UPDATED_SATURACION_OXIGENO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno is not null
        defaultSignosVitalesFiltering("saturacionOxigeno.specified=true", "saturacionOxigeno.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno is greater than or equal to
        defaultSignosVitalesFiltering(
            "saturacionOxigeno.greaterThanOrEqual=" + DEFAULT_SATURACION_OXIGENO,
            "saturacionOxigeno.greaterThanOrEqual=" + (DEFAULT_SATURACION_OXIGENO + 1)
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno is less than or equal to
        defaultSignosVitalesFiltering(
            "saturacionOxigeno.lessThanOrEqual=" + DEFAULT_SATURACION_OXIGENO,
            "saturacionOxigeno.lessThanOrEqual=" + SMALLER_SATURACION_OXIGENO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno is less than
        defaultSignosVitalesFiltering(
            "saturacionOxigeno.lessThan=" + (DEFAULT_SATURACION_OXIGENO + 1),
            "saturacionOxigeno.lessThan=" + DEFAULT_SATURACION_OXIGENO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesBySaturacionOxigenoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where saturacionOxigeno is greater than
        defaultSignosVitalesFiltering(
            "saturacionOxigeno.greaterThan=" + SMALLER_SATURACION_OXIGENO,
            "saturacionOxigeno.greaterThan=" + DEFAULT_SATURACION_OXIGENO
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where activo equals to
        defaultSignosVitalesFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where activo in
        defaultSignosVitalesFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where activo is not null
        defaultSignosVitalesFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta equals to
        defaultSignosVitalesFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta in
        defaultSignosVitalesFiltering(
            "fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA,
            "fechaAlta.in=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta is not null
        defaultSignosVitalesFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta is greater than or equal to
        defaultSignosVitalesFiltering(
            "fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA,
            "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta is less than or equal to
        defaultSignosVitalesFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta is less than
        defaultSignosVitalesFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaAlta is greater than
        defaultSignosVitalesFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja equals to
        defaultSignosVitalesFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja in
        defaultSignosVitalesFiltering(
            "fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA,
            "fechaBaja.in=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja is not null
        defaultSignosVitalesFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja is greater than or equal to
        defaultSignosVitalesFiltering(
            "fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA,
            "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA
        );
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja is less than or equal to
        defaultSignosVitalesFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja is less than
        defaultSignosVitalesFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList where fechaBaja is greater than
        defaultSignosVitalesFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllSignosVitalesesByConsultaIsEqualToSomething() throws Exception {
        Consulta consulta;
        if (TestUtil.findAll(em, Consulta.class).isEmpty()) {
            signosVitalesRepository.saveAndFlush(signosVitales);
            consulta = ConsultaResourceIT.createEntity(em);
        } else {
            consulta = TestUtil.findAll(em, Consulta.class).get(0);
        }
        em.persist(consulta);
        em.flush();
        signosVitales.setConsulta(consulta);
        signosVitalesRepository.saveAndFlush(signosVitales);
        Long consultaId = consulta.getId();
        // Get all the signosVitalesList where consulta equals to consultaId
        defaultSignosVitalesShouldBeFound("consultaId.equals=" + consultaId);

        // Get all the signosVitalesList where consulta equals to (consultaId + 1)
        defaultSignosVitalesShouldNotBeFound("consultaId.equals=" + (consultaId + 1));
    }

    private void defaultSignosVitalesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSignosVitalesShouldBeFound(shouldBeFound);
        defaultSignosVitalesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSignosVitalesShouldBeFound(String filter) throws Exception {
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signosVitales.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaHoraRegistro").value(hasItem(DEFAULT_FECHA_HORA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(sameNumber(DEFAULT_PESO))))
            .andExpect(jsonPath("$.[*].talla").value(hasItem(sameNumber(DEFAULT_TALLA))))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(sameNumber(DEFAULT_TEMPERATURA))))
            .andExpect(jsonPath("$.[*].presionArterial").value(hasItem(DEFAULT_PRESION_ARTERIAL)))
            .andExpect(jsonPath("$.[*].frecuenciaCardiaca").value(hasItem(DEFAULT_FRECUENCIA_CARDIACA)))
            .andExpect(jsonPath("$.[*].frecuenciaRespiratoria").value(hasItem(DEFAULT_FRECUENCIA_RESPIRATORIA)))
            .andExpect(jsonPath("$.[*].saturacionOxigeno").value(hasItem(DEFAULT_SATURACION_OXIGENO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSignosVitalesShouldNotBeFound(String filter) throws Exception {
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSignosVitales() throws Exception {
        // Get the signosVitales
        restSignosVitalesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSignosVitales() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signosVitales
        SignosVitales updatedSignosVitales = signosVitalesRepository.findById(signosVitales.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSignosVitales are not directly saved in db
        em.detach(updatedSignosVitales);
        updatedSignosVitales
            .fechaHoraRegistro(UPDATED_FECHA_HORA_REGISTRO)
            .peso(UPDATED_PESO)
            .talla(UPDATED_TALLA)
            .temperatura(UPDATED_TEMPERATURA)
            .presionArterial(UPDATED_PRESION_ARTERIAL)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(updatedSignosVitales);

        restSignosVitalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signosVitalesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signosVitalesDTO))
            )
            .andExpect(status().isOk());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSignosVitalesToMatchAllProperties(updatedSignosVitales);
    }

    @Test
    @Transactional
    void putNonExistingSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signosVitalesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signosVitalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signosVitalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signosVitalesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignosVitalesWithPatch() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signosVitales using partial update
        SignosVitales partialUpdatedSignosVitales = new SignosVitales();
        partialUpdatedSignosVitales.setId(signosVitales.getId());

        partialUpdatedSignosVitales
            .fechaHoraRegistro(UPDATED_FECHA_HORA_REGISTRO)
            .peso(UPDATED_PESO)
            .presionArterial(UPDATED_PRESION_ARTERIAL)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO);

        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignosVitales.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSignosVitales))
            )
            .andExpect(status().isOk());

        // Validate the SignosVitales in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignosVitalesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSignosVitales, signosVitales),
            getPersistedSignosVitales(signosVitales)
        );
    }

    @Test
    @Transactional
    void fullUpdateSignosVitalesWithPatch() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signosVitales using partial update
        SignosVitales partialUpdatedSignosVitales = new SignosVitales();
        partialUpdatedSignosVitales.setId(signosVitales.getId());

        partialUpdatedSignosVitales
            .fechaHoraRegistro(UPDATED_FECHA_HORA_REGISTRO)
            .peso(UPDATED_PESO)
            .talla(UPDATED_TALLA)
            .temperatura(UPDATED_TEMPERATURA)
            .presionArterial(UPDATED_PRESION_ARTERIAL)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO)
            .observaciones(UPDATED_OBSERVACIONES)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignosVitales.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSignosVitales))
            )
            .andExpect(status().isOk());

        // Validate the SignosVitales in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignosVitalesUpdatableFieldsEquals(partialUpdatedSignosVitales, getPersistedSignosVitales(partialUpdatedSignosVitales));
    }

    @Test
    @Transactional
    void patchNonExistingSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signosVitalesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(signosVitalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(signosVitalesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(signosVitalesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignosVitales() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.saveAndFlush(signosVitales);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the signosVitales
        restSignosVitalesMockMvc
            .perform(delete(ENTITY_API_URL_ID, signosVitales.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return signosVitalesRepository.count();
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

    protected SignosVitales getPersistedSignosVitales(SignosVitales signosVitales) {
        return signosVitalesRepository.findById(signosVitales.getId()).orElseThrow();
    }

    protected void assertPersistedSignosVitalesToMatchAllProperties(SignosVitales expectedSignosVitales) {
        assertSignosVitalesAllPropertiesEquals(expectedSignosVitales, getPersistedSignosVitales(expectedSignosVitales));
    }

    protected void assertPersistedSignosVitalesToMatchUpdatableProperties(SignosVitales expectedSignosVitales) {
        assertSignosVitalesAllUpdatablePropertiesEquals(expectedSignosVitales, getPersistedSignosVitales(expectedSignosVitales));
    }
}
