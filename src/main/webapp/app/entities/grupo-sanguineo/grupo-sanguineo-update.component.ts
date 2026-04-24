import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { GrupoSanguineo, type IGrupoSanguineo } from '@/shared/model/grupo-sanguineo.model';

import GrupoSanguineoService from './grupo-sanguineo.service';

export default defineComponent({
  name: 'GrupoSanguineoUpdate',
  setup() {
    const grupoSanguineoService = inject('grupoSanguineoService', () => new GrupoSanguineoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const grupoSanguineo: Ref<IGrupoSanguineo> = ref(new GrupoSanguineo());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveGrupoSanguineo = async grupoSanguineoId => {
      try {
        const res = await grupoSanguineoService().find(grupoSanguineoId);
        grupoSanguineo.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.grupoSanguineoId) {
      retrieveGrupoSanguineo(route.params.grupoSanguineoId);
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
    const v$ = useVuelidate(validationRules, grupoSanguineo as any);
    v$.value.$validate();

    return {
      grupoSanguineoService,
      alertService,
      grupoSanguineo,
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
      if (this.grupoSanguineo.id) {
        this.grupoSanguineoService()
          .update(this.grupoSanguineo)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A GrupoSanguineo is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.grupoSanguineoService()
          .create(this.grupoSanguineo)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A GrupoSanguineo is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
