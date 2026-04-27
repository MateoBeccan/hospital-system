import { Authority } from '@/shared/jhipster/constants';
const Entities = () => import('@/entities/entities.vue');

const TipoDocumento = () => import('@/entities/tipo-documento/tipo-documento.vue');
const TipoDocumentoUpdate = () => import('@/entities/tipo-documento/tipo-documento-update.vue');
const TipoDocumentoDetails = () => import('@/entities/tipo-documento/tipo-documento-details.vue');

const Sexo = () => import('@/entities/sexo/sexo.vue');
const SexoUpdate = () => import('@/entities/sexo/sexo-update.vue');
const SexoDetails = () => import('@/entities/sexo/sexo-details.vue');

const Pais = () => import('@/entities/pais/pais.vue');
const PaisUpdate = () => import('@/entities/pais/pais-update.vue');
const PaisDetails = () => import('@/entities/pais/pais-details.vue');

const Provincia = () => import('@/entities/provincia/provincia.vue');
const ProvinciaUpdate = () => import('@/entities/provincia/provincia-update.vue');
const ProvinciaDetails = () => import('@/entities/provincia/provincia-details.vue');

const Ciudad = () => import('@/entities/ciudad/ciudad.vue');
const CiudadUpdate = () => import('@/entities/ciudad/ciudad-update.vue');
const CiudadDetails = () => import('@/entities/ciudad/ciudad-details.vue');

const Persona = () => import('@/entities/persona/persona.vue');
const PersonaUpdate = () => import('@/entities/persona/persona-update.vue');
const PersonaDetails = () => import('@/entities/persona/persona-details.vue');

const ContactoEmergencia = () => import('@/entities/contacto-emergencia/contacto-emergencia.vue');
const ContactoEmergenciaUpdate = () => import('@/entities/contacto-emergencia/contacto-emergencia-update.vue');
const ContactoEmergenciaDetails = () => import('@/entities/contacto-emergencia/contacto-emergencia-details.vue');

const ObraSocial = () => import('@/entities/obra-social/obra-social.vue');
const ObraSocialUpdate = () => import('@/entities/obra-social/obra-social-update.vue');
const ObraSocialDetails = () => import('@/entities/obra-social/obra-social-details.vue');

const GrupoSanguineo = () => import('@/entities/grupo-sanguineo/grupo-sanguineo.vue');
const GrupoSanguineoUpdate = () => import('@/entities/grupo-sanguineo/grupo-sanguineo-update.vue');
const GrupoSanguineoDetails = () => import('@/entities/grupo-sanguineo/grupo-sanguineo-details.vue');

const FactorRh = () => import('@/entities/factor-rh/factor-rh.vue');
const FactorRhUpdate = () => import('@/entities/factor-rh/factor-rh-update.vue');
const FactorRhDetails = () => import('@/entities/factor-rh/factor-rh-details.vue');

const Paciente = () => import('@/entities/paciente/paciente.vue');
const PacienteUpdate = () => import('@/entities/paciente/paciente-update.vue');
const PacienteDetails = () => import('@/entities/paciente/paciente-details.vue');

const TipoEmpleado = () => import('@/entities/tipo-empleado/tipo-empleado.vue');
const TipoEmpleadoUpdate = () => import('@/entities/tipo-empleado/tipo-empleado-update.vue');
const TipoEmpleadoDetails = () => import('@/entities/tipo-empleado/tipo-empleado-details.vue');

const EstadoLaboral = () => import('@/entities/estado-laboral/estado-laboral.vue');
const EstadoLaboralUpdate = () => import('@/entities/estado-laboral/estado-laboral-update.vue');
const EstadoLaboralDetails = () => import('@/entities/estado-laboral/estado-laboral-details.vue');

const Cargo = () => import('@/entities/cargo/cargo.vue');
const CargoUpdate = () => import('@/entities/cargo/cargo-update.vue');
const CargoDetails = () => import('@/entities/cargo/cargo-details.vue');

const Empleado = () => import('@/entities/empleado/empleado.vue');
const EmpleadoUpdate = () => import('@/entities/empleado/empleado-update.vue');
const EmpleadoDetails = () => import('@/entities/empleado/empleado-details.vue');

