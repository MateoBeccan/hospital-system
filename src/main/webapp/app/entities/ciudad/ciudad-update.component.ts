import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import ProvinciaService from '@/entities/provincia/provincia.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Ciudad, type ICiudad } from '@/shared/model/ciudad.model';
import { type IProvincia } from '@/shared/model/provincia.model';

import CiudadService from './ciudad.service';

export default defineComponent({
  name: 'CiudadUpdate',
  setup() {
    const ciudadService = inject('ciudadService', () => new CiudadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const ciudad: Ref<ICiudad> = ref(new Ciudad());

    const provinciaService = inject('provinciaService', () => new ProvinciaService());

    const provincias: Ref<IProvincia[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCiudad = async ciudadId => {
      try {
        const res = await ciudadService().find(ciudadId);
        ciudad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.ciudadId) {
      retrieveCiudad(route.params.ciudadId);
    }

    const initRelationships = () => {
      provinciaService()
        .retrieve()
        .then(res => {
          provincias.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        minLength: validations.minLength('Este campo requiere al menos 2 caracteres.', 2),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      codigo: {
        maxLength: validations.maxLength('Este campo no puede superar más de 30 caracteres.', 30),
      },
      codigoPostal: {
        maxLength: validations.maxLength('Este campo no puede superar más de 20 caracteres.', 20),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      provincia: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, ciudad as any);
    v$.value.$validate();

    return {
      ciudadService,
      alertService,
      ciudad,
      previousState,
      isSaving,
      currentLanguage,
      provincias,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.ciudad.id) {
        this.ciudadService()
          .update(this.ciudad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Ciudad is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.ciudadService()
          .create(this.ciudad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Ciudad is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
