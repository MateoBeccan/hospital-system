import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { EstadoDiagnostico, type IEstadoDiagnostico } from '@/shared/model/estado-diagnostico.model';

import EstadoDiagnosticoService from './estado-diagnostico.service';

export default defineComponent({
  name: 'EstadoDiagnosticoUpdate',
  setup() {
    const estadoDiagnosticoService = inject('estadoDiagnosticoService', () => new EstadoDiagnosticoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const estadoDiagnostico: Ref<IEstadoDiagnostico> = ref(new EstadoDiagnostico());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEstadoDiagnostico = async estadoDiagnosticoId => {
      try {
        const res = await estadoDiagnosticoService().find(estadoDiagnosticoId);
        estadoDiagnostico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoDiagnosticoId) {
      retrieveEstadoDiagnostico(route.params.estadoDiagnosticoId);
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
    const v$ = useVuelidate(validationRules, estadoDiagnostico as any);
    v$.value.$validate();

    return {
      estadoDiagnosticoService,
      alertService,
      estadoDiagnostico,
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
      if (this.estadoDiagnostico.id) {
        this.estadoDiagnosticoService()
          .update(this.estadoDiagnostico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A EstadoDiagnostico is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.estadoDiagnosticoService()
          .create(this.estadoDiagnostico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A EstadoDiagnostico is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
