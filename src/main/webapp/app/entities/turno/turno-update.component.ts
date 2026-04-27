import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import CanalSolicitudService from '@/entities/canal-solicitud/canal-solicitud.service';
import EspecialidadService from '@/entities/especialidad/especialidad.service';
import EstadoTurnoService from '@/entities/estado-turno/estado-turno.service';
import MedicoService from '@/entities/medico/medico.service';
import PacienteService from '@/entities/paciente/paciente.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ICanalSolicitud } from '@/shared/model/canal-solicitud.model';
import { type IEspecialidad } from '@/shared/model/especialidad.model';
import { type IEstadoTurno } from '@/shared/model/estado-turno.model';
import { type IMedico } from '@/shared/model/medico.model';
import { type IPaciente } from '@/shared/model/paciente.model';
import { type ITurno, Turno } from '@/shared/model/turno.model';

import TurnoService from './turno.service';

export default defineComponent({
  name: 'TurnoUpdate',
  setup() {
    const turnoService = inject('turnoService', () => new TurnoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const turno: Ref<ITurno> = ref(new Turno());

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);

    const medicoService = inject('medicoService', () => new MedicoService());

    const medicos: Ref<IMedico[]> = ref([]);

    const especialidadService = inject('especialidadService', () => new EspecialidadService());

    const especialidads: Ref<IEspecialidad[]> = ref([]);

    const estadoTurnoService = inject('estadoTurnoService', () => new EstadoTurnoService());

    const estadoTurnos: Ref<IEstadoTurno[]> = ref([]);

    const canalSolicitudService = inject('canalSolicitudService', () => new CanalSolicitudService());

    const canalSolicituds: Ref<ICanalSolicitud[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTurno = async turnoId => {
      try {
        const res = await turnoService().find(turnoId);
        res.fechaHora = new Date(res.fechaHora);
        res.fechaCreacion = new Date(res.fechaCreacion);
        turno.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.turnoId) {
      retrieveTurno(route.params.turnoId);
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
      medicoService()
        .retrieve()
        .then(res => {
          medicos.value = res.data;
        });
      especialidadService()
        .retrieve()
        .then(res => {
          especialidads.value = res.data;
        });
      estadoTurnoService()
        .retrieve()
        .then(res => {
          estadoTurnos.value = res.data;
        });
      canalSolicitudService()
        .retrieve()
        .then(res => {
          canalSolicituds.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      codigo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 40 caracteres.', 40),
      },
      fechaHora: {
        required: validations.required('Este campo es obligatorio.'),
      },
      duracionMinutos: {
        required: validations.required('Este campo es obligatorio.'),
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 5.', 5),
        max: validations.maxValue('Este campo no puede ser mayor que 240.', 240),
      },
      motivoConsulta: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      observaciones: {},
      fechaCreacion: {
        required: validations.required('Este campo es obligatorio.'),
      },
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      paciente: {
        required: validations.required('Este campo es obligatorio.'),
      },
      medico: {
        required: validations.required('Este campo es obligatorio.'),
      },
      especialidad: {
        required: validations.required('Este campo es obligatorio.'),
      },
      estadoTurno: {
        required: validations.required('Este campo es obligatorio.'),
      },
      canalSolicitud: {
        required: validations.required('Este campo es obligatorio.'),
      },
      consulta: {},
    };
    const v$ = useVuelidate(validationRules, turno as any);
    v$.value.$validate();

    return {
      turnoService,
      alertService,
      turno,
      previousState,
      isSaving,
      currentLanguage,
      pacientes,
      medicos,
      especialidads,
      estadoTurnos,
      canalSolicituds,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: turno }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.turno.id) {
        this.turnoService()
          .update(this.turno)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Turno is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.turnoService()
          .create(this.turno)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Turno is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