const Especialidad = () => import('@/entities/especialidad/especialidad.vue');
const EspecialidadUpdate = () => import('@/entities/especialidad/especialidad-update.vue');
const EspecialidadDetails = () => import('@/entities/especialidad/especialidad-details.vue');

const Medico = () => import('@/entities/medico/medico.vue');
const MedicoUpdate = () => import('@/entities/medico/medico-update.vue');
const MedicoDetails = () => import('@/entities/medico/medico-details.vue');

const TurnoLaboral = () => import('@/entities/turno-laboral/turno-laboral.vue');
const TurnoLaboralUpdate = () => import('@/entities/turno-laboral/turno-laboral-update.vue');
const TurnoLaboralDetails = () => import('@/entities/turno-laboral/turno-laboral-details.vue');

const Enfermero = () => import('@/entities/enfermero/enfermero.vue');
const EnfermeroUpdate = () => import('@/entities/enfermero/enfermero-update.vue');
const EnfermeroDetails = () => import('@/entities/enfermero/enfermero-details.vue');

const HistoriaClinica = () => import('@/entities/historia-clinica/historia-clinica.vue');
const HistoriaClinicaUpdate = () => import('@/entities/historia-clinica/historia-clinica-update.vue');
const HistoriaClinicaDetails = () => import('@/entities/historia-clinica/historia-clinica-details.vue');

const AntecedenteClinico = () => import('@/entities/antecedente-clinico/antecedente-clinico.vue');
const AntecedenteClinicoUpdate = () => import('@/entities/antecedente-clinico/antecedente-clinico-update.vue');
const AntecedenteClinicoDetails = () => import('@/entities/antecedente-clinico/antecedente-clinico-details.vue');

const EstadoTurno = () => import('@/entities/estado-turno/estado-turno.vue');
const EstadoTurnoUpdate = () => import('@/entities/estado-turno/estado-turno-update.vue');
const EstadoTurnoDetails = () => import('@/entities/estado-turno/estado-turno-details.vue');

const CanalSolicitud = () => import('@/entities/canal-solicitud/canal-solicitud.vue');
const CanalSolicitudUpdate = () => import('@/entities/canal-solicitud/canal-solicitud-update.vue');
const CanalSolicitudDetails = () => import('@/entities/canal-solicitud/canal-solicitud-details.vue');

const TipoDiagnostico = () => import('@/entities/tipo-diagnostico/tipo-diagnostico.vue');
const TipoDiagnosticoUpdate = () => import('@/entities/tipo-diagnostico/tipo-diagnostico-update.vue');
const TipoDiagnosticoDetails = () => import('@/entities/tipo-diagnostico/tipo-diagnostico-details.vue');

const EstadoDiagnostico = () => import('@/entities/estado-diagnostico/estado-diagnostico.vue');
const EstadoDiagnosticoUpdate = () => import('@/entities/estado-diagnostico/estado-diagnostico-update.vue');
const EstadoDiagnosticoDetails = () => import('@/entities/estado-diagnostico/estado-diagnostico-details.vue');

const EstadoTratamiento = () => import('@/entities/estado-tratamiento/estado-tratamiento.vue');
const EstadoTratamientoUpdate = () => import('@/entities/estado-tratamiento/estado-tratamiento-update.vue');
const EstadoTratamientoDetails = () => import('@/entities/estado-tratamiento/estado-tratamiento-details.vue');

const Turno = () => import('@/entities/turno/turno.vue');
const TurnoUpdate = () => import('@/entities/turno/turno-update.vue');
const TurnoDetails = () => import('@/entities/turno/turno-details.vue');

const Consulta = () => import('@/entities/consulta/consulta.vue');
const ConsultaUpdate = () => import('@/entities/consulta/consulta-update.vue');
const ConsultaDetails = () => import('@/entities/consulta/consulta-details.vue');

const SignosVitales = () => import('@/entities/signos-vitales/signos-vitales.vue');
const SignosVitalesUpdate = () => import('@/entities/signos-vitales/signos-vitales-update.vue');
const SignosVitalesDetails = () => import('@/entities/signos-vitales/signos-vitales-details.vue');

