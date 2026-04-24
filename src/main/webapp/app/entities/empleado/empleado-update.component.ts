import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import CargoService from '@/entities/cargo/cargo.service';
import EstadoLaboralService from '@/entities/estado-laboral/estado-laboral.service';
import PersonaService from '@/entities/persona/persona.service';
import TipoEmpleadoService from '@/entities/tipo-empleado/tipo-empleado.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ICargo } from '@/shared/model/cargo.model';
import { Empleado, type IEmpleado } from '@/shared/model/empleado.model';
import { type IEstadoLaboral } from '@/shared/model/estado-laboral.model';
import { type IPersona } from '@/shared/model/persona.model';
import { type ITipoEmpleado } from '@/shared/model/tipo-empleado.model';

import EmpleadoService from './empleado.service';

export default defineComponent({
  name: 'EmpleadoUpdate',
  setup() {
    const empleadoService = inject('empleadoService', () => new EmpleadoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const empleado: Ref<IEmpleado> = ref(new Empleado());

    const personaService = inject('personaService', () => new PersonaService());

    const personas: Ref<IPersona[]> = ref([]);

    const tipoEmpleadoService = inject('tipoEmpleadoService', () => new TipoEmpleadoService());

    const tipoEmpleados: Ref<ITipoEmpleado[]> = ref([]);

    const estadoLaboralService = inject('estadoLaboralService', () => new EstadoLaboralService());

    const estadoLaborals: Ref<IEstadoLaboral[]> = ref([]);

    const cargoService = inject('cargoService', () => new CargoService());

    const cargos: Ref<ICargo[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEmpleado = async empleadoId => {
      try {
        const res = await empleadoService().find(empleadoId);
        empleado.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.empleadoId) {
      retrieveEmpleado(route.params.empleadoId);
    }

    const initRelationships = () => {
      personaService()
        .retrieve()
        .then(res => {
          personas.value = res.data;
        });
      tipoEmpleadoService()
        .retrieve()
        .then(res => {
          tipoEmpleados.value = res.data;
        });
      estadoLaboralService()
        .retrieve()
        .then(res => {
          estadoLaborals.value = res.data;
        });
      cargoService()
        .retrieve()
        .then(res => {
          cargos.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      legajo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      fechaIngreso: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      persona: {
        required: validations.required('Este campo es obligatorio.'),
      },
      tipoEmpleado: {
        required: validations.required('Este campo es obligatorio.'),
      },
      estadoLaboral: {
        required: validations.required('Este campo es obligatorio.'),
      },
      cargo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      medico: {},
      enfermero: {},
    };
    const v$ = useVuelidate(validationRules, empleado as any);
    v$.value.$validate();

    return {
      empleadoService,
      alertService,
      empleado,
      previousState,
      isSaving,
      currentLanguage,
      personas,
      tipoEmpleados,
      estadoLaborals,
      cargos,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.empleado.id) {
        this.empleadoService()
          .update(this.empleado)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Empleado is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.empleadoService()
          .create(this.empleado)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Empleado is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
