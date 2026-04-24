import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Cargo, type ICargo } from '@/shared/model/cargo.model';

import CargoService from './cargo.service';

export default defineComponent({
  name: 'CargoUpdate',
  setup() {
    const cargoService = inject('cargoService', () => new CargoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cargo: Ref<ICargo> = ref(new Cargo());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCargo = async cargoId => {
      try {
        const res = await cargoService().find(cargoId);
        cargo.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoId) {
      retrieveCargo(route.params.cargoId);
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
    const v$ = useVuelidate(validationRules, cargo as any);
    v$.value.$validate();

    return {
      cargoService,
      alertService,
      cargo,
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
      if (this.cargo.id) {
        this.cargoService()
          .update(this.cargo)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Cargo is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.cargoService()
          .create(this.cargo)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Cargo is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
