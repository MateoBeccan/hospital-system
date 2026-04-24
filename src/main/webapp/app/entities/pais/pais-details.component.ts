import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IPais } from '@/shared/model/pais.model';

import PaisService from './pais.service';

export default defineComponent({
  name: 'PaisDetails',
  setup() {
    const paisService = inject('paisService', () => new PaisService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pais: Ref<IPais> = ref({});

    const retrievePais = async paisId => {
      try {
        const res = await paisService().find(paisId);
        pais.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paisId) {
      retrievePais(route.params.paisId);
    }

    return {
      alertService,
      pais,

      previousState,
    };
  },
});
