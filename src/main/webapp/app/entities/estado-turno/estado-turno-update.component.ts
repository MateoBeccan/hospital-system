import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { EstadoTurno, type IEstadoTurno } from '@/shared/model/estado-turno.model';

import EstadoTurnoService from './estado-turno.service';

export default defineComponent({
  name: 'EstadoTurnoUpdate',
  setup() {
    const estadoTurnoService = inject('estadoTurnoService', () => new EstadoTurnoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const estadoTurno: Ref<IEstadoTurno> = ref(new EstadoTurno());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEstadoTurno = async estadoTurnoId => {
      try {
        const res = await estadoTurnoService().find(estadoTurnoId);
        estadoTurno.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.estadoTurnoId) {
      retrieveEstadoTurno(route.params.estadoTurnoId);
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
    const v$ = useVuelidate(validationRules, estadoTurno as any);
    v$.value.$validate();

    return {
      estadoTurnoService,
      alertService,
      estadoTurno,
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
      if (this.estadoTurno.id) {
        this.estadoTurnoService()
          .update(this.estadoTurno)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A EstadoTurno is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.estadoTurnoService()
          .create(this.estadoTurno)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A EstadoTurno is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
