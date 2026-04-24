import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IProvincia } from '@/shared/model/provincia.model';

import ProvinciaService from './provincia.service';

export default defineComponent({
  name: 'ProvinciaDetails',
  setup() {
    const provinciaService = inject('provinciaService', () => new ProvinciaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const provincia: Ref<IProvincia> = ref({});

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

    return {
      alertService,
      provincia,

      previousState,
    };
  },
});