const Diagnostico = () => import('@/entities/diagnostico/diagnostico.vue');
const DiagnosticoUpdate = () => import('@/entities/diagnostico/diagnostico-update.vue');
const DiagnosticoDetails = () => import('@/entities/diagnostico/diagnostico-details.vue');

const Tratamiento = () => import('@/entities/tratamiento/tratamiento.vue');
const TratamientoUpdate = () => import('@/entities/tratamiento/tratamiento-update.vue');
const TratamientoDetails = () => import('@/entities/tratamiento/tratamiento-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'tipo-documento',
      name: 'TipoDocumento',
      component: TipoDocumento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-documento/new',
      name: 'TipoDocumentoCreate',
      component: TipoDocumentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-documento/:tipoDocumentoId/edit',
      name: 'TipoDocumentoEdit',
      component: TipoDocumentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-documento/:tipoDocumentoId/view',
      name: 'TipoDocumentoView',
      component: TipoDocumentoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sexo',
      name: 'Sexo',
      component: Sexo,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sexo/new',
      name: 'SexoCreate',
      component: SexoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sexo/:sexoId/edit',
      name: 'SexoEdit',
      component: SexoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sexo/:sexoId/view',
      name: 'SexoView',
      component: SexoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais',
      name: 'Pais',
      component: Pais,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais/new',
      name: 'PaisCreate',
      component: PaisUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais/:paisId/edit',
      name: 'PaisEdit',
      component: PaisUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais/:paisId/view',
      name: 'PaisView',
      component: PaisDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'provincia',
      name: 'Provincia',
      component: Provincia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'provincia/new',
      name: 'ProvinciaCreate',
      component: ProvinciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'provincia/:provinciaId/edit',
      name: 'ProvinciaEdit',
      component: ProvinciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'provincia/:provinciaId/view',
      name: 'ProvinciaView',
      component: ProvinciaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad',
      name: 'Ciudad',
      component: Ciudad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad/new',
      name: 'CiudadCreate',
      component: CiudadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad/:ciudadId/edit',
      name: 'CiudadEdit',
      component: CiudadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'ciudad/:ciudadId/view',
      name: 'CiudadView',
      component: CiudadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'persona',
      name: 'Persona',
      component: Persona,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'persona/new',
      name: 'PersonaCreate',
      component: PersonaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'persona/:personaId/edit',
      name: 'PersonaEdit',
      component: PersonaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'persona/:personaId/view',
      name: 'PersonaView',
      component: PersonaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contacto-emergencia',
      name: 'ContactoEmergencia',
      component: ContactoEmergencia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contacto-emergencia/new',
      name: 'ContactoEmergenciaCreate',
      component: ContactoEmergenciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contacto-emergencia/:contactoEmergenciaId/edit',
      name: 'ContactoEmergenciaEdit',
      component: ContactoEmergenciaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'contacto-emergencia/:contactoEmergenciaId/view',
      name: 'ContactoEmergenciaView',
      component: ContactoEmergenciaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'obra-social',
      name: 'ObraSocial',
      component: ObraSocial,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'obra-social/new',
      name: 'ObraSocialCreate',
      component: ObraSocialUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'obra-social/:obraSocialId/edit',
      name: 'ObraSocialEdit',
      component: ObraSocialUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'obra-social/:obraSocialId/view',
      name: 'ObraSocialView',
      component: ObraSocialDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grupo-sanguineo',
      name: 'GrupoSanguineo',
      component: GrupoSanguineo,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grupo-sanguineo/new',
      name: 'GrupoSanguineoCreate',
      component: GrupoSanguineoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grupo-sanguineo/:grupoSanguineoId/edit',
      name: 'GrupoSanguineoEdit',
      component: GrupoSanguineoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'grupo-sanguineo/:grupoSanguineoId/view',
      name: 'GrupoSanguineoView',
      component: GrupoSanguineoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factor-rh',
      name: 'FactorRh',
      component: FactorRh,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factor-rh/new',
      name: 'FactorRhCreate',
      component: FactorRhUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factor-rh/:factorRhId/edit',
      name: 'FactorRhEdit',
      component: FactorRhUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'factor-rh/:factorRhId/view',
      name: 'FactorRhView',
      component: FactorRhDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente',
      name: 'Paciente',
      component: Paciente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/new',
      name: 'PacienteCreate',
      component: PacienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/:pacienteId/edit',
      name: 'PacienteEdit',
      component: PacienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/:pacienteId/view',
      name: 'PacienteView',
      component: PacienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-empleado',
      name: 'TipoEmpleado',
      component: TipoEmpleado,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-empleado/new',
      name: 'TipoEmpleadoCreate',
      component: TipoEmpleadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-empleado/:tipoEmpleadoId/edit',
      name: 'TipoEmpleadoEdit',
      component: TipoEmpleadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-empleado/:tipoEmpleadoId/view',
      name: 'TipoEmpleadoView',
      component: TipoEmpleadoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-laboral',
      name: 'EstadoLaboral',
      component: EstadoLaboral,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-laboral/new',
      name: 'EstadoLaboralCreate',
      component: EstadoLaboralUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-laboral/:estadoLaboralId/edit',
      name: 'EstadoLaboralEdit',
      component: EstadoLaboralUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-laboral/:estadoLaboralId/view',
      name: 'EstadoLaboralView',
      component: EstadoLaboralDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo',
      name: 'Cargo',
      component: Cargo,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo/new',
      name: 'CargoCreate',
      component: CargoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo/:cargoId/edit',
      name: 'CargoEdit',
      component: CargoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo/:cargoId/view',
      name: 'CargoView',
      component: CargoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado',
      name: 'Empleado',
      component: Empleado,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado/new',
      name: 'EmpleadoCreate',
      component: EmpleadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado/:empleadoId/edit',
      name: 'EmpleadoEdit',
      component: EmpleadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'empleado/:empleadoId/view',
      name: 'EmpleadoView',
      component: EmpleadoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'especialidad',
      name: 'Especialidad',
      component: Especialidad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'especialidad/new',
      name: 'EspecialidadCreate',
      component: EspecialidadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'especialidad/:especialidadId/edit',
      name: 'EspecialidadEdit',
      component: EspecialidadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'especialidad/:especialidadId/view',
      name: 'EspecialidadView',
      component: EspecialidadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico',
      name: 'Medico',
      component: Medico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico/new',
      name: 'MedicoCreate',
      component: MedicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico/:medicoId/edit',
      name: 'MedicoEdit',
      component: MedicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico/:medicoId/view',
      name: 'MedicoView',
      component: MedicoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno-laboral',
      name: 'TurnoLaboral',
      component: TurnoLaboral,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno-laboral/new',
      name: 'TurnoLaboralCreate',
      component: TurnoLaboralUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno-laboral/:turnoLaboralId/edit',
      name: 'TurnoLaboralEdit',
      component: TurnoLaboralUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno-laboral/:turnoLaboralId/view',
      name: 'TurnoLaboralView',
      component: TurnoLaboralDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermero',
      name: 'Enfermero',
      component: Enfermero,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermero/new',
      name: 'EnfermeroCreate',
      component: EnfermeroUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermero/:enfermeroId/edit',
      name: 'EnfermeroEdit',
      component: EnfermeroUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermero/:enfermeroId/view',
      name: 'EnfermeroView',
      component: EnfermeroDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica',
      name: 'HistoriaClinica',
      component: HistoriaClinica,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica/new',
      name: 'HistoriaClinicaCreate',
      component: HistoriaClinicaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica/:historiaClinicaId/edit',
      name: 'HistoriaClinicaEdit',
      component: HistoriaClinicaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica/:historiaClinicaId/view',
      name: 'HistoriaClinicaView',
      component: HistoriaClinicaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'antecedente-clinico',
      name: 'AntecedenteClinico',
      component: AntecedenteClinico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'antecedente-clinico/new',
      name: 'AntecedenteClinicoCreate',
      component: AntecedenteClinicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'antecedente-clinico/:antecedenteClinicoId/edit',
      name: 'AntecedenteClinicoEdit',
      component: AntecedenteClinicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'antecedente-clinico/:antecedenteClinicoId/view',
      name: 'AntecedenteClinicoView',
      component: AntecedenteClinicoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-turno',
      name: 'EstadoTurno',
      component: EstadoTurno,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-turno/new',
      name: 'EstadoTurnoCreate',
      component: EstadoTurnoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-turno/:estadoTurnoId/edit',
      name: 'EstadoTurnoEdit',
      component: EstadoTurnoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-turno/:estadoTurnoId/view',
      name: 'EstadoTurnoView',
      component: EstadoTurnoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'canal-solicitud',
      name: 'CanalSolicitud',
      component: CanalSolicitud,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'canal-solicitud/new',
      name: 'CanalSolicitudCreate',
      component: CanalSolicitudUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'canal-solicitud/:canalSolicitudId/edit',
      name: 'CanalSolicitudEdit',
      component: CanalSolicitudUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'canal-solicitud/:canalSolicitudId/view',
      name: 'CanalSolicitudView',
      component: CanalSolicitudDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-diagnostico',
      name: 'TipoDiagnostico',
      component: TipoDiagnostico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-diagnostico/new',
      name: 'TipoDiagnosticoCreate',
      component: TipoDiagnosticoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-diagnostico/:tipoDiagnosticoId/edit',
      name: 'TipoDiagnosticoEdit',
      component: TipoDiagnosticoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-diagnostico/:tipoDiagnosticoId/view',
      name: 'TipoDiagnosticoView',
      component: TipoDiagnosticoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-diagnostico',
      name: 'EstadoDiagnostico',
      component: EstadoDiagnostico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-diagnostico/new',
      name: 'EstadoDiagnosticoCreate',
      component: EstadoDiagnosticoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-diagnostico/:estadoDiagnosticoId/edit',
      name: 'EstadoDiagnosticoEdit',
      component: EstadoDiagnosticoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-diagnostico/:estadoDiagnosticoId/view',
      name: 'EstadoDiagnosticoView',
      component: EstadoDiagnosticoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-tratamiento',
      name: 'EstadoTratamiento',
      component: EstadoTratamiento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-tratamiento/new',
      name: 'EstadoTratamientoCreate',
      component: EstadoTratamientoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-tratamiento/:estadoTratamientoId/edit',
      name: 'EstadoTratamientoEdit',
      component: EstadoTratamientoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado-tratamiento/:estadoTratamientoId/view',
      name: 'EstadoTratamientoView',
      component: EstadoTratamientoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno',
      name: 'Turno',
      component: Turno,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno/new',
      name: 'TurnoCreate',
      component: TurnoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno/:turnoId/edit',
      name: 'TurnoEdit',
      component: TurnoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'turno/:turnoId/view',
      name: 'TurnoView',
      component: TurnoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'consulta',
      name: 'Consulta',
      component: Consulta,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'consulta/new',
      name: 'ConsultaCreate',
      component: ConsultaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'consulta/:consultaId/edit',
      name: 'ConsultaEdit',
      component: ConsultaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'consulta/:consultaId/view',
      name: 'ConsultaView',
      component: ConsultaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales',
      name: 'SignosVitales',
      component: SignosVitales,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/new',
      name: 'SignosVitalesCreate',
      component: SignosVitalesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/:signosVitalesId/edit',
      name: 'SignosVitalesEdit',
      component: SignosVitalesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/:signosVitalesId/view',
      name: 'SignosVitalesView',
      component: SignosVitalesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'diagnostico',
      name: 'Diagnostico',
      component: Diagnostico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'diagnostico/new',
      name: 'DiagnosticoCreate',
      component: DiagnosticoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'diagnostico/:diagnosticoId/edit',
      name: 'DiagnosticoEdit',
      component: DiagnosticoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'diagnostico/:diagnosticoId/view',
      name: 'DiagnosticoView',
      component: DiagnosticoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tratamiento',
      name: 'Tratamiento',
      component: Tratamiento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tratamiento/new',
      name: 'TratamientoCreate',
      component: TratamientoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tratamiento/:tratamientoId/edit',
      name: 'TratamientoEdit',
      component: TratamientoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tratamiento/:tratamientoId/view',
      name: 'TratamientoView',
      component: TratamientoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
