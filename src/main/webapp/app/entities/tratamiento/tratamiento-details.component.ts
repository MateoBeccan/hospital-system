import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ITratamiento } from '@/shared/model/tratamiento.model';

import TratamientoService from './tratamiento.service';

export default defineComponent({
  name: 'TratamientoDetails',
  setup() {
    const tratamientoService = inject('tratamientoService', () => new TratamientoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tratamiento: Ref<ITratamiento> = ref({});

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

    return {
      alertService,
      tratamiento,

      ...dataUtils,

      previousState,
    };
  },
});
