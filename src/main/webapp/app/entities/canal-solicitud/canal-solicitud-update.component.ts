import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { CanalSolicitud, type ICanalSolicitud } from '@/shared/model/canal-solicitud.model';

import CanalSolicitudService from './canal-solicitud.service';

export default defineComponent({
  name: 'CanalSolicitudUpdate',
  setup() {
    const canalSolicitudService = inject('canalSolicitudService', () => new CanalSolicitudService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const canalSolicitud: Ref<ICanalSolicitud> = ref(new CanalSolicitud());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCanalSolicitud = async canalSolicitudId => {
      try {
        const res = await canalSolicitudService().find(canalSolicitudId);
        canalSolicitud.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.canalSolicitudId) {
      retrieveCanalSolicitud(route.params.canalSolicitudId);
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
    const v$ = useVuelidate(validationRules, canalSolicitud as any);
    v$.value.$validate();

    return {
      canalSolicitudService,
      alertService,
      canalSolicitud,
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
      if (this.canalSolicitud.id) {
        this.canalSolicitudService()
          .update(this.canalSolicitud)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A CanalSolicitud is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.canalSolicitudService()
          .create(this.canalSolicitud)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A CanalSolicitud is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
