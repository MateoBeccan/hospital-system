import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { EstadoTratamiento, type IEstadoTratamiento } from '@/shared/model/estado-tratamiento.model';

import EstadoTratamientoService from './estado-tratamiento.service';

export default defineComponent({
  name: 'EstadoTratamientoUpdate',
  setup() {
    const estadoTratamientoService = inject('estadoTratamientoService', () => new EstadoTratamientoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const estadoTratamiento: Ref<IEstadoTratamiento> = ref(new EstadoTratamiento());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEstadoTratamiento = async estadoTratamientoId => {
      try {
        const res = await estadoTratamientoService().find(estadoTratamientoId);
        estadoTratamiento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoTratamientoId) {
      retrieveEstadoTratamiento(route.params.estadoTratamientoId);
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
    const v$ = useVuelidate(validationRules, estadoTratamiento as any);
    v$.value.$validate();

    return {
      estadoTratamientoService,
      alertService,
      estadoTratamiento,
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
      if (this.estadoTratamiento.id) {
        this.estadoTratamientoService()
          .update(this.estadoTratamiento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A EstadoTratamiento is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.estadoTratamientoService()
          .create(this.estadoTratamiento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A EstadoTratamiento is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
