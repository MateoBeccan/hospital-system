import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ITurnoLaboral, TurnoLaboral } from '@/shared/model/turno-laboral.model';

import TurnoLaboralService from './turno-laboral.service';

export default defineComponent({
  name: 'TurnoLaboralUpdate',
  setup() {
    const turnoLaboralService = inject('turnoLaboralService', () => new TurnoLaboralService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const turnoLaboral: Ref<ITurnoLaboral> = ref(new TurnoLaboral());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTurnoLaboral = async turnoLaboralId => {
      try {
        const res = await turnoLaboralService().find(turnoLaboralId);
        turnoLaboral.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.turnoLaboralId) {
      retrieveTurnoLaboral(route.params.turnoLaboralId);
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
      horaInicio: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 5 caracteres.', 5),
      },
      horaFin: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 5 caracteres.', 5),
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
    const v$ = useVuelidate(validationRules, turnoLaboral as any);
    v$.value.$validate();

    return {
      turnoLaboralService,
      alertService,
      turnoLaboral,
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
      if (this.turnoLaboral.id) {
        this.turnoLaboralService()
          .update(this.turnoLaboral)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A TurnoLaboral is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.turnoLaboralService()
          .create(this.turnoLaboral)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A TurnoLaboral is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
