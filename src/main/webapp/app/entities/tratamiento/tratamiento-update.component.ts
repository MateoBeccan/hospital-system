import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import DiagnosticoService from '@/entities/diagnostico/diagnostico.service';
import EstadoTratamientoService from '@/entities/estado-tratamiento/estado-tratamiento.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IDiagnostico } from '@/shared/model/diagnostico.model';
import { type IEstadoTratamiento } from '@/shared/model/estado-tratamiento.model';
import { type ITratamiento, Tratamiento } from '@/shared/model/tratamiento.model';

import TratamientoService from './tratamiento.service';

export default defineComponent({
  name: 'TratamientoUpdate',
  setup() {
    const tratamientoService = inject('tratamientoService', () => new TratamientoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tratamiento: Ref<ITratamiento> = ref(new Tratamiento());

    const diagnosticoService = inject('diagnosticoService', () => new DiagnosticoService());

    const diagnosticos: Ref<IDiagnostico[]> = ref([]);

    const estadoTratamientoService = inject('estadoTratamientoService', () => new EstadoTratamientoService());

    const estadoTratamientos: Ref<IEstadoTratamiento[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTratamiento = async tratamientoId => {
      try {
        const res = await tratamientoService().find(tratamientoId);
        tratamiento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tratamientoId) {
      retrieveTratamiento(route.params.tratamientoId);
    }

    const initRelationships = () => {
      diagnosticoService()
        .retrieve()
        .then(res => {
          diagnosticos.value = res.data;
        });
      estadoTratamientoService()
        .retrieve()
        .then(res => {
          estadoTratamientos.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      codigo: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 40 caracteres.', 40),
      },
      descripcion: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 3 caracteres.', 3),
        maxLength: validations.maxLength('Este campo no puede superar más de 255 caracteres.', 255),
      },
      fechaInicio: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaFin: {},
      observaciones: {},
      fechaProximaRevision: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      diagnostico: {
        required: validations.required('Este campo es obligatorio.'),
      },
      estadoTratamiento: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, tratamiento as any);
    v$.value.$validate();

    return {
      tratamientoService,
      alertService,
      tratamiento,
      previousState,
      isSaving,
      currentLanguage,
      diagnosticos,
      estadoTratamientos,
      ...dataUtils,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tratamiento.id) {
        this.tratamientoService()
          .update(this.tratamiento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Tratamiento is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tratamientoService()
          .create(this.tratamiento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Tratamiento is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
