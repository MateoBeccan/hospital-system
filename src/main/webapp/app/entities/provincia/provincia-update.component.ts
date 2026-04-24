import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PaisService from '@/entities/pais/pais.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IPais } from '@/shared/model/pais.model';
import { type IProvincia, Provincia } from '@/shared/model/provincia.model';

import ProvinciaService from './provincia.service';

export default defineComponent({
  name: 'ProvinciaUpdate',
  setup() {
    const provinciaService = inject('provinciaService', () => new ProvinciaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const provincia: Ref<IProvincia> = ref(new Provincia());

    const paisService = inject('paisService', () => new PaisService());

    const paises: Ref<IPais[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveProvincia = async provinciaId => {
      try {
        const res = await provinciaService().find(provinciaId);
        provincia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.provinciaId) {
      retrieveProvincia(route.params.provinciaId);
    }

    const initRelationships = () => {
      paisService()
        .retrieve()
        .then(res => {
          paises.value = res.data;
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
        maxLength: validations.maxLength('Este campo no puede superar más de 20 caracteres.', 20),
      },
      fechaAlta: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaBaja: {},
      activo: {
        required: validations.required('Este campo es obligatorio.'),
      },
      pais: {
        required: validations.required('Este campo es obligatorio.'),
      },
    };
    const v$ = useVuelidate(validationRules, provincia as any);
    v$.value.$validate();

    return {
      provinciaService,
      alertService,
      provincia,
      previousState,
      isSaving,
      currentLanguage,
      paises,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.provincia.id) {
        this.provinciaService()
          .update(this.provincia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Provincia is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.provinciaService()
          .create(this.provincia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Provincia is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
