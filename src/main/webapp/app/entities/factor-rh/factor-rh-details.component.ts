import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IFactorRh } from '@/shared/model/factor-rh.model';

import FactorRhService from './factor-rh.service';

export default defineComponent({
  name: 'FactorRhDetails',
  setup() {
    const factorRhService = inject('factorRhService', () => new FactorRhService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const factorRh: Ref<IFactorRh> = ref({});

    const retrieveFactorRh = async factorRhId => {
      try {
        const res = await factorRhService().find(factorRhId);
        factorRh.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.factorRhId) {
      retrieveFactorRh(route.params.factorRhId);
    }

    return {
      alertService,
      factorRh,

      previousState,
    };
  },
});
