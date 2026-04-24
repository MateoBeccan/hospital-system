import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { EstadoLaboral, type IEstadoLaboral } from '@/shared/model/estado-laboral.model';

import EstadoLaboralService from './estado-laboral.service';

export default defineComponent({
  name: 'EstadoLaboralUpdate',
  setup() {
    const estadoLaboralService = inject('estadoLaboralService', () => new EstadoLaboralService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const estadoLaboral: Ref<IEstadoLaboral> = ref(new EstadoLaboral());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEstadoLaboral = async estadoLaboralId => {
      try {
        const res = await estadoLaboralService().find(estadoLaboralId);
        estadoLaboral.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoLaboralId) {
      retrieveEstadoLaboral(route.params.estadoLaboralId);
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
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, estadoLaboral as any);
    v$.value.$validate();

    return {
      estadoLaboralService,
      alertService,
      estadoLaboral,
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
      if (this.estadoLaboral.id) {
        this.estadoLaboralService()
          .update(this.estadoLaboral)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A EstadoLaboral is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.estadoLaboralService()
          .create(this.estadoLaboral)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A EstadoLaboral is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
