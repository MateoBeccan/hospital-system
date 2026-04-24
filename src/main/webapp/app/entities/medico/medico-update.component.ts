import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import EmpleadoService from '@/entities/empleado/empleado.service';
import EspecialidadService from '@/entities/especialidad/especialidad.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IEmpleado } from '@/shared/model/empleado.model';
import { type IEspecialidad } from '@/shared/model/especialidad.model';
import { type IMedico, Medico } from '@/shared/model/medico.model';

import MedicoService from './medico.service';

export default defineComponent({
  name: 'MedicoUpdate',
  setup() {
    const medicoService = inject('medicoService', () => new MedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const medico: Ref<IMedico> = ref(new Medico());

    const empleadoService = inject('empleadoService', () => new EmpleadoService());

    const empleados: Ref<IEmpleado[]> = ref([]);

    const especialidadService = inject('especialidadService', () => new EspecialidadService());

    const especialidads: Ref<IEspecialidad[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMedico = async medicoId => {
      try {
        const res = await medicoService().find(medicoId);
        medico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.medicoId) {
      retrieveMedico(route.params.medicoId);
    }

    const initRelationships = () => {
      empleadoService()
        .retrieve()
        .then(res => {
          empleados.value = res.data;
        });
      especialidadService()
        .retrieve()
        .then(res => {
          especialidads.value = res.data;
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
      firmaDigital: {
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      atiendeConsultorio: {
        required: validations.required('Este campo es obligatorio.'),
      },
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
      especialidad: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, medico as any);
    v$.value.$validate();

    return {
      medicoService,
      alertService,
      medico,
      previousState,
      isSaving,
      currentLanguage,
      empleados,
      especialidads,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.medico.id) {
        this.medicoService()
          .update(this.medico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Medico is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.medicoService()
          .create(this.medico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Medico is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
