import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISignosVitales } from '@/shared/model/signos-vitales.model';

import SignosVitalesService from './signos-vitales.service';

export default defineComponent({
  name: 'SignosVitalesDetails',
  setup() {
    const dateFormat = useDateFormat();
    const signosVitalesService = inject('signosVitalesService', () => new SignosVitalesService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const signosVitales: Ref<ISignosVitales> = ref({});

    const retrieveSignosVitales = async signosVitalesId => {
      try {
        const res = await signosVitalesService().find(signosVitalesId);
        signosVitales.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.signosVitalesId) {
      retrieveSignosVitales(route.params.signosVitalesId);
    }

    return {
      ...dateFormat,
      alertService,
      signosVitales,

      ...dataUtils,

      previousState,
    };
  },
});
