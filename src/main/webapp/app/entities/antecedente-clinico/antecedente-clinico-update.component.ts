import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { AntecedenteClinico, type IAntecedenteClinico } from '@/shared/model/antecedente-clinico.model';
import { type IHistoriaClinica } from '@/shared/model/historia-clinica.model';

import AntecedenteClinicoService from './antecedente-clinico.service';

export default defineComponent({
  name: 'AntecedenteClinicoUpdate',
  setup() {
    const antecedenteClinicoService = inject('antecedenteClinicoService', () => new AntecedenteClinicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const antecedenteClinico: Ref<IAntecedenteClinico> = ref(new AntecedenteClinico());

    const historiaClinicaService = inject('historiaClinicaService', () => new HistoriaClinicaService());

    const historiaClinicas: Ref<IHistoriaClinica[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAntecedenteClinico = async antecedenteClinicoId => {
      try {
        const res = await antecedenteClinicoService().find(antecedenteClinicoId);
        antecedenteClinico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.antecedenteClinicoId) {
      retrieveAntecedenteClinico(route.params.antecedenteClinicoId);
    }

    const initRelationships = () => {
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
      titulo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      descripcion: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      fechaRegistro: {
        required: validations.required('Este campo es obligatorio.'),
      },
      observaciones: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      historiaClinica: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, antecedenteClinico as any);
    v$.value.$validate();

    return {
      antecedenteClinicoService,
      alertService,
      antecedenteClinico,
      previousState,
      isSaving,
      currentLanguage,
      historiaClinicas,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.antecedenteClinico.id) {
        this.antecedenteClinicoService()
          .update(this.antecedenteClinico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A AntecedenteClinico is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.antecedenteClinicoService()
          .create(this.antecedenteClinico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A AntecedenteClinico is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
