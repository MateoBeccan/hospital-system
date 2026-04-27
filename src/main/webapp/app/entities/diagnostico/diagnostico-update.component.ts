import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import ConsultaService from '@/entities/consulta/consulta.service';
import EstadoDiagnosticoService from '@/entities/estado-diagnostico/estado-diagnostico.service';
import MedicoService from '@/entities/medico/medico.service';
import PacienteService from '@/entities/paciente/paciente.service';
import TipoDiagnosticoService from '@/entities/tipo-diagnostico/tipo-diagnostico.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IConsulta } from '@/shared/model/consulta.model';
import { Diagnostico, type IDiagnostico } from '@/shared/model/diagnostico.model';
import { type IEstadoDiagnostico } from '@/shared/model/estado-diagnostico.model';
import { type IMedico } from '@/shared/model/medico.model';
import { type IPaciente } from '@/shared/model/paciente.model';
import { type ITipoDiagnostico } from '@/shared/model/tipo-diagnostico.model';

import DiagnosticoService from './diagnostico.service';

export default defineComponent({
  name: 'DiagnosticoUpdate',
  setup() {
    const diagnosticoService = inject('diagnosticoService', () => new DiagnosticoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const diagnostico: Ref<IDiagnostico> = ref(new Diagnostico());

    const consultaService = inject('consultaService', () => new ConsultaService());

    const consultas: Ref<IConsulta[]> = ref([]);

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);

    const medicoService = inject('medicoService', () => new MedicoService());

    const medicos: Ref<IMedico[]> = ref([]);

    const tipoDiagnosticoService = inject('tipoDiagnosticoService', () => new TipoDiagnosticoService());

    const tipoDiagnosticos: Ref<ITipoDiagnostico[]> = ref([]);

    const estadoDiagnosticoService = inject('estadoDiagnosticoService', () => new EstadoDiagnosticoService());

    const estadoDiagnosticos: Ref<IEstadoDiagnostico[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDiagnostico = async diagnosticoId => {
      try {
        const res = await diagnosticoService().find(diagnosticoId);
        diagnostico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.diagnosticoId) {
      retrieveDiagnostico(route.params.diagnosticoId);
    }

    const initRelationships = () => {
      consultaService()
        .retrieve()
        .then(res => {
          consultas.value = res.data;
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
      tipoDiagnosticoService()
        .retrieve()
        .then(res => {
          tipoDiagnosticos.value = res.data;
        });
      estadoDiagnosticoService()
        .retrieve()
        .then(res => {
          estadoDiagnosticos.value = res.data;
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
      fechaDiagnostico: {
        required: validations.required('Este campo es obligatorio.'),
      },
      descripcion: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      observaciones: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaResolucion: {},
      esPrincipal: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      consulta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      paciente: {
        required: validations.required('Este campo es obligatorio.'),
      },
      medico: {
        required: validations.required('Este campo es obligatorio.'),
      },
      tipoDiagnostico: {
        required: validations.required('Este campo es obligatorio.'),
      },
      estadoDiagnostico: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, diagnostico as any);
    v$.value.$validate();

    return {
      diagnosticoService,
      alertService,
      diagnostico,
      previousState,
      isSaving,
      currentLanguage,
      consultas,
      pacientes,
      medicos,
      tipoDiagnosticos,
      estadoDiagnosticos,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.diagnostico.id) {
        this.diagnosticoService()
          .update(this.diagnostico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Diagnostico is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.diagnosticoService()
          .create(this.diagnostico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Diagnostico is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
