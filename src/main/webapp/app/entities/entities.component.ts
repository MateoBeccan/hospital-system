import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import PacienteService from './paciente/paciente.service';
import TipoEmpleadoService from './tipo-empleado/tipo-empleado.service';
import EstadoLaboralService from './estado-laboral/estado-laboral.service';
import CargoService from './cargo/cargo.service';
import EmpleadoService from './empleado/empleado.service';
import EspecialidadService from './especialidad/especialidad.service';
import MedicoService from './medico/medico.service';
import TurnoLaboralService from './turno-laboral/turno-laboral.service';
import EnfermeroService from './enfermero/enfermero.service';
import HistoriaClinicaService from './historia-clinica/historia-clinica.service';
import AntecedenteClinicoService from './antecedente-clinico/antecedente-clinico.service';
import EstadoTurnoService from './estado-turno/estado-turno.service';
import CanalSolicitudService from './canal-solicitud/canal-solicitud.service';
import CiudadService from './ciudad/ciudad.service';
import TipoDiagnosticoService from './tipo-diagnostico/tipo-diagnostico.service';
import EstadoTratamientoService from './estado-tratamiento/estado-tratamiento.service';
import TurnoService from './turno/turno.service';
import ConsultaService from './consulta/consulta.service';
import ContactoEmergenciaService from './contacto-emergencia/contacto-emergencia.service';
import SignosVitalesService from './signos-vitales/signos-vitales.service';
import DiagnosticoService from './diagnostico/diagnostico.service';
import EstadoDiagnosticoService from './estado-diagnostico/estado-diagnostico.service';
import FactorRhService from './factor-rh/factor-rh.service';
import GrupoSanguineoService from './grupo-sanguineo/grupo-sanguineo.service';
import ObraSocialService from './obra-social/obra-social.service';
import PaisService from './pais/pais.service';
import PersonaService from './persona/persona.service';
import ProvinciaService from './provincia/provincia.service';
import SexoService from './sexo/sexo.service';
import TipoDocumentoService from './tipo-documento/tipo-documento.service';
import TratamientoService from './tratamiento/tratamiento.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('tipoDocumentoService', () => new TipoDocumentoService());
    provide('sexoService', () => new SexoService());
    provide('paisService', () => new PaisService());
    provide('provinciaService', () => new ProvinciaService());
    provide('ciudadService', () => new CiudadService());
    provide('personaService', () => new PersonaService());
    provide('contactoEmergenciaService', () => new ContactoEmergenciaService());
    provide('obraSocialService', () => new ObraSocialService());
    provide('grupoSanguineoService', () => new GrupoSanguineoService());
    provide('factorRhService', () => new FactorRhService());
    provide('pacienteService', () => new PacienteService());
    provide('tipoEmpleadoService', () => new TipoEmpleadoService());
    provide('estadoLaboralService', () => new EstadoLaboralService());
    provide('cargoService', () => new CargoService());
    provide('empleadoService', () => new EmpleadoService());
    provide('especialidadService', () => new EspecialidadService());
    provide('medicoService', () => new MedicoService());
    provide('turnoLaboralService', () => new TurnoLaboralService());
    provide('enfermeroService', () => new EnfermeroService());
    provide('historiaClinicaService', () => new HistoriaClinicaService());
    provide('antecedenteClinicoService', () => new AntecedenteClinicoService());
    provide('estadoTurnoService', () => new EstadoTurnoService());
    provide('canalSolicitudService', () => new CanalSolicitudService());
    provide('tipoDiagnosticoService', () => new TipoDiagnosticoService());
    provide('estadoDiagnosticoService', () => new EstadoDiagnosticoService());
    provide('estadoTratamientoService', () => new EstadoTratamientoService());
    provide('turnoService', () => new TurnoService());
    provide('consultaService', () => new ConsultaService());
    provide('signosVitalesService', () => new SignosVitalesService());
    provide('diagnosticoService', () => new DiagnosticoService());
    provide('tratamientoService', () => new TratamientoService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
