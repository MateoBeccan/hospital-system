import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
import MedicoService from '@/entities/medico/medico.service';
import PacienteService from '@/entities/paciente/paciente.service';
import TurnoService from '@/entities/turno/turno.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { Consulta, type IConsulta } from '@/shared/model/consulta.model';
import { type IHistoriaClinica } from '@/shared/model/historia-clinica.model';
import { type IMedico } from '@/shared/model/medico.model';
import { type IPaciente } from '@/shared/model/paciente.model';
import { type ITurno } from '@/shared/model/turno.model';

import ConsultaService from './consulta.service';

export default defineComponent({
  name: 'ConsultaUpdate',
  setup() {
    const consultaService = inject('consultaService', () => new ConsultaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const consulta: Ref<IConsulta> = ref(new Consulta());

    const turnoService = inject('turnoService', () => new TurnoService());

    const turnos: Ref<ITurno[]> = ref([]);

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);

    const medicoService = inject('medicoService', () => new MedicoService());

    const medicos: Ref<IMedico[]> = ref([]);

    const historiaClinicaService = inject('historiaClinicaService', () => new HistoriaClinicaService());

    const historiaClinicas: Ref<IHistoriaClinica[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveConsulta = async consultaId => {
      try {
        const res = await consultaService().find(consultaId);
        res.fechaHoraInicio = new Date(res.fechaHoraInicio);
        res.fechaHoraFin = new Date(res.fechaHoraFin);
        consulta.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.consultaId) {
      retrieveConsulta(route.params.consultaId);
    }

    const initRelationships = () => {
      turnoService()
        .retrieve()
        .then(res => {
          turnos.value = res.data;
        });
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
      historiaClinicaService()
        .retrieve()
        .then(res => {
          historiaClinicas.value = res.data;
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
      fechaHoraInicio: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaHoraFin: {},
      sintomas: {},
      motivoConsulta: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      examenFisico: {},
      observaciones: {},
      indicaciones: {},
      activa: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      turno: {},
      paciente: {
        required: validations.required('Este campo es obligatorio.'),
      },
      medico: {
        required: validations.required('Este campo es obligatorio.'),
      },
      historiaClinica: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, consulta as any);
    v$.value.$validate();

    return {
      consultaService,
      alertService,
      consulta,
      previousState,
      isSaving,
      currentLanguage,
      turnos,
      pacientes,
      medicos,
      historiaClinicas,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: consulta }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.consulta.id) {
        this.consultaService()
          .update(this.consulta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Consulta is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.consultaService()
          .create(this.consulta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Consulta is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
