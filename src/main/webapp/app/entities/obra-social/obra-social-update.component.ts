import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IObraSocial, ObraSocial } from '@/shared/model/obra-social.model';

import ObraSocialService from './obra-social.service';

export default defineComponent({
  name: 'ObraSocialUpdate',
  setup() {
    const obraSocialService = inject('obraSocialService', () => new ObraSocialService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const obraSocial: Ref<IObraSocial> = ref(new ObraSocial());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveObraSocial = async obraSocialId => {
      try {
        const res = await obraSocialService().find(obraSocialId);
        obraSocial.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.obraSocialId) {
      retrieveObraSocial(route.params.obraSocialId);
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
        maxLength: validations.maxLength('Este campo no puede superar más de 120 caracteres.', 120),
      },
      telefono: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      email: {
        maxLength: validations.maxLength('Este campo no puede superar más de 191 caracteres.', 191),
      },
      direccion: {
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
    const v$ = useVuelidate(validationRules, obraSocial as any);
    v$.value.$validate();

    return {
      obraSocialService,
      alertService,
      obraSocial,
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
      if (this.obraSocial.id) {
        this.obraSocialService()
          .update(this.obraSocial)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ObraSocial is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.obraSocialService()
          .create(this.obraSocial)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ObraSocial is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
