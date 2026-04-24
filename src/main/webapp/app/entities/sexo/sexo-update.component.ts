import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ISexo, Sexo } from '@/shared/model/sexo.model';

import SexoService from './sexo.service';

export default defineComponent({
  name: 'SexoUpdate',
  setup() {
    const sexoService = inject('sexoService', () => new SexoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sexo: Ref<ISexo> = ref(new Sexo());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSexo = async sexoId => {
      try {
        const res = await sexoService().find(sexoId);
        sexo.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sexoId) {
      retrieveSexo(route.params.sexoId);
    }

    const validations = useValidation();
    const validationRules = {
      codigo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 1 caracteres.', 1),
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
    const v$ = useVuelidate(validationRules, sexo as any);
    v$.value.$validate();

    return {
      sexoService,
      alertService,
      sexo,
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
      if (this.sexo.id) {
        this.sexoService()
          .update(this.sexo)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Sexo is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.sexoService()
          .create(this.sexo)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Sexo is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
