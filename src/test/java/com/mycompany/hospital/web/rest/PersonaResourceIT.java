package com.mycompany.hospital.web.rest;

import static com.mycompany.hospital.domain.PersonaAsserts.*;
import static com.mycompany.hospital.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.hospital.IntegrationTest;
import com.mycompany.hospital.domain.Ciudad;
import com.mycompany.hospital.domain.Persona;
import com.mycompany.hospital.domain.Sexo;
import com.mycompany.hospital.domain.TipoDocumento;
import com.mycompany.hospital.repository.PersonaRepository;
import com.mycompany.hospital.service.dto.PersonaDTO;
import com.mycompany.hospital.service.mapper.PersonaMapper;
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
 * Integration tests for the {@link PersonaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_NRO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NRO_DOCUMENTO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "j@V.HC`0";
    private static final String UPDATED_EMAIL = "v@L.=<.~qb";

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

    private static final String ENTITY_API_URL = "/api/personas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMapper personaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonaMockMvc;

    private Persona persona;

    private Persona insertedPersona;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createEntity(EntityManager em) {
        Persona persona = new Persona()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .nroDocumento(DEFAULT_NRO_DOCUMENTO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .direccion(DEFAULT_DIRECCION)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .fechaBaja(DEFAULT_FECHA_BAJA);
        // Add required entity
        TipoDocumento tipoDocumento;
        if (TestUtil.findAll(em, TipoDocumento.class).isEmpty()) {
            tipoDocumento = TipoDocumentoResourceIT.createEntity();
            em.persist(tipoDocumento);
            em.flush();
        } else {
            tipoDocumento = TestUtil.findAll(em, TipoDocumento.class).get(0);
        }
        persona.setTipoDocumento(tipoDocumento);
        // Add required entity
        Sexo sexo;
        if (TestUtil.findAll(em, Sexo.class).isEmpty()) {
            sexo = SexoResourceIT.createEntity();
            em.persist(sexo);
            em.flush();
        } else {
            sexo = TestUtil.findAll(em, Sexo.class).get(0);
        }
        persona.setSexo(sexo);
        return persona;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createUpdatedEntity(EntityManager em) {
        Persona updatedPersona = new Persona()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .nroDocumento(UPDATED_NRO_DOCUMENTO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        // Add required entity
        TipoDocumento tipoDocumento;
        if (TestUtil.findAll(em, TipoDocumento.class).isEmpty()) {
            tipoDocumento = TipoDocumentoResourceIT.createUpdatedEntity();
            em.persist(tipoDocumento);
            em.flush();
        } else {
            tipoDocumento = TestUtil.findAll(em, TipoDocumento.class).get(0);
        }
        updatedPersona.setTipoDocumento(tipoDocumento);
        // Add required entity
        Sexo sexo;
        if (TestUtil.findAll(em, Sexo.class).isEmpty()) {
            sexo = SexoResourceIT.createUpdatedEntity();
            em.persist(sexo);
            em.flush();
        } else {
            sexo = TestUtil.findAll(em, Sexo.class).get(0);
        }
        updatedPersona.setSexo(sexo);
        return updatedPersona;
    }

    @BeforeEach
    void initTest() {
        persona = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPersona != null) {
            personaRepository.delete(insertedPersona);
            insertedPersona = null;
        }
    }

    @Test
    @Transactional
    void createPersona() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);
        var returnedPersonaDTO = om.readValue(
            restPersonaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PersonaDTO.class
        );

        // Validate the Persona in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPersona = personaMapper.toEntity(returnedPersonaDTO);
        assertPersonaUpdatableFieldsEquals(returnedPersona, getPersistedPersona(returnedPersona));

        insertedPersona = returnedPersona;
    }

    @Test
    @Transactional
    void createPersonaWithExistingId() throws Exception {
        // Create the Persona with an existing ID
        persona.setId(1L);
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        persona.setNombre(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        persona.setApellido(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNroDocumentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        persona.setNroDocumento(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        persona.setActivo(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        persona.setFechaAlta(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonas() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].nroDocumento").value(hasItem(DEFAULT_NRO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));
    }

    @Test
    @Transactional
    void getPersona() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get the persona
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL_ID, persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(persona.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.nroDocumento").value(DEFAULT_NRO_DOCUMENTO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()));
    }

    @Test
    @Transactional
    void getPersonasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        Long id = persona.getId();

        defaultPersonaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPersonaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPersonaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nombre equals to
        defaultPersonaFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPersonasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nombre in
        defaultPersonaFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPersonasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nombre is not null
        defaultPersonaFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nombre contains
        defaultPersonaFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPersonasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nombre does not contain
        defaultPersonaFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPersonasByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where apellido equals to
        defaultPersonaFiltering("apellido.equals=" + DEFAULT_APELLIDO, "apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPersonasByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where apellido in
        defaultPersonaFiltering("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO, "apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPersonasByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where apellido is not null
        defaultPersonaFiltering("apellido.specified=true", "apellido.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByApellidoContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where apellido contains
        defaultPersonaFiltering("apellido.contains=" + DEFAULT_APELLIDO, "apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPersonasByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where apellido does not contain
        defaultPersonaFiltering("apellido.doesNotContain=" + UPDATED_APELLIDO, "apellido.doesNotContain=" + DEFAULT_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPersonasByNroDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nroDocumento equals to
        defaultPersonaFiltering("nroDocumento.equals=" + DEFAULT_NRO_DOCUMENTO, "nroDocumento.equals=" + UPDATED_NRO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllPersonasByNroDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nroDocumento in
        defaultPersonaFiltering(
            "nroDocumento.in=" + DEFAULT_NRO_DOCUMENTO + "," + UPDATED_NRO_DOCUMENTO,
            "nroDocumento.in=" + UPDATED_NRO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByNroDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nroDocumento is not null
        defaultPersonaFiltering("nroDocumento.specified=true", "nroDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByNroDocumentoContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nroDocumento contains
        defaultPersonaFiltering("nroDocumento.contains=" + DEFAULT_NRO_DOCUMENTO, "nroDocumento.contains=" + UPDATED_NRO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllPersonasByNroDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where nroDocumento does not contain
        defaultPersonaFiltering(
            "nroDocumento.doesNotContain=" + UPDATED_NRO_DOCUMENTO,
            "nroDocumento.doesNotContain=" + DEFAULT_NRO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento equals to
        defaultPersonaFiltering("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO, "fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento in
        defaultPersonaFiltering(
            "fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO,
            "fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is not null
        defaultPersonaFiltering("fechaNacimiento.specified=true", "fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is greater than or equal to
        defaultPersonaFiltering(
            "fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO,
            "fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is less than or equal to
        defaultPersonaFiltering(
            "fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO,
            "fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is less than
        defaultPersonaFiltering(
            "fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO,
            "fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaNacimiento is greater than
        defaultPersonaFiltering(
            "fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO,
            "fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllPersonasByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where telefono equals to
        defaultPersonaFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPersonasByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where telefono in
        defaultPersonaFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPersonasByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where telefono is not null
        defaultPersonaFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where telefono contains
        defaultPersonaFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPersonasByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where telefono does not contain
        defaultPersonaFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPersonasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where email equals to
        defaultPersonaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where email in
        defaultPersonaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where email is not null
        defaultPersonaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where email contains
        defaultPersonaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where email does not contain
        defaultPersonaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonasByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where direccion equals to
        defaultPersonaFiltering("direccion.equals=" + DEFAULT_DIRECCION, "direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPersonasByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where direccion in
        defaultPersonaFiltering("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION, "direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPersonasByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where direccion is not null
        defaultPersonaFiltering("direccion.specified=true", "direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where direccion contains
        defaultPersonaFiltering("direccion.contains=" + DEFAULT_DIRECCION, "direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPersonasByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where direccion does not contain
        defaultPersonaFiltering("direccion.doesNotContain=" + UPDATED_DIRECCION, "direccion.doesNotContain=" + DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPersonasByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where activo equals to
        defaultPersonaFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllPersonasByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where activo in
        defaultPersonaFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllPersonasByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where activo is not null
        defaultPersonaFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta equals to
        defaultPersonaFiltering("fechaAlta.equals=" + DEFAULT_FECHA_ALTA, "fechaAlta.equals=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta in
        defaultPersonaFiltering("fechaAlta.in=" + DEFAULT_FECHA_ALTA + "," + UPDATED_FECHA_ALTA, "fechaAlta.in=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta is not null
        defaultPersonaFiltering("fechaAlta.specified=true", "fechaAlta.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta is greater than or equal to
        defaultPersonaFiltering("fechaAlta.greaterThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.greaterThanOrEqual=" + UPDATED_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta is less than or equal to
        defaultPersonaFiltering("fechaAlta.lessThanOrEqual=" + DEFAULT_FECHA_ALTA, "fechaAlta.lessThanOrEqual=" + SMALLER_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta is less than
        defaultPersonaFiltering("fechaAlta.lessThan=" + UPDATED_FECHA_ALTA, "fechaAlta.lessThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaAltaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaAlta is greater than
        defaultPersonaFiltering("fechaAlta.greaterThan=" + SMALLER_FECHA_ALTA, "fechaAlta.greaterThan=" + DEFAULT_FECHA_ALTA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja equals to
        defaultPersonaFiltering("fechaBaja.equals=" + DEFAULT_FECHA_BAJA, "fechaBaja.equals=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja in
        defaultPersonaFiltering("fechaBaja.in=" + DEFAULT_FECHA_BAJA + "," + UPDATED_FECHA_BAJA, "fechaBaja.in=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja is not null
        defaultPersonaFiltering("fechaBaja.specified=true", "fechaBaja.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja is greater than or equal to
        defaultPersonaFiltering("fechaBaja.greaterThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.greaterThanOrEqual=" + UPDATED_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja is less than or equal to
        defaultPersonaFiltering("fechaBaja.lessThanOrEqual=" + DEFAULT_FECHA_BAJA, "fechaBaja.lessThanOrEqual=" + SMALLER_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja is less than
        defaultPersonaFiltering("fechaBaja.lessThan=" + UPDATED_FECHA_BAJA, "fechaBaja.lessThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPersonasByFechaBajaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList where fechaBaja is greater than
        defaultPersonaFiltering("fechaBaja.greaterThan=" + SMALLER_FECHA_BAJA, "fechaBaja.greaterThan=" + DEFAULT_FECHA_BAJA);
    }

    @Test
    @Transactional
    void getAllPersonasByTipoDocumentoIsEqualToSomething() throws Exception {
        TipoDocumento tipoDocumento;
        if (TestUtil.findAll(em, TipoDocumento.class).isEmpty()) {
            personaRepository.saveAndFlush(persona);
            tipoDocumento = TipoDocumentoResourceIT.createEntity();
        } else {
            tipoDocumento = TestUtil.findAll(em, TipoDocumento.class).get(0);
        }
        em.persist(tipoDocumento);
        em.flush();
        persona.setTipoDocumento(tipoDocumento);
        personaRepository.saveAndFlush(persona);
        Long tipoDocumentoId = tipoDocumento.getId();
        // Get all the personaList where tipoDocumento equals to tipoDocumentoId
        defaultPersonaShouldBeFound("tipoDocumentoId.equals=" + tipoDocumentoId);

        // Get all the personaList where tipoDocumento equals to (tipoDocumentoId + 1)
        defaultPersonaShouldNotBeFound("tipoDocumentoId.equals=" + (tipoDocumentoId + 1));
    }

    @Test
    @Transactional
    void getAllPersonasBySexoIsEqualToSomething() throws Exception {
        Sexo sexo;
        if (TestUtil.findAll(em, Sexo.class).isEmpty()) {
            personaRepository.saveAndFlush(persona);
            sexo = SexoResourceIT.createEntity();
        } else {
            sexo = TestUtil.findAll(em, Sexo.class).get(0);
        }
        em.persist(sexo);
        em.flush();
        persona.setSexo(sexo);
        personaRepository.saveAndFlush(persona);
        Long sexoId = sexo.getId();
        // Get all the personaList where sexo equals to sexoId
        defaultPersonaShouldBeFound("sexoId.equals=" + sexoId);

        // Get all the personaList where sexo equals to (sexoId + 1)
        defaultPersonaShouldNotBeFound("sexoId.equals=" + (sexoId + 1));
    }

    @Test
    @Transactional
    void getAllPersonasByCiudadIsEqualToSomething() throws Exception {
        Ciudad ciudad;
        if (TestUtil.findAll(em, Ciudad.class).isEmpty()) {
            personaRepository.saveAndFlush(persona);
            ciudad = CiudadResourceIT.createEntity(em);
        } else {
            ciudad = TestUtil.findAll(em, Ciudad.class).get(0);
        }
        em.persist(ciudad);
        em.flush();
        persona.setCiudad(ciudad);
        personaRepository.saveAndFlush(persona);
        Long ciudadId = ciudad.getId();
        // Get all the personaList where ciudad equals to ciudadId
        defaultPersonaShouldBeFound("ciudadId.equals=" + ciudadId);

        // Get all the personaList where ciudad equals to (ciudadId + 1)
        defaultPersonaShouldNotBeFound("ciudadId.equals=" + (ciudadId + 1));
    }

    private void defaultPersonaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPersonaShouldBeFound(shouldBeFound);
        defaultPersonaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonaShouldBeFound(String filter) throws Exception {
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].nroDocumento").value(hasItem(DEFAULT_NRO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
            .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())));

        // Check, that the count call also returns 1
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonaShouldNotBeFound(String filter) throws Exception {
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersona() throws Exception {
        // Get the persona
        restPersonaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersona() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the persona
        Persona updatedPersona = personaRepository.findById(persona.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersona are not directly saved in db
        em.detach(updatedPersona);
        updatedPersona
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .nroDocumento(UPDATED_NRO_DOCUMENTO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);
        PersonaDTO personaDTO = personaMapper.toDto(updatedPersona);

        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonaToMatchAllProperties(updatedPersona);
    }

    @Test
    @Transactional
    void putNonExistingPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .nombre(UPDATED_NOMBRE)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA);

        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPersona, persona), getPersistedPersona(persona));
    }

    @Test
    @Transactional
    void fullUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .nroDocumento(UPDATED_NRO_DOCUMENTO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .direccion(UPDATED_DIRECCION)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .fechaBaja(UPDATED_FECHA_BAJA);

        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonaUpdatableFieldsEquals(partialUpdatedPersona, getPersistedPersona(partialUpdatedPersona));
    }

    @Test
    @Transactional
    void patchNonExistingPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(personaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(personaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersona() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the persona
        restPersonaMockMvc
            .perform(delete(ENTITY_API_URL_ID, persona.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personaRepository.count();
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

    protected Persona getPersistedPersona(Persona persona) {
        return personaRepository.findById(persona.getId()).orElseThrow();
    }

    protected void assertPersistedPersonaToMatchAllProperties(Persona expectedPersona) {
        assertPersonaAllPropertiesEquals(expectedPersona, getPersistedPersona(expectedPersona));
    }

    protected void assertPersistedPersonaToMatchUpdatableProperties(Persona expectedPersona) {
        assertPersonaAllUpdatablePropertiesEquals(expectedPersona, getPersistedPersona(expectedPersona));
    }
}
