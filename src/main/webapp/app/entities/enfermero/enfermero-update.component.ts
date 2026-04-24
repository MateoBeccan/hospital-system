import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import EmpleadoService from '@/entities/empleado/empleado.service';
import TurnoLaboralService from '@/entities/turno-laboral/turno-laboral.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IEmpleado } from '@/shared/model/empleado.model';
import { Enfermero, type IEnfermero } from '@/shared/model/enfermero.model';
import { type ITurnoLaboral } from '@/shared/model/turno-laboral.model';

import EnfermeroService from './enfermero.service';

export default defineComponent({
  name: 'EnfermeroUpdate',
  setup() {
    const enfermeroService = inject('enfermeroService', () => new EnfermeroService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const enfermero: Ref<IEnfermero> = ref(new Enfermero());

    const empleadoService = inject('empleadoService', () => new EmpleadoService());

    const empleados: Ref<IEmpleado[]> = ref([]);

    const turnoLaboralService = inject('turnoLaboralService', () => new TurnoLaboralService());

    const turnoLaborals: Ref<ITurnoLaboral[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEnfermero = async enfermeroId => {
      try {
        const res = await enfermeroService().find(enfermeroId);
        enfermero.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.enfermeroId) {
      retrieveEnfermero(route.params.enfermeroId);
    }

    const initRelationships = () => {
      empleadoService()
        .retrieve()
        .then(res => {
          empleados.value = res.data;
        });
      turnoLaboralService()
        .retrieve()
        .then(res => {
          turnoLaborals.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      matricula: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 4 caracteres.', 4),
        maxLength: validations.maxLength('Este campo no puede superar más de 40 caracteres.', 40),
      },
      fechaMatriculacion: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      empleado: {
        required: validations.required('Este campo es obligatorio.'),
      },
      turnoLaboral: {},
    };
    const v$ = useVuelidate(validationRules, enfermero as any);
    v$.value.$validate();

    return {
      enfermeroService,
      alertService,
      enfermero,
      previousState,
      isSaving,
      currentLanguage,
      empleados,
      turnoLaborals,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.enfermero.id) {
        this.enfermeroService()
          .update(this.enfermero)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Enfermero is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.enfermeroService()
          .create(this.enfermero)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Enfermero is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
