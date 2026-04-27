import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ITipoDiagnostico, TipoDiagnostico } from '@/shared/model/tipo-diagnostico.model';

import TipoDiagnosticoService from './tipo-diagnostico.service';

export default defineComponent({
  name: 'TipoDiagnosticoUpdate',
  setup() {
    const tipoDiagnosticoService = inject('tipoDiagnosticoService', () => new TipoDiagnosticoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tipoDiagnostico: Ref<ITipoDiagnostico> = ref(new TipoDiagnostico());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTipoDiagnostico = async tipoDiagnosticoId => {
      try {
        const res = await tipoDiagnosticoService().find(tipoDiagnosticoId);
        tipoDiagnostico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoDiagnosticoId) {
      retrieveTipoDiagnostico(route.params.tipoDiagnosticoId);
    }

    const validations = useValidation();
    const validationRules = {
      codigo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 80 caracteres.', 80),
      },
      descripcion: {
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
    };
    const v$ = useVuelidate(validationRules, tipoDiagnostico as any);
    v$.value.$validate();

    return {
      tipoDiagnosticoService,
      alertService,
      tipoDiagnostico,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tipoDiagnostico.id) {
        this.tipoDiagnosticoService()
          .update(this.tipoDiagnostico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A TipoDiagnostico is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tipoDiagnosticoService()
          .create(this.tipoDiagnostico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A TipoDiagnostico is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
