import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { FactorRh, type IFactorRh } from '@/shared/model/factor-rh.model';

import FactorRhService from './factor-rh.service';

export default defineComponent({
  name: 'FactorRhUpdate',
  setup() {
    const factorRhService = inject('factorRhService', () => new FactorRhService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const factorRh: Ref<IFactorRh> = ref(new FactorRh());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFactorRh = async factorRhId => {
      try {
        const res = await factorRhService().find(factorRhId);
        factorRh.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.factorRhId) {
      retrieveFactorRh(route.params.factorRhId);
    }

    const validations = useValidation();
    const validationRules = {
      codigo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 1 caracteres.', 1),
        maxLength: validations.maxLength('Este campo no puede superar más de 10 caracteres.', 10),
      },
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 1 caracteres.', 1),
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      descripcion: {
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
    };
    const v$ = useVuelidate(validationRules, factorRh as any);
    v$.value.$validate();

    return {
      factorRhService,
      alertService,
      factorRh,
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
      if (this.factorRh.id) {
        this.factorRhService()
          .update(this.factorRh)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A FactorRh is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.factorRhService()
          .create(this.factorRh)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A FactorRh is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
