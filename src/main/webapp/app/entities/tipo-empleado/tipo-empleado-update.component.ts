import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ITipoEmpleado, TipoEmpleado } from '@/shared/model/tipo-empleado.model';

import TipoEmpleadoService from './tipo-empleado.service';

export default defineComponent({
  name: 'TipoEmpleadoUpdate',
  setup() {
    const tipoEmpleadoService = inject('tipoEmpleadoService', () => new TipoEmpleadoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tipoEmpleado: Ref<ITipoEmpleado> = ref(new TipoEmpleado());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTipoEmpleado = async tipoEmpleadoId => {
      try {
        const res = await tipoEmpleadoService().find(tipoEmpleadoId);
        tipoEmpleado.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoEmpleadoId) {
      retrieveTipoEmpleado(route.params.tipoEmpleadoId);
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
    const v$ = useVuelidate(validationRules, tipoEmpleado as any);
    v$.value.$validate();

    return {
      tipoEmpleadoService,
      alertService,
      tipoEmpleado,
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
      if (this.tipoEmpleado.id) {
        this.tipoEmpleadoService()
          .update(this.tipoEmpleado)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A TipoEmpleado is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tipoEmpleadoService()
          .create(this.tipoEmpleado)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A TipoEmpleado is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
