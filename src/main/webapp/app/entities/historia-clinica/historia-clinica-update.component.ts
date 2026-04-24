import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PacienteService from '@/entities/paciente/paciente.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { HistoriaClinica, type IHistoriaClinica } from '@/shared/model/historia-clinica.model';
import { type IPaciente } from '@/shared/model/paciente.model';

import HistoriaClinicaService from './historia-clinica.service';

export default defineComponent({
  name: 'HistoriaClinicaUpdate',
  setup() {
    const historiaClinicaService = inject('historiaClinicaService', () => new HistoriaClinicaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const historiaClinica: Ref<IHistoriaClinica> = ref(new HistoriaClinica());

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveHistoriaClinica = async historiaClinicaId => {
      try {
        const res = await historiaClinicaService().find(historiaClinicaId);
        historiaClinica.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historiaClinicaId) {
      retrieveHistoriaClinica(route.params.historiaClinicaId);
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      numero: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 40 caracteres.', 40),
      },
      fechaApertura: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaUltimaActualizacion: {},
      antecedentesPersonales: {},
      antecedentesFamiliares: {},
      enfermedadesPrevias: {},
      cirugiasPrevias: {},
      alergias: {},
      medicacionHabitual: {},
      habitos: {},
      observacionesGenerales: {},
      activa: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaCierre: {},
      motivoCierre: {
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      paciente: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, historiaClinica as any);
    v$.value.$validate();

    return {
      historiaClinicaService,
      alertService,
      historiaClinica,
      previousState,
      isSaving,
      currentLanguage,
      pacientes,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.historiaClinica.id) {
        this.historiaClinicaService()
          .update(this.historiaClinica)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A HistoriaClinica is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.historiaClinicaService()
          .create(this.historiaClinica)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A HistoriaClinica is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
